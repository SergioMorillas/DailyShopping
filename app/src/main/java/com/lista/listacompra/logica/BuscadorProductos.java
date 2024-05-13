package com.lista.listacompra.logica;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.R;
import com.lista.listacompra.modelo.Gestor;
import com.lista.listacompra.modelo.ListaCompra;
import com.lista.listacompra.modelo.Producto;
import com.lista.listacompra.modelo.SupermercadosDisponibles;
import com.lista.listacompra.modelo.SupermercadosFactoria;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BuscadorProductos extends AppCompatActivity {
    private Button buscar;
    private LinearLayout layout;
    private EditText texto;
    private String supermercado, lista;
    private TextView nombreAplicacion;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private SupermercadosFactoria superM;
    private ListaCompra miLista;
    private Gestor gestor;
    private ProgressBar progressBar;
    private long fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.buscador_productos);
        initializeViews();
        Bundle b = getIntent().getExtras();

        supermercado = b.getString("supermercado");
        lista = b.getString("nombreLista");
        fecha = b.getLong("fecha");
        superM = new SupermercadosFactoria();
        gestor = new Gestor(getApplicationContext());
        nombreAplicacion.setText("Buscador de " + supermercado);
        progressBar = findViewById(R.id.loadingProgressBar);
        new Thread(() -> {
            miLista = gestor.getListaPorNombre(lista, supermercado, fecha);
        }).start();

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
        progressBar.setVisibility(View.VISIBLE);

        if (layout.getChildCount() > 0) layout.removeAllViews();
        String producto = (texto.getText() != null) ? texto.getText().toString() : "";
        if (!producto.isBlank()) {
            new Thread(() -> {
                superM.crearSupermercado(SupermercadosDisponibles.valueOf(s));
                Set<Producto> set = superM.busqueda(producto);
                ArrayList<Producto> aux = new ArrayList<>(set);
                Collections.sort(aux);
                if (aux.size() != 0) {
                    for (int i = 0; i < (aux.size() - 1); i++) {
                        Producto p = aux.get(i);
                        mHandler.post(() -> añadirObjeto(p, superM)); // Añade el objeto en el hilo principal
                    }
                } else {
                    mHandler.post(() -> Toast.makeText(BuscadorProductos.this, "No se han encontrado productos con ese nombre", Toast.LENGTH_LONG).show());
                }
                mHandler.post(() -> progressBar.setVisibility(View.GONE));
            }).start();
        } else {
            Toast.makeText(BuscadorProductos.this, "Debes introducir un producto a buscar", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }


    }

    private void añadirObjeto(Producto p, SupermercadosFactoria s) {
        LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_comparador, null);
        TextView supermercado = fila.findViewById(R.id.supermercadoProducto);
        TextView nombre = fila.findViewById(R.id.nombreProducto);
        TextView precio = fila.findViewById(R.id.precioProducto);
        TextView precioKilo = fila.findViewById(R.id.precioPorKilo);
        ImageView imagen = fila.findViewById(R.id.imagenProducto);


        supermercado.setText(s.getNombre());
        nombre.setText(p.getName());

        precio.setText((String.format("%s€", p.getPrice())));

        if (p.getPricePerKilo() == -1) precioKilo.setText("No aplica");
        else precioKilo.setText(p.getPricePerKilo() + "€/kilo");

        Picasso.get().load(p.getImage()).into(imagen);

        fila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miLista.getProductos().add(p);
                Thread t1 = new Thread(() -> {
                    gestor.actualizarListaProductos(miLista);
                });
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Intent intent = new Intent(BuscadorProductos.this, ListaEspecifica.class);
                intent.putExtra("nombreLista", miLista.getNombre());
                intent.putExtra("supermercado", miLista.getSupermercado());
                startActivity(intent);
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
    /**
     * @brief Muestra un diálogo de menú.
     */
    private void showMenuDialog() {
        Dialog menuDialog = new Dialog(this, android.R.style.Theme);
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setContentView(R.layout.popup_menu);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = menuDialog.getWindow();
        if (window != null) {
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.START;
            layoutParams.horizontalMargin = 0.0f;
            layoutParams.verticalMargin = 0.0f;
            window.setAttributes(layoutParams);
        }

        menuDialog.show();
    }
    public void onSideBarClick(View view){
        showMenuDialog();
    }
    public void onCompararButtonClick(View view) {
        Intent i = new Intent(this, ComparadorProductos.class);
        startActivity(i);
    }

    public void onListasButtonClick(View view) {
        Intent i = new Intent(this, PrincipalListas.class);
        startActivity(i);
    }
    public void onJuegoButtonClick(View view) {
        Intent i = new Intent(this, JuegoPrecios.class);
        startActivity(i);
    }
}
