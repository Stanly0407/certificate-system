package com.epam.esm.services.service;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.services.dto.GiftCertificateDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateServiceImpl.class);
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private static final String SORT_BY_NAME = "name";
    private static final String SORT_BY_DATE = "date";
    private static final String SORT_ORDER_DESC = "desc";

    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public Optional<GiftCertificateDto> findById(Long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        return giftCertificate.map(this::createGiftCertificateDto);
    }

    @Transactional
    public void saveNewGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags) {
        Long newGiftCertificateId = giftCertificateRepository.save(giftCertificate);
        Optional<GiftCertificate> createdGiftCertificate = giftCertificateRepository.findById(newGiftCertificateId);
        Optional<Tag> newTag;
        Set<Tag> uniqueSetOfTags = getUniqueSetOfTags(tags);
        for (Tag tag : uniqueSetOfTags) {
            if (isNewTag(tag)) {
                Long newTagId = tagRepository.save(tag.getName());
                newTag = tagRepository.findById(newTagId);
            } else {
                newTag = tagRepository.findTagByName(tag.getName());
            }
            if (createdGiftCertificate.isPresent() && newTag.isPresent()) {
                giftCertificateRepository.addTagToGiftCertificate(createdGiftCertificate.get(), newTag.get());
            }
        }
    }

    @Transactional
    public void updateGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags) {
        giftCertificateRepository.update(giftCertificate);
        tagRepository.deleteGiftCertificateTags(giftCertificate.getId());
        List<String> newTags = tags.stream().filter(this::isNewTag).map(Tag::getName).collect(Collectors.toList());
        newTags.forEach(tagRepository::save);
        for (Tag tag : tags) {
            Optional<Tag> newTag = tagRepository.findTagByName(tag.getName());
            newTag.ifPresent(value -> giftCertificateRepository.addTagToGiftCertificate(giftCertificate, value));
        }
    }

    @Transactional
    public void deleteGiftCertificate(Long id) {
        giftCertificateRepository.delete(id);
    }

    @Transactional
    public List<GiftCertificateDto> findGiftCertificates(String tagName, List<String> sortParams, String order,
                                                         String searchCondition) {
        String sortQueryPart = getSortQueryPart(sortParams, order);
        List<GiftCertificate> giftCertificateList;
        if (tagName != null) {
            giftCertificateList = giftCertificateRepository.findGiftCertificatesByTag(sortQueryPart, tagName);
        } else if (searchCondition != null) {
            giftCertificateList = giftCertificateRepository.findByMatch(sortQueryPart, searchCondition);
        } else {
            giftCertificateList = giftCertificateRepository.findAllGiftCertificates(sortQueryPart);
        }
        return createCertificateDtoList(giftCertificateList);
    }

    private String getSortQueryPart(List<String> sortParams, String order) {
        StringBuilder sortQueryPart = new StringBuilder();
        if (sortParams != null) {
            if (sortParams.contains(SORT_BY_NAME)) {
                sortQueryPart.append(" ORDER BY name ");
            } else if (sortParams.size() >= 2) {
                sortQueryPart.append(" AND ");
            } else if (sortParams.contains(SORT_BY_DATE)) {
                sortQueryPart.append(" ORDER BY last_update_date ");
            }
            if (SORT_ORDER_DESC.equals(order)) {
                sortQueryPart.append(" DESC ");
            }
        }
        return new String(sortQueryPart);
    }

    private Set<Tag> getUniqueSetOfTags(List<Tag> tags) {
        return tags.stream()
                .map(Tag::getName)
                .map(tag -> tagRepository.findTagByName(tag).get())
                .collect(Collectors.toSet());
    }

    private boolean isNewTag(Tag tag) {
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
