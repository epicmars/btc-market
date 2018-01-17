package com.fafabtc.common.file;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/15.
 */

public class FileUtils {

    private static String DEFAULT_DIR = "fafabtc";

    public static File getExternalFile(String filename) {
        if (!Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())) {
            return null;
        }
        File documentDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File defaultDir = new File(documentDir, DEFAULT_DIR);
        if (!documentDir.exists()) {
            boolean result = documentDir.mkdirs();
            Timber.d("create document directory: %b.", result);
        }
        if (!defaultDir.exists()) {
            boolean result = defaultDir.mkdirs();
            Timber.d("create app data directory: %b.", result);
        }
        File file = new File(defaultDir, filename);
        try {
            if (!file.exists()) {
                boolean result = file.createNewFile();
                Timber.d("create app data directory: %b.", result);
            }
        } catch (IOException e) {
            Timber.e(e);
        }
        return file;
    }

    public static void writeFile(String filename, String data) {
        File file = getExternalFile(filename);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            Timber.e(e);
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {}
            }
        }
    }

    public static String readFile(String filename) {
        File file = getExternalFile(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            Timber.e(e);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {}
            }
        }
        return null;
    }
}
