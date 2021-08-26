package com.epam.esm.controllers;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public void createNewTag(@RequestBody Tag tag) {
        tagService.saveNewTag(tag);
    }

    @GetMapping("{id}")
    public Tag findTag(@PathVariable Long id) {
        return tagService.findTagById(id);
    }

    @DeleteMapping("{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
    }

}

