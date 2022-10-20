package com.schedulemaster;

import com.schedulemaster.misc.Heap;
import org.junit.jupiter.api.Test;

public class HeapTest {
    @Test
    public void testHeap() {
        Heap<Integer> heap = new Heap<>((a, b) -> a > b);

        for (int i = 0; i < 10; i++)
            heap.insert(i);

        while (!heap.isEmpty()) {
            System.out.println(heap.pop());
        }
    }
}
