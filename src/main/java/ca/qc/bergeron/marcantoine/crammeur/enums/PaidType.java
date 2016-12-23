/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.enums;

/**
 * Created by Marc-Antoine on 2016-04-07.
 */
public enum PaidType {

    Cash(0),
    Debit(1),
    Credit(2);

    public final int mPaidId;

    PaidType(int pPaidId) {
        this.mPaidId = pPaidId;
    }


}
