package ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Product;
import ca.qc.bergeron.marcantoine.crammeur.repository.Repository;
import ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLiteTemplate;

/**
 * Created by Marc-Antoine on 2016-10-30.
 */

public class SQLiteProduct extends SQLiteTemplate<Product,Integer> implements ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite.i.SQLiteProduct{

    public SQLiteProduct(Repository pRepository, Context pContext) {
        super(Product.class,Integer.class,pRepository,pContext);
    }

/*    public Cursor selectIds() {
        //String[] cols = new String[]{F_id};
        //Cursor mCursor = mDB.query(true, T_Produit, cols, null, null, null, null, null, null);
        Class c = mClazz;
        do {
            for (Field f : c.getDeclaredFields()) {
                final boolean b = f.isAccessible();
                try {
                    if (f.isAnnotationPresent(Entity.Id.class)) {
                        f.setAccessible(true);
                        Cursor mCursor = mDB.rawQuery("SELECT " + f.getAnnotation(Entity.Id.class).name() + " FROM  " + mDBName,new String[]{});
                        return mCursor;
                    }
                } finally {
                    f.setAccessible(b);
                }
            }
        } while ((c = c.getSuperclass()) != null);
        return null;
    }*/

    @Override
    public Integer convertCusrorToId(Cursor pCursor) {
        Integer result = null;
        result = pCursor.getInt(pCursor.getColumnIndex(F_id));
        return result;
    }

    @Override
    public Product convertCursorToEntity(Cursor pCursor) {
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


    @NonNull
    @Override
    public Integer save(@NonNull Product pData) throws KeyException {
        mRepository.save(pData.Company);
        ContentValues values = new ContentValues();
        values.put(F_id, pData.getId());
        values.put(F_nom, pData.Name);
        values.put(F_description, pData.Description);
        values.put(F_price, pData.Price);
        values.put(F_unit, pData.Unit);
        values.put(F_company, pData.Company.getId());
        Integer id = pData.getId();
        if (id == null || !this.contains(id)) {
            id = (int) mDB.insert(T_Produit, null, values);
            pData.setId(id);
        } else {
            mDB.update(T_Produit, values, F_id + " = ?", new String[]{String.valueOf(id)});
        }
        return id;
    }

    @NonNull
    @Override
    public Iterable<Integer> save(@NonNull Product... pDatas) throws KeyException {
        List<Integer> result = new ArrayList<>();
        for (Product p: pDatas) {
            if (p.getId() == null) result.add(this.save(p));
            else result.add(p.getId());
        }
        return result;
    }

    public boolean contains(Integer pId) {
        Cursor c = this.selectIds();
        while (c.moveToNext()) {
            if(pId.equals(this.convertCusrorToId(c))) return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Product getByKey(@NonNull Integer pKey) {
        Product result = null;
        Cursor c = mDB.rawQuery("SELECT * FROM " + mDBName + " WHERE " + F_id + "=?",new String[]{String.valueOf(pKey)});
        if (c.moveToNext()) {
            result = convertCursorToEntity(c);
        }
        return result;
    }

}
