/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.model.entity;

import ca.qc.bergeron.marcantoine.crammeur.annotations.repository.Entity;
import ca.qc.bergeron.marcantoine.crammeur.model.Data;

/**
 * Created by Marc-Antoine Bergeron on 2016-06-25.
 */
@Entity()
abstract class DataEntity<K> extends Data<K> implements ca.qc.bergeron.marcantoine.crammeur.model.entity.i.DataEntity<K> {

    public DataEntity() {
        super();
    }

    public DataEntity(K pKey) {
        super(pKey);
    }

    /**
     * @return
     */
    @Override
    public final String getAddress() {
        synchronized (this) {
            return java.lang.Object.class.cast(this).toString();
        }
    }
}
