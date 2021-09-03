package com.epam.esm.service.dto;


import com.epam.esm.entities.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GiftCertificateDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;

    private List<Tag> tags;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public static class Builder {
        private GiftCertificateDto giftCertificateDto;

        public Builder() {
            giftCertificateDto = new GiftCertificateDto();
        }

        public Builder id(Long id) {
            giftCertificateDto.id = id;
            return this;
        }

        public Builder name(String name) {
            giftCertificateDto.name = name;
            return this;
        }

        public Builder description(String description) {
            giftCertificateDto.description = description;
            return this;
        }

        public Builder price(BigDecimal price) {
            giftCertificateDto.price = price;
            return this;
        }

        public Builder duration(int duration) {
            giftCertificateDto.duration = duration;
            return this;
        }

        public Builder createDate(LocalDateTime createDate) {
            giftCertificateDto.createDate = createDate;
            return this;
        }

        public Builder lastUpdateDate(LocalDateTime lastUpdateDate) {
            giftCertificateDto.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public Builder tags(List<Tag> tags) {
            giftCertificateDto.tags = tags;
            return this;
        }

        public GiftCertificateDto build() {
            return giftCertificateDto;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GiftCertificateDto that = (GiftCertificateDto) o;
        return duration == that.duration &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate, tags);
    }

}
