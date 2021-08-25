package com.epam.esm.controllers;

import com.epam.esm.controllers.forms.GiftCertificateTagsWrapper;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping(value = "/createCertificate")
    public void createNewGiftCertificate(@RequestBody GiftCertificateTagsWrapper giftCertificate) {
        giftCertificateService.saveNewGiftCertificate(giftCertificate.getGiftCertificate(), giftCertificate.getTags());
    }

    @PostMapping(value = "/updateCertificate")
    public void updateGiftCertificate(@RequestBody GiftCertificateTagsWrapper giftCertificate) {
        giftCertificateService.updateGiftCertificate(giftCertificate.getGiftCertificate(), giftCertificate.getTags());
    }

    @GetMapping(path = "/findCertificate")
    public GiftCertificateDto getGiftCertificate(@RequestParam Long id) {
        return giftCertificateService.findById(id);
    }

    @PostMapping(value = "/deleteCertificate")
    public void deleteGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        giftCertificateService.deleteGiftCertificate(giftCertificate);
    }

    @PostMapping(path = "/findCertificatesByTag")
    public List<GiftCertificateDto> findCertificatesByName(@RequestBody Tag tag) {
        return giftCertificateService.findGiftCertificatesByTag(tag.getName());
    }

    @PostMapping(path = "/searchCertificates")
    public List<GiftCertificateDto> findCertificatesByNameOrDescription(@RequestBody String searchCondition) {
        return giftCertificateService.findGiftCertificatesByNameOrDescription(searchCondition);
    }

    @GetMapping(path = "/getSortedCertificates")
    public List<GiftCertificateDto> getSortedGiftCertificates(@RequestParam String condition) {
        return giftCertificateService.getGiftCertificatesSortedByCondition(condition);
    }

}
