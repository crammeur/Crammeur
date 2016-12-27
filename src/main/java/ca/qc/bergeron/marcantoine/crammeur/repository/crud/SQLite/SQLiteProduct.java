package ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Product;
import ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLiteDatabaseHelper;

/**
 * Created by Marc-Antoine on 2016-10-30.
 */

public class SQLiteProduct implements ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite.i.SQLiteProduct {

    private SQLiteDatabaseHelper dbHelper;

    private SQLiteDatabase mDB;

    public SQLiteProduct(Context context) {
        dbHelper = new SQLiteDatabaseHelper(context);
        mDB = dbHelper.getWritableDatabase();
    }


    private Product convertCursor(Cursor pCursor) {
        Product o = new Product(null, null, null, 0.0, 0);

        {
            Integer id = pCursor.getInt(pCursor.getColumnIndex(F_id));
            o.setId(id);
        }
        o.Name = pCursor.getString(pCursor.getColumnIndex(F_nom));
        o.Description = pCursor.getString(pCursor.getColumnIndex(F_description));
        o.Price = pCursor.getDouble(pCursor.getColumnIndex(F_price));
        return o;
    }

    public Cursor selectRecords() {
        String[] cols = new String[]{F_id, F_nom, F_description, F_price};
        Cursor mCursor = mDB.query(true, T_Produit, cols, null, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public Integer save(Product pData) throws KeyException {
        ContentValues values = new ContentValues();
        values.put(F_nom, pData.Name);
        values.put(F_description, pData.Description);
        values.put(F_price, pData.Price);
        Integer id = pData.getId();
        if (id == null) {
            id = (int) mDB.insert(T_Produit, null, values);
            pData.setId(id);
        } else {
            mDB.update(T_Produit, values, F_id + " = ?", new String[]{String.valueOf(id)});
        }
        return id;
    }

    public boolean contain(Integer pId) {
        Cursor c = this.selectRecords();
        do {
            if(pId.equals(this.convertCursor(c).getId())) return true;
        } while (c.moveToNext());
        return false;
    }
}
