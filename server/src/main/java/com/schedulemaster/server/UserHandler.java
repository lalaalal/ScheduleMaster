package com.schedulemaster.server;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.model.User;

import java.io.*;

public class UserHandler {
    private Hash<String, User> users;

    private final String filePath;

    private final Logger logger = Logger.getInstance();
    private static final String ACTOR = "UserHandler";

    @SuppressWarnings("unchecked")
    public UserHandler(String filePath) {
        this.filePath = filePath;
        logger.log("Reading user data from \"" + filePath + "\"", Logger.INFO, ACTOR);
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object object = ois.readObject();
            users = (Hash<String, User>) object;
        } catch (FileNotFoundException e) {
            users = new Hash<>();
            logger.log("No such file : \"" + filePath + "\"", Logger.ERROR, ACTOR);
        } catch (ClassNotFoundException e) {
            users = new Hash<>();
            logger.log("Class not found while reading data from \"" + filePath + "\"", Logger.ERROR, ACTOR);
        } catch (IOException e) {
            users = new Hash<>();
            logger.log("Something went wrong while load lectures from \"" + filePath + "\"", Logger.ERROR, ACTOR);
        }
    }

    public synchronized void save() {
        logger.log("Saving users to \"" + filePath + "\"", Logger.INFO, ACTOR);
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Failed to save Lectures");
            logger.log("Something went wrong while saving users to \"" + filePath + "\"", Logger.ERROR, ACTOR);
        }
    }

    public synchronized void addUser(String id, String pw) {
        User user = new User(id, pw);
        users.put(id, user);
        logger.log("New user \"" + id + "\" signup", Logger.INFO, ACTOR);
        save();
    }

    public synchronized void addUser(User user) {
        users.put(user.id, user);
        logger.log("New user \"" + user.id + "\" signup", Logger.INFO, ACTOR);
        save();
    }

    public boolean hasId(String id) {
        return users.get(id) != null;
    }

    public Hash<String, User> getUsers() {
        return users;
    }

    public User getUser(String id) {
        logger.log("Get \"" + id + "\"'s user data", Logger.DEBUG, ACTOR);
        return users.get(id);
    }

    public boolean verifyUser(String id, String hashedPassword) {
        User user = users.get(id);
        logger.log("\"" + id + "\" is trying login", Logger.DEBUG, ACTOR);
        if (user == null)
            return false;

        return user.verifyPassword(hashedPassword);
    }
}
