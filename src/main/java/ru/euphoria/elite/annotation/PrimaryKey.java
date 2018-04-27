package ru.euphoria.elite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A PRIMARY KEY is a column in a table whose value must be unique,
 * and that does not happen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {

    /** Automatic incremental when value inserted */
    boolean autoincrement() default false;
}