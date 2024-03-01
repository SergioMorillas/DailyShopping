package com.lista.listacompra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.listacompra.supermercado.Alcampo;
import com.lista.listacompra.supermercado.Mercadona;
import com.lista.listacompra.supermercado.Supermercado;
import com.squareup.picasso.Picasso;

import java.lang.*;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Productos extends AppCompatActivity {
    Button but;
    TextView text, palabra;
    LinearLayout lin;
    int n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        but = (Button) findViewById(R.id.buttonListCreate);
        text = (TextView) findViewById(R.id.textViewBusqueda);
        palabra = (TextView) findViewById(R.id.editTextItem);
        lin = (LinearLayout) findViewById(R.id.linearLayout);
        but.bringToFront();

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<Double, String[]> productos = agregarElementoDesdeURL(palabra.getText().toString());
                if (lin != null && lin.getChildCount() > 0) lin.removeAllViews();
                for (Map.Entry<Double, String[]> entry : productos.entrySet()) {
                    Double precio = entry.getKey();
                    String[] s = entry.getValue();

                    TextView nombrePrecio = new TextView(lin.getContext());
                    ImageView imagen = new ImageView(lin.getContext());
                    Picasso.get().load(s[1]).into(imagen);
                    nombrePrecio.setText(s[0] + "\n\t" + precio + "€");
                    nombrePrecio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nombrePrecio.setText("Pulsado");
                            try {
                                Thread.sleep(1500);
                            } catch (Exception ex) {
                            }
                            nombrePrecio.setText(s[0] + "\n\t" + precio + "€");
                        }
                    });
                    lin.addView(nombrePrecio);
                    lin.addView(imagen);
                }
            }
        });
    }

    private String modificaString(String entrada) {
        if (entrada.startsWith("\"") && entrada.endsWith("\"")) {
            return entrada.substring(1, entrada.length() - 1);
        } else {
            return entrada;
        }
    }

    private HashMap<Double, String[]> agregarElementoDesdeURL(String i) {
        HashMap<Double, String[]> productos = new HashMap<>();
        Supermercado al = null;

        if (CreaLista.getSupermercado().equals("Alcampo")){
            al = new Alcampo();
        } else if (CreaLista.getSupermercado().equals("Mercadona")){
            al = new Mercadona();
        }
        productos = (HashMap<Double, String[]>) al.busqueda(i);
        return productos;
    }
}