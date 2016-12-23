package ca.qc.bergeron.marcantoine.crammeur.repository.i;

import ca.qc.bergeron.marcantoine.crammeur.exceptions.repository.KeyException;
import ca.qc.bergeron.marcantoine.crammeur.model.i.Data;
/**
 * Created by Marc-Antoine on 2016-12-21.
 */

public interface Repository {
    Object save(Data pData) throws KeyException;
    boolean contains(Class<? extends Data> pClass);
    void clear();
}
