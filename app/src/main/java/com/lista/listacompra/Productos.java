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
import com.lista.listacompra.supermercado.Dia;
import com.lista.listacompra.supermercado.Mercadona;
import com.lista.listacompra.supermercado.Product;
import com.lista.listacompra.supermercado.Supermercado;
import com.squareup.picasso.Picasso;

import java.lang.*;
import java.util.ArrayList;

public class Productos extends AppCompatActivity {
    Button but;
    TextView text, palabra;
    LinearLayout lin;
    int n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String modificaString(String entrada) {
        if (entrada.startsWith("\"") && entrada.endsWith("\"")) {
            return entrada.substring(1, entrada.length() - 1);
        } else {
            return entrada;
        }
    }

    private ArrayList<Product> agregarElementoDesdeURL(String i) {
        ArrayList<Product> productos = null;
        Supermercado al = null;

        if (CreaLista.getSupermercado().equals("Alcampo")) {
            al = new Alcampo();
        } else if (CreaLista.getSupermercado().equals("Mercadona")) {
            al = new Mercadona();
        } else if (CreaLista.getSupermercado().equals("Dia")){
            al = new Dia();
        }
        productos = (ArrayList<Product>) al.search(i);
        return productos;
    }
}