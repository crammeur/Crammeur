/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.annotations.models.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Marc-Antoine on 2016-05-15.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    String name();
}
