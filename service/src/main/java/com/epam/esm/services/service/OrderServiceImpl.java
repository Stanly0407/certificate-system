package com.epam.esm.services.service;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.services.dto.OrderDto;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.services.exceptions.ExceptionMessageType.INCORRECT_PARAMETERS;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
                            GiftCertificateRepository giftCertificateRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    public Long createOrder(Long giftCertificateId, Long userId) throws ResourceNotFoundException {
        Optional<User> userOptional = userRepository.getById(userId);
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(giftCertificateId);
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException(userId);
        } else if (!giftCertificateOptional.isPresent()) {
            throw new ResourceNotFoundException(giftCertificateId);
        } else {
            GiftCertificate giftCertificate = giftCertificateOptional.get();
            User user = userOptional.get();
            BigDecimal orderPrice = giftCertificate.getPrice();
            Order newOrder = Order.builder().orderPrice(orderPrice).giftCertificate(giftCertificate).user(user).build();
            return orderRepository.saveOrder(newOrder);
        }
    }

    @Override
    public void payOrder(Long orderId, Long userId) throws ResourceNotFoundException, BadRequestException {
        Optional<User> userOptional = userRepository.getById(userId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Long userIdOfOrder;
        if (!orderOptional.isPresent()) {
            throw new ResourceNotFoundException(orderId);
        } else if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException(userId);
        } else {
            Order order = orderOptional.get();
            userIdOfOrder = order.getUser().getId();
            if (!userId.equals(userIdOfOrder) || order.isPaid()) {
                throw new BadRequestException(INCORRECT_PARAMETERS);
            } else {
                orderRepository.updateOrderStatus(orderId);
            }
        }
    }

    @Override
    public List<OrderDto> getPaidUserOrders(Long userId, int pageNumber, int pageSize) throws ResourceNotFoundException {
        Optional<User> user = userRepository.getById(userId);
        if (user.isPresent()) {
            List<Order> orders = orderRepository.getPaidUserOrders(userId, pageNumber, pageSize);
            return orders.stream().map(e -> OrderDto.builder()
                    .id(e.getId()).orderPrice(e.getOrderPrice()).purchaseDate(e.getPurchaseDate())
                    .build()).collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException(userId);
        }
    }

    public long getUsersPaginationInfo(Integer pageSize, Integer pageNumber, Long userId) throws ResourceNotFoundException {
        long countResult = orderRepository.getPaidUserOrdersQuantity(userId);
        long pageQuantity;

        if ((countResult % pageSize) != 0) {
            pageQuantity = (countResult / pageSize) + 1;
        } else {
            pageQuantity = (countResult / pageSize);
        }

        if (pageQuantity < pageNumber) {
            throw new ResourceNotFoundException();
        }
        return pageQuantity;
    }

    @Override
    public OrderDto getPaidOrderById(Long orderId) throws ResourceNotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.isPaid()) {
                BigDecimal orderPrice = order.getOrderPrice();
                LocalDateTime purchaseDate = order.getPurchaseDate();
                return new OrderDto(orderId, orderPrice, purchaseDate);
            } else {
                throw new ResourceNotFoundException(orderId);
            }
        } else {
            throw new ResourceNotFoundException(orderId);
        }
    }

}
