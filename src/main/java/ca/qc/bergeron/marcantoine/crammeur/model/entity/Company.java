/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.model.entity;

import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;

/**
 * Created by Marc-Antoine on 2016-05-15.
 */
public final class Company extends DataEntity<Integer> implements Data<Integer> {

    private String mName;

    public Company(String pName) {
        super(null);
        mName = pName;
    }

    public Company(Integer pId, String pName) {
        super(pId);
        mName = pName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String pName) {
        mName = pName;
    }
}
