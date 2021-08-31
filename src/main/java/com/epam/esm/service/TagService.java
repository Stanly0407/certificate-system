package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TagService {

    private final TagRepositoryImpl tagRepository;

    public TagService(TagRepositoryImpl tagRepository) {
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
