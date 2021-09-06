package com.epam.esm.controllers;

import com.epam.esm.entities.Tag;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.service.TagService;
import org.springframework.web.bind.annotation.*;

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
     * @param tag is an entity to be created;
     */
    @PostMapping
    public void createNewTag(@RequestBody Tag tag) {
        tagService.saveNewTag(tag);
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
            String resource = " (tag id = " + id + ")";
            throw new ResourceNotFoundException(resource);
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
            String resource = " (tag id " + id + ")";
            throw new ResourceNotFoundException(resource);
        }
    }

}
