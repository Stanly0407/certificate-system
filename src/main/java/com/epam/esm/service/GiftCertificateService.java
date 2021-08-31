package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftCertificateRepositoryImpl;
import com.epam.esm.repository.TagRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GiftCertificateService {

    private final GiftCertificateRepositoryImpl giftCertificateRepository;
    private final TagRepositoryImpl tagRepository;
    private static final String GIFT_CERTIFICATES_SORTING_CONDITION_BY_DATE = "date";
    private static final String GIFT_CERTIFICATES_SORTING_CONDITION_BY_DATE_DESC = "date-desc";
    private static final String GIFT_CERTIFICATES_SORTING_CONDITION_BY_NAME = "name";
    private static final String GIFT_CERTIFICATES_SORTING_CONDITION_BY_NAME_DESC = "name-desc";

    public GiftCertificateService(GiftCertificateRepositoryImpl giftCertificateRepository, TagRepositoryImpl tagRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
    }

    public Optional<GiftCertificateDto> findById(Long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        return giftCertificate.map(this::createGiftCertificateDto);
    }

    public void saveNewGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags) {
        Long newGiftCertificateId = giftCertificateRepository.save(giftCertificate);
        Optional<GiftCertificate> createdGiftCertificate = giftCertificateRepository.findById(newGiftCertificateId);
        Optional<Tag> newTag;
        for (Tag tag : tags) {
            if (checkNewTag(tag)) {
                Long newTagId = tagRepository.save(tag);
                newTag = tagRepository.findById(newTagId);
            } else {
                newTag = tagRepository.findTagByName(tag.getName());
            }
            if (createdGiftCertificate.isPresent() && newTag.isPresent()) {
                giftCertificateRepository.addTagToGiftCertificate(createdGiftCertificate.get(), newTag.get());
            }
        }
    }

    public void updateGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags) {
        giftCertificateRepository.update(giftCertificate);
        tagRepository.deleteGiftCertificateTags(giftCertificate);
        List<Tag> newTags = tags.stream().filter(this::checkNewTag).collect(Collectors.toList());
        newTags.forEach(tagRepository::save);
        for (Tag tag : tags) {
            Optional<Tag> newTag = tagRepository.findTagByName(tag.getName());
            newTag.ifPresent(value -> giftCertificateRepository.addTagToGiftCertificate(giftCertificate, value));
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
        return !tagRepository.findTagByName(tag.getName()).isPresent();
    }

    private GiftCertificateDto createGiftCertificateDto(GiftCertificate giftCertificate) {
        Long giftCertificateId = giftCertificate.getId();
        List<Tag> tags = tagRepository.findGiftCertificateTags(giftCertificateId);
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
