package com.epam.esm.controllers;

import com.epam.esm.entities.User;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
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
import java.util.Optional;

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
public class UserController implements BaseController {

    private final UserService userService;
    private final LinkBuilder linkBuilder;

    public UserController(UserService userService, LinkBuilder linkBuilder) {
        this.userService = userService;
        this.linkBuilder = linkBuilder;
    }

    /**
     * Finds the user by id;
     *
     * @param id is a unique field of the user;
     * @return the tag object
     * @throws ResourceNotFoundException if the requested user is not found;
     */
    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable @Min(1) Long id) throws ResourceNotFoundException {
        Optional<User> userOptional = userService.getById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            linkBuilder.addSelfLink(user, id, UserController.class);
            return ResponseEntity.ok(user);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    /**
     * Returns all users;
     *
     * @return the collection <code>List</code> of all users;
     */
    @GetMapping
    public ResponseEntity<CollectionModel<User>> getAllUsers(
            @Valid @RequestParam(defaultValue = "1", value = "page") @Min(1) int pageNumber,
            @Valid @RequestParam(defaultValue = "5", value = "size") @Min(1) @Max(50) int pageSize) throws ResourceNotFoundException {
        List<User> users = userService.getAllUsers(pageNumber, pageSize);
        linkBuilder.addSelfLinks(users, UserController.class);
        long pageQuantity = userService.getUsersPaginationInfo(pageNumber, pageSize);
        String uriString = linkTo(methodOn(UserController.class).getAllUsers(pageNumber, pageSize))
                .toUriComponentsBuilder().buildAndExpand().toString();
        List<Link> links = linkBuilder.createPaginationLinks(pageQuantity, pageNumber, uriString);
        CollectionModel<User> result = CollectionModel.of(users, links);
        return ResponseEntity.ok().body(result);
    }

}
