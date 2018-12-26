package com.importExpress.utli;

import java.io.*;

public class FileHelper {

    public static void readFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void writeFile(File file, String content) throws IOException {
        if (file.getParentFile().exists() && file.getParentFile().isDirectory()) {
            if (file.exists()) {
                file.delete();
            }
        } else {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (osw != null) {
                osw.flush();
            }
            if (fos != null) {
                osw.close();
            }
        }
    }

    public static void appendFile(File file, String content) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(
                new FileOutputStream(file, true), // true to append
                "UTF-8"
        );
        out.write(content);
        out.close();
    }

    // main for test
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\test.txt");
        writeFile(file, "");
        appendFile(file, "你好");
        appendFile(file, "!!!");
        readFile(file);
    }
}