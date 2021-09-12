package com.epam.esm.controllers;

import com.epam.esm.services.dto.GiftCertificateDto;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.forms.GiftCertificateTagsWrapper;
import com.epam.esm.services.service.GiftCertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            throw new ResourceNotFoundException(" (Gift certificate id = " + giftCertificateId + ")");
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
            throw new ResourceNotFoundException(" (Gift certificate id = " + id + ")");
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
            throw new ResourceNotFoundException(" (Gift certificate id " + id + ")");
        }
    }

    /**
     * Finds giftCertificates by name of the associated tag or by match in name or description
     * and creates a corresponding dto objects;
     *
     * @param tagName    is a unique name of the tag;
     * @param condition  is a part or whole word that may appear in the name or description of the giftCertificate;
     * @param sortParams is a collection <code>List</code> of sorting conditions by "date" (lastUpdateDate)
     *                   or "name" of the giftCertificate;
     * @param order      is an ascending ("acs" by default) or descending ("desc") order of sorting;
     * @return a collection <code>List</code> contains the GiftCertificateDto objects
     * or empty collection <code>List</code>;
     */
    @GetMapping
    public ResponseEntity<?> findCertificatesByParams(
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String condition,
            @RequestParam(value = "sort", required = false) List<String> sortParams,
            @RequestParam(required = false) String order) {
        if ((tagName != null && condition != null) ||
                !(tagName != null || sortParams != null || order != null || condition != null) ||
                (sortParams == null && order != null)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().body(giftCertificateService.findGiftCertificates(tagName, sortParams, order, condition));
        }
    }

}
