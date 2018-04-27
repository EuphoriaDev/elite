package ru.euphoria.elite;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.euphoria.elite.annotation.IgnoreColumn;
import ru.euphoria.elite.util.PrimaryKeyComparator;

public class Structure {
    public Class<?> aClass;
    public ArrayList<String> names;
    public ArrayList<Field> fields;
    public ArrayList<String> types;

    public Structure() {
        this.fields = new ArrayList<>();
        this.types = new ArrayList<>();
        this.names = new ArrayList<>();
    }

    public static Structure getStructure(List<?> values) {
        return getStructure(values.get(0).getClass());
    }

    public static Structure getStructure(Class<?> aClass) {
        Field[] fields = aClass.getFields();
        Arrays.sort(fields, new PrimaryKeyComparator());

        Structure structure = new Structure();
        structure.aClass = aClass;
        for (Field f : fields) {
            f.setAccessible(true);
            boolean ignore = f.isAnnotationPresent(IgnoreColumn.class);
            int modifiers = f.getModifiers();
            if (ignore || Modifier.isStatic(modifiers) || Modifier.isAbstract(modifiers)) {
                continue;
            }

            String type = f.getType().getSimpleName();
            structure.fields.add(f);
            structure.types.add(type);
            structure.names.add(f.getName());
        }

        return structure;
    }
}