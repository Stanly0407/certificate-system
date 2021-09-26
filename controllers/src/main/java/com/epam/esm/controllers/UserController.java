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
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class UserController {

    private static final String PREVIOUS_PAGE = "previousPage";
    private static final String NEXT_PAGE = "nextPage";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Finds the user by id;
     *
     * @param id is a unique field of the user;
     * @return the tag object
     * @throws ResourceNotFoundException if the requested user is not found;
     */
    @GetMapping("{id}")
    public User getUser(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<User> userOptional = userService.getById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
            return user;
        } else {
            throw new ResourceNotFoundException(" (user id = " + id + ")");
        }
    }

    /**
     * Returns all users;
     *
     * @return the collection <code>List</code> of all users;
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @Valid @RequestParam("page") @Min(value = 1, message = "incorrect page number value") int pageNumber,
            @Valid @RequestParam("size") @Min(value = 1, message = "incorrect page size value") int pageSize) {

        List<User> users = userService.getAllUsers(pageNumber, pageSize);

        if (!users.isEmpty()) {
            for (User user : users) {
                Long userId = user.getId();
                Link selfLink = linkTo(UserController.class).slash(userId).withSelfRel();
                user.add(selfLink);
            }
            Map<String, Integer> pages = userService.getUsersPaginationInfo(pageNumber, pageSize);
            List<Link> links = createPaginationLinks(pages, pageSize);

            Link link = linkTo(methodOn(UserController.class).getAllUsers(pageNumber, pageSize)).withSelfRel();
            links.add(link);

            CollectionModel<User> result = CollectionModel.of(users, links);

            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private List<Link> createPaginationLinks(Map<String, Integer> pages, int pageSize) {
        List<Link> links = new ArrayList<>();
        if (pages.containsKey(PREVIOUS_PAGE)) {
            int previousPage = pages.get(PREVIOUS_PAGE);
            Link previousPageLink = linkTo(methodOn(UserController.class).getAllUsers(previousPage, pageSize)).withRel(PREVIOUS_PAGE);
            links.add(previousPageLink);
        }
        if (pages.containsKey(NEXT_PAGE)) {
            int nextPage = pages.get(NEXT_PAGE);
            Link nextPageLink = linkTo(methodOn(UserController.class).getAllUsers(nextPage, pageSize)).withRel(NEXT_PAGE);
            links.add(nextPageLink);
        }
        return links;
    }


}
