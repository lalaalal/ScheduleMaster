package com.schedulemaster.model;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Index;
import com.schedulemaster.misc.LinkedList;

public class LectureBook {
    private LinkedList<Lecture> lectures;
    private final Hash<String, Lecture> lectureHash = new Hash<>();

    private final Hash<String, Index<String, Lecture>> indexes;

    public LectureBook(LinkedList<Lecture> lectures) {
        this.lectures = lectures;
        for (Lecture lecture : lectures) {
            lectureHash.put(lecture.lectureNum, lecture);
        }
        this.indexes = new Hash<>();
    }

    public void addIndex(String attributeName, Index.AttributeSelector<String, Lecture> selector) {
        Index<String, Lecture> index = new Index<>(lectures, selector);
        indexes.put(attributeName, index);
    }

    public LinkedList<String> getIndexAttributes(String attributeName) {
        return indexes.get(attributeName).getAttributes();
    }

    public LinkedList<Lecture> findLectures(String attributeName, String value) {
        Index<String, Lecture> index = indexes.get(attributeName);
        return index.get(value);
    }

    public Lecture findLecture(String lectureNum) {
        return lectureHash.get(lectureNum);
    }

    public LinkedList<Lecture> findWithComparator(Comparator comparator) {
        return findWithComparator(getLectures(), comparator);
    }

    public static LinkedList<Lecture> findWithComparator(LinkedList<Lecture> source, Comparator comparator) {
        LinkedList<Lecture> result = new LinkedList<>();
        for (Lecture lecture : source) {
            if (comparator.compare(lecture))
                result.push(lecture);
        }

        return result;
    }

    public LinkedList<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(LinkedList<Lecture> lectures) {
        this.lectures = lectures;
        for (Index<String, Lecture> index : indexes) {
            index.changeTable(lectures);
        }
        lectureHash.clear();
        for (Lecture lecture : lectures) {
            lectureHash.put(lecture.lectureNum, lecture);
        }
    }

    public interface Comparator {
        boolean compare(Lecture lecture);
    }
}
