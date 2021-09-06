package com.epam.esm.services.service;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.services.dto.GiftCertificateDto;

import java.util.List;
import java.util.Optional;

/**
 * An interface {@code GiftCertificateService} defines the service layer for a giftCertificate entity
 * with business logic methods (fetching data, updating, deleting, etc.) that access the data access layer
 * and prepares the data to the users if required.
 *
 * @author Sviatlana Shelestava
 * @see com.epam.esm.services.service.GiftCertificateServiceImpl
 * @since 1.0
 */
public interface GiftCertificateService {

    /**
     * Creates a new giftCertificate with associated tags;
     *
     * @param giftCertificate is an entity to be created;
     * @param tags            are tags that will be created if they do not exist and are associated with
     *                        the created giftCertificate;
     */
    void saveNewGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags);

    /**
     * Finds giftCertificate by id and creates a corresponding dto object;
     *
     * @param id is a unique field of giftCertificate in database;
     * @return an <code>Optional</code> contains the GiftCertificateDto object
     * or <code>Optional</code> contain a null value;
     */
    Optional<GiftCertificateDto> findById(Long id);

    /**
     * Updates the giftCertificate in database with associated tags;
     *
     * @param giftCertificate is an entity to be updated;
     * @param tags            are tags that will be created if they do not exist and are associated with
     *                        the updated giftCertificate;
     */
    void updateGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags);

    /**
     * Deletes giftCertificate from database by its id;
     *
     * @param id is a unique field of giftCertificate in database;
     */
    void deleteGiftCertificate(Long id);

    /**
     * Finds giftCertificates by name of tag and creates a corresponding dto objects;
     *
     * @param name is a unique name of tag in database;
     * @return a collection <code>List</code> contains the GiftCertificateDto objects
     * or empty collection <code>List</code>;
     */
    List<GiftCertificateDto> findGiftCertificatesByTag(String name);

    /**
     * Finds giftCertificates by match in name or description and creates a corresponding dto objects;
     *
     * @param searchCondition is a part and whole word that may appear in the name or description
     *                        of giftCertificate;
     * @return a collection <code>List</code> contains the GiftCertificateDto objects
     * or empty collection <code>List</code>;
     */
    List<GiftCertificateDto> findGiftCertificatesByNameOrDescription(String searchCondition);

    /**
     * Finds giftCertificates by concrete sort condition and creates a corresponding dto objects;
     *
     * @param sortCondition is sorting condition by creation date or  name of giftCertificate
     *                         in descending or descending order;
     * @return a collection <code>List</code> contains the GiftCertificateDto objects
     * or empty collection <code>List</code>;
     */
    List<GiftCertificateDto> getGiftCertificatesSortedByCondition(String sortCondition);

}
