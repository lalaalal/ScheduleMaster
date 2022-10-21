package com.schedulemaster.model;

import java.io.Serializable;

public record Priority(int priority, Lecture lecture) implements Serializable {
    public static final long serialVersionUID = 10L;
}
