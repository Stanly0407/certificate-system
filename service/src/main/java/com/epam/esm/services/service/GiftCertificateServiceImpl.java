package com.epam.esm.services.service;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.forms.GiftCertificateTagsWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.esm.services.exceptions.ExceptionMessageType.INCORRECT_PARAMETERS;

@Service
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final String SEARCH_ALL = "ALL";
    private static final String SEARCH_BY_TAG = "BY_TAG";
    private static final String SEARCH_BY_TAGS = "BY_SEVERAL_TAGS";
    private static final String SEARCH_MATCH = "MATCH";
    private static final String NAME_PARAM = "name";
    private static final String DESCRIPTION_PARAM = "description";
    private static final String PRICE_PARAM = "price";
    private static final String DURATION_PARAM = "duration";
    private static final String EMPTY_PARAM = null;
    private static final List<String> EMPTY_PARAM_LIST = null;
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;


    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
    }

    public GiftCertificate findById(Long id) throws ResourceNotFoundException {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(id);
        if (giftCertificateOptional.isPresent()) {
            return giftCertificateOptional.get();
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    public Long saveNewGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags) {
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
        return newGiftCertificateId;
    }

    public void updateGiftCertificate(Long giftCertificateId, GiftCertificateTagsWrapper giftCertificateTagsWrapper)
            throws ResourceNotFoundException {
        GiftCertificate giftCertificate = findById(giftCertificateId);
        GiftCertificate updatedGiftCertificate = giftCertificateTagsWrapper.getGiftCertificate();
        updatedGiftCertificate.setId(giftCertificateId);
        giftCertificateRepository.update(giftCertificate);
        tagRepository.deleteGiftCertificateTags(giftCertificate.getId());
        List<Tag> tags = giftCertificateTagsWrapper.getTags();
        Optional<Tag> newTag;
        Set<String> uniqueSetOfTags = getUniqueSetOfTags(tags);
        for (String tagName : uniqueSetOfTags) {
            if (!tagRepository.findTagByName(tagName).isPresent()) {
                Long newTagId = tagRepository.save(Tag.builder().name(tagName).build());
                newTag = tagRepository.findById(newTagId);
            } else {
                newTag = tagRepository.findTagByName(tagName);
            }
            newTag.ifPresent(tag -> giftCertificateRepository.addTagToGiftCertificate(giftCertificate, tag));
        }
    }

    public void deleteGiftCertificate(Long id) throws ResourceNotFoundException {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        if (!giftCertificate.isPresent()) {
            throw new ResourceNotFoundException(id);
        } else {
            giftCertificateRepository.delete(id);
        }
    }

    public List<GiftCertificate> findGiftCertificates(List<String> tagNames, List<String> sortParams, String order,
                                                      String searchCondition, int pageNumber, int pageSize) throws BadRequestException {
        if ((tagNames != null && searchCondition != null) || (sortParams == null && order != null)) {
            throw new BadRequestException(INCORRECT_PARAMETERS);
        } else {
            List<GiftCertificate> giftCertificateList;
            if (tagNames != null) {
                if (tagNames.size() == 1) {
                    String tagName = tagNames.get(0);
                    giftCertificateList = giftCertificateRepository.findGiftCertificatesByTag(sortParams, order, tagName, pageNumber, pageSize);
                } else {
                    giftCertificateList = giftCertificateRepository.findGiftCertificateBySeveralTags(sortParams, order, tagNames, pageNumber, pageSize);
                }
            } else if (searchCondition != null) {
                giftCertificateList = giftCertificateRepository.findByMatch(sortParams, order, searchCondition, pageNumber, pageSize);
            } else {
                giftCertificateList = giftCertificateRepository.findAllGiftCertificates(sortParams, order, pageNumber, pageSize);
            }
            return giftCertificateList;
        }
    }

    public Long getPaginationInfo(int pageNumber, int pageSize, List<String> tagNames, String searchCondition)
            throws ResourceNotFoundException {
        long countResult;
        if (tagNames != null && tagNames.size() == 1) {
            String tagName = tagNames.get(0);
            countResult = giftCertificateRepository.countGiftCertificateSelect(SEARCH_BY_TAG, EMPTY_PARAM_LIST, EMPTY_PARAM, tagName);
        } else if (tagNames != null && tagNames.size() > 1) {
            countResult = giftCertificateRepository.countGiftCertificateSelect(SEARCH_BY_TAGS, tagNames, EMPTY_PARAM, EMPTY_PARAM);
        } else if (searchCondition != null) {
            countResult = giftCertificateRepository.countGiftCertificateSelect(SEARCH_MATCH, EMPTY_PARAM_LIST, searchCondition, EMPTY_PARAM);
        } else {
            countResult = giftCertificateRepository.countGiftCertificateSelect(SEARCH_ALL, EMPTY_PARAM_LIST, EMPTY_PARAM, EMPTY_PARAM);
        }
        long pageQuantity;
        if ((countResult % pageSize) == 0) {
            pageQuantity = (countResult / pageSize);
        } else {
            pageQuantity = (countResult / pageSize) + 1;
        }

        if (pageQuantity < pageNumber && countResult != 0) {
            throw new ResourceNotFoundException();
        } else {
            return pageQuantity;
        }
    }

    @Override
    public boolean partialGiftCertificateUpdate(String name, String description, BigDecimal price, Integer duration, Long id)
            throws ResourceNotFoundException {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        if (giftCertificate.isPresent()) {
            List<Object> params = Stream.of(name, description, price, duration).filter(Objects::nonNull).collect(Collectors.toList());
            if (params.size() == 1) {
                String parameterName = null;
                String parameter = null;
                if (name != null) {
                    parameterName = NAME_PARAM;
                    parameter = name;
                } else if (description != null) {
                    parameterName = DESCRIPTION_PARAM;
                    parameter = description;
                } else if (price != null) {
                    parameterName = PRICE_PARAM;
                    parameter = price.toString();
                } else if (duration != null) {
                    parameterName = DURATION_PARAM;
                    parameter = Integer.toString(duration);
                }
                return updateGiftCertificateField(parameterName, parameter, id);
            } else {
                return false;
            }
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    private boolean updateGiftCertificateField(String parameterName, String parameter, Long giftCertificateId) {
        if (parameter == null) {
            return false;
        } else {
            giftCertificateRepository.partialGiftCertificateUpdate(parameterName, parameter, giftCertificateId);
            return true;
        }
    }

    private Set<String> getUniqueSetOfTags(List<Tag> tags) {
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
    }

}
