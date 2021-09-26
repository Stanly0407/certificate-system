package com.epam.esm.services.service;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.services.dto.OrderDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final String PREVIOUS_PAGE = "previousPage";
    private static final String NEXT_PAGE = "nextPage";

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
    public boolean createOrder(Long giftCertificateId, Long userId) {
        Optional<User> userOptional = userRepository.getById(userId);
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(giftCertificateId);

        if (userOptional.isPresent() && giftCertificateOptional.isPresent()) {
            GiftCertificate giftCertificate = giftCertificateOptional.get();
            User user = userOptional.get();
            BigDecimal orderPrice = giftCertificate.getPrice();
            orderRepository.saveOrder(Order.builder()
                    .orderPrice(orderPrice)
                    .giftCertificate(giftCertificate)
                    .user(user)
                    .build());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean payOrder(Long orderId, Long userId) {
        Optional<User> userOptional = userRepository.getById(userId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Long userIdOfOrder = null;
        boolean isAlreadyPaid = true;
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            userIdOfOrder = order.getUser().getId();
            isAlreadyPaid = order.isPaid();
        }
        if (userOptional.isPresent() && userId.equals(userIdOfOrder) && !isAlreadyPaid) {
            orderRepository.updateOrderStatus(orderId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Order> getPaidUserOrders(Long userId, int pageNumber, int pageSize) {
        // check if user does not exist
       return orderRepository.getPaidUserOrders(userId, pageNumber, pageSize);
    }

    public Map<String, Object> getPaginationInfo(Long userId, int pageNumber, int pageSize) {

        long pageQuantity = getPaidUserOrdersQuantity(pageSize, userId);

        //static common interface method - long pageQuantity int pageNumber, int pageSize
        Map<String, Object> pages = new HashMap<>();
        Integer previousPage = null;
        Integer nextPage = null;
        if ((pageNumber - 1) > 0) {
            previousPage = pageNumber - 1;
        }
        if ((pageNumber + 1) <= pageQuantity) {
            nextPage = pageNumber + 1;
        }

        if (previousPage != null) {
            pages.put(PREVIOUS_PAGE, previousPage);
        }
        if (nextPage != null) {
            pages.put(NEXT_PAGE, nextPage);
        }
        return pages;

    }

    public long getPaidUserOrdersQuantity(int pageSize, Long userId) {
        long countResult = orderRepository.getPaidUserOrdersQuantity(userId).size();
        if ((countResult % pageSize) != 0){
            return (countResult / pageSize) + 1;
        } else {
            return (countResult / pageSize);
        }
    }

    @Override
    public Optional<OrderDto> getPaidOrderById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = null;
        boolean isPaidOrder = false;

        if (orderOptional.isPresent()) {
            order = orderOptional.get();
            isPaidOrder = order.isPaid();
        }

        if (isPaidOrder) {
            OrderDto orderDto = OrderDto.builder()
                    .id(orderId)
                    .orderPrice(order.getOrderPrice())
                    .purchaseDate(order.getPurchaseDate())
                    .build();
            return Optional.of(orderDto);
        } else {
            return Optional.empty();
        }
    }


}
