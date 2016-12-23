/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.repository.rest.i;

import java.net.URI;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;
import ca.qc.bergeron.marcantoine.crammeur.repository.i.DataFramework;

/**
 * Created by Marc-Antoine on 2016-06-09.
 */
public interface REST<T extends Data<K>, K> extends DataFramework<T, K> {

    /**
     * Replace the entire collection with another collection.
     *
     * @param pURI
     * @param pData
     * @return
     */
    T put(URI pURI, T pData) throws KeyException;

    T get(URI pURI);

    Iterable<T> getAll(URI pURI);

    /**
     * Create a new entry in the collection. The new entry's URI is assigned automatically and is usually returned by the operation
     *
     * @param pURI
     * @param pData
     * @return
     */
    T post(URI pURI, T pData);

    void delete(URI pURI);
}
