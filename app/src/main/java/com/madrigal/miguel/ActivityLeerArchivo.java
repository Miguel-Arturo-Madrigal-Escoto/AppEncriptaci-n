package com.madrigal.miguel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ActivityLeerArchivo extends AppCompatActivity {

    private TextView tv_salida, tv_file;
    private Button btn_volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leer_archivo);

        this.tv_salida = findViewById(R.id.textview_salida);
        this.btn_volver = findViewById(R.id.button_volver);
        this.tv_file = findViewById(R.id.textview_file);
        this.tv_salida.setText(leerArchivo());
    }


    public void finish(View view){
        finish();
    }

    public String leerArchivo() {
        Intent intent = getIntent();
        String nombre_archivo = intent.getStringExtra("nombre_archivo");
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();

        try {
            fis = openFileInput(nombre_archivo);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String linea;
            while((linea = br.readLine()) != null){
                sb.append(linea);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                this.tv_file.setText("Archivo: " + nombre_archivo + ".txt");
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                this.tv_file.setText("Archivo: ");
            }
        }

        return (!sb.toString().equals("")? sb.toString(): "Archivo no encontrado");
    }

}