package ru.euphoria.elite;

import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;


/**
 * Created by admin on 29.03.18.
 */

public class QueryBuilder<T> {
    private static final int SQL_SELECT = 100;
    private static final int SQL_INSERT = 101;
    private static final int SQL_DELETE = 102;

    private Class<T> aClass;
    private boolean count;
    private int operator, offset, limit;
    private String table, order;
    private ArrayList<String> whereList;

    public static <T> QueryBuilder<T> select(Class<T> table) {
        return new QueryBuilder<>(SQL_SELECT, table, Tables.getTableName(table));
    }

    public static <T> QueryBuilder<T> delete(Class<T> table) {
        return new QueryBuilder<>(SQL_DELETE, table, Tables.getTableName(table));
    }

    public QueryBuilder(int operator, Class<T> aClass, String table) {
        this.operator = operator;
        this.aClass = aClass;
        this.table = table;
    }

    public QueryBuilder<T> where(String value) {
        if (whereList == null) {
            whereList = new ArrayList<>();
        }
        whereList.add(value);
        return this;
    }

    public QueryBuilder<T> where(String name, String value) {
       return where(String.format("%s = %s", name, value));
    }

    public QueryBuilder<T> where(String name, int value) {
        return where(name, String.valueOf(value));
    }

    public QueryBuilder<T> where(String name, boolean value) {
        return where(name, value ? 1 : 0);
    }

    public QueryBuilder<T> orderBy(String value) {
        order = value;
        return this;
    }

    public QueryBuilder<T> count() {
        count = true;
        return this;
    }

    public QueryBuilder<T> offset(int value) {
        offset = value;
        return this;
    }

    public QueryBuilder<T> limit(int value) {
        limit = value;
        return this;
    }

    /** Execute a single SQL statement */
    public void execute() {
        Elite.database().execSQL(build());
    }

    public Cursor cursor() {
        return Elite.database().rawQuery(build(), null);
    }

    public ArrayList<T> values() {
        return ValueStore.fetch(build(), aClass);
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        switch (operator) {
            case SQL_SELECT:
                builder.append(count
                        ? "SELECT COUNT(*) FROM "
                        : "SELECT * FROM ");
                break;

            case SQL_DELETE:
                builder.append("DELETE FROM ");
                break;
        }
        builder.append(table);

        if (whereList != null) {
            builder.append(" WHERE ");
            builder.append(whereList.get(0));
            if (whereList.size() > 1) {
                for (int i = 1; i < whereList.size(); i++) {
                    builder.append(" AND ");
                    builder.append(whereList.get(i));
                }
            }
        }
        if (limit != 0) {
            builder.append(" LIMIT ");
            builder.append(limit);
        }
        if (offset != 0) {
            builder.append(" OFFSET ");
            builder.append(offset);
        }
        if (!TextUtils.isEmpty(order)) {
            builder.append(" ORDER BY ");
            builder.append(order);
        }

        return builder.toString();
    }
}
