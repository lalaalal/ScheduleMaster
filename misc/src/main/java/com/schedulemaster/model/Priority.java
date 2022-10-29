package com.schedulemaster.model;

import java.io.Serializable;

public record Priority(int priority, String lectureNum) implements Serializable {
    public static final long serialVersionUID = 11L;
}
