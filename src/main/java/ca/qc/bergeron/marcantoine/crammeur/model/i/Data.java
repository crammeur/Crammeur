/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.model.i;

import java.io.Serializable;

import ca.qc.bergeron.marcantoine.crammeur.annotations.repository.Entity;

/**
 * Created by Marc-Antoine on 2016-03-28.
 */
@Entity()
public interface Data<K> extends Comparable<K>, Serializable {
    /**
     * Return object Id
     * @return id
     */
    K getId();

    /**
     * Set object Id
     * @param pId Id
     */
    void setId(K pId);
}
