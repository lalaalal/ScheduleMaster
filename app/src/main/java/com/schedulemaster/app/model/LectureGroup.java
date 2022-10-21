package com.schedulemaster.app.model;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;

public class LectureGroup extends LinkedList<Lecture> {
    public Heap<Priority> createHeap(Hash<Lecture, Integer> priorities) {
        Heap<Priority> heap = new Heap<>(Heap.Comparator.maxHeapInt(Priority::priority));
        for (Lecture lecture : this) {
            int priority = priorities.get(lecture);
            heap.insert(new Priority(lecture, priority));
        }

        return heap;
    }
}
