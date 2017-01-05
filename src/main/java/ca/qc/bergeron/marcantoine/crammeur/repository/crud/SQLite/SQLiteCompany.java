package ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.entity.Company;
import ca.qc.bergeron.marcantoine.crammeur.repository.Repository;
import ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLiteTemplate;

/**
 * Created by Marc-Antoine on 2016-12-30.
 */

public class SQLiteCompany extends SQLiteTemplate<Company,Integer> implements ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite.i.SQLiteCompany {

    public SQLiteCompany(@NonNull Repository pRepository, @NonNull Context pContext) {
        super(Company.class, Integer.class, pRepository, pContext);
    }

    @Override
    public Integer convertCusrorToId(Cursor pCursor) {
        Integer result;
        result = pCursor.getInt(pCursor.getColumnIndex(F_id));
        return result;
    }

    @Override
    public Company convertCursorToEntity(Cursor pCursor) {
        Company o = new Company(null, null);
        {
            Integer id = pCursor.getInt(pCursor.getColumnIndex(F_id));
            o.setId(id);
        }
        o.Name = pCursor.getString(pCursor.getColumnIndex(F_nom));
        return o;
    }

    @NonNull
    @Override
    public Integer save(@NonNull Company pData) throws KeyException {
        ContentValues values = new ContentValues();
        values.put(F_id,pData.getId());
        values.put(F_nom, pData.Name);
        Integer id = pData.getId();
        if (id == null || !this.contains(id)) {
            id = (int) mDB.insert(T_Company, null, values);
            pData.setId(id);
        } else {
            mDB.update(T_Company, values, F_id + " = ?", new String[]{String.valueOf(id)});
        }
        return id;
    }

    @NonNull
    @Override
    public Iterable<Integer> save(@NonNull Company... pDatas) throws KeyException {
        List<Integer> result = new ArrayList<>();
        for (Company p: pDatas) {
            result.add(this.save(p));
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
    public Company getByKey(@NonNull Integer pKey) {
        Company result = null;
        Cursor c = mDB.rawQuery("SELECT * FROM " + mDBName + " WHERE " + F_id + "=?",new String[]{String.valueOf(pKey)});
        if (c.moveToNext()) {
            result = convertCursorToEntity(c);
        }
        return result;
    }
}
