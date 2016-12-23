/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.repository.i;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;

/**
 * Created by Marc-Antoine Bergeron on 2016-07-02.
 */
public interface DataFramework<T extends Data<K>, K> {

    /**
     * Create or update data
     * @param pData data
     * @return key
     */
    @NonNull
    K save(T pData) throws KeyException;

    /**
     * @return all data
     */
    @NonNull
    Iterable<T> getAll();

    /**
     * @return all keys
     */
    @NonNull
    Set<K> getAllKeys();

    /**
     * Get data by key
     * @param pKey key
     * @return data
     */
    @Nullable
    T getByKey(@NonNull K pKey);

    /**
     * Get id from data
     * @param pEntity
     * @return key
     */
    @Nullable
    K getId(@NonNull T pEntity);

    /**
     * @param pKeys
     * @return
     */
    @NonNull
    Iterable<T> getByKeys(@NonNull Set<K> pKeys);

    /**
     * Check if contains pKey
     * @param pKey key
     * @return
     */
    boolean contains(@NonNull K pKey);

    /**
     * Check if contains pData
     * @param pData
     * @return
     */
    boolean contains(@NonNull T pData);

    /**
     * Delete pData
     * @param pData data
     * @throws KeyException
     * @throws DeleteException
     */
    void delete(@NonNull T pData) throws KeyException, DeleteException;
}
