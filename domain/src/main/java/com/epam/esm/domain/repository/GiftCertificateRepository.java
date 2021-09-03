package com.epam.esm.domain.repository;

import com.epam.esm.domain.entities.GiftCertificate;
import com.epam.esm.domain.entities.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiftCertificateRepository {

    Long save(GiftCertificate giftCertificate);

    Optional<GiftCertificate> findById(Long id);

    void update(GiftCertificate giftCertificate);

    void delete(Long id);

    void addTagToGiftCertificate(GiftCertificate giftCertificate, Tag tag);

    List<GiftCertificate> findGiftCertificatesByTag(String tagName);

    List<GiftCertificate> findByMatch(String searchCondition);

    List<GiftCertificate> findAllSortedByDate();

    List<GiftCertificate> findAllSortedByDateDesc();

    List<GiftCertificate> findAllSortedByName();

    List<GiftCertificate> findAllSortedByNameDesc();

}
