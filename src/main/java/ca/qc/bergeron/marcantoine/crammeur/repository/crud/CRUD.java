/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.repository.crud;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.NextAvailableIdException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.UpdateException;
import ca.qc.bergeron.marcantoine.crammeur.model.Data;
import ca.qc.bergeron.marcantoine.crammeur.repository.DataFramework;
import ca.qc.bergeron.marcantoine.crammeur.repository.Repository;

/**
 * Created by Marc-Antoine Bergeron on 2016-07-02.
 */
abstract class CRUD<T extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data<K>, K> extends DataFramework<T, K> implements ca.qc.bergeron.marcantoine.crammeur.repository.crud.i.CRUD<T, K> {

    static Map<Class, Set<Key>> mKeys = null;
    protected transient final Set<Field> mBDField;
    protected final boolean mRollBack;
    protected final boolean mUpdateCascade;
    protected final boolean mDeleteCascade;

    protected CRUD(Class<T> pClass, Class<K> pKey, final Repository pRepository) {
        this(pClass, pKey, pRepository, true);
    }

    protected CRUD(Class<T> pClass, Class<K> pKey, final Repository pRepositroy, boolean pRollback) {
        this(pClass, pKey, pRepositroy, pRollback, false, false);
    }

    protected CRUD(Class<T> pClass, Class<K> pKey, final Repository pRepositroy, boolean pRollback, boolean pUpdateCascade, boolean pDeleteCascade) {
        super(pClass, pKey, pRepositroy);
        Set<Field> fBD;
        synchronized (mClazz) {
            fBD = new HashSet<>();
            for (Field f : ca.qc.bergeron.marcantoine.crammeur.lang.Object.getAllSerializableFields(mClazz)) {
                final boolean b = f.isAccessible();
                f.setAccessible(true);
                if (ca.qc.bergeron.marcantoine.crammeur.model.i.Data.class.isAssignableFrom(f.getType())) {
                    if (pRepositroy.contains((Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data>) f.getType())) {
                        fBD.add(f);
                    }
                } else if (ParameterizedType.class.isInstance((f.getGenericType()))) {
                    ParameterizedType pt = (ParameterizedType) f.getGenericType();
                    for (Type t : pt.getActualTypeArguments()) {
                        if (ca.qc.bergeron.marcantoine.crammeur.model.i.Data.class.isAssignableFrom((Class<?>) t)) {
                            if (pRepositroy.contains((Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data>) t)) {
                                fBD.add(f);
                            }
                        }
                    }
                }
                f.setAccessible(b);
            }
            mBDField = fBD;
            mRollBack = pRollback;
            mUpdateCascade = pUpdateCascade;
            mDeleteCascade = pDeleteCascade;
        }
    }

    /**
     * @return
     */
    @NonNull
    @Override
    public List<T> getAll() {
        synchronized (mClazz) {
            List<T> result = new ArrayList<>();
            synchronized (mKey) {
                for (K key : this.getAllKeys()) {
                    result.add(this.getByKey(key));
                }
            }
            return result;
        }
    }

    /**
     * @return data set
     */
    @NonNull
    @Override
    public abstract SortedSet<K> getAllKeys();

