# Elite
ORM like database for android by Euphoria with <b>high performance</b> and <b>low memory</b> usage.

# Getting started
First, you should create extended class SQLiteOpenHelper:
```java
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    
    // sqlite filename and version
    private static final String DATABASE_NAME = "cache.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
```

and initialize this class in your app using ```Elite.setInstance```
```java
public class YourApplication extends Application {
    public static volatile Context context;
    // ...

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        
        // init database your database
        Elite.setInstance(new DatabaseHelper(content));
    }
}
```
Jood job! Half the work has already been done

# Creating table
This library uses reflection to create tables

For example, you have a Persn class with one String and two integer fields
```java
public class Person {
        @PrimaryKey
        public int id;
        public String name;
        public int age;

        public Person() {
            // definitely need an empty constructor
        }

        public Person(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }
    }
```

to create a table you should override ```DatabaseHelper.onCreate(...)``` method
```java
public class DatabaseHelper extends SQLiteOpenHelper {
    ...
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creates table in database from Person.class
        // CREATE TABLE persons(id INTEGER PRIMARY KEY, age INTEGER, name TEXT);
        db.execSQL(Tables.create(Person.class));
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // recreating table when version changed. Not necessarily
        db.execSQL(Tables.drop(Person.class));
        onCreate(db);
    }
}
```

# Select values
For select values from database using ```QueryBuilder``` class,
for example:

```java
// select all values from Persons table
ArrayList<User> users = QueryBuilder.select(Person.class).values();

// select values when age = 30 and city is Moscow in desc order
ArrayList<User> moscowUsers = QueryBuilder.select(Person.class)
        .where("age", 25)
        .where("city", "Moscow")
        .orderBy("DESC id")
        .values();

// just sql: SELECT * FROM persons;
String sql = QueryBuilder.select(Person.class).build();

// just sql: SELECT * FROM users;
String sql = QueryBuilder.select(User.class).build();
Cursor cursor = Elite.database().rawQuery(sql, null);
// do something
cursor.close();
```

# TODO: Supplement documentation and sleep
