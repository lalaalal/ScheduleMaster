package com.schedulemaster;

import com.schedulemaster.misc.LinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