    /**
     * Get data by key
     * @param pKey key
     * @return data
     */
    @Nullable
    @Override
    public final T getByKey(@NonNull K pKey) {
        synchronized (mKey) {
            try {
                return this.findById(pKey);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @param pEntity
     * @return id
     */
    @Nullable
    @Override
    public K findId(@NonNull T pEntity) {
        synchronized (mClazz) {
            return this.getId(pEntity);
        }
    }

    /**
     * @param pEntity
     * @return key
     * @throws KeyException
     */
    @NonNull
    @Override
    public final K save(@NonNull T pEntity) throws KeyException {
        synchronized (mClazz) {
            try {
                if (pEntity.getId() == null || !this.contains(pEntity.getId())) {
                    this.create(pEntity);
                } else {
                    this.update(pEntity);
                }
                return pEntity.getId();
            } catch (KeyException e) {
                e.printStackTrace();
                throw e;
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    @NonNull
    protected abstract K createToDB(@NonNull T pEntity) throws KeyException, NextAvailableIdException;

    /**
     * @param pEntity
     * @return
     * @throws KeyException
     */
    @NonNull
    @Override
    public final K create(@NonNull T pEntity) throws KeyException {
        synchronized (mClazz) {
            try {
                if (pEntity.getId() == null) {
                    pEntity.setId(this.nextAvailableId());
                } else if (this.contains(pEntity.getId())) {
                    throw new KeyException();
                }

                Set<Integer> indexs = new HashSet<>();
                java.lang.Object[] fa = mBDField.toArray();
                for (int i = 0; i < fa.length; i++) {
                    Field field = (Field) fa[i];
                    if (Data.class.isAssignableFrom(field.getType())) {
                        final boolean b = field.isAccessible();
                        try {
                            field.setAccessible(true);
                            mRepository.save((ca.qc.bergeron.marcantoine.crammeur.model.i.Data) field.get(pEntity));
                            if (mKeys != null) {
                                if (!mKeys.containsKey(field.getType())) {
                                    mKeys.put(field.getType(), new HashSet<Key>());
                                }
                                mKeys.get(field.getType()).add(new Key(pEntity.getId(), ((Data) field.get(pEntity)).getId(), mClazz));
                            }
                        } catch (Throwable e) {
                            throw e;
                        } finally {
                            field.setAccessible(b);
                        }
                    } else {
                        indexs.add(i);
                    }
                }

                if (this.createToDB(pEntity) != pEntity.getId()) throw new KeyException();

                for (Integer i : indexs) {
                    Field field = (Field) fa[i];
                    final boolean b = field.isAccessible();
                    try {
                        if (Iterable.class.isAssignableFrom(field.getType())) {
                            field.setAccessible(true);
                            final Iterable<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data> it = (Iterable<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data>) field.get(pEntity);
                            for (ca.qc.bergeron.marcantoine.crammeur.model.i.Data d : it) {
                                mRepository.save(d);
                                if (mKeys != null) {
                                    if (!mKeys.containsKey(field.getType())) {
                                        mKeys.put(field.getType(), new HashSet<Key>());
                                    }
                                    mKeys.get(field.getType()).add(new Key(pEntity.getId(), d.getId(), mClazz));
                                }
                            }
                        }
                    } catch (Throwable e) {
                        throw e;
                    } finally {
                        field.setAccessible(b);
                    }
                }
                return pEntity.getId();
            } catch (KeyException e) {
                e.printStackTrace();
                throw e;
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @param pEntity
     * @return
     * @throws ClassNotFoundException
     * @throws KeyException
     * @throws IllegalAccessException
     */
    @NonNull
    protected K updateToDB(@NonNull T pEntity) throws KeyException {
        synchronized (mClazz) {
            if (pEntity.getId() == null || !this.contains(pEntity)) throw new KeyException();
            //Create need to be destroy otherwise KeyException()
            try {
                this.delete(pEntity.getId());
            } catch (DeleteException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return this.create(pEntity);
        }
    }

    /**
     * @param pEntity
     * @throws KeyException
     * @throws UpdateException
     */
    @Override
    public final void update(@NonNull T pEntity) throws KeyException, UpdateException {
        synchronized (mClazz) {
            boolean error = false;
            T rollback = null;
            if (mRollBack) {
                rollback = this.findById(pEntity.getId());
            }
            if (pEntity.getId() == null || !this.contains(pEntity.getId()))
                throw new KeyException();
            try {
                Set<Integer> indexs = new HashSet<>();
                java.lang.Object[] fa = mBDField.toArray();

                if (mUpdateCascade) {
                    for (int i = 0; i < fa.length; i++) {
                        Field field = (Field) fa[i];
                        if (field.getType().isAssignableFrom(Data.class)) {
                            final boolean b = field.isAccessible();
                            field.setAccessible(true);
                            mRepository.save((ca.qc.bergeron.marcantoine.crammeur.model.i.Data) field.get(pEntity));
                            field.setAccessible(b);
                        } else {
                            indexs.add(i);
                        }
                    }
                }

                if (this.updateToDB(pEntity) != pEntity.getId()) throw new UpdateException();

                for (Integer i : indexs) {
                    Field field = (Field) fa[i];
                    if (field.getType().isAssignableFrom(Iterable.class)) {
                        final boolean b = field.isAccessible();
                        field.setAccessible(true);
                        Iterable<ca.qc.bergeron.marcantoine.crammeur.model.i.Data> it = (Iterable<ca.qc.bergeron.marcantoine.crammeur.model.i.Data>) field.get(pEntity);
                        for (ca.qc.bergeron.marcantoine.crammeur.model.i.Data d : it) {
                            mRepository.save(d);
                        }
                        field.setAccessible(b);
                    }
                }
            } catch (KeyException e) {
                e.printStackTrace();
                error = true;
                throw e;
            } catch (UpdateException e) {
                e.printStackTrace();
                error = true;
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
                throw new UpdateException(e);
            } finally {
                if (error && rollback != null) {
                    this.save(rollback);
                }
            }
        }
    }

    /**
     * @param pEntity data
     * @throws KeyException
     * @throws DeleteException
     */
    @Override
    public void delete(@NonNull T pEntity) throws KeyException, DeleteException {
        synchronized (mClazz) {
            boolean error = false;
            try {
                if (mDeleteCascade) {
                    for (java.lang.Object f : mBDField.toArray()) {
                        Field field = (Field) f;
                        if (Iterable.class.isAssignableFrom(field.getType())) {
                            final boolean b = field.isAccessible();
                            field.setAccessible(true);
                            Iterable<ca.qc.bergeron.marcantoine.crammeur.model.i.Data> it = (Iterable<ca.qc.bergeron.marcantoine.crammeur.model.i.Data>) field.get(pEntity);
                            for (ca.qc.bergeron.marcantoine.crammeur.model.i.Data d : it) {
                                mRepository.delete(d);
                            }
                            field.setAccessible(b);
                        } else if (Data.class.isAssignableFrom(field.getType())) {
                            mRepository.delete((ca.qc.bergeron.marcantoine.crammeur.model.i.Data) field.get(pEntity));
                        }
                    }
                }
                this.delete(pEntity.getId());
            } catch (KeyException e) {
                e.printStackTrace();
                error = true;
                throw e;
            } catch (DeleteException e) {
                e.printStackTrace();
                error = true;
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
                throw new DeleteException(e);
            } finally {
                if (error && mRollBack && !this.contains(pEntity.getId())) {
                    this.save(pEntity);
                }
            }
        }
    }

    public void clearTable() {
        synchronized (mClazz) {
            //performance + securite
            if (mRollBack) {
                List<T> rollback = new ArrayList<>();
                boolean error = false;
                for (T entity : this.getAll()) {
                    rollback.add(entity);
                    try {
                        this.delete(entity);
                    } catch (KeyException e) {
                        e.printStackTrace();
                        error = true;
                        throw new RuntimeException(e);
                    } catch (DeleteException e) {
                        e.printStackTrace();
                        error = true;
                        throw new RuntimeException(e);
                    } finally {
                        if (error) {
                            for (T entity2 : rollback) {
                                try {
                                    this.save(entity2);
                                } catch (KeyException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            } else {
                for (K key : this.getAllKeys()) {
                    try {
                        this.delete(key);
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
    }

    public abstract void deleteTable();

//    *
//     * @return key
//     * @throws NextAvailableIdException
//    @NonNull
//    protected K nextAvailableId() throws NextAvailableIdException {
//        synchronized (mClazz) {
//            try {
//                K max = null;
//                for (K a : this.getAllKeys()) {
//                    if (mKey.isAssignableFrom(Integer.class)) {
//                        K val = mKey.cast(Integer.parseInt(a.toString()) + 1);
//                        if (!this.contains(val)) {
//                            max = val;
//                        }
//                    } else if (mKey.isAssignableFrom(Long.class)) {
//                        K val = mKey.cast(Long.parseLong(a.toString()) + 1);
//                        if (!this.contains(val))
//                            max = val;
//                    } else if (mKey.isAssignableFrom(String.class)) {
//                        final Long m1;
//                        final Long m2;
//                        if (((String) mKey.cast(max)).length() > 16) {
//                            m1 = new BigInteger(((String) mKey.cast(max)).substring(0, 16), 16).longValue();
//                            m2 = new BigInteger(((String) mKey.cast(max)).substring(16), 16).longValue();
//                        } else {
//                            m1 = new BigInteger(((String) mKey.cast(max)), 16).longValue();
//                            m2 = 0l;
//                        }
//                        final String tk = (String) mKey.cast(a);
//                        final Long l1;
//                        final Long l2;
//                        if (tk.length() > 16) {
//                            l1 = new BigInteger(tk.substring(0, 16), 16).longValue();
//                            l2 = new BigInteger(tk.substring(16), 16).longValue();
//
//                        } else {
//                            l1 = new BigInteger(tk, 16).longValue();
//                            l2 = 0l;
//                        }
//                        if ((m2.equals(l2) && ((m1 - l1) <= 0 && ((m1 > 0 && l1 > 0) || (m1 < 0 && l1 < 0)) || (m1 > 0 && l1 < 0)) ||
//                                (m1.equals(l1) && ((m2 - l2) <= 0 && ((m2 > 0 && l2 > 0) || (m1 < 0 && l1 < 0)) || (m2 > 0 && l2 < 0))))) {
//                            if (l1 + 1 == 0) {
//                                K val = mKey.cast(Long.toHexString(l1) + Long.toHexString(l2 + 1));
//                                if (!this.contains(val))
//                                    max = val;
//                            } else {
//                                K val = mKey.cast(Long.toHexString(l1 + 1));
//                                if (!this.contains(val))
//                                    max = val;
//                            }
//                        }
//                    }
//                }
//                if (max == null && this.getAllKeys().size() == 0) {
//                    if (mKey.getGenericSuperclass().equals(Number.class)) {
//                        max = mKey.cast(1);
//                    } else if (mKey.isAssignableFrom(String.class)) {
//                        max = mKey.cast(Long.toHexString(1));
//                    }
//                }
//                if (max == null) throw new NextAvailableIdException();
//                return max;
//            } catch (NextAvailableIdException naie) {
//                throw naie;
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new NextAvailableIdException();
//            }
//        }
//    }

    static class Key implements Serializable {
        public Object mKey1;
        public Object mKey2;
        public Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data> mClass;

        public Key(@NonNull Object pKey1, @NonNull Object pkey2, @NonNull Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data> pClass) {
            mKey1 = pKey1;
            mKey2 = pkey2;
            mClass = pClass;
        }

        @Override
        public int hashCode() {
            int hash = 1;
            hash = hash * 2 + mKey1.hashCode();
            hash = hash * 22 + mKey2.hashCode();
            hash = hash * 222 + mClass.hashCode();
            return hash;
        }
    }

}
