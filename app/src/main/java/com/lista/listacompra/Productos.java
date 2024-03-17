package com.lista.listacompra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lista.listacompra.supermercado.Alcampo;
import com.lista.listacompra.supermercado.Mercadona;
import com.lista.listacompra.supermercado.Producto;
import com.lista.listacompra.supermercado.Supermercado;
import com.squareup.picasso.Picasso;

import java.lang.*;
import java.util.ArrayList;
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
                ArrayList<Producto> productos = agregarElementoDesdeURL(palabra.getText().toString());
                if (lin != null && lin.getChildCount() > 0) lin.removeAllViews();
                for (Producto producto : productos) {

                    TextView nombrePrecio = new TextView(lin.getContext());
                    ImageView imagen = new ImageView(lin.getContext());
                    Picasso.get().load(producto.getImagen()).into(imagen);
                    nombrePrecio.setText(producto.getNombre() + "\n\t" + producto.getPrecio() + "€");
                    nombrePrecio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            nombrePrecio.setText("Pulsado");
                            try {
                                Thread.sleep(1500);
                            } catch (Exception ex) {
                            }
                            nombrePrecio.setText(producto.getNombre() + "\n\t" + producto.getPrecio() + "€");
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

    private ArrayList<Producto> agregarElementoDesdeURL(String i) {
        ArrayList<Producto> productos = null;
        Supermercado al = null;

        if (CreaLista.getSupermercado().equals("Alcampo")) {
            al = new Alcampo();
        } else if (CreaLista.getSupermercado().equals("Mercadona")) {
            al = new Mercadona();
        }
        productos = (ArrayList<Producto>) al.busqueda(i);
        return productos;
    }
}