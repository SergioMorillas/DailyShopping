package com.lista.listacompra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CreaLista extends AppCompatActivity {
    private static Spinner comboBox;
    EditText nombreLista, fecha;
    Button aceptar, cancelar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static String getSupermercado(){
        String supermercado = (String) comboBox.getSelectedItem();
        return supermercado;
    }
}
