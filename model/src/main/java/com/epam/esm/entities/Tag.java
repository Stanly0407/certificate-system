package com.epam.esm.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@javax.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Builder
public class Tag extends Entity {

    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(min = 2, max = 20)
    private String name;

    @ManyToMany(mappedBy = "tags",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = GiftCertificate.class)
    @JsonBackReference
    private List<GiftCertificate> giftCertificates;

    @Builder
    public Tag(long id, String name, List<GiftCertificate> giftCertificates) {
        super(id);
        this.name = name;
        this.giftCertificates = giftCertificates;
    }
}
