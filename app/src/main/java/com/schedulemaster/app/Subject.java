package com.schedulemaster.app;

import com.schedulemaster.misc.LinkedList;

public class Subject {
    private final LinkedList<Observer> observers = new LinkedList<>();

    public void addObserver(Observer observer) {
        if (!observers.has(observer))
            observers.push(observer);
    }

    public void notice() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
