package com.schedulemaster.model;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.LinkedList;

public class LectureGroup extends LinkedList<Lecture> {
    public Heap<Priority> createHeap(Hash<Lecture, Integer> priorities) {
        Heap<Priority> heap = new Heap<>(Heap.Comparator.maxHeapInt(Priority::priority));
        for (Lecture lecture : this) {
            int priority = priorities.get(lecture);
            heap.insert(new Priority(priority, lecture));
        }

        return heap;
    }
}
