package com.lista.listacompra;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.persistencia.AppDatabase;
import com.lista.listacompra.persistencia.Producto;
import com.lista.listacompra.supermercado.Supermercado;
import com.lista.listacompra.supermercado.SupermercadosDisponibles;
import com.lista.listacompra.supermercado.SupermercadosFactoria;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class ComparadorProductos extends AppCompatActivity {
    Button buscar;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    LinearLayout layout;
    EditText texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparador_productos);
        initializeViews();
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar.setClickable(false);
                buscarProductos();
                buscar.setClickable(true);
            }
        });

    }

    private void buscarProductos() {
        if (layout.getChildCount() > 0) layout.removeAllViews();
        String producto = (texto.getText() != null) ? texto.getText().toString() : "";
        if (!producto.isBlank()) {
            new Thread(() -> {
                for (SupermercadosDisponibles nombre : SupermercadosDisponibles.values()) {
                    Supermercado supermercado = SupermercadosFactoria.crearSupermercado(nombre);
                    if (supermercado != null) {
                        ArrayList<Producto> listaTemporal = supermercado.search(producto);
                        Collections.sort(listaTemporal);
                        if (listaTemporal.size() != 0) {
                            Producto p = listaTemporal.get(0);
                            mHandler.post(() -> añadirObjeto(p, supermercado)); // Añade el objeto en el hilo principal
                        } else {
                            mHandler.post(() -> añadirObjetoVacio(new Producto(), supermercado));
                        }
                    }
                }
            }).start();

        } else {
            Toast.makeText(ComparadorProductos.this, "Debes introducir un producto a buscar", Toast.LENGTH_SHORT).show();
        }
    }

    private void añadirObjetoVacio(Producto p, Supermercado s) {
        LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_comparador, null);
        TextView nombre = fila.findViewById(R.id.nombreProducto);
        ImageView imagen = fila.findViewById(R.id.imageProducto);

        nombre.setText(p.getName());
        imagen.setImageResource(R.drawable.imagen_no_encontrada);
        layout.addView(fila);

    }

    private void añadirObjeto(Producto p, Supermercado s) {
        LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_comparador, null);
        TextView supermercado = fila.findViewById(R.id.supermercadoProducto);
        TextView nombre = fila.findViewById(R.id.nombreProducto);
        TextView precio = fila.findViewById(R.id.precioProducto);
        TextView precioKilo = fila.findViewById(R.id.precioPorKilo);
        ImageView imagen = fila.findViewById(R.id.imageProducto);

        supermercado.setText(s.getClass().getSimpleName());
        nombre.setText(p.getName());

        precio.setText((String.format("%s€", p.getPrice())));

        if (p.getPricePerKilo() == -1) precioKilo.setText("No aplica");
        else precioKilo.setText(p.getPricePerKilo() + "€/kilo");

        Picasso.get().load(p.getImage()).into(imagen);

        layout.addView(fila);

    }

    private void initializeViews() {
        layout = findViewById(R.id.layout);
        buscar = findViewById(R.id.btnBuscar);
        texto = findViewById(R.id.txtBuscador);
    }

}
