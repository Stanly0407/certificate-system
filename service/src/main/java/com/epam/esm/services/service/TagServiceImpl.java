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
    public void saveNewTag(Tag tag) {
        tagRepository.save(tag);
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
    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }

    @Override
    public Optional<Tag> getMostWidelyUsedTagOfUserWithHighestCostOrders() {
        return tagRepository.getMostWidelyUsedTagOfUserWithHighestCostOrders();
    }

}
