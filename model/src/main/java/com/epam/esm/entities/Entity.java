package com.epam.esm.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * This abstract Class {@code Entity} is the root of all com.epam.esm.entities of this application
 * and defines id field as mandatory field of every subclass.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@SuperBuilder
public abstract class Entity extends RepresentationModel<Entity> implements Cloneable, Serializable {

    /**
     * This is a unique field of an entity that allows it to be distinguished from other com.epam.esm.entities
     * and to identify it.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

}

