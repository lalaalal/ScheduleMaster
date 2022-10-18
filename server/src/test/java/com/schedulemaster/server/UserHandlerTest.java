package com.schedulemaster.server;

import com.schedulemaster.model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class UserHandlerTest {
    @Test
    public void testLoad() throws IOException {
        UserHandler userHandler = new UserHandler("/Users/lalaalal/Downloads/users");
        for (User user : userHandler.getUsers()) {
            System.out.println(user);
        }
    }

}
