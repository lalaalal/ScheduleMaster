package com.schedulemaster.server;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.model.User;

import java.io.*;

public class UserHandler {
    private Hash<String, User> users;

    private final String filePath;

    @SuppressWarnings("unchecked")
    public UserHandler(String filePath) throws IOException {
        this.filePath = filePath;
        try (FileInputStream fis = new FileInputStream(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                Object object = ois.readObject();
                users = (Hash<String, User>) object;
            }
        } catch (FileNotFoundException e) {
            users = new Hash<>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(users);
            }
        }
    }

    public void addUser(String id, String pw) {
        User user = new User(id, pw);
        users.put(id, user);
    }

    public void addUser(User user) {
        users.put(user.id, user);
    }

    public Hash<String, User> getUsers() {
        return users;
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public boolean verifyUser(String id, String hashedPassword) {
        User user = users.get(id);
        if (user == null)
            return false;

        return user.verifyPassword(hashedPassword);
    }
}
