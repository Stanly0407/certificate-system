package com.epam.esm.services.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderCreateRequest {

    @NonNull
    private Long userId;

    @NonNull
    private Long giftCertificateId;

}
