package ca.qc.bergeron.marcantoine.crammeur.repository.i;

import java.util.Set;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.DeleteException;
import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;
/**
 * Created by Marc-Antoine on 2016-12-21.
 */

public interface Repository {
    Object save(Data pData) throws KeyException;
    Iterable<Data<?>> getAll(Class<? extends Data> pClass);
    Set<?> getAllKeys(Class<? extends Data> pClass);
    Data<?> getByKey(Class<? extends Data> pClass, Object pKey);
    Iterable<Data<Object>> getByKeys(Class<? extends Data> pClass, Set<Object> pKeys);
    boolean contains(Class<? extends Data> pClass);
    void delete(Data pData) throws KeyException, DeleteException;
    void clear();
}
