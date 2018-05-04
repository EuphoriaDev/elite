package ru.euphoria.elite;

import android.util.Log;

import java.lang.reflect.Field;

import ru.euphoria.elite.annotation.PrimaryKey;
import ru.euphoria.elite.annotation.Serialize;

/**
 * Created by admin on 30.03.18.
 */

public class Tables {
    private static final String LOG_TAG = "ELite.Tables";
    private static final boolean DEBUG = Elite.DEBUG;

    public static String drop(Class<?> aClass) {
        return "DROP TABLE IF EXISTS " + getTableName(aClass);
    }

    public static String create(Class<?> aClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ");
        sql.append(getTableName(aClass));
        sql.append(" (");

        Structure structure = Structure.getStructure(aClass);
        for (Field f : structure.fields) {
            boolean primaryKey = f.isAnnotationPresent(PrimaryKey.class);
            String type = f.getType().getSimpleName();
            String name = f.getName();

            sql.append("\'")
                .append(name)
                .append("\'");
            switch (type) {
                case "boolean":
                case "long":
                case "int": sql.append(" INTEGER"); break;
                case "double": sql.append(" REAL"); break;
                case "String": sql.append(" TEXT"); break;

                default: sql.append(" TEXT");
            }
            if (primaryKey) {
                sql.append(" PRIMARY KEY NOT NULL");
            }
            sql.append(", ");
        }
        sql.delete(sql.length() - 2, sql.length() - 1);
        sql.append(");");

        if (DEBUG) {
            Log.i(LOG_TAG, sql.toString());
        }
        return sql.toString();
    }

    public static String getTableName(Class aClass) {
        String simpleName = aClass.getSimpleName();

        int index;
        if ((index = hasUpperCase(simpleName)) != -1) {
            simpleName = simpleName.toLowerCase();
            return simpleName.substring(0, index) +
                    "_" +
                    simpleName.substring(index, simpleName.length());
        } else {
            return simpleName.toLowerCase().concat("s");
        }

    }

    private static int hasUpperCase(String s) {
        for (int i = 1; i < s.length(); i++) {
            if (Character.isUpperCase(s.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
}
