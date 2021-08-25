package com.epam.esm.dto;

import com.epam.esm.model.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class GiftCertificateDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private LocalDateTime createDate;
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

}
