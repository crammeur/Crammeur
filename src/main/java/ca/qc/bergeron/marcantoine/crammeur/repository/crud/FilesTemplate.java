/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.repository.crud;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.NextAvailableIdException;
import ca.qc.bergeron.marcantoine.crammeur.model.Data;
import ca.qc.bergeron.marcantoine.crammeur.repository.Repository;

/**
 * Created by Marc-Antoine on 2016-03-29.
 */
public class FilesTemplate<T extends Data<K>, K> extends CRUD<T, K> {

    private final File mBase;
    private final File mFolderData;
    private final File mIndexKeys;
    private final File mRelationKeys;

    private Map<String, K> mKeysMap;

    public FilesTemplate(Class<T> pClass, Class<K> pKey, Repository pRepository, File pFile) throws IOException, ClassNotFoundException {
        super(pClass, pKey, pRepository);
        synchronized (mClazz) {
            mBase = new File(pFile, "database");
            mBase.mkdirs();
            mFolderData = new File(mBase, mClazz.getSimpleName());
            mRelationKeys = new File(mBase, "Index");
            mFolderData.mkdirs();
            mIndexKeys = new File(mFolderData, "IndexKeys");
            if (false && !mIndexKeys.createNewFile()) {
                try{
                    FileInputStream fis = new FileInputStream(mIndexKeys);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    mKeysMap = (Map<String, K>) ois.readObject();
                    ois.close();
                    fis.close();
                } catch (EOFException e) {
                    mKeysMap = new HashMap<>();
                }

            } else {
                mKeysMap = new HashMap<>();
            }
            if (mKeys == null) {
                if (false && !mRelationKeys.createNewFile()) {
                    try {
                        FileInputStream fis = new FileInputStream(mRelationKeys);
                        ObjectInputStream ois = new ObjectInputStream(fis);
                        mKeys = (Map<Class, Set<Key>>) ois.readObject();
                        ois.close();
                        fis.close();
                    } catch (EOFException e) {
                        mKeys = new HashMap<>();
                    }
                } else {
                    mKeys = new HashMap<>();
                }
            }
            updateIndex();

        }
    }

    /**
     * @return
     */
    @Override
    public final List<T> getAll() {
        return super.getAll();
    }

    /**
     * @return
     */
    @Override
    public final SortedSet<K> getAllKeys() {
        synchronized (mClazz) {
            SortedSet<K> result = new TreeSet<>();
            for (K key : mKeysMap.values()) {
                result.add(key);
            }
            return result;
        }
    }

