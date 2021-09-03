package com.epam.esm.service;

import com.epam.esm.domain.entities.Tag;
import com.epam.esm.domain.repository.TagRepository;
import com.epam.esm.domain.service.TagService;
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
