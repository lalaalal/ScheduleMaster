package com.schedulemaster.misc;

import java.io.Serializable;

public record Response(Status status, Object data) implements Serializable {
    public static final long serialVersionUID = 10L;
}
