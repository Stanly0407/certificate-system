package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository {

    Tag findById(Long id);

    Long save(Tag tag);

    void delete(Tag tag);

}
