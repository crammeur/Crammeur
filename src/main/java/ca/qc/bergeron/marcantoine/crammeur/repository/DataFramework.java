/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.repository;

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

    private long index = 0;
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

    /**
     * @return
     * @throws NextAvailableIdException
     */
    @NonNull
    protected K nextAvailableId() throws NextAvailableIdException {
        synchronized (mClazz) {
            try {
                K max = null;
                for (K a : this.getAllKeys()) {
                    if (mKey.isAssignableFrom(Integer.class)) {
                        K val = mKey.cast(Integer.parseInt(a.toString()) + 1);
                        if (!this.contains(val)) {
                            max = val;
                        }
                    } else if (mKey.isAssignableFrom(Long.class)) {
                        K val = mKey.cast(Long.parseLong(a.toString()) + 1);
                        if (!this.contains(val))
                            max = val;
                    } else if (mKey.isAssignableFrom(String.class)) {
                        final Long m1;
                        final Long m2;
                        if (((String) mKey.cast(max)).length() > 16) {
                            m1 = new BigInteger(((String) mKey.cast(max)).substring(0, 16), 16).longValue();
                            m2 = new BigInteger(((String) mKey.cast(max)).substring(16), 16).longValue();
                        } else {
                            m1 = new BigInteger(((String) mKey.cast(max)), 16).longValue();
                            m2 = 0l;
                        }
                        final String tk = (String) mKey.cast(a);
                        final Long l1;
                        final Long l2;
                        if (tk.length() > 16) {
                            l1 = new BigInteger(tk.substring(0, 16), 16).longValue();
                            l2 = new BigInteger(tk.substring(16), 16).longValue();

                        } else {
                            l1 = new BigInteger(tk, 16).longValue();
                            l2 = 0l;
                        }
                        if ((m2.equals(l2) && ((m1 - l1) <= 0 && ((m1 > 0 && l1 > 0) || (m1 < 0 && l1 < 0)) || (m1 > 0 && l1 < 0)) ||
                                (m1.equals(l1) && ((m2 - l2) <= 0 && ((m2 > 0 && l2 > 0) || (m1 < 0 && l1 < 0)) || (m2 > 0 && l2 < 0))))) {
                            if (l1 + 1 == 0) {
                                K val = mKey.cast(Long.toHexString(l1) + Long.toHexString(l2 + 1));
                                if (!this.contains(val))
                                    max = val;
                            } else {
                                K val = mKey.cast(Long.toHexString(l1 + 1));
                                if (!this.contains(val))
                                    max = val;
                            }
                        }
                    }
                }
                if (max == null && this.getAllKeys().size() == 0) {
                    if (mKey.getGenericSuperclass().equals(Number.class)) {
                        max = mKey.cast(1);
                    } else if (mKey.isAssignableFrom(String.class)) {
                        max = mKey.cast(Long.toHexString(1));
                    }
                }
                if (max == null) throw new NextAvailableIdException();
                return max;
            } catch (NextAvailableIdException naie) {
                throw naie;
            } catch (Exception e) {
                e.printStackTrace();
                throw new NextAvailableIdException();
            }
        }
    }
}
