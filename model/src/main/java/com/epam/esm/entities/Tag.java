package com.epam.esm.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, max = 20, message = "The tag name size must be between {min} and {max} characters long")
    private String name;

    @ManyToMany(mappedBy = "tags",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = GiftCertificate.class)
    @JsonBackReference
    private List<GiftCertificate> giftCertificates;

}
