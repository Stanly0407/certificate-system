package com.epam.esm.controllers;

import com.epam.esm.services.dto.OrderDto;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.forms.OrderCreateRequest;
import com.epam.esm.services.service.OrderService;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * A class {@code OrderController} as request handler defines method which accepts
 * user Order requests and performs interactions on the data model objects by using service layer.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
@RestController
@RequestMapping("orders")
@Validated
public class OrderController {

    private final OrderService orderService;
    private final LinkBuilder linkBuilder;

    public OrderController(OrderService orderService, LinkBuilder linkBuilder) {
        this.orderService = orderService;
        this.linkBuilder = linkBuilder;
    }

    /**
     * Creates a new user Order;
     *
     * @param orderCreateRequest is request body form with user id and ordered giftCertificate id;
     * @return ResponseEntity representing the whole HTTP response: status code 201 and headers;
     * @throws ResourceNotFoundException if invalid parameters input for order creation;
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderCreateRequest orderCreateRequest)
            throws ResourceNotFoundException {
        Long userId = orderCreateRequest.getUserId();
        Long giftCertificateId = orderCreateRequest.getGiftCertificateId();
        Long newOrderId = orderService.createOrder(giftCertificateId, userId);
        Link newOrderLink = linkBuilder.getSelfLink(newOrderId, OrderController.class);
        return ResponseEntity.created(newOrderLink.toUri()).build();
    }

    /**
     * Pays user Order - only change order payment status;
     *
     * @param orderId is a unique field of the order;
     * @return ResponseEntity representing the whole HTTP response: status code 201 and headers;
     * @throws ResourceNotFoundException if invalid parameters input for order creation;
     */
    @PostMapping("/payment/{orderId}")
    public ResponseEntity<?> payOrder(@PathVariable @Min(1) Long orderId)
            throws BadRequestException, ResourceNotFoundException {
        orderService.payOrder(orderId);
        return ResponseEntity.ok().build();
    }



    /**
     * Finds paid user order by its id;
     *
     * @param orderId is a unique field of the order;
     * @return ResponseEntity representing the whole HTTP response: status code 200, headers and orderDto in the body;
     * @throws ResourceNotFoundException if the requested tag is not found;
     */
    @GetMapping("{orderId}")
    public ResponseEntity<?> getPaidOrderMainInfo(@PathVariable @Min(1) Long orderId) throws ResourceNotFoundException {
        OrderDto order = orderService.getPaidOrderById(orderId);
        linkBuilder.addSelfLink(order, orderId, OrderController.class);
        return ResponseEntity.ok().body(order);
    }

}
