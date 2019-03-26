package com.home.search.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by soncd on 24/01/2019
 */
public class FileUtils {

    /**
     * Đọc file JSON --> chuyển về dạng String
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String fileToString(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        try {

            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } finally {
            br.close();
        }

        return sb.toString();
    }
}
