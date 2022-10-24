package com.schedulemaster.model;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Index;
import com.schedulemaster.misc.LinkedList;

public class LectureBook {
    private LinkedList<Lecture> lectures;

    private final Hash<String, Index<String, Lecture>> indexes;

    public LectureBook(LinkedList<Lecture> lectures) {
        this.lectures = lectures;
        this.indexes = new Hash<>();
    }

    public void addIndex(String attributeName, Index.AttributeSelector<String, Lecture> selector) {
        Index<String, Lecture> index = new Index<>(lectures, selector);
        indexes.put(attributeName, index);
    }

    public LinkedList<Lecture> findLectures(String attributeName, String value) {
        Index<String, Lecture> index = indexes.get(attributeName);
        return index.get(value);
    }

    public LinkedList<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(LinkedList<Lecture> lectures) {
        this.lectures = lectures;
        for (Index<String, Lecture> index : indexes) {
            index.changeTable(lectures);
        }
    }
}
