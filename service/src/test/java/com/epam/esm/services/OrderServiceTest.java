package com.epam.esm.services;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.services.dto.OrderDto;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.service.OrderServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private static final Long TEST_ID = 1L;
    private static final int PAGE = 1;
    private static final int SIZE = 5;
    private static final GiftCertificate GIFT_CERTIFICATE = new GiftCertificate(1L, "test", "test",
            new BigDecimal("1.00"), 1, LocalDateTime.of(2021, 8, 26, 10, 10, 10),
            LocalDateTime.of(2021, 8, 26, 10, 10, 10), new ArrayList<>(), new ArrayList<>());
    private static final User USER = User.builder().id(1L).name("First").lastname("LastnameFirst").login("test1").password("1111").build();
    private static final Order ORDER = Order.builder().orderPrice(new BigDecimal("1.00"))
            .purchaseDate(LocalDateTime.of(2021, 8, 26, 10, 10, 10))
            .user(USER).giftCertificate(GIFT_CERTIFICATE).isPaid(false).build();
    private static final Order PAID_ORDER = Order.builder().orderPrice(new BigDecimal("1.00"))
            .purchaseDate(LocalDateTime.of(2021, 8, 26, 10, 10, 10))
            .user(USER).giftCertificate(GIFT_CERTIFICATE).isPaid(true).build();

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(orderRepository, userRepository, giftCertificateRepository);
    }

    @Test
    public void createOrderTestShouldReturnNewOrderId() throws ResourceNotFoundException {
        Order order = Order.builder().orderPrice(new BigDecimal("1.00")).user(USER).giftCertificate(GIFT_CERTIFICATE).build();
        when(userRepository.getById(TEST_ID)).thenReturn(Optional.of(USER));
        when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.of(GIFT_CERTIFICATE));
        when(orderRepository.saveOrder(order)).thenReturn(TEST_ID);

        Long actual = orderService.createOrder(TEST_ID, TEST_ID);
        Assertions.assertEquals(TEST_ID, actual);

        Mockito.verify(userRepository).getById(anyLong());
        Mockito.verify(giftCertificateRepository).findById(anyLong());
        Mockito.verify(orderRepository).saveOrder(any());
    }

    @Test
    public void createOrderTestShouldThrowExceptionIfNonexistentUser() {
        when(userRepository.getById(TEST_ID)).thenReturn(Optional.empty());
        when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.of(GIFT_CERTIFICATE));

        Executable executable = () -> orderService.createOrder(TEST_ID, TEST_ID);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);

        Mockito.verify(userRepository).getById(anyLong());
        Mockito.verify(giftCertificateRepository).findById(anyLong());
    }

    @Test
    public void createOrderTestShouldThrowExceptionIfNonexistentGiftCertificate() {
        when(userRepository.getById(TEST_ID)).thenReturn(Optional.of(USER));
        when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        Executable executable = () -> orderService.createOrder(TEST_ID, TEST_ID);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);

        Mockito.verify(userRepository).getById(anyLong());
        Mockito.verify(giftCertificateRepository).findById(anyLong());
    }

    @Test
    public void payOrderTest() throws ResourceNotFoundException, BadRequestException {
        when(userRepository.getById(TEST_ID)).thenReturn(Optional.of(USER));
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.of(ORDER));
        doNothing().when(orderRepository).updateOrderStatus(TEST_ID);

        orderService.payOrder(TEST_ID, TEST_ID);

        Mockito.verify(userRepository).getById(anyLong());
        Mockito.verify(orderRepository).findById(anyLong());
        Mockito.verify(orderRepository).updateOrderStatus(anyLong());
    }

    @Test
    public void payOrderTestShouldThrowExceptionIfOrderPaid() {
        when(userRepository.getById(TEST_ID)).thenReturn(Optional.of(USER));
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.of(PAID_ORDER));

        Executable executable = () -> orderService.payOrder(TEST_ID, TEST_ID);

        Assertions.assertThrows(BadRequestException.class, executable);

        Mockito.verify(userRepository).getById(anyLong());
        Mockito.verify(orderRepository).findById(anyLong());
    }

    @Test
    public void payOrderTestShouldThrowExceptionIfNonExistentOrder() {
        when(userRepository.getById(TEST_ID)).thenReturn(Optional.of(USER));
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        Executable executable = () -> orderService.payOrder(TEST_ID, TEST_ID);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);

        Mockito.verify(userRepository).getById(anyLong());
        Mockito.verify(orderRepository).findById(anyLong());
    }

    @Test
    public void payOrderTestShouldThrowExceptionIfNonExistentUser() {
        when(userRepository.getById(TEST_ID)).thenReturn(Optional.empty());
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.of(ORDER));

        Executable executable = () -> orderService.payOrder(TEST_ID, TEST_ID);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);

        Mockito.verify(userRepository).getById(anyLong());
        Mockito.verify(orderRepository).findById(anyLong());
    }

    @Test
    public void payOrderTestShouldThrowExceptionIfDifferentUserId() {
        User user = User.builder().id(2L).build();
        when(userRepository.getById(TEST_ID)).thenReturn(Optional.of(user));
        Order order = Order.builder().id(1L).orderPrice(new BigDecimal("1.00")).user(user).isPaid(false).build();
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.of(order));

        Executable executable = () -> orderService.payOrder(TEST_ID, 1L);

        Assertions.assertThrows(BadRequestException.class, executable);

        Mockito.verify(userRepository).getById(anyLong());
        Mockito.verify(orderRepository).findById(anyLong());
    }

    @Test
    public void getUsersPaginationInfoTest() throws ResourceNotFoundException {
        when(orderRepository.getPaidUserOrdersQuantity(TEST_ID)).thenReturn(10L);
        long actual = 2L;

        long expected = orderService.getUsersPaginationInfo(SIZE, PAGE, TEST_ID);

        Assertions.assertEquals(expected, actual);

        Mockito.verify(orderRepository).getPaidUserOrdersQuantity(anyLong());
    }

    @Test
    public void getUsersPaginationInfoTestShouldThrowException() {
        when(orderRepository.getPaidUserOrdersQuantity(TEST_ID)).thenReturn(10L);
        int nonExistentPage = 3;

        Executable executable = () -> orderService.getUsersPaginationInfo(SIZE, nonExistentPage, TEST_ID);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);

        Mockito.verify(orderRepository).getPaidUserOrdersQuantity(anyLong());
    }

    @Test
    public void getPaidOrderByIdTest() throws ResourceNotFoundException {
        OrderDto expectedOrderDto = OrderDto.builder().id(1L).orderPrice(new BigDecimal("1.00"))
                .purchaseDate(LocalDateTime.of(2021, 8, 26, 10, 10, 10)).build();
        Optional<OrderDto> expected = Optional.of(expectedOrderDto);
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.of(PAID_ORDER));

        Optional<OrderDto> actual = orderService.getPaidOrderById(TEST_ID);

        Assertions.assertEquals(expected, actual);

        Mockito.verify(orderRepository).findById(anyLong());

    }

    @Test
    public void getPaidOrderByIdTestShouldThrowExceptionIfNonexistentOrder() {
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        Executable executable = () -> orderService.getPaidOrderById(TEST_ID);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);

        Mockito.verify(orderRepository).findById(anyLong());
    }

    @Test
    public void getPaidOrderByIdTestShouldThrowExceptionIfUnpaidOrder() {
        when(orderRepository.findById(TEST_ID)).thenReturn(Optional.of(ORDER));

        Executable executable = () -> orderService.getPaidOrderById(TEST_ID);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);

        Mockito.verify(orderRepository).findById(anyLong());
    }

}
