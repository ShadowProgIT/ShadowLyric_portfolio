package com.logistic.service;


import com.logistic.dto.EntrantRequest;
import com.logistic.dto.EntrantResponse;
import com.logistic.dto.PagedResponse;
import com.logistic.dto.PatchEntrantRequest;
import com.logistic.event.EventPublisher;
import com.logistic.events.EntrantEvent;
import com.logistic.events.RoutingKeys;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.storage.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EntrantService {

    private final InMemoryStorage storage;
    private final EventPublisher eventPublisher;

    public EntrantService(InMemoryStorage storage, EventPublisher eventPublisher) {
        this.storage = storage;
        this.eventPublisher = eventPublisher;
    }

    public PagedResponse<EntrantResponse> findAll(int page, int size) {
        List<EntrantResponse> all = storage.entrants.values().stream()
                .sorted(Comparator.comparingLong(EntrantResponse::getId))
                .toList();

        int totalElements = all.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);

        List<EntrantResponse> content = (from >= totalElements) ? List.of() : all.subList(from, to);

        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public EntrantResponse findById(Long id) {
        return Optional.ofNullable(storage.entrants.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Entrant", id));
    }

    public EntrantResponse create(EntrantRequest request) {
        long id = storage.entrantSequence.incrementAndGet();

        EntrantResponse entrant = EntrantResponse.builder()
                .id(id)
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .build();

        storage.entrants.put(id, entrant);
        return entrant;
    }

    public EntrantResponse update(Long id, EntrantRequest request) {
        findById(id);

        EntrantResponse updated = EntrantResponse.builder()
                .id(id)
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .phone(request.phone())
                .address(request.address())
                .build();

        storage.entrants.put(id, updated);
        return updated;
    }

    public EntrantResponse patchEntrant(Long id, PatchEntrantRequest request) {
        EntrantResponse existing = findById(id);

        EntrantResponse patched = EntrantResponse.builder()
                .id(id)
                .firstname(request.firstname() != null ? request.firstname() : existing.getFirstname())
                .lastname(request.lastname() != null ? request.lastname() : existing.getLastname())
                .email(request.email() != null ? request.email() : existing.getEmail())
                .phone(request.phone() != null ? request.phone() : existing.getPhone())
                .address(request.address() != null ? request.address() : existing.getAddress())
                .build();

        storage.entrants.put(id, patched);
        return patched;
    }

    public void delete(Long id) {
        EntrantResponse existing = findById(id);

        boolean hasActiveShipments = storage.sendings.values().stream()
                .anyMatch(s -> s.getSenderId().equals(id) || s.getRecipientId().equals(id));
        if (hasActiveShipments) {
            throw new IllegalStateException("Невозможно удалить, так как участник связан с активными отправками");
        }

        EntrantEvent.Deleted deletedEvent = new EntrantEvent.Deleted(
                existing.getId(),
                existing.getFirstname(),
                existing.getLastname(),
                existing.getPhone()
        );

        eventPublisher.publish(deletedEvent, RoutingKeys.ENTRANT_DELETED);
        storage.entrants.remove(id);
    }

    public PagedResponse<EntrantResponse> searchByName(String query, int page, int size) {
        List<EntrantResponse> filtered = storage.entrants.values().stream()
                .filter(e -> (e.getFirstname() != null && e.getFirstname().toLowerCase().contains(query.toLowerCase())) ||
                        (e.getLastname() != null && e.getLastname().toLowerCase().contains(query.toLowerCase())))
                .sorted(Comparator.comparingLong(EntrantResponse::getId))
                .toList();

        int totalElements = filtered.size();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 1;
        int from = page * size;
        int to = Math.min(from + size, totalElements);

        List<EntrantResponse> content = (from >= totalElements) ? List.of() : filtered.subList(from, to);

        return new PagedResponse<>(content, page, size, totalElements, totalPages, page >= totalPages - 1);
    }
}

