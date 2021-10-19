package com.epam.esm.services.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class GiftCertificatePartialUpdateRequest {

    @Size(min = 3, max = 32)
    private String name;

    @Size(min = 3, max = 1000)
    private String description;

    @DecimalMin(value = "0.01", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @Min(1)
    @Max(300)
    private Integer duration; // the validity of the gift certificate

}
