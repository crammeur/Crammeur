/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.repository;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.os.ParcelableCompat;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Company;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Invoice;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Product;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Sale;
import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;
import ca.qc.bergeron.marcantoine.crammeur.repository.crud.FilesTemplate;
import ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite.SQLiteCompany;
import ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite.SQLiteProduct;
import ca.qc.bergeron.marcantoine.crammeur.repository.i.DataFramework;

/**
 * Created by Marc-Antoine on 2016-05-06.
 */
public final class Repository implements ca.qc.bergeron.marcantoine.crammeur.repository.i.Repository {

    protected final DataFramework<Company, Integer> Companys;
    protected final DataFramework<Invoice, Integer> Invoices;
    protected final DataFramework<Sale, Long> Sales;
    protected final DataFramework<Product, Integer> Products;


    public Repository(Context pContext) {
        this(pContext,pContext.getFilesDir());
    }

    /**
     * @param pFile
     */
    public Repository(Context pContext, File pFile) {
        try {
            Companys = new SQLiteCompany(this, pContext);
            Invoices = new FilesTemplate<>(Invoice.class, Integer.class, this, pFile);
            Sales = new FilesTemplate<>(Sale.class, Long.class, this, pFile);
            Products = new SQLiteProduct(this,pContext);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param pClass
     * @return
     */
    public final boolean contains(Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data> pClass) {
        for (Field f : this.getClass().getDeclaredFields()) {
            if (DataFramework.class.isAssignableFrom(f.getType()) && ParameterizedType.class.isInstance(f.getGenericType()) &&
                    (((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]).equals(pClass)) {
                return true;
            }
        }
        return false;
    }

    public List<DataFramework> getAllDataFramework() {
        List<DataFramework> result = new ArrayList<>();
        for (Field f : this.getClass().getDeclaredFields()) {
            if (DataFramework.class.isAssignableFrom(f.getType())) {
                final boolean b = f.isAccessible();
                f.setAccessible(true);
                try {
                    result.add(DataFramework.class.cast(f.get(this)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                f.setAccessible(b);
                break;
            }
        }
        return result;
    }

    /**
     * @param pClass
     * @return
     * @throws IllegalAccessException
     */
    @Nullable
    public final DataFramework<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data, ?> getDataFramework(Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data> pClass) {
        DataFramework result = null;
        for (Field f : this.getClass().getDeclaredFields()) {
            if (DataFramework.class.isAssignableFrom(f.getType()) && ParameterizedType.class.isInstance(f.getGenericType()) &&
                    (((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]).equals(pClass)) {
                final boolean b = f.isAccessible();
                f.setAccessible(true);
                try {
                    result = DataFramework.class.cast(f.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                f.setAccessible(b);
                break;
            }
        }
        return result;
    }

    /**
     * @param pClass
     * @return
     */
    public final Iterable<ca.qc.bergeron.marcantoine.crammeur.model.i.Data<?>> getAll(Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data> pClass) {
        return ((DataFramework<ca.qc.bergeron.marcantoine.crammeur.model.i.Data<?>, ?>) this.getDataFramework(pClass)).getAll();
    }

    /**
     * @param pClass
     * @return
     */
    public final Set<?> getAllKeys(Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data> pClass) {
        return ((DataFramework<ca.qc.bergeron.marcantoine.crammeur.model.i.Data<?>, ?>) this.getDataFramework(pClass)).getAllKeys();
    }

    /**
     * @param pClass
     * @param pKey
     * @return
     */
    public final ca.qc.bergeron.marcantoine.crammeur.model.i.Data<?> getByKey(Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data> pClass, Object pKey) {
        return ((DataFramework<ca.qc.bergeron.marcantoine.crammeur.model.i.Data<java.lang.Object>, java.lang.Object>) this.getDataFramework(pClass)).getByKey(pKey);
    }

    /**
     * @param pClass
     * @param pKeys
     * @return
     */
    public final Iterable<ca.qc.bergeron.marcantoine.crammeur.model.i.Data<Object>> getByKeys(Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data> pClass, Set<Object> pKeys) {
        return ((DataFramework<ca.qc.bergeron.marcantoine.crammeur.model.i.Data<java.lang.Object>, java.lang.Object>) this.getDataFramework(pClass)).getByKeys(pKeys);
    }

    /**
     * @param pData
     * @return
     * @throws KeyException
     */
    public final Object save(ca.qc.bergeron.marcantoine.crammeur.model.i.Data pData) throws KeyException {
        try {
            if(!this.save(pData.getClass(), pData)) throw new RuntimeException();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return pData.getId();
    }
    
    /**
     * @param pClass
     * @param pObject
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    private boolean save(Class<? extends ca.qc.bergeron.marcantoine.crammeur.model.i.Data> pClass, ca.qc.bergeron.marcantoine.crammeur.model.i.Data pObject) throws ClassNotFoundException, IllegalAccessException, KeyException {
        for (Field f : this.getClass().getDeclaredFields()) {
            if (DataFramework.class.isAssignableFrom(f.getType()) && ParameterizedType.class.isInstance(f.getGenericType()) &&
                    (((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]).equals(pClass)) {
                final boolean b = f.isAccessible();
                f.setAccessible(true);
                boolean result = DataFramework.class.cast(f.get(this)).save(pClass.cast(pObject)).equals(pClass.cast(pObject).getId());
                f.setAccessible(b);
                return result;
            }
        }
        throw new ClassNotFoundException();
    }

    /**
     * @param pData
     * @throws KeyException
     * @throws DeleteException
     */
    public final void delete(ca.qc.bergeron.marcantoine.crammeur.model.i.Data pData) throws KeyException, DeleteException {
        this.delete(pData.getClass(), pData.getId());
    }

    private final void delete(Class<? extends Data> pClass, Object pId) {
        for (Field f : this.getClass().getDeclaredFields()) {
            if (DataFramework.class.isAssignableFrom(f.getType()) && ParameterizedType.class.isInstance(f.getGenericType()) &&
                    (((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0]).equals(pClass)) {
                try {
                    DataFramework.class.cast(f.get(this)).delete(getByKey(pClass, pId));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (KeyException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (DeleteException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    public void clear() {
        List<DataFramework> l = this.getAllDataFramework();
        for (int i=l.size()-1;i >= 0; i--) {
            l.get(i).deleteAll();
        }
    }
}
