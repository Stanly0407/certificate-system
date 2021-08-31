package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository {

    Optional<Tag> findById(Long id);

    Long save(Tag tag);

    void delete(Long id);

}
