package com.epam.esm.controllers;

import com.epam.esm.entities.Order;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.requests.OrderCreateRequest;
import com.epam.esm.services.requests.PaymentRequest;
import com.epam.esm.services.service.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A class {@code OrderController} as request handler defines method which accepts
 * user Order requests and performs interactions on the data model objects by using service layer.
 *
 * @author Sviatlana Shelestava
 * @since 2.0
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
        Long newOrderId = orderService.createOrder(orderCreateRequest);
        Link newOrderLink = linkBuilder.getSelfLink(newOrderId, OrderController.class);
        return ResponseEntity.created(newOrderLink.toUri()).build();
    }

    /**
     * Pays user Order - only change order payment status;
     *
     * @param paymentRequest contains the id of the order;
     * @return ResponseEntity representing the whole HTTP response: status code 201 and headers;
     * @throws ResourceNotFoundException if invalid parameters input for order creation;
     */
    @PostMapping("payment")
    public ResponseEntity<?> payOrder(@RequestBody @Valid PaymentRequest paymentRequest)
            throws BadRequestException, ResourceNotFoundException {
        orderService.payOrder(paymentRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * Finds paid user order by its id;
     *
     * @param orderId is a unique field of the order;
     * @param isPaid  is an order payment status;
     * @return ResponseEntity representing the whole HTTP response: status code 200, headers and order in the body;
     * @throws ResourceNotFoundException if the requested tag is not found;
     */
    @GetMapping("{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable @Min(1) Long orderId,
                                          @RequestParam(required = false) Boolean isPaid) throws ResourceNotFoundException {
        Object order = orderService.getOrderById(orderId, isPaid);
        linkBuilder.addSelfLink((RepresentationModel<?>) order, orderId, OrderController.class);
        return ResponseEntity.ok().body(order);
    }

    /**
     * Finds paid user orders by user id;
     *
     * @param pageNumber is a requested number of page with search result;
     * @param pageSize   is a number of request result displayed;
     * @param isPaid     is an an order payment status;
     * @return ResponseEntity representing the whole HTTP response: status code, headers, and body with collection
     * <code>List</code> contains users or empty collection <code>List</code>;
     * @throws ResourceNotFoundException if the requested tag is not found;
     */
    @GetMapping
    public ResponseEntity<?> getOrders(@RequestParam(defaultValue = "1", value = "page") @Min(1) int pageNumber,
                                       @RequestParam(defaultValue = "5", value = "size") @Min(1) @Max(50) int pageSize,
                                       @RequestParam(required = false) Boolean isPaid)
            throws ResourceNotFoundException {
        long pageQuantity = orderService.getOrdersPaginationInfo(pageSize, isPaid, pageNumber, null);
        List<Order> userOrders = orderService.getUserOrders(null, isPaid, pageNumber, pageSize);
        if (userOrders.isEmpty()) {
            return ResponseEntity.ok().body(userOrders);
        } else {
            String uriString = linkTo(methodOn(UserController.class).getUserOrders(null, pageNumber, pageSize, isPaid))
                    .toUriComponentsBuilder().buildAndExpand().toString();
            userOrders.forEach(e -> linkBuilder.addSelfLink(e, e.getId(), OrderController.class));
            List<Link> links = linkBuilder.createPaginationLinks(pageQuantity, pageNumber, uriString);
            CollectionModel<Order> result = CollectionModel.of(userOrders, links);
            return ResponseEntity.ok().header("x-total-page-count", String.valueOf(pageQuantity)).body(result);
        }
    }

}
