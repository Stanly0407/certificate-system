package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exceptions.ResourceNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftCertificateRepositoryImpl;
import com.epam.esm.repository.TagRepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCertificateService {

    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateService.class);
    private final GiftCertificateRepositoryImpl giftCertificateRepository;
    private final TagRepositoryImpl tagRepository;
    private static final String GIFT_CERTIFICATES_SORTING_CONDITION_BY_DATE = "date";
    private static final String GIFT_CERTIFICATES_SORTING_CONDITION_BY_DATE_DESC = "date-desc";
    private static final String GIFT_CERTIFICATES_SORTING_CONDITION_BY_NAME = "name";
    private static final String GIFT_CERTIFICATES_SORTING_CONDITION_BY_NAME_DESC = "name-desc";

    @Autowired
    public GiftCertificateService(GiftCertificateRepositoryImpl giftCertificateRepository, TagRepositoryImpl tagRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
    }

    public GiftCertificateDto findById(Long id) throws ResourceNotFoundException {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id);
        return createGiftCertificateDto(giftCertificate);
    }

    @Transactional
    public void saveNewGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags) {
        Long newGiftCertificateId = giftCertificateRepository.save(giftCertificate);
        GiftCertificate cratedGiftCertificate = giftCertificateRepository.findById(newGiftCertificateId);
        Tag newTag;
        for (Tag tag : tags) {
            if (checkNewTag(tag)) {
                Long newTagId = tagRepository.save(tag);
                newTag = tagRepository.findById(newTagId);
            } else {
                newTag = tagRepository.findTagByName(tag.getName());
            }
            giftCertificateRepository.addTagToGiftCertificate(cratedGiftCertificate, newTag);
        }
    }

    @Transactional
    public void updateGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags) {
        giftCertificateRepository.update(giftCertificate);
        tagRepository.deleteGiftCertificateTags(giftCertificate);
        List<Tag> newTags = tags.stream().filter(this::checkNewTag).collect(Collectors.toList());
        newTags.forEach(tagRepository::save);
        for (Tag tag : tags) {
            Tag newTag = tagRepository.findTagByName(tag.getName());
            giftCertificateRepository.addTagToGiftCertificate(giftCertificate, newTag);
        }
    }

    public void deleteGiftCertificate(Long id) {
        giftCertificateRepository.delete(id);
    }

    public List<GiftCertificateDto> findGiftCertificatesByTag(String name) {
        List<GiftCertificate> giftCertificateList = giftCertificateRepository.findGiftCertificatesByTag(name);
        return createCertificateDtoList(giftCertificateList);
    }

    public List<GiftCertificateDto> findGiftCertificatesByNameOrDescription(String searchCondition) {
        List<GiftCertificate> giftCertificateList = giftCertificateRepository.findByMatch(searchCondition);
        return createCertificateDtoList(giftCertificateList);
    }

    public List<GiftCertificateDto> getGiftCertificatesSortedByCondition(String sortingCondition) {
        List<GiftCertificate> giftCertificateList = new ArrayList<>();
        switch (sortingCondition) {
            case GIFT_CERTIFICATES_SORTING_CONDITION_BY_DATE:
                giftCertificateList = giftCertificateRepository.findAllSortedByDate();
                break;
            case GIFT_CERTIFICATES_SORTING_CONDITION_BY_DATE_DESC:
                giftCertificateList = giftCertificateRepository.findAllSortedByDateDesc();
                break;
            case GIFT_CERTIFICATES_SORTING_CONDITION_BY_NAME:
                giftCertificateList = giftCertificateRepository.findAllSortedByName();
                break;
            case GIFT_CERTIFICATES_SORTING_CONDITION_BY_NAME_DESC:
                giftCertificateList = giftCertificateRepository.findAllSortedByNameDesc();
                break;
        }
        return createCertificateDtoList(giftCertificateList);
    }

    private boolean checkNewTag(Tag tag) {
        return tagRepository.findTagByName(tag.getName()) == null;
    }

    private GiftCertificateDto createGiftCertificateDto(GiftCertificate giftCertificate) {
        List<Tag> tags = tagRepository.findGiftCertificateTags(giftCertificate.getId());
        return new GiftCertificateDto.Builder()
                .id(giftCertificate.getId())
                .name(giftCertificate.getName())
                .description(giftCertificate.getDescription())
                .price(giftCertificate.getPrice())
                .duration(giftCertificate.getDuration())
                .createDate(giftCertificate.getCreateDate())
                .lastUpdateDate(giftCertificate.getLastUpdateDate())
                .tags(tags)
                .build();
    }

    private List<GiftCertificateDto> createCertificateDtoList(List<GiftCertificate> giftCertificates) {
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        for (GiftCertificate giftCertificate : giftCertificates) {
            GiftCertificateDto giftCertificateDto = createGiftCertificateDto(giftCertificate);
            giftCertificateDtoList.add(giftCertificateDto);
        }
        return giftCertificateDtoList;
    }

}