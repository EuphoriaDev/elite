package ru.euphoria.elite.annotation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Type converter. Convert one object to another that fits the database type such as
 * {@link String} or {@link Integer}
 * @param <F> java object, such as {@link java.util.Date}
 * @param <S> database column object, such as INTEGER
 *
 * @see Serialize
 */
public interface TypeSerializer<F, S> {

    public S serialize(F value);

    public F deserialize(S value);
}
