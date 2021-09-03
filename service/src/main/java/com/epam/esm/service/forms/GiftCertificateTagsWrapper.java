package com.epam.esm.service.forms;


import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;

import java.util.List;

public class GiftCertificateTagsWrapper {

    private GiftCertificate giftCertificate;
    private List<Tag> tags;

    public GiftCertificateTagsWrapper() {
    }

    public GiftCertificateTagsWrapper(GiftCertificate giftCertificate, List<Tag> tags) {
        this.giftCertificate = giftCertificate;
        this.tags = tags;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

}
