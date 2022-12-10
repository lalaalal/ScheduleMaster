package com.schedulemaster.misc;

import java.io.Serializable;

/**
 * Request record provides some available request commands.
 *
 * @param command
 * @param data
 * @author lalaalal
 */
public record Request(String command, Object data) implements Serializable {
    public static final long serialVersionUID = 11L;

    public static final String LOGIN = "login";
    public static final String SIGNUP = "signup";
    public static final String REQ_LECTURES = "request_lectures";
    public static final String BYE = "bye";
    public static final String REQ_USER = "request_user";
    public static final String ENROLL = "enroll";
    public static final String SELECT = "select";
    public static final String CANCEL = "cancel";
    public static final String UNSELECT = "unselect";
    public static final String SET_PRIORITIES = "set_priorities";
    public static final String SET_UNWANTED_TIME = "set_unwanted_time";
    public static final String ADD_RATING = "add_rating";
    public static final String GET_RATING = "get_rating";
    public static final String REMOVE_RATING = "remove_rating";

    /**
     * @return Simple name of data type.
     */
    public String getDataType() {
        if (data == null)
            return "null";
        return data.getClass().getSimpleName();
    }
}
