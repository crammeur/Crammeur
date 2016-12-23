/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.model.entity;


import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;

/**
 * Created by Marc-Antoine on 2016-04-06.
 */
public final class User extends DataEntity<Integer> implements Data<Integer> {

    private String mEmail;
    private String mUser;
    private String mPassword;

    private User() {
        this(null, null, null);
    }

    public User(String pEmail, String pUser, String pPassword) {
        this(null, pEmail, pUser, pPassword);
    }

    public User(Integer pId, String pEmail, String pUser, String pPassword) {
        super(pId);
        mEmail = pEmail;
        mUser = pUser;
        mPassword = pPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String pEmail) {
        mEmail = pEmail;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String pUser) {
        mUser = pUser;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String pPassword) {
        mPassword = pPassword;
    }
}
