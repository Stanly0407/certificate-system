package com.epam.esm.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class AuditListener {

    private static final Logger LOGGER = LogManager.getLogger(AuditListener.class);

    @PrePersist
    @PreUpdate
    @PreRemove
    private void beforeAnyUpdate(Entity entity) {
        LOGGER.info("[AUDIT] About to create/update/delete " + entity.getClass().getName());
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    private void afterAnyUpdate(Entity entity) {
        LOGGER.info("[AUDIT] create/update/delete complete for " + entity.getClass().getName() + ", id: " + entity.getId());
    }

    @PostLoad
    private void afterLoad(Entity entity) {
        LOGGER.info("[AUDIT] " + entity.getClass().getName() + " loaded from database, id: " + entity.getId());
    }

}
