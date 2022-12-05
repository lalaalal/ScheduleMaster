package com.schedulemaster.misc;

import java.io.Serializable;

/**
 * Response record provides some response status message.
 *
 * @param status
 * @param data
 * @author lalaalal
 */
public record Response(Status status, Object data) implements Serializable {
    public static final String WRONG_REQUEST = "wrong_request";
    public static final String SUCCEED = "succeed";
    public static final String FAILED = "failed";
    public static final String LOGIN_REQUIRED = "login_required";

    public static final long serialVersionUID = 11L;

    /**
     * @return Simple name of data type.
     */
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
