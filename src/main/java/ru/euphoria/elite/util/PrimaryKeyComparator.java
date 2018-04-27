package ru.euphoria.elite.util;

import java.lang.reflect.Field;
import java.util.Comparator;

import ru.euphoria.elite.annotation.PrimaryKey;

public class PrimaryKeyComparator implements Comparator<Field> {
        @Override
        public int compare(Field o1, Field o2) {
            return Boolean.compare(
                    o2.isAnnotationPresent(PrimaryKey.class),
                    o1.isAnnotationPresent(PrimaryKey.class));
        }
    }