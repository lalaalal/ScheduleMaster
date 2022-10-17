package com.schedulemaster.misc;

import java.io.Serializable;

public record Request(String command, Object data) implements Serializable {
    public static final String LOGIN = "login";
}
