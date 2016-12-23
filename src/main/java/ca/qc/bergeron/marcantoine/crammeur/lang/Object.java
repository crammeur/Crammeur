/*
 * Copyright (c) 2016.
 */

package ca.qc.bergeron.marcantoine.crammeur.lang;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Created by Marc-Antoine on 2016-06-18.
 */
public abstract class Object implements ca.qc.bergeron.marcantoine.crammeur.lang.i.Object {

    @Override
    public String toGenericString() {
        return toGenericString(this.getClass());
    }

    /**
     * @param pClass
     * @return
     */
    private String toGenericString(Class<?> pClass) {
        StringBuffer sb = new StringBuffer(pClass.getSimpleName() + "{");
        Field[] fields = pClass.getDeclaredFields();
        for (int index = 0; index < fields.length; index++) {
            if (!Modifier.isTransient(fields[index].getModifiers()) && (!Modifier.isStatic(fields[index].getModifiers()) && !Modifier.isFinal(fields[index].getModifiers())))
                try {
                    final boolean b = fields[index].isAccessible();
                    fields[index].setAccessible(true);
                    sb.append(fields[index].getName() + "=");
                    if (String.class.isAssignableFrom(fields[index].getType())) {
                        sb.append("'" + fields[index].get(this) + "'");
                    } else
                        sb.append(fields[index].get(this));
                    fields[index].setAccessible(b);
                    if (index < fields.length - 1)
                        sb.append(", ");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
        }
        if (pClass.getSuperclass() != null) {
            sb.append("} ");
            sb.append(toGenericString(pClass.getSuperclass()));
        } else {
            sb.append("}");
        }
        return sb.toString();
    }

    @Override
    public final LinkedHashSet<Field> getAllFields() {
        return Object.getAllFields(this.getClass());
    }

    @Override
    public final LinkedHashSet<Field> getAllSerializableFields() {
        return Object.getAllSerializableFields(this.getClass());
    }

    /**
     * @param pObject
     * @return
     */
    public final boolean equals(Object pObject) {
        if (!this.equals((java.lang.Object) pObject)) return false;
        return (this.toGenericString() == pObject.toGenericString());
    }

    /**
     * @param pObject
     * @return
     */
    @Override
    public final boolean equals(java.lang.Object pObject) {
        return java.lang.Object.class.equals(pObject);
    }


    /**
     * @param pType
     * @return
     */
    private static LinkedHashSet<Field> getAllFields(Class<?> pType) {
        LinkedHashSet<Field> fs = new LinkedHashSet<>();
        for (Field f : Arrays.asList(pType.getDeclaredFields())) {
            fs.add(f);
        }
        if (pType.getSuperclass() != null) {
            fs.addAll(getAllFields(pType.getSuperclass()));
        }
        return fs;
    }

    /**
     * @param pType
     * @return
     */
    public static LinkedHashSet<Field> getAllSerializableFields(Class<?> pType) {
        LinkedHashSet<Field> fs = new LinkedHashSet<>();
        for (Field f : Arrays.asList(pType.getDeclaredFields())) {
            if (!Modifier.isTransient(f.getModifiers()) && (!Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()))) {
                fs.add(f);
            }
        }
        if (pType.getSuperclass() != null) {
            fs.addAll(getAllSerializableFields(pType.getSuperclass()));
        }
        return fs;
    }
}
