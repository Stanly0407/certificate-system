package com.epam.esm.controllers;

import com.epam.esm.services.dto.GiftCertificateDto;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.forms.GiftCertificateTagsWrapper;
import com.epam.esm.services.service.GiftCertificateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * A class {@code GiftCertificateController} as request handler defines method which accepts
 * user GiftCertificate requests and performs interactions on the data model objects by using service layer.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
@RestController
@RequestMapping("certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * Creates a new giftCertificate with associated tags, which will be created if
     * they do not exist and are associated with the created giftCertificate;
     *
     * @param giftCertificate is a GiftCertificateTagsWrapper entity included new giftCertificate with associated tags;
     */
    @PostMapping
    public void createNewGiftCertificate(@RequestBody GiftCertificateTagsWrapper giftCertificate) {
        giftCertificateService.saveNewGiftCertificate(giftCertificate.getGiftCertificate(), giftCertificate.getTags());
    }

    /**
     * Updates the giftCertificate with associated tags, which will be created if
     * they do not exist and are associated with the updated giftCertificate;
     *
     * @param giftCertificate is an entity to be updated;
     * @throws ResourceNotFoundException if the resource being updated does not found;
     */
    @PutMapping
    public void updateGiftCertificate(@RequestBody GiftCertificateTagsWrapper giftCertificate) throws ResourceNotFoundException {
        Long giftCertificateId = giftCertificate.getGiftCertificate().getId();
        Optional<GiftCertificateDto> giftCertificateDto = giftCertificateService.findById(giftCertificateId);
        if (giftCertificateDto.isPresent()) {
            giftCertificateService.updateGiftCertificate(giftCertificate.getGiftCertificate(), giftCertificate.getTags());
        } else {
            String resource = " (Gift certificate id = " + giftCertificateId + ")";
            throw new ResourceNotFoundException(resource);
        }
    }

    /**
     * Finds giftCertificate by id and creates a corresponding dto object;
     *
     * @param id is a unique field of giftCertificate;
     * @return GiftCertificateDto is the generated corresponding dto object based on the found giftCertificate
     * @throws ResourceNotFoundException if the requested giftCertificate is not found;
     */
    @GetMapping("{id}")
    public GiftCertificateDto getGiftCertificate(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<GiftCertificateDto> giftCertificateDto = giftCertificateService.findById(id);
        if (giftCertificateDto.isPresent()) {
            return giftCertificateDto.get();
        } else {
            String resource = " (Gift certificate id = " + id + ")";
            throw new ResourceNotFoundException(resource);
        }
    }

    /**
     * Deletes giftCertificate by its id;
     *
     * @param id is a unique field of giftCertificate;
     * @throws ResourceNotFoundException if the resource being deleted does not found;
     */
    @DeleteMapping("{id}")
    public void deleteGiftCertificate(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<GiftCertificateDto> giftCertificate = giftCertificateService.findById(id);
        if (giftCertificate.isPresent()) {
            giftCertificateService.deleteGiftCertificate(id);
        } else {
            String resource = " (Gift certificate id " + id + ")";
            throw new ResourceNotFoundException(resource);
        }
    }

    /**
     * Finds giftCertificates by name of the associated tag and creates a corresponding dto objects;
     *
     * @param tagName is a unique name of tag;
     * @return a collection <code>List</code> contains the GiftCertificateDto objects
     * or empty collection <code>List</code>;
     */
    @GetMapping("tag/{name}")
    public List<GiftCertificateDto> findCertificatesByTag(@PathVariable("name") String tagName) {
        return giftCertificateService.findGiftCertificatesByTag(tagName);
    }

    /**
     * Finds giftCertificates by match in name or description and creates a corresponding dto objects;
     *
     * @param searchCondition is a part and whole word that may appear in the name or description
     *                        of giftCertificate;
     * @return a collection <code>List</code> contains the GiftCertificateDto objects
     * or empty collection <code>List</code>;
     */
    @GetMapping("search/{condition}")
    public List<GiftCertificateDto> findCertificatesByNameOrDescription(@PathVariable("condition") String searchCondition) {
        return giftCertificateService.findGiftCertificatesByNameOrDescription(searchCondition);
    }

    /**
     * Finds giftCertificates by concrete sort condition and creates a corresponding dto objects;
     *
     * @param sortCondition is sorting condition by creation date or name of giftCertificate
     *                      in descending or descending order;
     * @return a collection <code>List</code> contains the GiftCertificateDto objects
     * or empty collection <code>List</code>;
     */
    @GetMapping("sort/{condition}")
    public List<GiftCertificateDto> getSortedGiftCertificates(@PathVariable("condition") String sortCondition) {
        return giftCertificateService.getGiftCertificatesSortedByCondition(sortCondition);
    }

}
