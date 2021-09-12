package com.epam.esm.services.service;

import com.epam.esm.entities.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public void saveNewTag(String tagName) {
        tagRepository.save(tagName);
    }

    @Transactional
    public Optional<Tag> findTagById(Long id) {
        return tagRepository.findById(id);
    }

    @Transactional
    public Optional<Tag> findTagByName(String tagName) {
        return tagRepository.findTagByName(tagName);
    }

    @Transactional
    public void deleteTag(Long id) {
        tagRepository.delete(id);
    }

}
