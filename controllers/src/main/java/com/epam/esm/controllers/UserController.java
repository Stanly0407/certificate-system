package com.epam.esm.controllers;

import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.service.OrderService;
import com.epam.esm.services.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 * A class {@code UserController} as request handler defines method which accepts
 * user Tag requests and performs interactions on the data model objects by using service layer.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
@RestController
@RequestMapping("users")
@Validated
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final LinkBuilder linkBuilder;

    public UserController(UserService userService, OrderService orderService, LinkBuilder linkBuilder) {
        this.userService = userService;
        this.orderService = orderService;
        this.linkBuilder = linkBuilder;
    }

    /**
     * Finds the user by id;
     *
     * @param id is a unique field of the user;
     * @return ResponseEntity representing the whole HTTP response: status code 200, headers and user in the body;
     * @throws ResourceNotFoundException if the requested user is not found;
     */
    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable @Min(1) Long id) throws ResourceNotFoundException {
        User user = userService.getById(id);
        linkBuilder.addSelfLink(user, id, UserController.class);
        return ResponseEntity.ok(user);
    }

    /**
     * Returns all users;
     *
     * @return ResponseEntity representing the whole HTTP response: status code 200, headers and
     * the collection <code>List</code> of users in the body;
     * @throws ResourceNotFoundException if the requested resource is not found;
     */
    @GetMapping
    public ResponseEntity<CollectionModel<User>> getAllUsers(
            @Valid @RequestParam(defaultValue = "1", value = "page") @Min(1) int pageNumber,
            @Valid @RequestParam(defaultValue = "5", value = "size") @Min(1) @Max(50) int pageSize)
            throws ResourceNotFoundException {
        List<User> users = userService.getAllUsers(pageNumber, pageSize);
        linkBuilder.addSelfLinks(users, UserController.class);
        long pageQuantity = userService.getUsersPaginationInfo(pageNumber, pageSize);
        String uriString = linkTo(methodOn(UserController.class).getAllUsers(pageNumber, pageSize))
                .toUriComponentsBuilder().buildAndExpand().toString();
        List<Link> links = linkBuilder.createPaginationLinks(pageQuantity, pageNumber, uriString);
        CollectionModel<User> result = CollectionModel.of(users, links);
        return ResponseEntity.ok()
                // info with common pagination pages quantity
                .header("x-total-page-count", String.valueOf(pageQuantity))
                .body(result);
    }

    /**
     * Finds paid user orders by user id;
     *
     * @param userId     is a unique field of the user;
     * @param pageNumber is a requested number of page with search result;
     * @param pageSize   is a number of request result displayed
     * @return ResponseEntity representing the whole HTTP response: status code, headers, and body with collection
     * <code>List</code> contains users or empty collection <code>List</code>;
     * @throws ResourceNotFoundException if the requested tag is not found;
     */
    @GetMapping("{userId}/purchases")
    public ResponseEntity<?> getPaidUserOrders(@PathVariable @Min(1) Long userId,
                                           @RequestParam(defaultValue = "1", value = "page") @Min(1) int pageNumber,
                                           @RequestParam(defaultValue = "5", value = "size") @Min(1) @Max(50) int pageSize)
            throws ResourceNotFoundException {
        long pageQuantity = orderService.getUsersPaginationInfo(pageSize, pageNumber, userId);
        List<Order> paidUserOrders = orderService.getPaidUserOrders(userId, pageNumber, pageSize);
        if (paidUserOrders.isEmpty()) {
            return ResponseEntity.ok().body(paidUserOrders);
        } else {
            String uriString = linkTo(methodOn(UserController.class).getPaidUserOrders(userId, pageNumber, pageSize))
                    .toUriComponentsBuilder().buildAndExpand().toString();
            paidUserOrders.forEach(e -> linkBuilder.addSelfLink(e, e.getId(), OrderController.class));
            List<Link> links = linkBuilder.createPaginationLinks(pageQuantity, pageNumber, uriString);
            CollectionModel<Order> result = CollectionModel.of(paidUserOrders, links);
            return ResponseEntity.ok()
                    // info with common pagination pages quantity
                    .header("x-total-page-count", String.valueOf(pageQuantity))
                    .body(result);
        }
    }

}
