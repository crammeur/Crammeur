/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.lang.i;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by Marc-Antoine on 2016-06-18.
 */
public interface Object {
    Set<Field> getAllFields();

    Set<Field> getAllSerializableFields();

    String toGenericString();
}
