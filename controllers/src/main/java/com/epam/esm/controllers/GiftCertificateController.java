package com.epam.esm.controllers;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.forms.GiftCertificateTagsWrapper;
import com.epam.esm.services.service.GiftCertificateService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.services.exceptions.ExceptionMessageType.COMMON_BAD_REQUEST;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A class {@code GiftCertificateController} as request handler defines method which accepts
 * user GiftCertificate requests and performs interactions on the data model objects by using service layer.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
@RestController
@RequestMapping("certificates")
@Validated
public class GiftCertificateController implements BaseController {

    private final GiftCertificateService giftCertificateService;
    private final LinkBuilder linkBuilder;

    public GiftCertificateController(GiftCertificateService giftCertificateService, LinkBuilder linkBuilder) {
        this.giftCertificateService = giftCertificateService;
        this.linkBuilder = linkBuilder;
    }

    /**
     * Creates a new giftCertificate with associated tags, which will be created if
     * they do not exist and are associated with the created giftCertificate;
     *
     * @param giftCertificateTagsWrapper is a GiftCertificateTagsWrapper entity included
     *                                   new giftCertificate params with associated tags;
     * @return ResponseEntity representing the whole HTTP response: status code 201 and headers
     */
    @PostMapping
    public ResponseEntity<?> createNewGiftCertificate(@RequestBody @Valid GiftCertificateTagsWrapper giftCertificateTagsWrapper) {
        GiftCertificate giftCertificate = giftCertificateTagsWrapper.getGiftCertificate();
        List<Tag> tagsGiftCertificate = giftCertificateTagsWrapper.getTags();
        Long newGiftCertificateId = giftCertificateService.saveNewGiftCertificate(giftCertificate, tagsGiftCertificate);
        Link newGiftCertificateLink = linkBuilder.getSelfLink(newGiftCertificateId, GiftCertificateController.class);
        return ResponseEntity.created(newGiftCertificateLink.toUri()).build();
    }

    /**
     * Updates the giftCertificate with associated tags, which will be created if
     * they do not exist and are associated with the updated giftCertificate;
     *
     * @param giftCertificateId          is a unique field of giftCertificate to be updated;
     * @param giftCertificateTagsWrapper is a GiftCertificateTagsWrapper entity included
     *                                   new giftCertificate params with associated tags;
     * @return ResponseEntity representing the whole HTTP response: status code, headers, and body
     * @throws ResourceNotFoundException if the resource being updated does not found;
     */
    @PutMapping("{giftCertificateId}")
    public ResponseEntity<?> updateGiftCertificate(@PathVariable @Min(1) Long giftCertificateId,
                                                   @RequestBody @Valid GiftCertificateTagsWrapper giftCertificateTagsWrapper)
            throws ResourceNotFoundException {
        giftCertificateService.updateGiftCertificate(giftCertificateId, giftCertificateTagsWrapper);
        return ResponseEntity.ok().build();
    }

