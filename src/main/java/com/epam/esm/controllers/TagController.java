package com.epam.esm.controllers;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping(value = "/createTag")
    public void createNewTag(@RequestBody Tag tag) {
        tagService.saveNewTag(tag);
    }

    @GetMapping(value = "/findTag")
    public ResponseEntity<Tag> findTag(@RequestParam Long id) {
        Tag tag = tagService.findTagById(id);
        return ResponseEntity.ok(tag);
    }

    @PostMapping(value = "/deleteTag")
    public void deleteTag(@RequestBody Tag tag) {
        tagService.deleteTag(tag);
    }

}

