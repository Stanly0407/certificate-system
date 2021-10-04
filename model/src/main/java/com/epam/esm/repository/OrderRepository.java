package com.epam.esm.repository;

import com.epam.esm.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    long saveOrder(Order order);

    void updateOrderStatus(Long orderId);

    Optional<Order> findById(Long orderId);

    long getPaidUserOrdersQuantity(Long userId);

    List<Order> getPaidUserOrders(Long userId, int pageNumber, int pageSize);

}
