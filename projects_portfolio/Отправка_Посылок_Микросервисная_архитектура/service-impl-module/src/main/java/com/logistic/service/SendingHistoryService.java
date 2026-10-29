package com.logistic.service;

import com.logistic.dto.PagedResponse;
import com.logistic.dto.SendingHistoryRequest;
import com.logistic.dto.SendingHistoryResponse;
import com.logistic.dto.SendingResponse;
import com.logistic.storage.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SendingHistoryService {

    private final InMemoryStorage storage;
    private final SendingService sendingService;

    public SendingHistoryService(InMemoryStorage storage, SendingService sendingService) {
        this.storage = storage;
        this.sendingService = sendingService;
    }

    public SendingHistoryResponse addEvent(Long sendingId, SendingHistoryRequest request) {
        SendingResponse sending = sendingService.findById(sendingId);

        long historyId = storage.historySequence.incrementAndGet();

        SendingHistoryResponse event = SendingHistoryResponse.builder()
                .id(historyId)
                .shipmentId(sendingId)
                .status(request.status())
                .location(request.location())
                .timestamp(LocalDateTime.now())
                .comment(request.comment())
                .build();

        storage.sendingHistories.put(historyId, event);

        sendingService.updateStatus(sendingId, request.status());

        return event;
    }

    public PagedResponse<SendingHistoryResponse> findBySendingId(Long sendingId, int page, int size) {
        List<SendingHistoryResponse> all = storage.sendingHistories.values().stream()
                .filter(ev -> ev.getShipmentId().equals(sendingId))
                .sorted(Comparator.comparing(SendingHistoryResponse::getTimestamp).reversed())
                .collect(Collectors.toList());

        int total = all.size();
        int start = Math.min(page * size, total);
        int end = Math.min(start + size, total);
        List<SendingHistoryResponse> content = all.subList(start, end);
        boolean last = (end == total);
        int totalPages = (total + size - 1) / size;

        return new PagedResponse<>(content, page, size, total, totalPages, last);
    }
}