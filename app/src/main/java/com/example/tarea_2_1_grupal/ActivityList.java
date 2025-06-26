package com.example.tarea_2_1_grupal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import Configuracion.Personas;
import Configuracion.SQLliteConexion;
import Configuracion.Trans;

public class ActivityList extends AppCompatActivity {

    SQLliteConexion conexion;
    ListView listaperson;
    ArrayList<Personas> lista;
    ArrayList<String> Arreglo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        conexion = new SQLliteConexion(this, Trans.DBname,null, Trans.Version);
        listaperson = (ListView) findViewById(R.id.listapersona);

        obtenerinfo();

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Arreglo);
        listaperson.setAdapter(adp);

        listaperson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ElementoSeleccionado = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), ElementoSeleccionado, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void obtenerinfo(){
        SQLiteDatabase db = conexion.getReadableDatabase();
        Personas person=null;
        lista = new ArrayList<Personas>();

        Cursor cursor = db.rawQuery(Trans.SelectAllPerson,null);

        while(cursor.moveToNext()){
            person = new Personas();
            person.setId(cursor.getInt(0));
            person.setNombre(cursor.getString(1));
            person.setApellido(cursor.getString(2));
            person.setEdad(cursor.getString(3));
            person.setCorreo(cursor.getString(4));
            person.setTelefono(cursor.getString(5));

            lista.add(person);
        }

        cursor.close();
        FillDate();

    }

    private void FillDate() {
        Arreglo = new ArrayList<>();
        for(int i=0; i < lista.size(); i++)
        {
            Arreglo.add(
                    lista.get(i).getId() + " "+
                    lista.get(i).getNombre() + " "+
                    lista.get(i).getApellido() + " "+
                    lista.get(i).getEdad() + " "+
                    lista.get(i).getCorreo() + " " +
                    lista.get(i).getTelefono());

        }
    }
}