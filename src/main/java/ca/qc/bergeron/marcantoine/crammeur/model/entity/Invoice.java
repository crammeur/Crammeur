/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.qc.bergeron.marcantoine.crammeur.enums.PaidType;
import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;

/**
 * Created by Marc-Antoine on 2016-03-28.
 */
public final class Invoice extends DataEntity<Integer> implements Serializable, Data<Integer> {

    private Date mDate;
    private volatile Company mCompany;
    private String mCode;
    private String mImagePath;
    private double mTotal;
    private double mTPS;
    private double mTVQ;
    private double mTVA;
    private PaidType mPaidType = null;
    private List<Sale> mSales = new ArrayList<>();

    private Invoice() {
        this(null, null, null, null, 0, 0, 0);
    }

    public Invoice(Date pDate, Company pCompany, String pCode, String pImagePath, double pTotal, double pTPS, double pTVQ) {
        this(pDate, pCompany, pCode, pImagePath, pTotal, pTPS, pTVQ, 0);
    }

    public Invoice(Date pDate, Company pCompany, String pCode, String pImagePath, double pTotal, double pTPS, double pTVQ, double pTVA) {
        this(null, pDate, pCompany, pCode, pImagePath, pTotal, pTPS, pTVQ, pTVA);
    }

    public Invoice(Integer pId, Date pDate, Company pCompany, String pCode, String pImagePath, double pTotal, double pTPS, double pTVQ, double pTVA) {
        super(pId);
        mDate = pDate;
        mCompany = pCompany;
        mCode = pCode;
        mImagePath = pImagePath;
        mTotal = pTotal;
        mTPS = pTPS;
        mTVQ = pTVQ;
        mTVA = pTVA;
    }
}
