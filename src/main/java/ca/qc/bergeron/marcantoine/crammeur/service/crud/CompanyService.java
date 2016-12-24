/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.service.crud;

import android.support.annotation.NonNull;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Company;
import ca.qc.bergeron.marcantoine.crammeur.repository.i.DataFramework;

/**
 * Created by Marc-Antoine Bergeron on 2016-10-02.
 */
public class CompanyService extends ca.qc.bergeron.marcantoine.crammeur.service.EntityFramework<Company, Integer> {

    public CompanyService(DataFramework<Company, Integer> pDataFramework) {
        super(pDataFramework);
    }

    @NonNull
    @Override
    public Integer save(@NonNull Company pData) throws KeyException {
        if (pData.getId() == null && this.contains(pData)) {
            pData.setId(this.getId(pData));
            return pData.getId();
        } else if (pData.getId() != null) {
            Company c = pData;
            c.setId(null);
            if (this.contains(c)) {
                c.setId(this.getId(c));
                try {
                    mDataFramework.delete(c);
                } catch (DeleteException e) {
                    e.printStackTrace();
                    throw new KeyException();
                }
            }
        }
        return mDataFramework.save(pData);
    }
}
