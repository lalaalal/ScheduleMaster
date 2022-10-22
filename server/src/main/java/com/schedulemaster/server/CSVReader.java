package com.schedulemaster.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVReader implements AutoCloseable {

    private final BufferedReader reader;

    public CSVReader(String path) throws IOException {
        reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8));
    }

    public String[] read() throws IOException {
        String line = reader.readLine();
        if (line == null)
            return null;

        String[] tuple = line.split("\"");
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < tuple.length; i++) {
            if (i % 2 == 0) {
                if (tuple[i].charAt(0) == ',')
                    tuple[i] = tuple[i].substring(1);
                String[] parts = tuple[i].split(",");
                result.addAll(Arrays.stream(parts).toList());
            } else {
                result.add(tuple[i]);
            }
        }
        return result.toArray(new String[0]);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
