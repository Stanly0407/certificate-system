package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private static final Logger LOGGER = LogManager.getLogger(TagService.class);

    @Autowired
    private TagRepositoryImpl tagRepository;

    public void saveNewTag(Tag tag) {
        if (tagRepository.findTagByName(tag.getName()) == null) {
            tagRepository.save(tag);
        }
    }

    public Tag findTagById(Long id) {
        return tagRepository.findById(id);
    }

    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }

}
