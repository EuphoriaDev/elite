package ru.euphoria.elite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Objects;

/**
 * Created by admin on 28.04.18.
 */

public class Elite {
    private static SQLiteOpenHelper instance;

    /** Returns a database that will be used for reading and writing */
    public static SQLiteDatabase database() {
        return instance.getWritableDatabase();
    }

    public static SQLiteOpenHelper setInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (instance == null) {
                    instance = helper;
                }
            }
        }
        return instance;
    }

    private static void checkNull() {
        Objects.requireNonNull(instance, "SQLiteOpenHelper can't be null. You should init helper using setInstance(helper)");
    }
}
