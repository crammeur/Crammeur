/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.repository;

import android.content.Context;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.qc.bergeron.marcantoine.crammeur.annotations.repository.Entity;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.NextAvailableIdException;
import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;
/**
 * Created by Marc-Antoine Bergeron on 2016-07-02.
 */
public abstract class DataFramework<T extends Data<K>, K> implements ca.qc.bergeron.marcantoine.crammeur.repository.i.DataFramework<T, K> {

    protected final Class<T> mClazz;
    protected final Class<K> mKey;
    protected final Repository mRepository;
    protected final String mDBName;

    protected DataFramework(@NonNull Class<T> pClass, @NonNull Class<K> pKey, @NonNull Repository pRepository) {
        mClazz = pClass;
        synchronized (mClazz) {
            String dbName;
            if (!mClazz.isAnnotationPresent(Entity.class) || (dbName = mClazz.getAnnotation(Entity.class).dbName()) == "")
                dbName = mClazz.getSimpleName() + "s";
            mDBName = dbName;
            mKey = pKey;
            synchronized (mKey) {
                mRepository = pRepository;
            }
        }
    }

    /**
     * @param pData data
     * @return
     */
    public boolean contains(T pData) {
        synchronized (mClazz) {
            boolean result = false;
            if (pData.getId() != null) {
                result = this.contains(pData.getId());
            } else {
                for (T data : this.getAll()) {
                    data.setId(null);
                    if (data.toString().equals(pData.toString())) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }
    }

    @Override
    /**
     *
     * @param pKey
     * @return
     */
    public boolean contains(K pKey) {
        synchronized (mKey) {
            return this.getAllKeys().contains(pKey);
        }
    }

    /**
     * @return
     */
    @Override
    public List<T> getAll() {
        return this.getByKeys(this.getAllKeys());
    }

    @Override
    public abstract java.util.SortedSet<K> getAllKeys();

    /**
     * @param pKeys
     * @return
     */
    @Override
    public List<T> getByKeys(Set<K> pKeys) {
        synchronized (mClazz) {
            List<T> result = new ArrayList<>();
            for (K key : pKeys) {
                result.add(this.getByKey(key));
            }
            return result;
        }
    }

    @Override
    public K getId(T pEntity) {
        synchronized (mClazz) {
            if (pEntity.getId() != null) return pEntity.getId();
            for (T entity : this.getAll()) {
                K key = entity.getId();
                entity.setId(null);
                if (pEntity.toString().equals(entity.toString())) {
                    pEntity.setId(key);
                    return pEntity.getId();
                }
            }
            return null;
        }
    }

    @Override
    public void deleteAll() {
        for (T entity : this.getAll()) {
            try {
                this.delete(entity);
            } catch (KeyException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (DeleteException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

}
