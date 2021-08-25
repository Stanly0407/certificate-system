package com.epam.esm.controllers.forms;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

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

    @Override
    public String toString() {
        return "GiftCertificateTagsWrapper{" +
                "giftCertificate=" + giftCertificate +
                ", tags=" + tags +
                '}';
    }
}
