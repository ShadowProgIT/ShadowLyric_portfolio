package com.logistic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "shipmentHistories", itemRelation = "shipmentHistory")
@Schema(description = "Событие в истории перемещения отправки")
public class SendingHistoryResponse extends RepresentationModel<SendingHistoryResponse> {
    @Schema(description = "Уникальный идентификатор события истории", example = "5001")
    private Long id;

    @Schema(description = "ID отправки, к которой относится событие", example = "10174535")
    private Long shipmentId;

    @Schema(
            description = "Статус отправки в момент события",
            example = "in_way",
            allowableValues = {
                    "created", "waiting", "picked_up", "in_way", "delivered", "cancelled", "lost"
            }
    )
    private String status;

    @Schema(description = "Местоположение события", example = "Москва, Сортировочный центр")
    private String location;

    @Schema(description = "Время события", example = "2027-01-09T14:30:00Z")
    private LocalDateTime timestamp;

    @Schema(description = "Комментарий к событию", example = "Отправка принята курьером")
    private String comment;

    protected SendingHistoryResponse() {}

    public SendingHistoryResponse(Long id, Long shipmentId, String status, String location, LocalDateTime timestamp, String comment) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.status = status;
        this.location = location;
        this.timestamp = timestamp;
        this.comment = comment;
    }

    public Long getId() { return id; }
    public Long getShipmentId() { return shipmentId; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getComment() { return comment; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendingHistoryResponse that = (SendingHistoryResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(shipmentId, that.shipmentId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(location, that.location) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shipmentId, status, location, timestamp, comment);
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long shipmentId;
        private String status;
        private String location;
        private LocalDateTime timestamp;
        private String comment;

        Builder() {}

        public Builder id(Long id) { this.id = id; return this; }
        public Builder shipmentId(Long shipmentId) { this.shipmentId = shipmentId; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public Builder comment(String comment) { this.comment = comment; return this; }

        public SendingHistoryResponse build() {
            return new SendingHistoryResponse(id, shipmentId, status, location, timestamp, comment);
        }

        @Override
        public String toString() {
            return "SendingHistoryResponse.Builder(id=" + id + ", shipmentId=" + shipmentId + ", status=" + status + ", location=" + location + ", timestamp=" + timestamp + ", comment=" + comment + ")";
        }
    }
}