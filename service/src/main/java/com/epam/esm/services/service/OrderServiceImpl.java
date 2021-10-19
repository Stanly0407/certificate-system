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
import com.epam.esm.services.requests.OrderCreateRequest;
import com.epam.esm.services.requests.PaymentRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.services.exceptions.ExceptionMessageType.INCORRECT_PARAMETERS;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);
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
    public Long createOrder(OrderCreateRequest orderCreateRequest) throws ResourceNotFoundException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Long giftCertificateId = orderCreateRequest.getGiftCertificateId();
        Optional<User> userOptional = userRepository.findByLogin(login);
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(giftCertificateId);
        if (!giftCertificateOptional.isPresent()) {
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
    public void payOrder(PaymentRequest paymentRequest) throws ResourceNotFoundException, BadRequestException {
        Long orderId = paymentRequest.getOrderId();
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userRepository.findByLogin(login);
        User user = userOptional.get();
        Optional<Order> orderOptional = orderRepository.findById(orderId, user);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.isPaid()) {
                LOGGER.info("Order id=" + orderId + " is already paid!");
                throw new BadRequestException(INCORRECT_PARAMETERS);
            } else {
                orderRepository.updateOrderStatus(orderId);
            }
        } else {
            throw new ResourceNotFoundException(orderId);
        }
    }

    @Override
    public List<Order> getUserOrders(Long userId, Boolean isPaid, int pageNumber, int pageSize)
            throws ResourceNotFoundException {
        if (userId == null) {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> userOptional = userRepository.findByLogin(login);
            userId = userOptional.get().getId();
        }
        Optional<User> user = userRepository.getById(userId);
        if (user.isPresent()) {
            if (isPaid != null) {
                return orderRepository.getUserOrders(userId, isPaid, pageNumber, pageSize);
            } else {
                return orderRepository.getAllUserOrders(userId, pageNumber, pageSize);
            }
        } else {
            throw new ResourceNotFoundException(userId);
        }
    }

    public long getOrdersPaginationInfo(Integer pageSize, Boolean isPaid, Integer pageNumber, Long userId)
            throws ResourceNotFoundException {
        long countResult;
        if (userId == null) {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> userOptional = userRepository.findByLogin(login);
            userId = userOptional.get().getId();
        }
        if (isPaid != null) {
            countResult = orderRepository.getUserOrdersQuantity(userId, isPaid);
        } else {
            countResult = orderRepository.getAllUserOrdersQuantity(userId);
        }
        long pageQuantity;

        if ((countResult % pageSize) != 0) {
            pageQuantity = (countResult / pageSize) + 1;
        } else {
            pageQuantity = (countResult / pageSize);
        }

        if (pageQuantity < pageNumber) {
            throw new ResourceNotFoundException(pageNumber);
        }
        return pageQuantity;
    }

    @Override
    public Object getOrderById(Long orderId, Boolean isPaid) throws ResourceNotFoundException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userRepository.findByLogin(login);
        User user = userOptional.get();
        Optional<Order> orderOptional = orderRepository.findById(orderId, user);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if ((isPaid == null && order.isPaid()) || (isPaid != null && isPaid && order.isPaid())) {
                BigDecimal orderPrice = order.getOrderPrice();
                LocalDateTime purchaseDate = order.getPurchaseDate();
                return new OrderDto(orderId, orderPrice, purchaseDate);
            } else if ((isPaid == null && !order.isPaid()) || (isPaid != null && !isPaid && !order.isPaid())) {
                return orderOptional.get();
            } else {
                throw new ResourceNotFoundException(orderId);
            }
        } else {
            throw new ResourceNotFoundException(orderId);
        }
    }

}
