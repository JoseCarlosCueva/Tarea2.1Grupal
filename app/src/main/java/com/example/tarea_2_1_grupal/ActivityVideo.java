package com.example.tarea_2_1_grupal;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ActivityVideo extends AppCompatActivity {

    private static final int REQUEST_CAPTURE_VIDEO = 103;
    private Uri videoUri;
    private VideoView videoView;
    private Button btntakevideo, btnsavevideo;
    private String video64;

    // Si deseas guardar en base de datos:
    private VideoDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        btntakevideo = findViewById(R.id.btntakevideo);
        btnsavevideo = findViewById(R.id.btnsavevideo);
        dbHelper = new VideoDBHelper(this);

        btntakevideo.setOnClickListener(v -> requestPermissions());

        btnsavevideo.setOnClickListener(v -> {
            if (videoUri != null) {
                new ConvertirVideoAsyncTask().execute(videoUri); // Conversión en segundo plano
            } else {
                Toast.makeText(this, "Primero graba un video", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnvervideos = findViewById(R.id.btnvervideos);
        btnvervideos.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityVideo.this, ActivityListaVideos.class);
            startActivity(intent);
        });

    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                    REQUEST_CAPTURE_VIDEO);
        } else {
            captureVideo();
        }
    }

    private void captureVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAPTURE_VIDEO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_VIDEO && resultCode == RESULT_OK && data != null) {
            videoUri = data.getData();
            if (videoUri != null) {
                videoView.setVideoURI(videoUri);
                videoView.start();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // AÑADE ESTO

        if (requestCode == REQUEST_CAPTURE_VIDEO) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = true;
                    break;
                }
            }
            if (granted) {
                captureVideo();
            } else {
                Toast.makeText(this, "Permisos requeridos no concedidos", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class ConvertirVideoAsyncTask extends AsyncTask<Uri, Void, String> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(ActivityVideo.this, "Convirtiendo video...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Uri... uris) {
            return convertVideoToBase64(uris[0]);
        }

        @Override
        protected void onPostExecute(String base64) {
            if (base64 != null) {
                video64 = base64;
                Toast.makeText(ActivityVideo.this, "Video convertido a Base64", Toast.LENGTH_SHORT).show();

                // SQLite
                /*
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("path", videoUri.toString());
                db.insert("videos", null, values);
                Toast.makeText(ActivityVideo.this, "Ruta guardada en BD", Toast.LENGTH_SHORT).show();
                */

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("path", videoUri.toString());
                db.insert("videos", null, values);
                Toast.makeText(ActivityVideo.this, "Ruta guardada en BD", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivityVideo.this, "Error al convertir el video", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String convertVideoToBase64(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream != null ? inputStream.read(buffer) : -1) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
