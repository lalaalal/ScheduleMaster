package com.schedulemaster.server;

import com.schedulemaster.misc.LinkedList;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    public static final int ERROR = 0;
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final String[] LOG_LEVEL = { "ERROR", "INFO", "DEBUG" };

    private final LinkedList<String> log = new LinkedList<>();
    private final LinkedList<OutputStream> outputStreams = new LinkedList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private String actor = "App";

    public int logLevel = INFO;

    private static Logger _instance;

    public static Logger getInstance() {
        if (_instance == null)
            return _instance = new Logger();
        return _instance;
    }

    private Logger() {

    }

    public synchronized void setActor(String actor) {
        this.actor = actor;
    }

    public void addOutputStream(OutputStream os) {
        if (outputStreams.has(os))
            return;

        outputStreams.push(os);
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public void setLogLevel(String logLevel) {
        int index;
        for (index = 0; index < LOG_LEVEL.length; index++) {
            if (logLevel.equals(LOG_LEVEL[index])) {
                setLogLevel(index);
                return;
            }
        }
    }

    public synchronized void log(String msg, int logLevel, String actor) {
        setActor(actor);
        log(msg, logLevel);
    }

    public synchronized void log(String msg, int logLevel) {
        if (this.logLevel < logLevel)
            return;

        try {
            String stamp = timeStamp();
            log.push(stamp + " " + msg);

            for (OutputStream writer : outputStreams) {
                String line = String.format("[%s %s\t%s] %s\n", stamp, LOG_LEVEL[logLevel], actor, msg);
                writer.write(line.getBytes());
                writer.flush();
            }

        } catch (IOException e) {
            log.push(e.getMessage());
        }
    }

    private String timeStamp() {
        LocalDateTime now = LocalDateTime.now();
        return formatter.format(now);
    }
}
