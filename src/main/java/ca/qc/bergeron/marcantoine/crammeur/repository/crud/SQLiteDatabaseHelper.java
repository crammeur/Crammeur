package ca.qc.bergeron.marcantoine.crammeur.repository.crud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite.i.SQLiteCompany;
import ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite.i.SQLiteProduct;

/**
 * Created by Marc-Antoine on 2016-10-30.
 */

public final class SQLiteDatabaseHelper extends SQLiteOpenHelper implements SQLiteProduct,SQLiteCompany{

    protected final static String DATABASE_NAME = "database";
    protected final static int DATABASE_VERSION = 1;

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_Company);
        db.execSQL(CREATE_TABLE_Product);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_Produit);
        db.execSQL("DROP TABLE IF EXISTS " + T_Company);
        onCreate(db);
    }
}
