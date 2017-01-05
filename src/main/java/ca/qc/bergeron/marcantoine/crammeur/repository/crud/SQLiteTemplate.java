package ca.qc.bergeron.marcantoine.crammeur.repository.crud;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import ca.qc.bergeron.marcantoine.crammeur.annotations.repository.Entity;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;
import ca.qc.bergeron.marcantoine.crammeur.repository.DataFramework;
import ca.qc.bergeron.marcantoine.crammeur.repository.Repository;
/**
 * Created by Marc-Antoine on 2016-12-28.
 */
public abstract class SQLiteTemplate<T extends Data<K>,K> extends DataFramework<T,K> {

    protected SQLiteDatabaseHelper dbHelper;
    protected SQLiteDatabase mDB;

    protected SQLiteTemplate(@NonNull Class<T> pClass, @NonNull Class<K> pKey, @NonNull Repository pRepository, @NonNull Context pContext) {
        super(pClass, pKey, pRepository);
        dbHelper = new SQLiteDatabaseHelper(pContext);
        mDB = dbHelper.getWritableDatabase();
    }

    public abstract K convertCusrorToId(Cursor pCursor); /*{
        K result = null;
        System.out.println(mKey.getName());
        switch (mKey.getName()) {
            case "java.lang.Integer": result = mKey.cast(pCursor.getInt(pCursor.getColumnIndex(mClazz.getAnnotation(Entity.class).getClass().getAnnotation(Entity.Id.class).name())));
                break;
            case "java.lang.Long": result = mKey.cast(pCursor.getLong(pCursor.getColumnIndex(mClazz.getAnnotation(Entity.class).getClass().getAnnotation(Entity.Id.class).name())));
                break;
            case "java.lang.String": result = mKey.cast(pCursor.getString(pCursor.getColumnIndex(mClazz.getAnnotation(Entity.class).getClass().getAnnotation(Entity.Id.class).name())));
                break;
            default: throw new RuntimeException(mKey.getName());
        }

        return result;
    }*/

    public abstract T convertCursorToEntity(Cursor pCursor); /*{
        T result = null;
        System.out.println(mKey.getSimpleName());
        switch (mKey.getSimpleName()) {
            case "Integer": result.setId(mKey.cast(pCursor.getInt(pCursor.getInt(pCursor.getColumnIndex(mClazz.getAnnotation(Entity.class).getClass().getAnnotation(Entity.Id.class).name())))));
                break;

        }

        return result;
    }*/

    public Cursor selectAll() {
        //String[] cols = new String[]{F_id};
        //Cursor mCursor = mDB.query(true, T_Produit, cols, null, null, null, null, null, null);
        Cursor mCursor = mDB.rawQuery("SELECT * FROM  " + mDBName,new String[]{});
        return mCursor;
    }

    public Cursor selectIds() {
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
    }



    @Override
    public final SortedSet<K> getAllKeys() {
        SortedSet result = new TreeSet();
        Cursor c = this.selectIds();
        while (c.moveToNext()) {
            result.add(this.convertCusrorToId(c));
        }
        return result;
    }


    @NonNull
    public Iterable<K> save(@NonNull T... pDatas) throws KeyException {
        synchronized (mClazz) {
            List<K> result = new ArrayList<>();
            for (T p: pDatas) {
                if (p.getId() == null) result.add(this.save(p));
                else result.add(p.getId());
            }
            return result;
        }
    }

    @Override
    public void delete(@NonNull T pData) throws KeyException, DeleteException {
        if (pData.getId() == null) throw new KeyException("id null");
        Class c = mClazz;
        do {
            for (Field f : c.getDeclaredFields()) {
                if (f.isAnnotationPresent(Entity.Id.class)) {
                    mDB.execSQL("DELETE FROM " + mDBName + " WHERE " + f.getAnnotation(Entity.Id.class).name() + "=" + pData.getId());
                    return;
                }
            }
        } while ((c = c.getSuperclass()) != null);

    }
}
