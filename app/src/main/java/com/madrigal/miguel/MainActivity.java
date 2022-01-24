package com.madrigal.miguel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogEncriptar.DialogListener, DialogDesencriptar.DialogListener{

    private EditText et_nombre_archivo;
    private Button btn_crear;
    private Button btn_leer;
    private Button btn_encriptar;
    private Button btn_desencriptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.et_nombre_archivo = findViewById(R.id.edittext_archivo);
        this.btn_crear = findViewById(R.id.button_crear);
        this.btn_leer = findViewById(R.id.button_leer);
        this.btn_encriptar = findViewById(R.id.button_encriptar);
        this.btn_desencriptar = findViewById(R.id.button_desencriptar);

        btn_encriptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogEncriptar();
            }
        });
        btn_desencriptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogDesencriptar();
            }
        });

    }

    public void crearArchivo(View view){
        Intent intent = new Intent(this, ActivityCrearArchivo.class);
        intent.putExtra("nombre_archivo", et_nombre_archivo.getText().toString());
        startActivity(intent);
    }

    public void leerArchivo(View view){
        Intent intent = new Intent(this, ActivityLeerArchivo.class);
        intent.putExtra("nombre_archivo", et_nombre_archivo.getText().toString());
        startActivity(intent);
    }

    public void openDialogEncriptar(){
        DialogEncriptar dialog = new DialogEncriptar();
        dialog.show(getSupportFragmentManager(), "Encriptando archivo");
    }

    private void openDialogDesencriptar() {
        DialogDesencriptar dialog = new DialogDesencriptar();
        dialog.show(getSupportFragmentManager(), "Desencriptando archivo");
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void cifrarArchivo(String clave) {
        String nombre_archivo = et_nombre_archivo.getText().toString();
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
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /* --------------------------------------------------*/
        // CIFRADO TEXTO
        String contenido_original = sb.toString();
        String contenido_cifrado;

        CifradoCesar cesar = new CifradoCesar(contenido_original,  clave);
        CifradoXor xor = new CifradoXor();

        int j = 0;
        char c;

        for (int i = 0; i < cesar.getMsg().length(); i++){
            if (j == cesar.getClave().length()){
                j = 0;
            }
            int ascii_clave = (int)cesar.getClave().charAt(j);
            c = cesar.cifrar(
                    cesar.getMsg().charAt(i),
                    (int)CifradoCesar.getSBox(ascii_clave)
            );

            xor.cifrar(
                    c,
                    cesar.getClave().charAt(j)
            );
            j++;
        }

        contenido_cifrado = xor.toString();

        nombre_archivo = et_nombre_archivo.getText().toString();
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(nombre_archivo, MODE_PRIVATE);
            fos.write(contenido_cifrado.getBytes());
            Log.d("TAG1", "Archivo guardado en: " + getFilesDir() + "/" + nombre_archivo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    Toast.makeText(this, "Archivo encriptado con éxito", Toast.LENGTH_LONG).show();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void descifrarArchivo(String clave){
        String nombre_archivo = et_nombre_archivo.getText().toString();
        String archivo_original = nombre_archivo + "original";
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        StringBuilder or = new StringBuilder();

        //lectura para cifrar
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
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //lectura del original
        fis = null;
        try {
            fis = openFileInput(archivo_original);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String linea;
            while((linea = br.readLine()) != null){
                or.append(linea);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        CifradoCesar cesar = new CifradoCesar(clave);
        CifradoXor xor = new CifradoXor();
        String contenido_cifrado = sb.toString();
        String contenido_descifrado;
        String array_list [] = contenido_cifrado.split(" ");

        for(String s: array_list){
                xor.getList().add(s);
        }
        List<String> lista = xor.getList();

        int j = 0;
        char c ;
        try{
            for (int i = 0; i < lista.size(); i++) {
                if (j == cesar.getClave().length()){
                    j = 0;
                }

                int ascii_clave = (int)cesar.getClave().charAt(j);

                c = xor.descrifrar(
                        (char)Integer.parseInt(lista.get(i),16),
                        cesar.getClave().charAt(j)
                );

                cesar.descrifrar(
                        c,
                        (int)CifradoCesar.getSBox(ascii_clave)
                );

                j++;

            }
        } catch (Exception e){
            e.printStackTrace();
        }

        contenido_descifrado = cesar.toString();
        String aux1 = contenido_descifrado.replaceAll("\\s", "");
        String aux2 = or.toString().replaceAll("\\s", "");
        if(aux1.equals(aux2)){
            nombre_archivo = et_nombre_archivo.getText().toString();
            FileOutputStream fos = null;
            try {
                fos = openFileOutput(nombre_archivo, MODE_PRIVATE);
                fos.write(or.toString().getBytes());
                Log.d("TAG1", "Archivo guardado en: " + getFilesDir() + "/" + nombre_archivo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        Toast.makeText(this, "Archivo desencriptado con éxito", Toast.LENGTH_LONG).show();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Toast.makeText(this, "La clave de cifrado es incorrecta", Toast.LENGTH_LONG).show();
        }


    }
}