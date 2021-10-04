package com.epam.esm.repository;

import com.epam.esm.entities.Tag;

import java.util.Optional;

/**
 * An interface {@code TagRepository} defines the methods for retrieving tag data from the database;
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
public interface TagRepository {

    /**
     * Executes the SQL <code>INSERT</code> statement, which creates new tag in the database;
     *
     * @param tag is a new tag to be created;
     * @return a <code>Long</code> generated id of the newly created tag
     */
    Long save(Tag tag);

    /**
     * Executes an SQL <code>SELECT</code> statement, which searches for tag by id;
     *
     * @param id is a unique field of tag in database;
     * @return an <code>Optional</code> contains the tag with matching id
     * or <code>Optional</code> contain a null value;
     */
    Optional<Tag> findById(Long id);

    /**
     * Executes the SQL <code>DELETE</code> statement, which deletes tag from the database by id;
     *
     * @param tag is a tag in database;
     */
    void delete(Tag tag);

    /**
     * Executes an SQL <code>SELECT</code> statement, which searches for tag by its name;
     *
     * @param name is a name of tag in database;
     * @return an <code>Optional</code> contains the tag with matching name
     * or <code>Optional</code> contain a null value;
     */
    Optional<Tag> findTagByName(String name);

    /**
     * Executes the SQL <code>DELETE</code> statement, which cleans up the giftCertificate
     * related tags from database by id of giftCertificate;
     *
     * @param giftCertificateId is a unique field of giftCertificate in the database;
     */
    void deleteGiftCertificateTags(Long giftCertificateId);

    /**
     * Executes the SQL <code>SELECT</code> statement, which returns
     * the most widely used tag of a user with the highest cost of all orders;
     *
     * @return an <code>Optional</code> contains the tag with matching name
     * or <code>Optional</code> contain a null value;
     */
    Optional<Tag> getMostWidelyUsedTagOfUserWithHighestCostOrders();

}
