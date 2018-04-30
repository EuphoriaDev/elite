package ru.euphoria.elite.util;

import android.util.Pair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by admin on 30.04.18.
 */

public class BlobUtils {

    public static byte[] serialize(Object data) throws IOException {
        // Serialize data object to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(data);
        out.close();

        return bos.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);

        try (ObjectInputStream in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }
}
