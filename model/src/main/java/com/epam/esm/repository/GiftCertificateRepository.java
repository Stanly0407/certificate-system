package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;

import java.util.List;
import java.util.Optional;

/**
 * An interface {@code GiftCertificateRepository} defines the methods for retrieving
 * giftCertificate data from the database;
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
public interface GiftCertificateRepository {

    /**
     * Executes the SQL <code>INSERT</code> statement, which creates new giftCertificate in database;
     *
     * @param giftCertificate is an entity to be created;
     * @return a <code>Long</code> generated id of the newly created tag
     */
    Long save(GiftCertificate giftCertificate);

    /**
     * Executes an SQL <code>SELECT</code> statement, which searches for giftCertificate by id;
     *
     * @param id is a unique field of giftCertificate in database;
     * @return an <code>Optional</code> contains the giftCertificate with matching id
     * or <code>Optional</code> contain a null value;
     */
    Optional<GiftCertificate> findById(Long id);

    /**
     * Executes an SQL <code>UPDATE</code> statement, which updates giftCertificate in the database;
     *
     * @param giftCertificate is an entity to be updated;
     */
    void update(GiftCertificate giftCertificate);

    /**
     * Executes the SQL <code>DELETE</code> statement, which deletes giftCertificate from database by its id;
     *
     * @param id is a unique field of giftCertificate in a database;
     */
    void delete(Long id);

    /**
     * Executes the SQL <code>INSERT</code> statement, which creates an entry in the table certificate_tag
     * which associates the certificate with the corresponding tag;
     *
     * @param giftCertificate is an entity with which the tag will be associated;
     * @param tag             is an entity with which the giftCertificate will be associated;
     */
    void addTagToGiftCertificate(GiftCertificate giftCertificate, Tag tag);

    /**
     * Executes the SQL <code>SELECT</code> statement, which returns
     * a collection of giftCertificates associated with a specific tag in the database;
     *
     * @param query   is a part of the SQL statement with sorting conditions if exist;
     * @param tagName is the name of a tag that may be associated with some giftCertificates;
     * @return a collection <code>List</code> contains the giftCertificates or empty collection <code>List</code>;
     */
    List<GiftCertificate> findGiftCertificatesByTag(String query, String tagName, int pageNumber, int pageSize);

    /**
     * Executes the SQL <code>SELECT</code> statement, which returns
     * a collection of giftCertificates containing in their name or description searchCondition;
     *
     * @param query           is a part of the SQL statement with sorting conditions if exist;
     * @param searchCondition is a part and whole word that may appear in the name or description
     *                        of the giftCertificate;
     * @return a collection <code>List</code> contains the giftCertificates or empty collection <code>List</code>;
     */
    List<GiftCertificate> findByMatch(String query, String searchCondition, int pageNumber, int pageSize);

    /**
     * Executes the SQL <code>SELECT</code> statement, which returns
     * a collection of all existed giftCertificates;
     *
     * @param query is a part of the SQL statement with sorting conditions if exist;
     * @return a collection <code>List</code> contains the giftCertificates or empty collection <code>List</code>;
     */
    List<GiftCertificate> findAllGiftCertificates(String query, int pageNumber, int pageSize);

    void partialGiftCertificateUpdate(String parameterName, String parameter, Long giftCertificateId);

    List<GiftCertificate> findGiftCertificateBySeveralTags(String queryPart, List<String> tags, int pageNumber, int pageSize);

    long getGiftCertificateCommonQuantity();

}
