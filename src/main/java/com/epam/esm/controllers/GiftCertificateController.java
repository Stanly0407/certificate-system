package com.epam.esm.controllers;

import com.epam.esm.controllers.forms.GiftCertificateTagsWrapper;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exceptions.ResourceNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void updateGiftCertificate(@RequestBody GiftCertificateTagsWrapper giftCertificate) {
        giftCertificateService.updateGiftCertificate(giftCertificate.getGiftCertificate(), giftCertificate.getTags());
    }

    @GetMapping("{id}")
    public GiftCertificateDto getGiftCertificate(@PathVariable Long id) throws ResourceNotFoundException {
        return giftCertificateService.findById(id);
    }

    @DeleteMapping("{id}")
    public void deleteGiftCertificate(@PathVariable Long id) {
        giftCertificateService.deleteGiftCertificate(id);
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
