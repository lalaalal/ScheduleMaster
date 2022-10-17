package com.schedulemaster.misc;

import java.io.Serializable;

public record Request(String command, Object data) implements Serializable {
    public static final String LOGIN = "login";
    public static final String REQ_LECTURES = "request_lectures";
    public static final String BYE = "bye";
}
