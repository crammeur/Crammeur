/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.i.DataEntity;
import ca.qc.bergeron.marcantoine.crammeur.repository.i.DataFramework;

/**
 * Created by Marc-Antoine Bergeron on 2016-06-25.
 */
public class EntityFramework<T extends DataEntity<K>, K> implements ca.qc.bergeron.marcantoine.crammeur.service.i.EntityFramework<T, K> {

    protected final DataFramework<T, K> mDataFramework;

    protected EntityFramework(DataFramework pDataFramework) {
        mDataFramework = pDataFramework;
    }

    public void setGetAll(int pCount) {
        count = pCount;
    }

    private int count = 100;
    private int index = 0;
    @NonNull
    @Override
    public final List<T> getAll() {
        List<T> result = new ArrayList<>();
        Object[] l = this.getAllKeys().toArray();
        for (int i = index; (i < index + count && i < l.length ) || (count == 0 && i < l.length);i++) {
            result.add(mDataFramework.getByKey((K) l[i]));
        }
        index += count;
        return result;
    }

    @NonNull
    @Override
    public Set<K> getAllKeys() {
        return mDataFramework.getAllKeys();
    }

    @Override
    public final Iterable<T> getByKeys(Set<K> pKeys) {
        return mDataFramework.getByKeys(pKeys);
    }

    @NonNull
    @Override
    public T getByKey(@NonNull K pKey) {
        return mDataFramework.getByKey(pKey);
    }

    @Nullable
    @Override
    public K getId(@NonNull T pEntity) {
        return mDataFramework.getId(pEntity);
    }

    @NonNull
    @Override
    public K save(@NonNull T pData) throws KeyException {
        return mDataFramework.save(pData);
    }

    @Override
    public Iterable<K> save(T... pEntitys) throws KeyException {
        return mDataFramework.save(pEntitys);
    }

    @Override
    public final boolean contains(K pKey) {
        return mDataFramework.contains(pKey);
    }

    @Override
    public boolean contains(T pData) {
        return mDataFramework.contains(pData);
    }


    @Override
    public void delete(T pData) throws KeyException, DeleteException {
        try {
            mDataFramework.delete(pData);
        } catch (KeyException e) {
            e.printStackTrace();
            throw e;
        } catch (DeleteException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteAll() {
        mDataFramework.deleteAll();
    }
}
