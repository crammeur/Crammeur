/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.repository.crud.i;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.UpdateException;
import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;
import ca.qc.bergeron.marcantoine.crammeur.repository.i.DataFramework;

/**
 * Created by Marc-Antoine on 2016-03-29.
 */
public interface CRUD<T extends Data<K>, K> extends DataFramework<T, K> {

    @NonNull
    K create(@NonNull T pEntity) throws KeyException;

    @NonNull
    Iterable<T> getAll();

    @Nullable
    T findById(@NonNull K pId) throws KeyException;

    @Nullable
    K findId(@NonNull T pEntity);

    void update(@NonNull T pEntity) throws KeyException, UpdateException;

    void delete(@NonNull K pId) throws KeyException, DeleteException;
}
