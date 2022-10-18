package com.schedulemaster.misc;

import java.io.Serializable;

public record Request(String command, Object data) implements Serializable {
    public static final long serialVersionUID = 10L;

    public static final String LOGIN = "login";
    public static final String SIGNUP = "signup";
    public static final String REQ_LECTURES = "request_lectures";
    public static final String BYE = "bye";
    public static final String REQ_USER = "request_user";
    public static final String ENROLL = "enroll";
    public static final String SELECT = "select";
    public static final String CANCEL = "cancel";
    public static final String UNSELECT = "unselect";
}
