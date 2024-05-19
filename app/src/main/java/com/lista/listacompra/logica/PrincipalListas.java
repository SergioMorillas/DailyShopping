package com.lista.listacompra.logica;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.R;
import com.lista.listacompra.modelo.Gestor;
import com.lista.listacompra.modelo.ListaCompra;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Actividad principal que muestra todas las listas de compras existentes.
 */
public class PrincipalListas extends AppCompatActivity {

    private LinearLayout layout;
    private Button buttonAdd;
    private ImageButton buscar;
    private Gestor gestor;
    private final Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listas_principal);
        initializeViews();
        setupListeners();
        gestor = new Gestor(getApplicationContext());
        new Thread(() -> {
            insertarListasEnVistas(gestor.getTodaslistas());
        }).start();
        buscar = findViewById(R.id.search);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showViewMenu();
            }
        });
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        });
    }

    /**
     * Inicializa las vistas de la actividad.
     */
    private void initializeViews() {
        layout = findViewById(R.id.linearLayout);
        buttonAdd = findViewById(R.id.buttonAdd);
    }

    /**
     * Configura los listeners para los botones de la actividad.
     */
    private void setupListeners() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToListasCreador();
            }
        });
    }


    /**
     * Inserta las listas de compras en las vistas de la actividad.
     *
     * @param listas Lista de compras a insertar.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void insertarListasEnVistas(Set<ListaCompra> listas) {
        for (ListaCompra lista : listas) {
            View linea = new View(this);
            LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_lista, null);

            TextView nombreListaTextView = fila.findViewById(R.id.nombreLista);
            TextView supermercadoTextView = fila.findViewById(R.id.supermercadoProducto);
            TextView fechaTextView = fila.findViewById(R.id.fecha);

            anadeVista(lista, nombreListaTextView, supermercadoTextView, fechaTextView);

            fila.setOnTouchListener(new View.OnTouchListener() {
                private float empiezaY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            empiezaY = event.getY();
                            break;

                        case MotionEvent.ACTION_UP:
                            float finY = event.getY();

                            float decimoVista = v.getHeight() / 10;
                            float movimientoY = Math.abs(finY - empiezaY);

                            if (movimientoY > decimoVista) {
                                borrarLista(lista, fila, linea);
                                Toast.makeText(PrincipalListas.this, "Se ha borrado la lista " + lista.getNombre(), Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(PrincipalListas.this, ListaEspecifica.class);
                                intent.putExtra("nombreLista", lista.getNombre());
                                intent.putExtra("supermercado", lista.getSupermercado());
                                intent.putExtra("fecha", lista.getFecha());
                                startActivity(intent);
                            }
                            break;
                    }
                    return true;
                }
            });


            mHandler.post(() -> layout.addView(fila));
            mHandler.post(() -> addSeparatorLine(linea));
        }
    }

    /**
     * Configura la vista de la lista de compra.
     *
     * @param lista                Lista de compra.
     * @param nombreListaTextView  TextView para el nombre de la lista.
     * @param supermercadoTextView TextView para el supermercado.
     * @param fechaTextView        TextView para la fecha.
     */
    private static void anadeVista(ListaCompra lista, TextView nombreListaTextView, TextView supermercadoTextView, TextView fechaTextView) {
        Date fecha = new Date(lista.getFecha());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        nombreListaTextView.setText(lista.getNombre());
        supermercadoTextView.setText(lista.getSupermercado());
        fechaTextView.setText(df.format(fecha));
    }

    /**
     * Borra una lista de compra.
     *
     * @param lista Lista de compra a borrar.
     * @param fila  Fila correspondiente a la lista.
     * @param linea Línea correspondiente a la lista.
     */
    private void borrarLista(ListaCompra lista, LinearLayout fila, View linea) {
        new Thread(() -> {
            gestor.borrarLista(lista.getNombre(), lista.getSupermercado(), lista.getFecha());
            mHandler.post(() -> {
                layout.removeView(fila);
                layout.removeView(linea);
            });
        }).start();
    }

    /**
     * Muestra un diálogo para la búsqueda.
     */
    private void showViewMenu() {
        Dialog menuDialog = new Dialog(this);
        menuDialog.requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        menuDialog.setContentView(R.layout.popup_busqueda);
        menuDialog.show();
        EditText editTextSearch = menuDialog.findViewById(R.id.editTextSearch);
        Button btnAceptar = menuDialog.findViewById(R.id.btnAceptar);
        Button btnCancelar = menuDialog.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDialog.dismiss();
            }
        });
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextSearch.getText().toString().trim();
                new Thread(() -> {
                    Intent i = new Intent(PrincipalListas.this, PrincipalListasBusqueda.class);
                    i.putExtra("busqueda", nombre);
                    startActivity(i);
                }).start();
                menuDialog.dismiss();
            }
        });
    }

    /**
     * Navega a la actividad CreadorListas.
     */
    private void navigateToListasCreador() {
        Intent intent = new Intent(this, CreadorListas.class);
        startActivity(intent);
    }

    /**
     * Añade una línea separadora a la disposición lineal.
     */
    private void addSeparatorLine(View linea) {

        linea.setBackgroundColor(Color.LTGRAY);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4);
        params.setMargins(0, 5, 0, 5);
        linea.setLayoutParams(params);
        layout.addView(linea);
    }

    /**
     * Acción al hacer clic en el botón de comparar productos.
     *
     * @param view La vista.
     */
    public void onCompararButtonClick(View view) {
        Intent i = new Intent(this, ComparadorProductos.class);
        startActivity(i);
    }

    /**
     * Acción al hacer clic en el botón de listas.
     *
     * @param view La vista.
     */
    public void onListasButtonClick(View view) {
        Intent i = new Intent(this, PrincipalListas.class);
        startActivity(i);
    }

    /**
     * Acción al hacer clic en el botón de juego de precios.
     *
     * @param view La vista.
     */
    public void onJuegoButtonClick(View view) {
        Intent i = new Intent(this, JuegoPrecios.class);
        startActivity(i);
    }

    /**
     * Muestra un diálogo de menú.
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

    /**
     * Acción al hacer clic en la barra lateral.
     *
     * @param view La vista.
     */
    public void onSideBarClick(View view) {
        showMenuDialog();
    }
}
