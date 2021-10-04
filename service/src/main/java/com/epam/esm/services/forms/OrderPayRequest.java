package com.epam.esm.services.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderPayRequest {

    @NonNull
    @Min(1)
    private Long userId;

    @NonNull
    @Min(1)
    private Long orderId;

}
