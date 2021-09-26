package com.epam.esm.controllers;

import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.services.dto.OrderDto;
import com.epam.esm.services.forms.OrderCreateRequest;
import com.epam.esm.services.forms.OrderPayRequest;
import com.epam.esm.services.service.GiftCertificateService;
import com.epam.esm.services.service.OrderService;
import com.epam.esm.services.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("orders")
@Validated
public class OrderController implements BaseController {

    private static final String PREVIOUS_PAGE = "previousPage";
    private static final String NEXT_PAGE = "nextPage";

    private final OrderService orderService;
    private final LinkBuilder linkBuilder;
    private final UserService userService;

    public OrderController(OrderService orderService, LinkBuilder linkBuilder, UserService userService) {
        this.orderService = orderService;
        this.linkBuilder = linkBuilder;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        Long userId = orderCreateRequest.getUserId();
        Long giftCertificateId = orderCreateRequest.getGiftCertificateId();
        boolean isCreated = orderService.createOrder(giftCertificateId, userId);
        if (!isCreated) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<?> payOrder(@RequestBody OrderPayRequest orderPayRequest) {
        Long orderId = orderPayRequest.getOrderId();
        Long userId = orderPayRequest.getUserId();
        boolean isPaid = orderService.payOrder(orderId, userId);
        if (!isPaid) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("{orderId}")
    public ResponseEntity<?> getPaidOrderMainInfo(@PathVariable Long orderId) {
        Optional<OrderDto> orderOptional = orderService.getPaidOrderById(orderId);
        if (orderOptional.isPresent()) {
            return ResponseEntity.ok().body(orderOptional.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("info/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable Long userId,
                                           @RequestParam(defaultValue = "1", value = "page") @Min(1) int pageNumber,
                                           @RequestParam(defaultValue = "3", value = "size") @Min(2) int pageSize) {
        Optional<User> user = userService.getById(userId);

        if (user.isPresent()) {
            List<Order> paidUserOrders = orderService.getPaidUserOrders(userId, pageNumber, pageSize);
            if (!paidUserOrders.isEmpty()) {
                long pageQuantity = orderService.getPaidUserOrdersQuantity(pageSize,userId);
                Map<String, Object> params = BaseController.getPaginationInfo(pageQuantity, pageNumber);
                String uriString = linkTo(methodOn(OrderController.class)
                        .getUserOrders(userId, pageNumber, pageSize))
                        .toUriComponentsBuilder().buildAndExpand().toString();
                params.put("userId", userId);
                params.put("page", pageNumber);
                params.put("size", pageSize);
                params.put("uri", uriString);

                linkBuilder.addSelfLinks(paidUserOrders, OrderController.class);
                List<Link> links = linkBuilder.createPaginationLinks(params, OrderController.class);

                CollectionModel<Order> result = CollectionModel.of(paidUserOrders, links);
                return ResponseEntity.ok().body(result);
            }
            return ResponseEntity.ok().body(paidUserOrders);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
