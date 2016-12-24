package ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;
import java.util.SortedSet;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Product;
import ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLiteTemplate;

/**
 * Created by Marc-Antoine on 2016-10-30.
 */

public class SQLiteProduct extends SQLiteTemplate<Product, Integer> {

    public final static String T_Produit = "Produit";        // nom de la table
    public final static String F_id = "F_id";                // nom de chacun des champs (F pour field)
    public final static String F_nom = "F1_1";
    public final static String F_description = "F1_2";
    public final static String F_price = "F1_3";

    private static final String CREATE_TABLE_Produit = "CREATE TABLE " + T_Produit + " (" +
            F_id + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            F_nom + " TEXT ," +
            F_description + " TEXT ," +
            F_price + " REAL ," +
            "GEN_LASTMOD                INTEGER " + // Used for debugging purpose
            ")";

    public SQLiteProduct(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_Produit);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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

    @Override
    public Integer save(Product pData) throws KeyException {
        ContentValues values = new ContentValues();
        values.put(F_nom, pData.Name);
        values.put(F_description, pData.Description);
        values.put(F_price, pData.Price);
        Integer id = pData.getId();
        if (id == null) {
            id = Integer.valueOf((int) mDB.insert(T_Produit, null, values));
            pData.setId(id);
        } else {
            mDB.update(T_Produit, values, F_id + " = ?", new String[]{String.valueOf(id)});
        }
        return id;
    }

    @Override
    public Iterable<Integer> save(Product... pDatas) throws KeyException {
        return null;
    }

    @Override
    public Iterable<Product> getAll() {
        return null;
    }

    @Override
    public SortedSet<Integer> getAllKeys() {
        return null;
    }

    @Override
    public Product getByKey(Integer pKey) {
        return null;
    }

    @Nullable
    @Override
    public Integer getId(@NonNull Product pEntity) {
        return null;
    }

    @Override
    public Iterable<Product> getByKeys(Set<Integer> pKeys) {
        return null;
    }

    @Override
    public boolean contains(Integer pKey) {
        return false;
    }

    @Override
    public boolean contains(Product pData) {
        return false;
    }

    @Override
    public void delete(Product pData) throws KeyException, DeleteException {

    }

    @Override
    public void deleteAll() {

    }
}
