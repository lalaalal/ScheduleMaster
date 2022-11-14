package com.schedulemaster.util;

import java.io.*;

/**
 * Provides serialize and deserialize api.
 *
 * @author lalaalal
 */
public class SerializeManager {
    public static <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis)) {
            Object object = ois.readObject();
            return type.cast(object);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static byte[] serialize(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(object);
            return bos.toByteArray();
        }
    }
}
