package com.schedulemaster.misc;

import java.io.Serializable;

public record Response(Status status, Object data) implements Serializable {
    public static final long serialVersionUID = 11L;

    public String getDataType() {
        if (data == null)
            return "null";
        return data.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}
