package com.lista.listacompra.vista;

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
import com.lista.listacompra.controlador.Gestor;
import com.lista.listacompra.controlador.ListaCompra;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Esta clase representa la actividad principal de la aplicación,
 * la cual muestra todas las listas de compras existentes.
 */
public class PrincipalListasBusqueda extends AppCompatActivity {

    // Variables de clase
    private LinearLayout layout;
    private Button buttonAdd;
    private ImageButton buscar;
    private Gestor gestor;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * Método que se llama cuando la actividad es creada.
     * @param savedInstanceState Datos de estado guardados de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listas_principal);
        initializeViews();
        setupListeners();
        gestor = new Gestor(getApplicationContext());
        String supermercadosBuscar = this.getIntent().getStringExtra("busqueda");
        new Thread(() -> {
            insertarListasEnVistas(gestor.getBusquedaListasNombre(supermercadosBuscar));
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
                Intent intent = new Intent(PrincipalListasBusqueda.this, PrincipalListas.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // Métodos auxiliares

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
                                Toast.makeText(PrincipalListasBusqueda.this, "Se ha borrado la lista " + lista.getNombre(), Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(PrincipalListasBusqueda.this, ListaEspecifica.class);
                                intent.putExtra("nombreLista", lista.getNombre());
                                intent.putExtra("supermercado", lista.getSupermercado());
                                startActivity(intent);
                                finish();
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
     * Añade la información de una lista de compra a las vistas correspondientes.
     * @param lista La lista de compra a mostrar.
     * @param nombreListaTextView TextView donde se mostrará el nombre de la lista.
     * @param supermercadoTextView TextView donde se mostrará el nombre del supermercado.
     * @param fechaTextView TextView donde se mostrará la fecha de la lista.
     */
    private static void anadeVista(ListaCompra lista, TextView nombreListaTextView, TextView supermercadoTextView, TextView fechaTextView) {
        Date fecha = new Date(lista.getFecha());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        nombreListaTextView.setText(lista.getNombre());
        supermercadoTextView.setText(lista.getSupermercado());
        fechaTextView.setText(df.format(fecha));
    }

    /**
     * Borra una lista de compra de la base de datos y de las vistas.
     * @param lista La lista de compra a borrar.
     * @param fila La fila de la lista en la vista.
     * @param linea La línea separadora asociada a la fila.
     */
    private void borrarLista(ListaCompra lista, LinearLayout fila, View linea) {
        new Thread(() -> {
            gestor.borrarLista(lista.getNombre(),
                    lista.getSupermercado(),
                    lista.getFecha());
            mHandler.post(() -> {
                layout.removeView(fila);
                layout.removeView(linea);
            });
        }).start();
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

    private void showViewMenu() {
        Dialog menuDialog = new Dialog(this);
        menuDialog.requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        menuDialog.setContentView(R.layout.popup_busqueda);
        menuDialog.show();
        EditText editTextSearch = menuDialog.findViewById(R.id.editTextSearch);
        Button btnAceptar = menuDialog.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextSearch.getText().toString().trim();
                new Thread(() -> {
                    if (layout.getChildCount() > 0) mHandler.post(() -> layout.removeAllViews());
                    insertarListasEnVistas(gestor.getBusquedaListasNombre(nombre));
                }).start();
                menuDialog.dismiss();
            }
        });
    }

    /**
     * Navega a la actividad CreadorListas.
     */
    private void navigateToListasCreador()
    {
        Intent intent = new Intent(this, CreadorListas.class);
        startActivity(intent);
        finish();
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
     * Método llamado cuando se hace clic en el botón "Comparar".
     * @param view La vista del botón.
     */
    public void onCompararButtonClick(View view) {
        Intent i = new Intent(this, ComparadorProductos.class);
        startActivity(i);
        finish();
    }

    /**
     * Método llamado cuando se hace clic en el botón "Listas".
     * @param view La vista del botón.
     */
    public void onListasButtonClick(View view) {
        Intent i = new Intent(this, PrincipalListasBusqueda.class);
        startActivity(i);
        finish();
    }

    /**
     * Método llamado cuando se hace clic en el botón de la barra lateral.
     * @param view La vista del botón.
     */
    public void onSideBarClick(View view) {
        showMenuDialog();
    }

    /**
     * Método llamado cuando se hace clic en el botón "Juego".
     * @param view La vista del botón.
     */
    public void onJuegoButtonClick(View view) {
        Intent i = new Intent(this, JuegoPrecios.class);
        startActivity(i);
        finish();
    }

}
