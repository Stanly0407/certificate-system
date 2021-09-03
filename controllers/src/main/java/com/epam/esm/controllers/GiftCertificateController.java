package com.epam.esm.controllers;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exceptions.ResourceNotFoundException;
import com.epam.esm.service.forms.GiftCertificateTagsWrapper;
import com.epam.esm.service.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    public void createNewGiftCertificate(@RequestBody GiftCertificateTagsWrapper giftCertificate) {
        giftCertificateService.saveNewGiftCertificate(giftCertificate.getGiftCertificate(), giftCertificate.getTags());
    }

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

    @GetMapping("{id}")
    public ResponseEntity<?> getGiftCertificate(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<GiftCertificateDto> giftCertificateDto = giftCertificateService.findById(id);
        if (giftCertificateDto.isPresent()) {
            return ResponseEntity.ok().body(giftCertificateDto.get());
        } else {
            String resource = " (Gift certificate id = " + id + ")";
            throw new ResourceNotFoundException(resource);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteGiftCertificate(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<GiftCertificateDto> giftCertificate = giftCertificateService.findById(id);
        if (giftCertificate.isPresent()) {
            giftCertificateService.deleteGiftCertificate(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            String resource = " (Gift certificate id " + id + ")";
            throw new ResourceNotFoundException(resource);
        }
    }

    @GetMapping("tag/{name}")
    public List<GiftCertificateDto> findCertificatesByTag(@PathVariable("name") String tagName) {
        return giftCertificateService.findGiftCertificatesByTag(tagName);
    }

    @GetMapping("search/{condition}")
    public List<GiftCertificateDto> findCertificatesByNameOrDescription(@PathVariable("condition") String searchCondition) {
        return giftCertificateService.findGiftCertificatesByNameOrDescription(searchCondition);
    }

    @GetMapping("sort/{condition}")
    public List<GiftCertificateDto> getSortedGiftCertificates(@PathVariable("condition") String sortCondition) {
        return giftCertificateService.getGiftCertificatesSortedByCondition(sortCondition);
    }

}
