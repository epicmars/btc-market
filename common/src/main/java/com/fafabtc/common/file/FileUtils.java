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


    /**
     * Check if external storage is writable.
     *
     * @return true if external storage is writable, otherwise false
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Check if external storage is readable.
     *
     * @return true if external storage is readable, otherwise false
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Get external file in given directory specified by a directory name.
     * <p>
     * If the directory and file doesn't exist, they will be created.
     *
     * @param directory a directory of external storage
     * @param childDir  a array of directory path segment
     * @param filename  file name
     * @return a File if it's existed or created successfully. Otherwise, return null.
     */
    public static File getExternalFile(String filename, String directory, String... childDir) {
        if (!isExternalStorageWritable()) {
            return null;
        }
        File rootDir = Environment.getExternalStoragePublicDirectory(directory);
        File defaultDir = buildFilePath(rootDir, childDir);
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

    /**
     * Build a file path for the given base directory path and a array of optional path segments.
     *
     * @param base         the base file path
     * @param pathSegments optional path segments
     * @return a file path which is the child directory of base directory o
     * path composed by the given segments
     */
    public static File buildFilePath(File base, String... pathSegments) {
        File cur = base;
        for (String segment : pathSegments) {
            if (cur == null) {
                cur = new File(segment);
            } else {
                cur = new File(cur, segment);
            }
        }
        return cur;
    }

    /**
     * Write string to a file.
     *
     * @param file file to be written
     * @param data data to write
     * @return true if write succeed, false otherwise
     */
    public static boolean writeFile(File file, String data) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(data);
            writer.flush();
            return true;
        } catch (IOException e) {
            Timber.e(e);
            return false;
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    /**
     * Read string from a file.
     *
     * @param file file to be read
     * @return string read from file or null if read failed
     */
    public static String readFile(File file) {
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
            return null;
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
