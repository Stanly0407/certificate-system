package com.epam.esm.service.service;

import com.epam.esm.entities.Tag;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TagService {

    void saveNewTag(Tag tag);

    Optional<Tag> findTagById(Long id);

    void deleteTag(Long id);

}
