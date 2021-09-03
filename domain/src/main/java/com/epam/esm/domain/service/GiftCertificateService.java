package com.epam.esm.domain.service;

import com.epam.esm.domain.dto.GiftCertificateDto;
import com.epam.esm.domain.entities.GiftCertificate;
import com.epam.esm.domain.entities.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GiftCertificateService {

    Optional<GiftCertificateDto> findById(Long id);

    void saveNewGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags);

    void updateGiftCertificate(GiftCertificate giftCertificate, List<Tag> tags);

    void deleteGiftCertificate(Long id);

    List<GiftCertificateDto> findGiftCertificatesByTag(String name);

    List<GiftCertificateDto> findGiftCertificatesByNameOrDescription(String searchCondition);

    List<GiftCertificateDto> getGiftCertificatesSortedByCondition(String sortingCondition);

}
