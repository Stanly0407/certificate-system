package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateRepository {

    GiftCertificate findById(Long id);

    Long save(GiftCertificate giftCertificate);

    void delete(Long id);

}
