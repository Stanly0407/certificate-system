package com.epam.esm.services.service;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.requests.GiftCertificatePartialUpdateRequest;
import com.epam.esm.services.requests.GiftCertificateTagsWrapper;

import java.util.List;

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
     * @return <code>Long</code> id of new created giftCertificate;
     */
    Long saveNewGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags) throws BadRequestException;

    /**
     * Finds giftCertificate by id;
     *
     * @param id is a unique field of giftCertificate in database;
     * @return GiftCertificate with matching id;
     * @throws ResourceNotFoundException if the resource does not found;
     */
    GiftCertificate findById(Long id) throws ResourceNotFoundException;

    /**
     * Updates the giftCertificate in database with associated tags;
     *
     * @param giftCertificateId          is an id of gift certificate to be updated;
     * @param giftCertificateTagsWrapper is a GiftCertificateTagsWrapper entity included
     *                                   new giftCertificate params with associated tags;
     * @throws ResourceNotFoundException if updated resource does not found;
     */
    void updateGiftCertificate(Long giftCertificateId, GiftCertificateTagsWrapper giftCertificateTagsWrapper)
            throws ResourceNotFoundException, BadRequestException;

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
     * @param pageNumber      is a requested number of page with search result;
     * @param pageSize        is a number of request result displayed
     * @param tagNames        is one or more unique name of the tag;
     * @param searchCondition is a part and whole word that may appear in the name or description of giftCertificate;
     * @return a number of <code>Long</code> request result pages;
     * @throws ResourceNotFoundException if page does not found;
     */
    Long getPaginationInfo(int pageNumber, int pageSize, List<String> tagNames, String searchCondition)
            throws ResourceNotFoundException, BadRequestException;

    /**
     * Updates the single field of giftCertificate;
     *
     * @param id              is a unique field of giftCertificate to be updated if not null;
     * @param giftCertificate is a new GiftCertificate name to to be updated if not null;
     * @throws ResourceNotFoundException if the resource being updated does not found;
     */
    void partialGiftCertificateUpdate(GiftCertificatePartialUpdateRequest giftCertificate, Long id)
            throws ResourceNotFoundException, BadRequestException;

}
