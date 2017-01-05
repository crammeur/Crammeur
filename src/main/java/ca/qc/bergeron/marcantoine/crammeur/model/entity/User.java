/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.model.entity;


import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;

/**
 * Created by Marc-Antoine on 2016-04-06.
 */
public final class User extends DataEntity<Integer>{

    public String Email;
    public String User;
    public String Password;

    private User() {
        this(null, null, null);
    }

    public User(String pEmail, String pUser, String pPassword) {
        this(null, pEmail, pUser, pPassword);
    }

    public User(Integer pId, String pEmail, String pUser, String pPassword) {
        super(pId);
        Email = pEmail;
        User = pUser;
        Password = pPassword;
    }
}
