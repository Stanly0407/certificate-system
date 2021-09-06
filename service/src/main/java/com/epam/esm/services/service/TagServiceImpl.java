package com.epam.esm.services.service;

import com.epam.esm.entities.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public void saveNewTag(Tag tag) {
        if (!tagRepository.findTagByName(tag.getName()).isPresent()) {
            tagRepository.save(tag);
        }
    }

    public Optional<Tag> findTagById(Long id) {
        return tagRepository.findById(id);
    }

    public void deleteTag(Long id) {
        tagRepository.delete(id);
    }

}
