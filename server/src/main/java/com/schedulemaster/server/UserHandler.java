package com.schedulemaster.server;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.User;

import java.io.*;

public class UserHandler {
    private Hash<String, User> users;
    private final LinkedList<User> loginUsers = new LinkedList<>();

    private final String filePath;

    private final Logger logger = Logger.getInstance();

    @SuppressWarnings("unchecked")
    public UserHandler(String filePath) {
        this.filePath = filePath;
        logger.log("Reading user data from \"" + filePath + "\"", Logger.INFO);
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object object = ois.readObject();
            users = (Hash<String, User>) object;
        } catch (FileNotFoundException e) {
            users = new Hash<>();
            logger.log("No such file : \"" + filePath + "\"", Logger.ERROR);
        } catch (ClassNotFoundException e) {
            users = new Hash<>();
            logger.log("Class not found while reading data from \"" + filePath + "\"", Logger.ERROR);
        } catch (IOException e) {
            users = new Hash<>();
            logger.log("Something went wrong while load users from \"" + filePath + "\"", Logger.ERROR);
        }
    }

    public synchronized void save() {
        logger.log("Saving users to \"" + filePath + "\"", Logger.DEBUG);
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Failed to save Lectures");
            logger.log("Something went wrong while saving users to \"" + filePath + "\"", Logger.ERROR);
        }
    }

    public synchronized void addUser(String id, String pw) {
        User user = new User(id, pw);
        users.put(id, user);
        logger.log("New user \"" + id + "\" signup", Logger.INFO);
        save();
    }

    public synchronized void addUser(User user) {
        users.put(user.id, user);
        logger.log("New user \"" + user.id + "\" signup", Logger.INFO);
        save();
    }

    public boolean hasId(String id) {
        return users.get(id) != null;
    }

    public Hash<String, User> getUsers() {
        return users;
    }

    public User getUser(String id) {
        logger.log("Get \"" + id + "\"'s user data", Logger.DEBUG);
        return users.get(id);
    }

    public boolean verifyUser(String id, String hashedPassword) {
        User user = users.get(id);
        logger.log("\"" + id + "\" is trying login", Logger.DEBUG);
        if (user == null)
            return false;

        return user.verifyPassword(hashedPassword);
    }

    public synchronized boolean login(User user) {
        if (loginUsers.has(user)) {
            logger.log("\"" + user.id + "\" is already in loginUsers", Logger.VERBOSE);
            return false;
        }

        loginUsers.push(user);
        logger.log("\"" + user.id + "\" was added to loginUsers", Logger.VERBOSE);
        return true;
    }

    public synchronized void logout(User user) {
        loginUsers.remove(user);
        logger.log("\"" + user.id + "\" was removed from loginUsers", Logger.VERBOSE);
    }
}
