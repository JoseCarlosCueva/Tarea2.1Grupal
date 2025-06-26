package com.example.tarea_2_1_grupal;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Configuracion.SQLliteConexion;
import Configuracion.Trans;

public class ActivityInit extends AppCompatActivity {

    EditText nombre,apellido,edad,correo,telefono;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_init);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nombre = (EditText) findViewById(R.id.txtnombre);
        apellido = (EditText) findViewById(R.id.txtapellido);
        edad = (EditText) findViewById(R.id.txtedad);
        correo = (EditText) findViewById(R.id.txtcorreo);
        telefono = (EditText) findViewById(R.id.txttelefono);

        btn = (Button) findViewById(R.id.button);
        
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ActivityInit.this, "Hola "+nombre.getText()+" "+apellido.getText()+" "+edad.getText()+" "+correo.getText()+" "+telefono.getText(), Toast.LENGTH_SHORT).show();
                Agregar();
            }
        });
    }

    private void Agregar(){

        try {


        SQLliteConexion conexion = new SQLliteConexion(this, Trans.DBname, null, Trans.Version);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores  = new ContentValues();
        valores.put(Trans.nombre, nombre.getText().toString());
        valores.put(Trans.apellido, apellido.getText().toString());
        valores.put(Trans.edad, edad.getText().toString());
        valores.put(Trans.correo, correo.getText().toString());
        valores.put(Trans.telefono, telefono.getText().toString());

        Long resultado = db.insert(Trans.TablePersonas, Trans.id, valores);

        Toast.makeText(getApplicationContext(), "Registro con exito "+resultado, Toast.LENGTH_SHORT).show();

        db.close();

        }catch (Exception ex){
            ex.toString();
        }
    }

}