package ca.qc.bergeron.marcantoine.crammeur.repository.i;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;
/**
 * Created by Marc-Antoine on 2016-12-21.
 */

public interface Repository {
    @NonNull
    Object save(@NonNull Data pData) throws KeyException;
    @NonNull
    Iterable<Data<?>> getAll(@NonNull Class<? extends Data> pClass);
    @NonNull
    Set<?> getAllKeys(@NonNull Class<? extends Data> pClass);
    @Nullable
    Data<?> getByKey(@NonNull Class<? extends Data> pClass, @NonNull Object pKey);
    @NonNull
    Iterable<Data<Object>> getByKeys(@NonNull Class<? extends Data> pClass, @NonNull Set<Object> pKeys);
    boolean contains(@NonNull Class<? extends Data> pClass);
    void delete(@NonNull Data pData) throws KeyException, DeleteException;
    void clear();
}
