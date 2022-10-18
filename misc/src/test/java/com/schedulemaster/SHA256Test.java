package com.schedulemaster;

import com.schedulemaster.util.SHA256;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SHA256Test {
    @Test
    public void testHash() {
        String hashed1 = SHA256.encrypt("A");
        String hashed2 = SHA256.encrypt("A");

        Assertions.assertEquals(hashed1, hashed2);
    }
}
