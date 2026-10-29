package com.logistic.storage;

import com.logistic.dto.EntrantResponse;
import com.logistic.dto.SendingHistoryResponse;
import com.logistic.dto.SendingResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {

    public final Map<Long, EntrantResponse> entrants = new ConcurrentHashMap<>();
    public final AtomicLong entrantSequence = new AtomicLong(0);

    public final Map<Long, SendingResponse> sendings = new ConcurrentHashMap<>();
    public final AtomicLong sendingSequence = new AtomicLong(0);

    public final Map<Long, SendingHistoryResponse> sendingHistories = new ConcurrentHashMap<>();
    public final AtomicLong historySequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        EntrantResponse sender = EntrantResponse.builder()
                .id(entrantSequence.incrementAndGet())
                .firstname("Иван")
                .lastname("Иванов")
                .email("ivan@example.com")
                .phone("+7 (999) 123-45-67")
                .address("г. Москва, ул. Тверская, 1")
                .build();

        EntrantResponse recipient = EntrantResponse.builder()
                .id(entrantSequence.incrementAndGet())
                .firstname("Мария")
                .lastname("Петрова")
                .email("maria@example.com")
                .phone("+7 (999) 987-65-43")
                .address("г. СПб, Невский пр., 100")
                .build();

        entrants.put(sender.getId(), sender);
        entrants.put(recipient.getId(), recipient);

        SendingResponse sending1 = SendingResponse.builder()
                .id(sendingSequence.incrementAndGet())
                .trackingNumber("RU10-21-56")
                .status("in_way")
                .weight(10.5)
                .size("30x20x20")
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now())
                .senderId(sender.getId())
                .recipientId(recipient.getId())
                .build();

        SendingResponse sending2 = SendingResponse.builder()
                .id(sendingSequence.incrementAndGet())
                .trackingNumber("RU11-22-78")
                .status("created")
                .weight(2.0)
                .size("15x10x10")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .senderId(recipient.getId())
                .recipientId(sender.getId())
                .build();

        sendings.put(sending1.getId(), sending1);
        sendings.put(sending2.getId(), sending2);
    }
}
