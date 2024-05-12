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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.R;
import com.lista.listacompra.modelo.Producto;
import com.lista.listacompra.modelo.SupermercadosDisponibles;
import com.lista.listacompra.modelo.SupermercadosFactoria;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ComparadorProductos extends AppCompatActivity {
    private Button buscar;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private SupermercadosFactoria superM;
    private LinearLayout layout;
    private EditText texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparador_productos);
        initializeViews();
        superM = new SupermercadosFactoria();
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar.setClickable(false);
                buscarProductos();
                buscar.setClickable(true);
            }
        });
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ComparadorProductos.this, PrincipalListas.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void buscarProductos() {
        if (layout.getChildCount() > 0) layout.removeAllViews();
        String producto = (texto.getText() != null) ? texto.getText().toString() : "";
        if (!producto.isBlank()) {
            new Thread(() -> {
                for (SupermercadosDisponibles nombre : SupermercadosDisponibles.values()) {
                    superM.crearSupermercado(nombre);
                    if (superM.getNombre() != null) {
                        Set<Producto> set = superM.busqueda(producto);
                        ArrayList<Producto> aux = new ArrayList<>(set);
                        Collections.sort(aux);
                        if (set.size() != 0) {
                            Producto p = aux.get(0);
                            mHandler.post(() -> añadirObjeto(p, nombre.name())); // Añade el objeto en el hilo principal
                        } else {
                            mHandler.post(() -> añadirObjetoVacio(new Producto()));
                        }
                    }
                }
            }).start();

        } else {
            Toast.makeText(ComparadorProductos.this, "Debes introducir un producto a buscar", Toast.LENGTH_SHORT).show();
        }
    }

    private void añadirObjetoVacio(Producto p) {
        LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_comparador, null);
        TextView nombre = fila.findViewById(R.id.nombreProducto);
        ImageView imagen = fila.findViewById(R.id.imagenProducto);

        nombre.setText(p.getName());
        imagen.setImageResource(R.drawable.imagen_no_encontrada);
        layout.addView(fila);

    }

    private void añadirObjeto(Producto p, String s) {
        LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_comparador, null);
        TextView supermercado = fila.findViewById(R.id.supermercadoProducto);
        TextView nombre = fila.findViewById(R.id.nombreProducto);
        TextView precio = fila.findViewById(R.id.precioProducto);
        TextView precioKilo = fila.findViewById(R.id.precioPorKilo);
        ImageView imagen = fila.findViewById(R.id.imagenProducto);

        supermercado.setText(s);
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
