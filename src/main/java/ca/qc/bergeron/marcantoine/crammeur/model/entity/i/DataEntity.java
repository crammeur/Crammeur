/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.model.entity.i;

import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;

/**
 * Created by Marc-Antoine Bergeron on 2016-06-25.
 */
public interface DataEntity<K> extends Data<K> {
    String getAddress();
    @Override
    String toString();
}
