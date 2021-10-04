package com.epam.esm.controllers;

import com.epam.esm.services.dto.OrderDto;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.forms.OrderCreateRequest;
import com.epam.esm.services.forms.OrderPayRequest;
import com.epam.esm.services.service.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("orders")
@Validated
public class OrderController implements BaseController {

    private final OrderService orderService;
    private final LinkBuilder linkBuilder;

    public OrderController(OrderService orderService, LinkBuilder linkBuilder) {
        this.orderService = orderService;
        this.linkBuilder = linkBuilder;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderCreateRequest orderCreateRequest) throws ResourceNotFoundException {
        Long userId = orderCreateRequest.getUserId();
        Long giftCertificateId = orderCreateRequest.getGiftCertificateId();
        Long newOrderId = orderService.createOrder(giftCertificateId, userId);
        Link newOrderLink = linkBuilder.getSelfLink(newOrderId, OrderController.class);
        return ResponseEntity.created(newOrderLink.toUri()).build();
    }

    @PostMapping("/payment")
    public ResponseEntity<?> payOrder(@RequestBody @Valid OrderPayRequest orderPayRequest)
            throws BadRequestException, ResourceNotFoundException {
        Long orderId = orderPayRequest.getOrderId();
        Long userId = orderPayRequest.getUserId();
        orderService.payOrder(orderId, userId);
        return ResponseEntity.ok().build();

    }

    @GetMapping("info/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable @Min(1) Long userId,
                                           @RequestParam(defaultValue = "1", value = "page") @Min(1) int pageNumber,
                                           @RequestParam(defaultValue = "5", value = "size") @Min(1) @Max(50) int pageSize)
            throws ResourceNotFoundException {
        long pageQuantity = orderService.getUsersPaginationInfo(pageSize, pageNumber, userId);
        List<OrderDto> paidUserOrders = orderService.getPaidUserOrders(userId, pageNumber, pageSize);
        if (!paidUserOrders.isEmpty()) {
            String uriString = linkTo(methodOn(OrderController.class).getUserOrders(userId, pageNumber, pageSize))
                    .toUriComponentsBuilder().buildAndExpand().toString();
            paidUserOrders.forEach(e -> linkBuilder.addSelfLink(e, e.getId(), OrderController.class));
            List<Link> links = linkBuilder.createPaginationLinks(pageQuantity, pageNumber, uriString);
            CollectionModel<OrderDto> result = CollectionModel.of(paidUserOrders, links);
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.ok().body(paidUserOrders);
        }
    }

    @GetMapping("{orderId}")
    public ResponseEntity<?> getPaidOrderMainInfo(@PathVariable @Min(1) Long orderId) throws ResourceNotFoundException {
        Optional<OrderDto> orderOptional = orderService.getPaidOrderById(orderId);
        if (orderOptional.isPresent()) {
            OrderDto order = orderOptional.get();
            linkBuilder.addSelfLink(order, orderId, OrderController.class);
            return ResponseEntity.ok().body(order);
        } else {
            throw new ResourceNotFoundException(orderId);
        }
    }

}
