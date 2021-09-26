package com.epam.esm.services.service;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.services.dto.GiftCertificateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final String PREVIOUS_PAGE = "previousPage";
    private static final String NEXT_PAGE = "nextPage";
    private static final String SORT_BY_NAME = "name";
    private static final String SORT_BY_DATE = "date";
    private static final String SORT_ORDER_DESC = "desc";

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;


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

        Set<String> uniqueSetOfTags = getUniqueSetOfTags(tags);
        for (String tagName : uniqueSetOfTags) {
            if (!tagRepository.findTagByName(tagName).isPresent()) {
                Long newTagId = tagRepository.save(Tag.builder().name(tagName).build());
                newTag = tagRepository.findById(newTagId);
            } else {
                newTag = tagRepository.findTagByName(tagName);
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
        //    List<String> newTags = tags.stream().filter(this::isNewTag).map(Tag::getName).collect(Collectors.toList());
        List<Tag> newTags = tags.stream().filter(this::isNewTag).collect(Collectors.toList());
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
    public List<GiftCertificate> findGiftCertificates(List<String> tagNames, List<String> sortParams, String order,
                                                         String searchCondition, int pageNumber, int pageSize) {
        String sortQueryPart = getSortQueryPart(sortParams, order);
        List<GiftCertificate> giftCertificateList;

        if (tagNames != null) {
            if (tagNames.size() == 1) {
                String tagName = tagNames.get(0);
                giftCertificateList = giftCertificateRepository.findGiftCertificatesByTag(sortQueryPart, tagName, pageNumber, pageSize);
            } else {
                giftCertificateList = giftCertificateRepository.findGiftCertificateBySeveralTags(sortQueryPart, tagNames, pageNumber, pageSize);
            }
        } else if (searchCondition != null) {
            giftCertificateList = giftCertificateRepository.findByMatch(sortQueryPart, searchCondition, pageNumber, pageSize);
        } else {
            giftCertificateList = giftCertificateRepository.findAllGiftCertificates(sortQueryPart, pageNumber, pageSize);
        }
        return giftCertificateList;
    }

    public long
    //Map<String, Integer>
    getPaginationInfo(int pageNumber, int pageSize, List<String> tagNames, String searchCondition) {
        List<GiftCertificate> giftCertificates;
        if(tagNames != null && tagNames.size() == 1){ //todo const and separate method
            giftCertificates = giftCertificateRepository.findGiftCertificatesByTag("", tagNames.get(0), 0, 0);
        } else if (tagNames != null && tagNames.size() > 1){
            giftCertificates = giftCertificateRepository.findGiftCertificateBySeveralTags("", tagNames, 0, 0);
        } else if (searchCondition != null){
            giftCertificates = giftCertificateRepository.findByMatch("", searchCondition, 0, 0);
        } else {
            giftCertificates = giftCertificateRepository.findAllGiftCertificates("", 0, 0);
        }

        long countResult = giftCertificates.size();
        long pageQuantity;
        if ((countResult % pageSize) == 0) {
            pageQuantity =  (countResult / pageSize);
        } else {
            pageQuantity = (countResult / pageSize)  + 1;
        }
return pageQuantity;
        //static common interface method
//        Map<String, Integer> pages = new HashMap<>();
//        Integer previousPage = null;
//        Integer nextPage = null;
//
//        if ((pageNumber - 1) > 0) {
//            previousPage = pageNumber - 1;
//        }
//        if ((pageNumber + 1) <= pageQuantity) {
//            nextPage = pageNumber + 1;
//        }
//
//        if (previousPage != null) {
//            pages.put(PREVIOUS_PAGE, previousPage);
//        }
//        if (nextPage != null) {
//            pages.put(NEXT_PAGE, nextPage);
//        }
//        return pages;
    }


    @Override
    public boolean partialGiftCertificateUpdate(Map<String, Object> updates, Long id) {
        String parameterName = null;
        String parameter = null;
        if (updates.containsKey("name")) {
            parameterName = "name";
            parameter = (String) updates.get("name");
        } else if (updates.containsKey("description")) {
            parameterName = "description";
            parameter = (String) updates.get("description");
        } else if (updates.containsKey("price")) {
            BigDecimal price = (BigDecimal) updates.get("price");
            parameterName = "price";
            parameter = price.toString();
        } else if (updates.containsKey("duration")) {
            Integer duration = (Integer) updates.get("duration");
            parameterName = "duration";
            parameter = Integer.toString(duration);
        }
        return updateGiftCertificateField(parameterName, parameter, id);
    }

    private boolean updateGiftCertificateField(String parameterName, String parameter, Long giftCertificateId) {
        if (parameter != null) {
            giftCertificateRepository.partialGiftCertificateUpdate(parameterName, parameter, giftCertificateId);
            return true;
        } else {
            return false;
        }
    }


    private String getSortQueryPart(List<String> sortParams, String order) {
        StringBuilder sortQueryPart = new StringBuilder();
        if (sortParams != null) {
            if (sortParams.contains(SORT_BY_NAME)) {
                sortQueryPart.append(" order by c.name");
            } else if (sortParams.size() >= 2) {
                sortQueryPart.append(" and ");
            } else if (sortParams.contains(SORT_BY_DATE)) {
                sortQueryPart.append(" order by c.lastUpdateDate ");
            }
            if (SORT_ORDER_DESC.equals(order)) {
                sortQueryPart.append(" DESC ");
            }
        }
        return new String(sortQueryPart);
    }

    private Set<String> getUniqueSetOfTags(List<Tag> tags) {
        return tags.stream()
                .map(Tag::getName)
                //     .map(tag -> tagRepository.findTagByName(tag).get())
                .collect(Collectors.toSet());
    }

    private boolean isNewTag(Tag tag) {
        return !tagRepository.findTagByName(tag.getName()).isPresent();
    }

    private GiftCertificateDto createGiftCertificateDto(GiftCertificate giftCertificate) {
        List<Tag> tags = giftCertificate.getTags();
        return GiftCertificateDto.builder()
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
