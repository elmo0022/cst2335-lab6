package com.example.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * TV-style activity:
 *  - Fullscreen ImageView
 *  - Horizontal ProgressBar at bottom
 *  - Background task (CatImages) that fetches/loads cat images on a loop
 *
 * Make sure AndroidManifest.xml includes:
 * <uses-permission android:name="android.permission.INTERNET" />
 */
public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private static final String TAG = "CAT_LAB";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // layout with @id/imageView and @id/progressBar

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

// show something immediately so the screen isn't black
        imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);
        new CatImages().execute();

    }

    /**
     * Downloads or loads cached cat images and updates the UI.
     * - Gets JSON from https://cataas.com/cat?json=true
     * - Uses "id" from JSON as local filename (id.png)
     * - If file exists, load from disk; else download then save
     * - Calls publishProgress to update ProgressBar and show image
     */
    private class CatImages extends AsyncTask<Void, Integer, Void> {
        private Bitmap currentBitmap;

        @Override
        protected Void doInBackground(Void... params) {
            while (!isCancelled()) {
                try {
                    // Try JSON first
                    String json = null;
                    try {
                        URL jsonUrl = new URL("https://cataas.com/cat?json=true");
                        HttpURLConnection jc = (HttpURLConnection) jsonUrl.openConnection();
                        jc.setConnectTimeout(10000);
                        jc.setReadTimeout(15000);
                        jc.setRequestProperty("User-Agent", "AndroidTV-Lab/1.0");
                        try (InputStream jis = new BufferedInputStream(jc.getInputStream())) {
                            json = readFully(jis);
                        }
                    } catch (Exception e) {
                        android.util.Log.e(TAG, "JSON fetch failed, will fallback to direct image", e);
                    }

                    File imgFile;
                    if (json != null) {
                        org.json.JSONObject obj = new org.json.JSONObject(json);
                        String id = obj.optString("id", "");
                        String urlPath = obj.optString("url", "");
                        String imgUrlStr = urlPath.startsWith("http") ? urlPath : ("https://cataas.com" + urlPath);
                        if (id.isEmpty()) {
                            int slash = imgUrlStr.lastIndexOf('/');
                            id = (slash >= 0 ? imgUrlStr.substring(slash + 1) : "cat") + "_" + System.currentTimeMillis();
                        }
                        imgFile = new File(getFilesDir(), id + ".png");

                        if (!imgFile.exists()) {
                            downloadToFile(imgUrlStr, imgFile);
                        }
                    } else {
                        // Fallback: direct random image (no JSON)
                        String fallbackUrl = "https://cataas.com/cat"; // returns an image
                        String name = "fallback_" + System.currentTimeMillis();
                        imgFile = new File(getFilesDir(), name + ".png");
                        downloadToFile(fallbackUrl, imgFile);
                    }

                    currentBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    publishProgress(0); // show image

                    // Animate progress 0→100
                    for (int p = 0; p <= 100 && !isCancelled(); p += 2) {
                        publishProgress(p);
                        Thread.sleep(30);
                    }
                } catch (Exception e) {
                    android.util.Log.e(TAG, "Loop error", e);
                    // keep UI alive
                    for (int p = 0; p <= 100 && !isCancelled(); p += 5) {
                        publishProgress(p);
                        try { Thread.sleep(20); } catch (InterruptedException ignored) {}
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int p = values[0];
            if (progressBar.isIndeterminate()) progressBar.setIndeterminate(false);
            if (p == 0 && currentBitmap != null) imageView.setImageBitmap(currentBitmap);
            progressBar.setProgress(p);
        }


    }

    // Helper: read an InputStream fully into a String (API 28+ compatible)
    private static String readFully(InputStream is) throws java.io.IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = is.read(buf)) != -1) {
            baos.write(buf, 0, n);
        }
        return baos.toString(StandardCharsets.UTF_8.name());
    }

    private void downloadToFile(String urlStr, File out) throws Exception {
        URL u = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(20000);
        conn.setRequestProperty("User-Agent", "AndroidTV-Lab/1.0");
        try (InputStream is = new BufferedInputStream(conn.getInputStream());
             BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(out))) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = is.read(buf)) != -1) os.write(buf, 0, n);
        }
    }

}
