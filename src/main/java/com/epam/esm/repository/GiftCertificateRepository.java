package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GiftCertificateRepository {

    Long save(GiftCertificate giftCertificate);

    Optional<GiftCertificate> findById(Long id);

    void update(GiftCertificate giftCertificate);

    void delete(Long id);

}
