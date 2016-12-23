/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.model.entity;

/**
 * Created by Marc-Antoine on 2016-10-15.
 */
public final class Product extends DataEntity<Integer> {

    public String Name;
    public String Description;
    public Double Price;
    public Integer Unit;
    public Company Company;

    public Product(String pName, Company pCompany, String pDescription, Double pPrice, Integer pUnit) {
        this(null, pName, pCompany, pDescription, pPrice, pUnit);
    }

    public Product(Integer pId, String pName, Company pCompany, String pDescription, Double pPrice, Integer pUnit) {
        super(pId);
        Name = pName;
        Description = pDescription;
        Price = pPrice;
        Unit = pUnit;
        this.Company = pCompany;
    }

}
