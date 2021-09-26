package com.epam.esm.services.service;

import com.epam.esm.entities.Tag;

import java.util.Optional;

/**
 * An interface {@code TagService} defines the service layer for a tag entity with business logic
 * methods (fetching data, deleting, etc.) that access the data access layer
 * and prepares the data to the users if required.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
public interface TagService {

    /**
     * Creates a new tag;
     *
     * @param tag is a tag to be created;
     */
    void saveNewTag(Tag tag);

    /**
     * Finds tag by id;
     *
     * @param tagName is a unique name of the tag;
     * @return an <code>Optional</code> contains the tag object
     * or <code>Optional</code> contain a null value;
     */
    Optional<Tag> findTagByName(String tagName);

    /**
     * Finds tag by id;
     *
     * @param id is a unique field of the tag in the database;
     * @return an <code>Optional</code> contains the tag object
     * or <code>Optional</code> contain a null value;
     */
    Optional<Tag> findTagById(Long id);

    /**
     * Deletes tag from a database by its id;
     *
     * @param tag is tag in the database;
     */
    void deleteTag(Tag tag);

    Optional<Tag> getMostWidelyUsedTagOfUserWithHighestCostOrders();

}
