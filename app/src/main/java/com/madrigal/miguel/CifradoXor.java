package com.madrigal.miguel;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.LinkedList;
import java.util.List;

public class CifradoXor {
    private List lista;

    public CifradoXor(){
        lista = new LinkedList();
    }

    public void cifrar(char msg, char clave){
        lista.add(Integer.toHexString(msg ^ clave));
    }

    public char descrifrar(char msg, char clave){
        lista.add(msg^clave);

        return (char) (msg^clave);
    }

    public List getList(){
        return this.lista;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        lista.forEach( e -> {
            sb.append(e);
            sb.append(" ");
        });

        return sb.toString();
    }

}
