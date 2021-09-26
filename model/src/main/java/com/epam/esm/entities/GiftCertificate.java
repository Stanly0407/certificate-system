package com.epam.esm.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@javax.persistence.Entity
@Table(name = "certificate")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Builder
public class GiftCertificate extends Entity {

    private String name;

    private String description;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    private int duration;    // the validity of the gift certificate

    @Column(name = "create_date", nullable = false)
    @Generated(value = GenerationTime.INSERT)
    private LocalDateTime createDate;

    @Column(name = "last_update_date", nullable = false)
    @Generated(value = GenerationTime.ALWAYS)
    private LocalDateTime lastUpdateDate;

    @ManyToMany(
            fetch = FetchType.LAZY,
            targetEntity = Tag.class)
    @JoinTable(name = "certificate_tag",
            joinColumns = {@JoinColumn(name = "certificate_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    //  @JsonManagedReference
  // @JsonBackReference
    private List<Tag> tags;

    @OneToMany(mappedBy = "giftCertificate", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    //  @JsonManagedReference
    @JsonBackReference
    private List<Order> orders;

}
