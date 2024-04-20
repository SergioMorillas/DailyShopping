package com.lista.listacompra;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.persistencia.Producto;
import com.lista.listacompra.supermercado.Supermercado;
import com.lista.listacompra.supermercado.SupermercadosDisponibles;
import com.lista.listacompra.supermercado.SupermercadosFactoria;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class BuscadorProductos extends AppCompatActivity {
    Button buscar, cancelar;
    LinearLayout layout;
    EditText texto;
    String supermercado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscador_productos);
        initializeViews();
        supermercado = getIntent().getExtras().getString("supermercado");
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar.setClickable(false);
                buscarProductos(supermercado);
                buscar.setClickable(true);
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    private void buscarProductos(String s) {
        if (layout.getChildCount() > 0) layout.removeAllViews();
        String producto = (texto.getText() != null) ? texto.getText().toString() : "";
        if (!producto.isBlank()) {
            Supermercado supermercado = SupermercadosFactoria.crearSupermercado(SupermercadosDisponibles.valueOf(s));
            ArrayList<Producto> listaTemporal = supermercado.search(producto);
            Collections.sort(listaTemporal);
            if (listaTemporal.size() != 0) {
                Producto p = listaTemporal.get(0);
                añadirObjeto(p, supermercado);
            }

        } else {
            Toast.makeText(BuscadorProductos.this, "Debes introducir un producto a buscar", Toast.LENGTH_SHORT).show();
        }
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
        cancelar = findViewById(R.id.btnCancelar);
        texto = findViewById(R.id.txtBuscador);
    }
}
