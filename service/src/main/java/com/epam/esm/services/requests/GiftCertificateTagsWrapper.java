package com.epam.esm.services.requests;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiftCertificateTagsWrapper {

    @NotBlank
    @Size(min = 3, max = 32)
    private String name;

    @NotBlank
    @Size(min = 3, max = 1000)
    private String description;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @NotNull
    @Min(1)
    @Max(300)
    private int duration; // the validity of the gift certificate

    @NotNull
    private List<@Valid Tag> tags;

    public GiftCertificate getGiftCertificate() {
        return GiftCertificate.builder()
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .build();
    }

}
