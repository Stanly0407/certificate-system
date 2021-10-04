package com.epam.esm.services.service;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * An interface {@code GiftCertificateService} defines the service layer for a giftCertificate entity
 * with business logic methods (fetching data, updating, deleting, etc.) that access the data access layer
 * and prepares the data to the users if required.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
public interface GiftCertificateService {

    /**
     * Creates a new giftCertificate with associated tags;
     *
     * @param giftCertificate is an entity to be created;
     * @param tags            are tags that will be created if they do not exist and are associated with
     *                        the created giftCertificate;
     * @return <code>Long</code> id of new created tag
     */
    Long saveNewGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags);

    /**
     * Finds giftCertificate by id;
     *
     * @param id is a unique field of giftCertificate in database;
     * @return an <code>Optional</code> contains the GiftCertificate object
     * or <code>Optional</code> contain a null value;
     */
    Optional<GiftCertificate> findById(Long id);

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
     * @throws ResourceNotFoundException if the resource being deleted does not found;
     */
    void deleteGiftCertificate(Long id) throws ResourceNotFoundException;

    /**
     * Finds giftCertificates by name of tag/s or by match in name or description;
     *
     * @param tagName         is one or more unique name of the tag;
     * @param searchCondition is a part and whole word that may appear in the name
     *                        or description of giftCertificate;
     * @param sortParams      is a collection <code>List</code> of sorting conditions by "date" (createDate)
     *                        or "name" of giftCertificate;
     * @param order           is a ascending ("acs" by default) or descending ("desc") order of sorting;
     * @param pageNumber      is a requested number of page with search result;
     * @param pageSize        is a number of request result displayed
     * @return a collection <code>List</code> contains the GiftCertificates
     * or empty collection <code>List</code>;
     * @throws BadRequestException if invalid parameters input;
     */
    List<GiftCertificate> findGiftCertificates(List<String> tagName, List<String> sortParams, String order,
                                               String searchCondition, int pageNumber, int pageSize)
            throws BadRequestException;

    /**
     * Determines, based on the parameters of the request, the number of result pages for pagination purposes;
     *
     * @param pageNumber      is one or more unique name of the tag;
     * @param pageSize        is a part and whole word that may appear in the name
     *                        or description of giftCertificate;
     * @param tagNames        is one or more unique name of the tag;
     * @param searchCondition is a part and whole word that may appear in the name or description of giftCertificate;
     * @return a number of <code>Long</code> request result pages;
     * @throws ResourceNotFoundException if the resource being updated does not found;
     */
    Long getPaginationInfo(int pageNumber, int pageSize, List<String> tagNames, String searchCondition)
            throws ResourceNotFoundException;

    /**
     * Updates the single field of giftCertificate;
     *
     * @param id          is a unique field of giftCertificate to be updated if not null;
     * @param name        is a new GiftCertificate name to to be updated if not null;
     * @param description is a new GiftCertificate description to to be updated if not null;
     * @param price       is a new GiftCertificate price to to be updated if not null;
     * @param duration    is a new GiftCertificate duration to to be updated if not null;
     * @return boolean true if successful update;
     * @throws ResourceNotFoundException if the resource being updated does not found;
     */
    boolean partialGiftCertificateUpdate(String name, String description, BigDecimal price, Integer duration, Long id)
            throws ResourceNotFoundException;

}
