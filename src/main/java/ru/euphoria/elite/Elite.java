package ru.euphoria.elite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Objects;

/**
 * ORM like database for android.
 */
public class Elite {
    static final boolean DEBUG = false;
    private static SQLiteOpenHelper instance;

    /** Returns a database that will be used for reading and writing */
    public static SQLiteDatabase database() {
        return instance.getWritableDatabase();
    }

    /**
     * Initializes the database by specified {@link SQLiteOpenHelper}
     * @param helper database helper for init
     */
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

    /** Releases any open database object */
    public static void close() {
        if (instance != null) {
            instance.close();
        }
    }

    private static void checkNull() {
        Objects.requireNonNull(instance, "SQLiteOpenHelper can't be null. You should init helper using setInstance(helper)");
    }
}
