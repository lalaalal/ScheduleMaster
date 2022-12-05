package com.schedulemaster.model;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.LinkedList;

/**
 * An LinkedList type of String. LectureGroup should contain only lecture number.
 *
 * @author lalaalal
 */
public class LectureGroup extends LinkedList<String> {

    /**
     * Create a priority queue (Heap) using priorities.
     *
     * @param priorities Priorities of each lecture.
     * @return Priority queue.
     */
    public Heap<Priority> createHeap(Hash<String, Integer> priorities) {
        Heap<Priority> heap = new Heap<>(Heap.Comparator.maxHeapInt(Priority::priority));
        for (String lectureNum : this) {
            int priority = priorities.get(lectureNum);
            heap.insert(new Priority(priority, lectureNum));
        }

        return heap;
    }
}
