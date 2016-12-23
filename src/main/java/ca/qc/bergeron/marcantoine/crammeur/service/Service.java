/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.service;

import java.io.File;

import ca.qc.bergeron.marcantoine.crammeur.model.entity.Company;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Invoice;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Product;
import ca.qc.bergeron.marcantoine.crammeur.repository.DataFramework;
import ca.qc.bergeron.marcantoine.crammeur.repository.Repository;
import ca.qc.bergeron.marcantoine.crammeur.service.crud.CompanyService;
/**
 * Created by Marc-Antoine Bergeron on 2016-10-02.
 */
public class Service {

    private final Repository repo;

    public final CompanyService Companys;
    public final EntityFramework<Invoice, Integer> Invoices;
    public final EntityFramework<Product, Integer> Products;

    public Service(File pFile) {
        repo = new Repository(pFile);
        Companys = new CompanyService((DataFramework<Company, Integer>) repo.getDataFramework(Company.class));
        Invoices = new EntityFramework<>(repo.getDataFramework(Invoice.class));
        Products = new EntityFramework<>(repo.getDataFramework(Product.class));
    }
}
