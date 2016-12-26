/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.model.entity;

import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;

/**
 * Created by Marc-Antoine on 2016-05-15.
 */
public final class Company extends DataEntity<Integer> implements Data<Integer> {

    public String Name;

    public Company(String pName) {
        super(null);
        Name = pName;
    }

    public Company(Integer pId, String pName) {
        super(pId);
        Name = pName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String pName) {
        Name = pName;
    }
}
