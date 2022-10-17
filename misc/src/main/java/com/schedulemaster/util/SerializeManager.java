package com.schedulemaster.util;

import java.io.*;

public class SerializeManager {
    public static <T> T deserialize(byte[] bytes, Class<T> type) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream ois = new ObjectInputStream(bis)) {
                Object object = ois.readObject();
                return type.cast(object);
            }
        }
    }

    public static byte[] serialize(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(object);
                return bos.toByteArray();
            }
        }
    }
}
