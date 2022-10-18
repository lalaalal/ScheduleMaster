package com.schedulemaster;

import com.schedulemaster.misc.LinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;

public class LinkedListTest {
    @Test
    public void testList() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.push(i);
        }

        Assertions.assertEquals(5, list.getLength());
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(i, list.at(i));
        }

        list.remove(0);
        Assertions.assertEquals(4, list.getLength());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void serializeTest() {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.push(i);
        }

        try (FileOutputStream fos = new FileOutputStream("data")) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(list);
            }
        } catch (IOException ignored) {

        }
        try (FileInputStream fis = new FileInputStream("data")) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                Object obj = ois.readObject();
                LinkedList<Integer> readList = (LinkedList<Integer>) obj;
                for (int i = 0; i < 5; i++) {
                    Assertions.assertEquals(i, readList.at(i));
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException ignored) {

        }

    }
}
