package com.logistic.service;

import com.logistic.dto.*;
import com.logistic.event.EventPublisher;
import com.logistic.events.RoutingKeys;
import com.logistic.events.SendingEvent;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.TrackingNumberAlreadyExistsException;
import com.logistic.storage.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class SendingService {

    private final InMemoryStorage storage;
    private final EventPublisher eventPublisher;

    public SendingService(InMemoryStorage storage, EventPublisher eventPublisher) {
        this.storage = storage;
        this.eventPublisher = eventPublisher;
    }

    public PagedResponse<SendingResponse> findAll(
            String status, String trackingNumber, Long senderId, Long recipientId,
            int page, int size) {

        Stream<SendingResponse> stream = storage.sendings.values().stream();

        if (status != null && !status.isBlank()) {
            stream = stream.filter(s -> status.equalsIgnoreCase(s.getStatus()));
        }
        if (trackingNumber != null && !trackingNumber.isBlank()) {
            stream = stream.filter(s -> trackingNumber.equalsIgnoreCase(s.getTrackingNumber()));
        }
        if (senderId != null) {
            stream = stream.filter(s -> senderId.equals(s.getSenderId()));
        }
        if (recipientId != null) {
            stream = stream.filter(s -> recipientId.equals(s.getRecipientId()));
        }

        List<SendingResponse> filtered = stream
                .sorted(Comparator.comparingLong(SendingResponse::getId))
                .toList();

        int totalElements = filtered.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);
        List<SendingResponse> content = (from >= totalElements) ? List.of() : filtered.subList(from, to);

        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public SendingResponse findById(Long id) {
        return Optional.ofNullable(storage.sendings.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Sending", id));
    }

    public SendingResponse create(SendingRequest request) {
        boolean exists = storage.sendings.values().stream()
                .anyMatch(s -> request.trackingNumber().equalsIgnoreCase(s.getTrackingNumber()));
        if (exists) {
            throw new TrackingNumberAlreadyExistsException(request.trackingNumber());
        }

        if (!storage.entrants.containsKey(request.senderId())) {
            throw new ResourceNotFoundException("Entrant", request.senderId());
        }
        if (!storage.entrants.containsKey(request.recipientId())) {
            throw new ResourceNotFoundException("Entrant", request.recipientId());
        }

        long id = storage.sendingSequence.incrementAndGet();
        LocalDateTime now = LocalDateTime.now();

        SendingResponse sending = SendingResponse.builder()
                .id(id)
                .trackingNumber(request.trackingNumber())
                .status("created")
                .weight(request.weight())
                .size(request.size())
                .createdAt(now)
                .updatedAt(now)
                .senderId(request.senderId())
                .recipientId(request.recipientId())
                .build();

        SendingEvent.Created createdEvent = new SendingEvent.Created(
                sending.getId(),
                sending.getTrackingNumber(),
                sending.getStatus(),
                String.valueOf(sending.getWeight()),
                sending.getSize(),
                sending.getCreatedAt().toString(),
                sending.getRecipientId(),
                sending.getSenderId()
        );

        storage.sendings.put(id, sending);
        eventPublisher.publish(createdEvent, RoutingKeys.SENDING_CREATED);
        return sending;
    }

    public SendingResponse update(Long id, SendingRequest request) {
        SendingResponse existing = findById(id);

        if (!existing.getTrackingNumber().equalsIgnoreCase(request.trackingNumber())) {
            boolean exists = storage.sendings.values().stream()
                    .anyMatch(s -> request.trackingNumber().equalsIgnoreCase(s.getTrackingNumber()));
            if (exists) {
                throw new TrackingNumberAlreadyExistsException(request.trackingNumber());
            }
        }

        if (!storage.entrants.containsKey(request.senderId())) {
            throw new ResourceNotFoundException("Entrant", request.senderId());
        }
        if (!storage.entrants.containsKey(request.recipientId())) {
            throw new ResourceNotFoundException("Entrant", request.recipientId());
        }

        SendingResponse updated = SendingResponse.builder()
                .id(id)
                .trackingNumber(request.trackingNumber())
                .status(existing.getStatus())
                .weight(request.weight())
                .size(request.size())
                .createdAt(existing.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .senderId(request.senderId())
                .recipientId(request.recipientId())
                .build();

        storage.sendings.put(id, updated);
        return updated;
    }

    public SendingResponse patchSending(Long id, PatchSendingRequest request) {
        SendingResponse existing = findById(id);

        if (request.trackingNumber() != null && !existing.getTrackingNumber().equalsIgnoreCase(request.trackingNumber())) {
            boolean exists = storage.sendings.values().stream()
                    .anyMatch(s -> request.trackingNumber().equalsIgnoreCase(s.getTrackingNumber()));
            if (exists) {
                throw new TrackingNumberAlreadyExistsException(request.trackingNumber());
            }
        }

        SendingResponse patched = SendingResponse.builder()
                .id(id)
                .trackingNumber(request.trackingNumber() != null ? request.trackingNumber() : existing.getTrackingNumber())
                .status(existing.getStatus())
                .weight(request.weight() != null ? request.weight() : existing.getWeight())
                .size(request.size() != null ? request.size() : existing.getSize())
                .createdAt(existing.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .senderId(request.senderId() != null ? request.senderId() : existing.getSenderId())
                .recipientId(request.recipientId() != null ? request.recipientId() : existing.getRecipientId())
                .build();

        storage.sendings.put(id, patched);
        return patched;
    }

    public void delete(Long id) {
        SendingResponse existing = findById(id);

        List<String> allowedStatuses = List.of("created", "cancelled");
        if (!allowedStatuses.contains(existing.getStatus())) {
            throw new IllegalStateException(
                    "Невозможно удалить отправку со статусом '%s'. Разрешено только для: %s"
                            .formatted(existing.getStatus(), String.join(", ", allowedStatuses)));
        }

        SendingEvent.Deleted deletedEvent = new SendingEvent.Deleted(
                existing.getId(),
                existing.getTrackingNumber()
        );
        eventPublisher.publish(deletedEvent, RoutingKeys.SENDING_DELETED);

        storage.sendings.remove(id);
        storage.sendingHistories.values().removeIf(h -> h.getShipmentId().equals(id));
    }

    public void updateStatus(Long id, String newStatus) {
        SendingResponse existing = findById(id);

        SendingResponse updated = SendingResponse.builder()
                .id(id)
                .trackingNumber(existing.getTrackingNumber())
                .status(newStatus)
                .weight(existing.getWeight())
                .size(existing.getSize())
                .createdAt(existing.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .senderId(existing.getSenderId())
                .recipientId(existing.getRecipientId())
                .build();

        storage.sendings.put(id, updated);

        SendingEvent.Update statusEvent = new SendingEvent.Update(
                id,
                existing.getTrackingNumber(),
                existing.getStatus(),
                newStatus
        );
        eventPublisher.publish(statusEvent, RoutingKeys.SENDING_STATUS_UPDATED);
    }

    public PagedResponse<SendingHistoryResponse> getHistory(Long sendingId, int page, int size) {
        findById(sendingId);
        int pageSize = Math.max(1, size);

        List<SendingHistoryResponse> all = storage.sendingHistories.values().stream()
                .filter(h -> h.getShipmentId().equals(sendingId))
                .sorted(Comparator.comparing(SendingHistoryResponse::getId))
                .toList();

        int totalElements = all.size();
        int totalPages = totalElements > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
        int from = page * pageSize;
        int to = Math.min(from + pageSize, totalElements);
        List<SendingHistoryResponse> content = (from >= totalElements) ? List.of() : all.subList(from, to);
        return new PagedResponse<>(content, page, pageSize, totalElements, totalPages, page >= totalPages - 1);
    }

}
