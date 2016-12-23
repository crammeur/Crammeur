package ca.qc.bergeron.marcantoine.crammeur.repository.crud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;
import ca.qc.bergeron.marcantoine.crammeur.repository.i.DataFramework;

/**
 * Created by Marc-Antoine on 2016-10-30.
 */

public abstract class SQLiteTemplate<T extends Data<K>, K> extends SQLiteOpenHelper implements DataFramework<T, K> {

    protected final static String DATABASE_NAME = "database";
    protected final static int DATABASE_VERSION = 1;

    protected final SQLiteDatabase mDB;

    public SQLiteTemplate(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = this.getWritableDatabase();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
