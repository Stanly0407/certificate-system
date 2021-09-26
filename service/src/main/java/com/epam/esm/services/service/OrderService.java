package com.epam.esm.services.service;

import com.epam.esm.entities.Order;
import com.epam.esm.services.dto.OrderDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderService {

    boolean createOrder(Long giftCertificateId, Long userId);

    boolean payOrder(Long orderId, Long userId);

    List<Order> getPaidUserOrders(Long userId, int pageNumber, int pageSize);

    Optional<OrderDto> getPaidOrderById(Long orderId);

    Map<String, Object> getPaginationInfo(Long userId, int pageNumber, int pageSize);

    long getPaidUserOrdersQuantity(int pageSize, Long userId);

}
