package com.logistic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "entrants", itemRelation = "entrant")
@Schema(description = "Информация об участнике")
public class EntrantResponse extends RepresentationModel<EntrantResponse> {
    @Schema(description = "Уникальный идентификатор участника", example = "12345678")
    private Long id;

    @Schema(description = "Имя участника", example = "Ivan")
    private String firstname;

    @Schema(description = "Фамилия участника", example = "Ivanov")
    private String lastname;

    @Schema(description = "E-mail адрес участника", example = "example@mail.ru")
    private String email;

    @Schema(description = "Телефонный номер участника", example = "88005553535")
    private String phone;

    @Schema(description = "Адрес участника", example = "Ул.Пушкина, д.Колотушкина")
    private String address;

    protected EntrantResponse() {}

    public EntrantResponse(Long id, String firstname, String lastname, String email, String phone, String address) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Long getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntrantResponse that = (EntrantResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, phone, address);
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String firstname;
        private String lastname;
        private String email;
        private String phone;
        private String address;

        Builder() {}

        public Builder id(Long id) { this.id = id; return this; }
        public Builder firstname(String firstname) { this.firstname = firstname; return this; }
        public Builder lastname(String lastname) { this.lastname = lastname; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder address(String address) { this.address = address; return this; }

        public EntrantResponse build() {
            return new EntrantResponse(id, firstname, lastname, email, phone, address);
        }

        @Override
        public String toString() {
            return "EntrantResponse.Builder(id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email + ", phone=" + phone + ", address=" + address + ")";
        }
    }
}