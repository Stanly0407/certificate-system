package com.epam.esm.controllers;

import com.epam.esm.entities.Tag;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.service.TagService;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * A class {@code TagController} as request handler defines method which accepts
 * user Tag requests and performs interactions on the data model objects by using service layer.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
@RestController
@RequestMapping("tags")
@Validated
public class TagController {

    private final TagService tagService;
    private final LinkBuilder linkBuilder;

    public TagController(TagService tagService, LinkBuilder linkBuilder) {
        this.tagService = tagService;
        this.linkBuilder = linkBuilder;
    }

    /**
     * Creates a new tag;
     *
     * @param tag is a new tag to be created;
     * @return ResponseEntity representing the whole HTTP response: status code 201 and headers;
     * @throws BadRequestException if invalid parameters input;
     */
    @PostMapping
    public ResponseEntity<?> createNewTag(@RequestBody @Valid Tag tag) throws BadRequestException {
        String tagName = tag.getName();
        Long newTagId = tagService.saveNewTag(tagName);
        Link newTagLocation = linkBuilder.getSelfLink(newTagId, TagController.class);
        return ResponseEntity.created(newTagLocation.toUri()).build();
    }

    /**
     * Finds the tag by id;
     *
     * @param id is a unique field of the tag;
     * @return ResponseEntity representing the whole HTTP response: status code 200, headers and tag in the body;
     * @throws ResourceNotFoundException if the requested tag is not found;
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getTagById(@PathVariable @Min(1) Long id) throws ResourceNotFoundException {
        Tag tag = tagService.findTagById(id);
        return ResponseEntity.ok(tag);
    }

    /**
     * Deletes the tag by its id;
     *
     * @param id is a unique field of the tag;
     * @return ResponseEntity representing the whole HTTP response: status code 200, headers;
     * @throws ResourceNotFoundException if the resource being deleted does not found;
     */
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) throws ResourceNotFoundException {
        tagService.deleteTag(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Finds the most widely used tag of a user with the highest cost of all orders;
     *
     * @return ResponseEntity representing the whole HTTP response: status code 200, headers and tag in the body;
     */
    @GetMapping("/widely-used")
    public ResponseEntity<?> getMostWidelyUsedTagOfUserWithHighestCostOrders() {
        List<Tag> tags = tagService.getMostWidelyUsedTagOfUserWithHighestCostOrders();
        linkBuilder.addSelfLinks(tags, TagController.class);
        return ResponseEntity.ok().body(tags);
    }

}
