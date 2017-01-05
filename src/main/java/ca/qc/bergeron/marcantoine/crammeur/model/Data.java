/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.model;

import com.google.gson.Gson;

import ca.qc.bergeron.marcantoine.crammeur.annotations.repository.Entity;


/**
 * Created by Marc-Antoine Bergeron on 2016-06-18.
 */
@Entity()
public abstract class Data<K> extends ca.qc.bergeron.marcantoine.crammeur.lang.Object implements ca.qc.bergeron.marcantoine.crammeur.model.i.Data<K> {

    @Entity.Id(name = "Id")
    private K mId;

    public Data() {
        this(null);
    }

    public Data(K pId) {
        mId = pId;
    }

    @Override
    public final K getId() {
        return mId;
    }

    @Override
    public void setId(K pId) {
        mId = pId;
    }

    public String toJson() {
        return new Gson().toJson(this, getClass());
    }

    public static Data<?> fromJson(String pJson, Class<? extends Data> pClass) {
        return new Gson().fromJson(pJson, pClass);
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return this.toGenericString();
    }

    /**
     * @param pKey
     * @return 0 ==; x <>
     */
    @Override
    public final int compareTo(K pKey) {
        synchronized (this.getId().getClass()) {
            final int result;
            if (pKey == this.getId() || this.getId().equals(pKey))
                result = 0;
            else {
                result = getId().toString().compareTo(pKey.toString());
                if (result == 0) throw new RuntimeException();
            }
            return result;
        }
    }
}
