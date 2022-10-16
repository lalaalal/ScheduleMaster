package com.schedulemaster.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader implements AutoCloseable {

    private final BufferedReader reader;

    public CSVReader(String path) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(path));
    }

    public String[] read() throws IOException {
        String line = reader.readLine();

        if (line != null)
            return line.split(",");
        return null;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
