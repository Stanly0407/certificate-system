package com.epam.esm.domain.repository;

import com.epam.esm.domain.entities.GiftCertificate;
import com.epam.esm.domain.entities.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TagRepository {

    Optional<Tag> findById(Long id);

    Long save(Tag tag);

    void delete(Long id);

    Optional<Tag> findTagByName(String name);

    void deleteGiftCertificateTags(GiftCertificate giftCertificate);

    List<Tag> findGiftCertificateTags(Long giftCertificateId);

}
