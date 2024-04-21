package com.lista.listacompra.logica;

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

import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.R;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;
import com.lista.listacompra.modelo.Producto;
import com.lista.listacompra.modelo.SupermercadosDisponibles;
import com.lista.listacompra.modelo.SupermercadosFactoria;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class BuscadorProductos extends AppCompatActivity {
    Button buscar;
    LinearLayout layout;
    EditText texto;
    String supermercado, lista;
    TextView nombreAplicacion;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    SupermercadosFactoria superM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscador_productos);
        initializeViews();
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        supermercado = b.getString("supermercado");
        lista = b.getString("nombreLista");
        superM = new SupermercadosFactoria();
        nombreAplicacion.setText("Buscador de " + supermercado);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar.setClickable(false);
                buscarProductos(supermercado);
                buscar.setClickable(true);
            }
        });
    }

    private void buscarProductos(String s) {
        if (layout.getChildCount() > 0) layout.removeAllViews();
        String producto = (texto.getText() != null) ? texto.getText().toString() : "";
        if (!producto.isBlank()) {
            new Thread(() -> {
                superM.crearSupermercado(SupermercadosDisponibles.valueOf(s));

                List<Producto> listaTemporal = superM.busqueda(producto);
                Collections.sort(listaTemporal);
                if (listaTemporal.size() != 0) {
                    for (int i = 0; i < 20 && i < (listaTemporal.size() - 1); i++) {
                        Producto p = listaTemporal.get(i);
                        mHandler.post(() -> añadirObjeto(p, superM)); // Añade el objeto en el hilo principal
                    }
                }
            }).start();
        } else {
            Toast.makeText(BuscadorProductos.this, "Debes introducir un producto a buscar", Toast.LENGTH_SHORT).show();
        }


    }

    private void añadirObjeto(Producto p, SupermercadosFactoria s) {
        LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(
                R.layout.productos_comparador, null);
        TextView supermercado = fila.findViewById(R.id.supermercadoProducto);
        TextView nombre = fila.findViewById(R.id.nombreProducto);
        TextView precio = fila.findViewById(R.id.precioProducto);
        TextView precioKilo = fila.findViewById(R.id.precioPorKilo);
        ImageView imagen = fila.findViewById(R.id.imageProducto);

        supermercado.setText(s.getNombre());
        nombre.setText(p.getName());

        precio.setText((String.format("%s€", p.getPrice())));

        if (p.getPricePerKilo() == -1) precioKilo.setText("No aplica");
        else precioKilo.setText(p.getPricePerKilo() + "€/kilo");

        Picasso.get().load(p.getImage()).into(imagen);

        fila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        layout.addView(fila);

    }


    private void initializeViews() {
        layout = findViewById(R.id.layout);
        buscar = findViewById(R.id.btnBuscar);
        texto = findViewById(R.id.txtBuscador);
        nombreAplicacion = findViewById(R.id.nombreAplicacion);
    }
}
