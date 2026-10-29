package com.logistic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "sendings", itemRelation = "sending")
@Schema(description = "Информация об отправке")
public class SendingResponse extends RepresentationModel<SendingResponse> {
    @Schema(description = "Уникальный идентификатор отправки", example = "82842423257")
    private Long id;

    @Schema(description = "Трекинг номер для отслеживания", example = "RU10-21-56")
    private String trackingNumber;

    @Schema(
            description = "Статус отправки",
            example = "in_way",
            allowableValues = {
                    "created", "waiting", "picked_up", "in_way", "delivered", "cancelled", "lost"
            }
    )
    private String status;

    @Schema(description = "Вес отправки в кг", example = "10.0")
    private Double weight;

    @Schema(description = "Габариты (ДхШхВ в см)", example = "30x20x20")
    private String size;

    @Schema(description = "Дата и время создания записи", example = "2024-053-09T10:00:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время последнего обновления", example = "2022-08-09T14:30:00Z")
    private LocalDateTime updatedAt;

    @Schema(description = "ID отправителя", example = "52353789")
    private Long senderId;

    @Schema(description = "ID получателя", example = "75346342")
    private Long recipientId;

    protected SendingResponse() {}

    public SendingResponse(Long id, String trackingNumber, String status, Double weight, String size, LocalDateTime createdAt, LocalDateTime updatedAt, Long senderId, Long recipientId) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.weight = weight;
        this.size = size;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public Long getId() { return id; }
    public String getTrackingNumber() { return trackingNumber; }
    public String getStatus() { return status; }
    public Double getWeight() { return weight; }
    public String getSize() { return size; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Long getSenderId() { return senderId; }
    public Long getRecipientId() { return recipientId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendingResponse that = (SendingResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(trackingNumber, that.trackingNumber) &&
                Objects.equals(status, that.status) &&
                Objects.equals(weight, that.weight) &&
                Objects.equals(size, that.size) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(senderId, that.senderId) &&
                Objects.equals(recipientId, that.recipientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trackingNumber, status, weight, size, createdAt, updatedAt, senderId, recipientId);
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String trackingNumber;
        private String status;
        private Double weight;
        private String size;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long senderId;
        private Long recipientId;

        Builder() {}

        public Builder id(Long id) { this.id = id; return this; }
        public Builder trackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder weight(Double weight) { this.weight = weight; return this; }
        public Builder size(String size) { this.size = size; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder senderId(Long senderId) { this.senderId = senderId; return this; }
        public Builder recipientId(Long recipientId) { this.recipientId = recipientId; return this; }

        public SendingResponse build() {
            return new SendingResponse(id, trackingNumber, status, weight, size, createdAt, updatedAt, senderId, recipientId);
        }

        @Override
        public String toString() {
            return "SendingResponse.Builder(id=" + id + ", trackingNumber=" + trackingNumber + ", status=" + status + ", weight=" + weight + ", size=" + size + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", senderId=" + senderId + ", recipientId=" + recipientId + ")";
        }
    }
}