    /**
     * @param pId
     * @return
     */
    @Nullable
    @Override
    public final T findById(@NonNull K pId) {
        synchronized (mClazz) {
            T o = null;
            File f = new File(mFolderData, pId.toString());
            if (mKeysMap.values().contains(pId) || f.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(f);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    o = mClazz.cast(this.deserialize(mClazz, (Map<String, Map<String, Object>>) ois.readObject()));
                    ois.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            return o;
        }
    }


    @Override
    protected final K createToDB(T pEntity) throws KeyException, NextAvailableIdException {
        synchronized (mClazz) {
            try {
                mKeysMap.put(pEntity.getId().toString(), pEntity.getId());
                File f = new File(mFolderData, pEntity.getId().toString());
                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(this.serialize(pEntity));
                oos.flush();
                oos.close();
                fos.close();
                updateIndex();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return pEntity.getId();
        }
    }

    /**
     * @param pEntity
     * @return
     * @throws KeyException
     */
    @Override
    protected final K updateToDB(T pEntity) throws KeyException {
        return super.updateToDB(pEntity);
    }

    /**
     *
     * @param pId
     * @throws KeyException
     * @throws DeleteException
     */
    @Override
    public final void delete(K pId) throws KeyException, DeleteException {
        synchronized (mClazz) {
            Data d = getByKey(pId);
            if (mKeys != null && mKeys.containsKey(d.getClass())) {
                for (Key key : mKeys.get(d.getClass())) {
                    if (d.getId().equals(key.mKey1)) {
                        try {
                            Data dd = (Data) mRepository.getByKey(key.mClass, key.mKey2);
                            if (dd != null) {
                                mRepository.delete(dd);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            throw new DeleteException("Class Not Found");
                        }
                        Set<Key> keys = mKeys.get(d.getClass());
                        keys.remove(key);
                        mKeys.put(d.getClass(), keys);
                    }
                }
            }
            System.gc();
            File f = new File(mFolderData, pId.toString());
            if (f.exists()) {
                if (!f.delete()) throw new DeleteException("File not deleted");
            }
            mKeysMap.remove(pId.toString());
            updateIndex();
        }
    }

    @Override
    public final void deleteTable() {
        clearTable();
        for (File f : mFolderData.listFiles()){
            System.gc();
            f.delete();
        }
        if (!mFolderData.delete()) throw new RuntimeException("Delete Table");
    }

    /**
     *
     */
    private void updateIndex() {
        try {
            FileOutputStream fos = new FileOutputStream(mIndexKeys);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mKeysMap);
            oos.flush();
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            FileOutputStream fos = new FileOutputStream(mRelationKeys);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mKeys);
            oos.flush();
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param pObject
     * @return
     */
    private Map<String, Map<String, Object>> serialize(ca.qc.bergeron.marcantoine.crammeur.model.i.Data pObject) {
        Map<String, Map<String, Object>> fields = new HashMap<>();
        Class c = pObject.getClass();
        do {
            Map<String, Object> map = new HashMap<>();
            for (Field f : c.getDeclaredFields()) {
                if (!Modifier.isTransient(f.getModifiers()) && !(Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers()))) {
                    final boolean b = f.isAccessible();
                    try {
                        f.setAccessible(true);
                        if (Iterable.class.isAssignableFrom(f.getType()) && ca.qc.bergeron.marcantoine.crammeur.model.i.Data.class.isAssignableFrom((Class<?>) (((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]))
                                && mRepository.contains((Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0])) {
                            List<Object> ids = new ArrayList<>();
                            Iterator i = ((Iterable) f.get(pObject)).iterator();
                            while (i.hasNext()) {
                                ids.add(((Data) i.next()).getId());
                            }
                            map.put(f.getName(), ids);
                        } else if (ca.qc.bergeron.marcantoine.crammeur.model.i.Data.class.isAssignableFrom(f.getType()) && mRepository.contains((Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data>) f.getType())) {
                            map.put(f.getName(), ((Data) f.get(pObject)).getId());
                        } else {
                            map.put(f.getName(), f.get(pObject));
                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    } finally {
                        f.setAccessible(b);
                    }
                }
            }
            fields.put(c.getName(), map);
        } while ((c = c.getSuperclass()) != null);
        return fields;
    }

    /**
     * @param pClass
     * @param pMap
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private Data deserialize(Class<? extends Data> pClass, Map<String, Map<String, Object>> pMap) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = null;
        for (Constructor<?> c : pClass.getDeclaredConstructors()) {
            if (constructor == null || constructor.getParameterTypes().length > c.getParameterTypes().length) {
                constructor = c;
            }
        }
        Data result;
        final boolean cb = constructor.isAccessible();
        constructor.setAccessible(true);
        if (constructor.getParameterTypes().length == 0) {
            result = (Data) constructor.newInstance();
        } else {
            java.lang.Object[] o = new Object[constructor.getParameterTypes().length];
            for (int i = 0; i < constructor.getParameterTypes().length; i++) {
                if (constructor.getParameterTypes()[i].isPrimitive()) {
                    o[i] = 0;
                } else {
                    o[i] = null;
                }
            }
            result = (Data) constructor.newInstance(o);
        }
        constructor.setAccessible(cb);
        Class c = pClass;
        do {
            for (Field f : c.getDeclaredFields()) {
                if (pMap.get(c.getName()).containsKey(f.getName())) {
                    final boolean b = f.isAccessible();
                    f.setAccessible(true);
                    if (Iterable.class.isAssignableFrom(f.getType()) && Data.class.isAssignableFrom((Class<?>) (((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0])) &&
                            mRepository.contains((Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0])) {
                        Iterator<Object> ids = ((Iterable) pMap.get(c.getName()).get(f.getName())).iterator();
                        List<Data> r = new ArrayList<>();
                        while (ids.hasNext()) {
                            r.add((Data) mRepository.getByKey((Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0], ids.next()));
                        }
                        f.set(result, r);
                    } else if (Data.class.isAssignableFrom(f.getType()) && mRepository.contains((Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data>) f.getType())) {
                        f.set(result, mRepository.getByKey((Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data>) f.getType(), pMap.get(c.getName()).get(f.getName())));
                    } else {
                        f.set(result, pMap.get(c.getName()).get(f.getName()));
                    }
                    f.setAccessible(b);
                }
            }
        } while ((c = c.getSuperclass()) != null);
        return result;
    }

}
