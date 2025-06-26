package com.example.tarea_2_1_grupal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityListaVideos extends AppCompatActivity {

    private int selectedPosition = -1; // Guarda el índice seleccionado

    private ListView listView;
    private VideoView videoView;
    private VideoDBHelper dbHelper;
    private ArrayList<String> videoPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_videos);

        listView = findViewById(R.id.listView);
        videoView = findViewById(R.id.videoViewPreview);
        dbHelper = new VideoDBHelper(this);

        loadVideoPaths();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            String path = videoPaths.get(position);
            videoView.setVideoURI(Uri.parse(path));
            videoView.start();
        });

        Button btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(v -> {
            if (selectedPosition >= 0 && selectedPosition < videoPaths.size()) {
                String pathToDelete = videoPaths.get(selectedPosition);

                // Eliminar de SQLite
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int rowsDeleted = db.delete(VideoDBHelper.TABLE_NAME, VideoDBHelper.COLUMN_PATH + "=?", new String[]{pathToDelete});

                if (rowsDeleted > 0) {
                    Toast.makeText(this, "Video eliminado de la base de datos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                }

                selectedPosition = -1;
                videoView.stopPlayback();
                videoView.setVideoURI(null);
                loadVideoPaths();
            } else {
                Toast.makeText(this, "Primero selecciona un video", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadVideoPaths() {
        videoPaths = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(VideoDBHelper.TABLE_NAME, new String[]{VideoDBHelper.COLUMN_PATH},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndexOrThrow(VideoDBHelper.COLUMN_PATH));
            videoPaths.add(path);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videoPaths);
        listView.setAdapter(adapter);
    }

}
