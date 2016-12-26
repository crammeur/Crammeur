/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.model.entity;

import ca.qc.bergeron.marcantoine.crammeur.enums.models.data.SaleType;

/**
 * Created by Marc-Antoine on 2016-05-15.
 */
public final class Sale extends DataEntity<Long> implements ca.qc.bergeron.marcantoine.crammeur.model.i.Data<Long> {

    public SaleType SaleType;

    public Sale(SaleType pSaleType) {
        super();
        SaleType = pSaleType;
    }

    public Sale(Long pId, SaleType pSaleType) {
        super(pId);
        SaleType = pSaleType;
    }

}
