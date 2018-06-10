package ru.euphoria.elite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.euphoria.elite.annotation.Serialize;
import ru.euphoria.elite.annotation.TypeSerializer;

/**
 * Created by admin on 29.03.18.
 */

public class ValueStore {

    public static <E> ArrayList<E> fetch(String sql, Class<E> aClass) {
        SQLiteDatabase db = Elite.database();
        Structure structure = Structure.getStructure(aClass);
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<E> values = new ArrayList<>(cursor.getCount());

        try {
            Constructor<E> constructor = aClass.getConstructor();
            while (cursor.moveToNext()) {
                E object = createObject(structure, constructor, cursor);
                values.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return values;
    }

    public static <E> E createObject(Structure structure, Constructor<E> constructor, Cursor cursor) throws Exception {
        E object = constructor.newInstance();
        for (int i = 0; i < structure.fields.size(); i++) {
            Field field = structure.fields.get(i);
            String type = structure.types.get(i);
            boolean serialize = structure.serialize.get(i);

            switch (type) {
                case "boolean":
                    field.setBoolean(object, cursor.getInt(i) == 1);
                    break;
                case "long":
                    field.setLong(object, cursor.getLong(i));
                    break;
                case "int":
                    field.setInt(object, cursor.getInt(i));
                    break;
                case "double":
                    field.setDouble(object, cursor.getDouble(i));
                    break;
                case "String":
                    field.set(object, cursor.getString(i));
                    break;

                    default: {
                        if (!serialize) break;

                        switch (cursor.getType(i)) {
                            case Cursor.FIELD_TYPE_STRING:
                                TypeSerializer serializer = structure.serializers.get(i);
                                field.set(object, serializer.deserialize(cursor.getString(i)));
                                break;
                        }
                    }
            }
        }
        return object;
    }

    public static <E> E fetchFirst(String sql, Class<E> aClass) {
        ArrayList<E> values = fetch(sql, aClass);
        if (values.size() > 0) {
            return values.get(0);
        }
        return null;
    }

    public static void insert(Object value) {
        insert(Collections.singletonList(value));
    }

    public static void insert(List<?> values) {
        SQLiteDatabase db = Elite.database();
        Structure structure = Structure.getStructure(values);
        SQLiteStatement statement = compileStatement(db, structure);

        db.beginTransaction();
        bindValues(structure, statement, values);
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    private static void bindValues(Structure structure, SQLiteStatement statement, List<?> values) {
        for (int i = 0; i < values.size(); i++) {
            Object object = values.get(i);
            for (int j = 0; j < structure.fields.size(); j++) {
                Field field = structure.fields.get(j);
                String type = structure.types.get(j);
                boolean serialize = structure.serialize.get(j);

                try {
                    Object s = null;
                    if (serialize) {
                        s = structure.serializers.get(j).serialize(field.get(object));
                        type = s.getClass().getSimpleName();
                    }

                    int index = j + 1;
                    switch (type) {
                        case "String": statement.bindString(index, String.valueOf(s != null ? s : field.get(object))); break;
                        case "boolean": statement.bindLong(index, field.getBoolean(object) ? 1 : 0); break;
                        case "int": statement.bindLong(index, field.getInt(object)); break;
                        case "long": statement.bindLong(index, field.getLong(object)); break;
                        case "double": statement.bindDouble(index, field.getDouble(object)); break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            statement.executeInsert();
            statement.clearBindings();
        }
        statement.releaseReference();

    }

    private static SQLiteStatement compileStatement(SQLiteDatabase db, Structure struct) {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT OR REPLACE INTO ");
        sql.append(Tables.getTableName(struct.aClass));
        sql.append("(");

        sql.append(struct.names.get(0));
        int size = struct.names.size();
        for (int i = 1; i < size; i++) {
            String name = struct.names.get(i);
            sql.append(", ");
            sql.append("\'")
                    .append(name)
                    .append("\'");
        }

        sql.append(") VALUES (");
        for (int i = 0; i < size; i++) {
            sql.append((i > 0) ? ",?" : "?");
        }
        sql.append(")");

        System.out.println(sql.toString());
        return db.compileStatement(sql.toString());
    }
}