    /**
     * Finds giftCertificate by id;
     *
     * @param id is a unique field of giftCertificate;
     * @return ResponseEntity representing the whole HTTP response: status code, headers, and body with GiftCertificate
     * @throws ResourceNotFoundException if the requested giftCertificate is not found;
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getGiftCertificate(@PathVariable @Min(1) Long id) throws ResourceNotFoundException {
        GiftCertificate giftCertificate = giftCertificateService.findById(id);
        return ResponseEntity.ok(giftCertificate);
    }

    /**
     * Deletes giftCertificate by its id;
     *
     * @param id is a unique field of giftCertificate;
     * @return ResponseEntity representing the whole HTTP response: status code, headers, and body if applicable
     * @throws ResourceNotFoundException if the resource being deleted does not found;
     */
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteGiftCertificate(@PathVariable @Min(1) Long id) throws ResourceNotFoundException {
        giftCertificateService.deleteGiftCertificate(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Finds giftCertificates by name of the associated tag/s or by match in name or description;
     *
     * @param tagNames        is one or more unique name of the tag;
     * @param searchCondition is a part or whole word that may appear in the name or description of the giftCertificate;
     * @param sortParams      is a collection <code>List</code> of sorting conditions by "date" (lastUpdateDate)
     *                        or "name" of the giftCertificate;
     * @param order           is an ascending ("acs" by default in model) or descending ("desc") order of sorting;
     * @param pageNumber      is a requested number of page with search result;
     * @param pageSize        is a number of request result displayed
     * @return ResponseEntity representing the whole HTTP response: status code, headers, and body with collection
     * <code>List</code> contains the GiftCertificates or empty collection <code>List</code>;
     * @throws BadRequestException       if invalid parameters input;
     * @throws ResourceNotFoundException if the resource being updated does not found;
     */
    @GetMapping
    public ResponseEntity<?> findGiftCertificatesByParams(
            @RequestParam(required = false, value = "tag") List<String> tagNames,
            @RequestParam(required = false, value = "condition") String searchCondition,
            @RequestParam(required = false, value = "sort") List<String> sortParams,
            @RequestParam(required = false) String order,
            @RequestParam(defaultValue = "1", value = "page") @Min(1) Integer pageNumber,
            @RequestParam(defaultValue = "5", value = "size") @Min(1) @Max(50) Integer pageSize)
            throws BadRequestException, ResourceNotFoundException {
        Long pageQuantity = giftCertificateService.getPaginationInfo(pageNumber, pageSize, tagNames, searchCondition);
        List<GiftCertificate> giftCertificates = giftCertificateService
                .findGiftCertificates(tagNames, sortParams, order, searchCondition, pageNumber, pageSize);

        if (!giftCertificates.isEmpty()) {
            linkBuilder.addSelfLinks(giftCertificates, GiftCertificateController.class);
            String uriString = linkTo(methodOn(GiftCertificateController.class)
                    .findGiftCertificatesByParams(tagNames, searchCondition, sortParams, order, pageNumber, pageSize))
                    .toUriComponentsBuilder().buildAndExpand().toString();
            List<Link> links = linkBuilder.createPaginationLinks(pageQuantity, pageNumber, uriString);

            CollectionModel<GiftCertificate> result = CollectionModel.of(giftCertificates, links);
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.ok().body(giftCertificates);
        }
    }

    /**
     * Updates the single field of giftCertificate;
     *
     * @param id          is a unique field of giftCertificate to be updated if not null;
     * @param name        is a new GiftCertificate name to to be updated if not null;
     * @param description is a new GiftCertificate description to to be updated if not null;
     * @param price       is a new GiftCertificate price to to be updated if not null;
     * @param duration    is a new GiftCertificate duration to to be updated if not null;
     * @return ResponseEntity representing the whole HTTP response: status code, headers, and body
     * @throws ResourceNotFoundException if the resource being updated does not found;
     * @throws BadRequestException       if invalid parameters input;
     */
    @PatchMapping("{id}")
    public ResponseEntity<?> partialUpdateGiftCertificate(
            @PathVariable Long id,
            @RequestParam(required = false) @Size(min = 2, max = 30) String name,
            @RequestParam(required = false) @Size(min = 2, max = 250) String description,
            @RequestParam(required = false)
            @DecimalMin(value = "0.01", inclusive = false) @Digits(integer = 10, fraction = 2) BigDecimal price,
            @RequestParam(required = false) @Min(1) @Max(300) Integer duration)
            throws ResourceNotFoundException, BadRequestException {
        boolean isSuccessfulUpdated = giftCertificateService.partialGiftCertificateUpdate(name, description, price, duration, id);
        if (isSuccessfulUpdated) {
            return ResponseEntity.ok().build();
        } else {
            throw new BadRequestException(COMMON_BAD_REQUEST);
        }
    }
}
