/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.service;

import android.content.Context;

import java.io.File;
import java.io.Serializable;
import java.util.Set;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Company;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Invoice;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Product;
import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;
import ca.qc.bergeron.marcantoine.crammeur.repository.DataFramework;
import ca.qc.bergeron.marcantoine.crammeur.repository.Repository;
import ca.qc.bergeron.marcantoine.crammeur.service.crud.CompanyService;
/**
 * Created by Marc-Antoine Bergeron on 2016-10-02.
 */
public class Service implements ca.qc.bergeron.marcantoine.crammeur.service.i.Service {

    private final Repository repo;

    public final CompanyService Companys;
    public final EntityFramework<Invoice, Integer> Invoices;
    public final EntityFramework<Product, Integer> Products;

    public Service(Context pContext) {
        repo = new Repository(pContext);
        Companys = new CompanyService((DataFramework<Company, Integer>) repo.getDataFramework(Company.class));
        Invoices = new EntityFramework<>(repo.getDataFramework(Invoice.class));
        Products = new EntityFramework<>(repo.getDataFramework(Product.class));
    }

    @Override
    public Object save(Data pData) throws KeyException {
        return null;
    }

    @Override
    public Iterable<Data<?>> getAll(Class<? extends Data> pClass) {
        return null;
    }

    @Override
    public Set<?> getAllKeys(Class<? extends Data> pClass) {
        return null;
    }

    @Override
    public Data<?> getByKey(Class<? extends Data> pClass, Object pKey) {
        return null;
    }

    @Override
    public Iterable<Data<Object>> getByKeys(Class<? extends Data> pClass, Set<Object> pKeys) {
        return null;
    }

    @Override
    public boolean contains(Class<? extends Data> pClass) {
        return false;
    }

    @Override
    public void delete(Data pData) throws KeyException, DeleteException {

    }

    @Override
    public void clear() {

    }
}
