package com.epam.esm.model;

import java.io.Serializable;

/**
 * This abstract Class {@code Entity} is the root of all entities of this application
 * and defines id field as mandatory field of every subclass.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
public abstract class Entity implements Cloneable, Serializable {

    /**
     * This is a unique field of an entity that allows it to be distinguished from other entities and to identify it.
     */
    private Long id;

    /**
     * Default constructor.
     */
    public Entity() {
    }

    /**
     * Constructs a new application entity with unique identifier.
     */
    public Entity(Long id) {
        this.id = id;
    }

    /**
     * public method to access the value of a private variable.
     *
     * @return a unique entity identifier;
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

