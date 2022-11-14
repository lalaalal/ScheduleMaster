package com.schedulemaster.server;

import com.schedulemaster.misc.LinkedList;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Log to provided output streams. Call Logger.getInstance() to get logger instance.
 *
 * @author lalaalal
 */
public class Logger {
    public static final int ERROR = 0;
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int VERBOSE = 3;
    public static final String[] LOG_LEVEL = {"ERROR  ", "INFO   ", "DEBUG  ", "VERBOSE"};
    private final LinkedList<OutputStream> outputStreams = new LinkedList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public int logLevel = INFO;

    private static Logger _instance;

    /**
     * Get logger instance. Create if instance is not initialized.
     *
     * @return Logger instance.
     */
    public static Logger getInstance() {
        if (_instance == null)
            return _instance = new Logger();
        return _instance;
    }

    private Logger() {

    }

    /**
     * Add output stream to log.
     *
     * @param os New output stream.
     */
    public void addOutputStream(OutputStream os) {
        if (outputStreams.has(os))
            return;

        outputStreams.push(os);
    }

    /**
     * Set log level with integer.
     *
     * @param logLevel Number of log level. (0 <= logLevel < LOG_LEVEL.length)
     */
    public void setLogLevel(int logLevel) {
        if (0 <= logLevel && logLevel < LOG_LEVEL.length)
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

    /**
     * Log message to all output streams if current log level is lower than provided log level.
     *
     * @param msg      Message to log.
     * @param logLevel Log level for message.
     */
    public synchronized void log(String msg, int logLevel) {
        if (this.logLevel < logLevel)
            return;

        try {
            String stamp = timeStamp();
            String threadID = Thread.currentThread().getName();
            for (OutputStream writer : outputStreams) {
                String line = String.format("[%s %s %s] %s\n", stamp, LOG_LEVEL[logLevel], threadID, msg);
                writer.write(line.getBytes());
                writer.flush();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String timeStamp() {
        LocalDateTime now = LocalDateTime.now();
        return formatter.format(now);
    }
}
