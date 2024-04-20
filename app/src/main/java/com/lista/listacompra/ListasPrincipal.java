package com.lista.listacompra;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.lista.listacompra.persistencia.AppDatabase;
import com.lista.listacompra.persistencia.ListaCompra;

import java.sql.Date;
import java.util.List;

/**
 * Actividad principal que muestra todas las listas de compras existentes.
 */
public class ListasPrincipal extends AppCompatActivity {

    private LinearLayout layout;
    private Button buttonAdd;
    private ImageButton menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listas_principal);
        initializeViews();
        setupListeners();
        observeListasCompra();
    }

    /**
     * @brief Inicializa las vistas de la actividad.
     */
    private void initializeViews() {
        layout = findViewById(R.id.linearLayout);
        buttonAdd = findViewById(R.id.buttonAdd);
        menu = findViewById(R.id.menu);
    }

    /**
     * @brief Configura los listeners para los botones de la actividad.
     */
    private void setupListeners() {
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuDialog();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToListasCreador();
            }
        });
    }

    /**
     * @brief Observa los cambios en la lista de compras en la base de datos y actualiza la interfaz de usuario.
     */
    private void observeListasCompra() {
        AppDatabase database = AppDatabase.getDatabase(getApplicationContext());
        database.listaCompraDao().getAllListasCompra().observe(this, new Observer<List<ListaCompra>>() {
            @Override
            public void onChanged(List<ListaCompra> listaCompras) {
                insertarListasEnVistas(listaCompras);
            }
        });
    }

    /**
     * @brief Inserta las listas de compras en las vistas de la actividad.
     *
     * @param listas Lista de compras a insertar.
     */
    private void insertarListasEnVistas(List<ListaCompra> listas) {
        for (ListaCompra lista : listas) {
            LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_lista, null);

            TextView nombreListaTextView = fila.findViewById(R.id.nombreLista);
            TextView supermercadoTextView = fila.findViewById(R.id.supermercadoProducto);
            TextView fechaTextView = fila.findViewById(R.id.fecha);

            nombreListaTextView.setText(lista.getNombre());
            supermercadoTextView.setText(lista.getSupermercado());
            fechaTextView.setText(new Date(lista.getFecha()).toString()); // Suponiendo que getFecha() devuelve un objeto LocalDate

            fila.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListasPrincipal.this, BuscadorProductos.class);
                    intent.putExtra("supermercado",lista.getSupermercado());
                    startActivity(intent);
                    Toast.makeText(ListasPrincipal.this, "Metodo en progreso", Toast.LENGTH_SHORT).show();
                }
            });
            addSeparatorLine();
            layout.addView(fila);
        }
    }

    /**
     * @brief Muestra un diálogo de menú.
     */
    private void showMenuDialog() {
        Dialog menuDialog = new Dialog(this, android.R.style.Theme);
        menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        menuDialog.setContentView(R.layout.menu_popup);

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

    /**
     *@brief Navega a la actividad ListasCreador.
     */
    private void navigateToListasCreador() {
        Intent intent = new Intent(this, ListasCreador.class);
        startActivity(intent);
    }

    /**
     *@brief Añade una línea separadora a la disposición lineal.
     */
    private void addSeparatorLine() {
        View linea = new View(this);
        linea.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 8);
        params.setMargins(0, 0, 0, 8);
        linea.setLayoutParams(params);
        layout.addView(linea);
    }
    public void onCompararButtonClick(View view){
        Intent i = new Intent(this, ComparadorProductos.class);
        startActivity(i);
    }
}
