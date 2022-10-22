package com.schedulemaster.server;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    public static final int ERROR = 1;
    public static final int INFO = 2;
    public static final int DEBUG = 3;
    public static final int VERBOSE = 4;

    private final LinkedList<String> log = new LinkedList<>();
    private final Hash<OutputStream, BufferedWriter> outputStreams = new Hash<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[[HH:mm:ss]]");

    public int logLevel = INFO;

    private static Logger _instance;

    public static Logger getInstance() {
        if (_instance == null)
            return _instance = new Logger();
        return _instance;
    }

    private Logger() {

    }

    public void addOutputStream(OutputStream os) {
        if (outputStreams.hasKey(os))
            return;

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
        outputStreams.put(os, bufferedWriter);
    }

    public synchronized void log(String msg, int logLevel) {
        if (this.logLevel < logLevel)
            return;

        try {
            String stamp = timeStamp();
            log.push(stamp + " " + msg);

            for (BufferedWriter writer : outputStreams) {
                writer.write(stamp + " " + msg);
                writer.newLine();
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
