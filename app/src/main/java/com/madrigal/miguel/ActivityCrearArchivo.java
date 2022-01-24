package com.madrigal.miguel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ActivityCrearArchivo extends AppCompatActivity {

    private EditText et_contenido;
    private Button btn_guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_archivo);

        setUpView();
    }

    public void setUpView(){
        this.et_contenido = findViewById(R.id.edittext_contenido);
        this.btn_guardar = findViewById(R.id.button_guardar);
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarArchivo();
            }
        });
    }

    public void guardarArchivo(){
        Intent intent = getIntent();
        String contenido = this.et_contenido.getText().toString();
        String nombre_archivo = intent.getStringExtra("nombre_archivo");
        String archivo_original = nombre_archivo + "original";
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(nombre_archivo, MODE_PRIVATE);
            fos.write(contenido.getBytes());
            Log.d("TAG1", "Archivo guardado en: " + getFilesDir() + "/" + nombre_archivo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    Toast.makeText(this, "Archivo guardado con éxito", Toast.LENGTH_LONG).show();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        fos = null;
        try {
            fos = openFileOutput(archivo_original, MODE_PRIVATE);
            fos.write(contenido.getBytes());
            Log.d("TAG1", "Archivo guardado en: " + getFilesDir() + "/" + archivo_original);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        finish();
    }
}