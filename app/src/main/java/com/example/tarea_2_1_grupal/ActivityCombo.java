package com.example.tarea_2_1_grupal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import Configuracion.Personas;
import Configuracion.SQLliteConexion;
import Configuracion.Trans;

public class ActivityCombo extends AppCompatActivity {

    SQLliteConexion conexion;
    Spinner combopersonas;
    EditText nombre, apellido, correo;
    ArrayList<Personas> lista;
    ArrayList<String> Arreglo;
    int ElementoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_combo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        conexion = new SQLliteConexion(this, Trans.DBname, null, Trans.Version);
        combopersonas = (Spinner) findViewById(R.id.spinner);
        nombre = (EditText) findViewById(R.id.cbnombre);
        apellido = (EditText) findViewById(R.id.cbapellido);
        correo = (EditText) findViewById(R.id.cbcorreo);

        ObtenerInfo();

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Arreglo);
        combopersonas.setAdapter(adp);

        ElementoSeleccionado = combopersonas.getSelectedItemPosition();

        combopersonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ElementoSeleccionado = combopersonas.getSelectedItemPosition();
                //Toast.makeText(getApplicationContext(), ""+ElementoSeleccionado, Toast.LENGTH_SHORT).show();
                ObtenerInfoDeta();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void ObtenerInfo() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Personas person=null;
        lista = new ArrayList<Personas>();

        Cursor cursor = db.rawQuery(Trans.SelectAllPerson,null);

        while(cursor.moveToNext()){
            person = new Personas();
            person.setId(cursor.getInt(0));
            person.setNombre(cursor.getString(1));
            person.setApellido(cursor.getString(2));
            person.setCorreo(cursor.getString(4));

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
                            lista.get(i).getApellido()/* + " "+
                            lista.get(i).getCorreo()*/);

        }

    }

    private void ObtenerInfoDeta() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Personas person=null;

        int x=ElementoSeleccionado+1;

        Cursor cursor = db.rawQuery(Trans.SelectAllPerson+" WHERE id = "+x,null);

        while(cursor.moveToNext()){
            person = new Personas();

            /*person.setNombre(cursor.getString(1));
            person.setApellido(cursor.getString(2));
            person.setCorreo(cursor.getString(4));*/

            nombre.setText(cursor.getString(1));
            apellido.setText(cursor.getString(2));
            correo.setText(cursor.getString(4));

        }

    }

}