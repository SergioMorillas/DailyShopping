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
        setContentView(R.layout.crea_lista);
        comboBox = findViewById(R.id.comboBoxSupermercados);
        List<String> listSpinner = Arrays.asList("Alcampo", "Mercadona");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listSpinner);
        comboBox.setAdapter(adapter);
        nombreLista = findViewById(R.id.editTextNombreLista);
        fecha = findViewById(R.id.editTextDate);
        aceptar = findViewById(R.id.buttonAceptar);
        cancelar = findViewById(R.id.buttonCancelar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nombreLista.getText().toString();
                String sFecha = fecha.getText().toString();
                if(nombre.equals("")){
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                } else if (sFecha.equals("")){
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    sFecha = dateFormat.format(date);
                    String dev = nombre + "\n\t" + sFecha + "\t" + comboBox.getSelectedItem().toString();
                    Intent data = new Intent();
                    data.putExtra("nombreLista", dev);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                } else {
                    String dev = nombre + "\n\t" + sFecha + "\t" + comboBox.getSelectedItem().toString();
                    Intent data = new Intent();
                    data.putExtra("nombreLista", dev);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
    public static String getSupermercado(){
        String supermercado = (String) comboBox.getSelectedItem();
        return supermercado;
    }
}
