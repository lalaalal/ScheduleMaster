package com.schedulemaster;

import com.schedulemaster.misc.Hash;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashTest {

    @Test
    public void testHash() {
        Hash<String, String> hash = new Hash<>();

        String hello = "Hello";
        String hi = "hi";
        hash.put("A", hello);
        hash.put("B", hi);

        Assertions.assertEquals(hello, hash.get("A"));
        Assertions.assertEquals(hi, hash.get("B"));

        hash.remove("A");
        Assertions.assertNull(hash.get("A"));
    }
}
