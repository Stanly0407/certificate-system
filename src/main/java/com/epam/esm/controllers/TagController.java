package com.epam.esm.controllers;

import com.epam.esm.exceptions.ResourceNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public void createNewTag(@RequestBody Tag tag) {
        tagService.saveNewTag(tag);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findTag(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Tag> tag = tagService.findTagById(id);
        if (tag.isPresent()) {
            return ResponseEntity.ok().body(tag.get());
        } else {
            String resource = " (tag id = " + id + ")";
            throw new ResourceNotFoundException(resource);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Tag> tag = tagService.findTagById(id);
        if (tag.isPresent()) {
            tagService.deleteTag(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            String resource = " (tag id " + id + ")";
            throw new ResourceNotFoundException(resource);
        }
    }

}
