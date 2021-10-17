package com.epam.esm.services.service;

import com.epam.esm.entities.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.services.exceptions.ExceptionMessageType.ALREADY_EXISTS;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    public Long saveNewTag(String tagName) throws BadRequestException {
        Optional<Tag> tagOptional = tagRepository.findTagByName(tagName);
        if (tagOptional.isPresent()) {
            throw new BadRequestException(ALREADY_EXISTS);
        } else {
            Tag newTag = Tag.builder().name(tagName).build();
            return tagRepository.save(newTag);
        }
    }

    public Tag findTagById(Long id) throws ResourceNotFoundException {
        Optional<Tag> tag = tagRepository.findById(id);
        if (!tag.isPresent()) {
            throw new ResourceNotFoundException(id);
        } else {
            return tag.get();
        }
    }

    public void deleteTag(Long id) throws ResourceNotFoundException {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (!tagOptional.isPresent()) {
            throw new ResourceNotFoundException(id);
        } else {
            Tag tag = tagOptional.get();
            tagRepository.delete(tag);
        }
    }

    public List<Tag> getMostWidelyUsedTagOfUserWithHighestCostOrders() {
        return tagRepository.getMostWidelyUsedTagOfUserWithHighestCostOrders();
    }

}
