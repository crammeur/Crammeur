/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.service.i;

import ca.qc.bergeron.marcantoine.crammeur.model.entity.i.DataEntity;
import ca.qc.bergeron.marcantoine.crammeur.repository.i.DataFramework;

/**
 * Created by Marc-Antoine Bergeron on 2016-06-18.
 */
public interface EntityFramework<T extends DataEntity<K>, K> extends DataFramework<T, K> {
}
