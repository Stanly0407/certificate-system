package com.epam.esm.services.service;

import com.epam.esm.entities.Tag;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;

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
     * @return <code>Long</code> id of new created tag
     * @throws BadRequestException if invalid parameters input;
     */
    Long saveNewTag(String tag) throws BadRequestException;

    /**
     * Finds tag by id;
     *
     * @param id is a unique field of the tag in the database;
     * @return tag if exists;
     * @throws ResourceNotFoundException if the resource being deleted does not found;
     */
    Tag findTagById(Long id) throws ResourceNotFoundException;

    /**
     * Deletes tag from a database by its id;
     *
     * @param id is unique field of tag;
     * @throws ResourceNotFoundException if the resource being deleted does not found;
     */
    void deleteTag(Long id) throws ResourceNotFoundException;

    /**
     * Finds the most widely used tag of a user with the highest cost of all orders;
     *
     * @return an <code>Optional</code> contains the tag object
     * or <code>Optional</code> contain a null value;
     */
    Optional<Tag> getMostWidelyUsedTagOfUserWithHighestCostOrders();

}
