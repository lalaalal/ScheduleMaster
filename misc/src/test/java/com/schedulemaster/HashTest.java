package com.schedulemaster;

import com.schedulemaster.misc.Hash;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashTest {

    @Test
    public void testHash() {
        Hash<Integer, Integer> hash = new Hash<>();

        for (int i = 0; i < 1000; i++) {
            hash.put(i, i * 100);
        }

        for (int i = 0; i < 1000; i++) {
            Assertions.assertEquals(i * 100, hash.get(i));
        }

        for (int i = 0; i < 1000; i++) {
            hash.remove(i);
        }
        Assertions.assertEquals(0, hash.getLength());

        hash.put(0, 10);
        hash.put(1, 11);

        for (Integer i : hash) {
            System.out.println(i);
        }

    }
}
