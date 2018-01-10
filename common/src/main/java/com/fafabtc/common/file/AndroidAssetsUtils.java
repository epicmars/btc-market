package com.fafabtc.common.file;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class AndroidAssetsUtils {

    public static String readFromAssets(AssetManager am, String filename) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            InputStream is = am.open(filename);
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            Timber.e(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                    Timber.e(ignore);
                }
            }
        }
        return sb.toString();
    }

}
