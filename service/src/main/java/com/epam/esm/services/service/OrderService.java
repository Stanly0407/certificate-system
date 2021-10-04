package com.epam.esm.services.service;

import com.epam.esm.services.dto.OrderDto;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Long createOrder(Long giftCertificateId, Long userId) throws ResourceNotFoundException;

    void payOrder(Long orderId, Long userId) throws ResourceNotFoundException, BadRequestException;

    List<OrderDto> getPaidUserOrders(Long userId, int pageNumber, int pageSize) throws ResourceNotFoundException;

    Optional<OrderDto> getPaidOrderById(Long orderId) throws ResourceNotFoundException;

    long getUsersPaginationInfo(Integer pageSize, Integer pageNumber, Long userId) throws ResourceNotFoundException;

}
