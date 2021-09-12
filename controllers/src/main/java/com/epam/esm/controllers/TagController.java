package com.epam.esm.controllers;

import com.epam.esm.entities.Tag;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * A class {@code TagController} as request handler defines method which accepts
 * user Tag requests and performs interactions on the data model objects by using service layer.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
@RestController
@RequestMapping("tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Creates a new tag;
     *
     * @param tagName is an name of new tag to be created;
     */
    @PostMapping
    public ResponseEntity<?> createNewTag(@RequestBody String tagName) {
        if (tagService.findTagByName(tagName).isPresent()) {
            return ResponseEntity.badRequest().build();
        } else {
            tagService.saveNewTag(tagName);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

    /**
     * Finds the tag by id;
     *
     * @param id is a unique field of the tag;
     * @return the tag object
     * @throws ResourceNotFoundException if the requested giftCertificate is not found;
     */
    @GetMapping("{id}")
    public Tag findTag(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Tag> tag = tagService.findTagById(id);
        if (tag.isPresent()) {
            return tag.get();
        } else {
            throw new ResourceNotFoundException(" (tag id = " + id + ")");
        }
    }

    /**
     * Deletes the tag by its id;
     *
     * @param id is a unique field of the tag;
     * @throws ResourceNotFoundException if the resource being deleted does not found;
     */
    @DeleteMapping("{id}")
    public void deleteTag(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Tag> tag = tagService.findTagById(id);
        if (tag.isPresent()) {
            tagService.deleteTag(id);
        } else {
            throw new ResourceNotFoundException(" (tag id " + id + ")");
        }
    }

}
