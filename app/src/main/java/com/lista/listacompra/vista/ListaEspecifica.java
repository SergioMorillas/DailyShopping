package com.lista.listacompra.vista;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.R;
import com.lista.listacompra.controlador.Gestor;
import com.lista.listacompra.controlador.ListaCompra;
import com.lista.listacompra.controlador.Producto;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * Actividad principal que muestra todas las listas de compras existentes.
 */
public class ListaEspecifica extends AppCompatActivity {

    private LinearLayout layout;
    private Button buttonAdd;
    private String nombreLista;
    private String supermercadoNombre;
    private TextView nombreAplicacion;
    private Gestor gestor;
    private ListaCompra productos;
    private long fecha;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * Método heredado de AppCompatActivity, se llama cuando la actividad se está deteniendo.
     * Actualiza la lista de productos para asegurar que los productos mantienen la cantidad y el marcado.
     */
    @Override
    protected void onStop() {
        new Thread(() -> {
            gestor.actualizarListaProductos(productos);
        }).start();
        super.onStop();
    }

    /**
     * Método llamado al crear la actividad.
     *
     * @param savedInstanceState Si la actividad está siendo reinicializada después de haber sido detenida,
     *                           este Bundle contiene los datos que más recientemente ha suministrado en {@link #onSaveInstanceState}.
     *                           De lo contrario, es nulo.
     *                           Instancia los elementos de la vista, lee los valores pasados como parámetros a la vista
     *                           (como el supermercado y el nombre de la lista), y crea los métodos onClick de la vista.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_especifica);
        nombreLista = this.getIntent().getStringExtra("nombreLista");
        supermercadoNombre = this.getIntent().getStringExtra("supermercado");
        fecha = this.getIntent().getLongExtra("fecha", -1); // Si no hay fecha (imposible), por defecto es -1.
        layout = findViewById(R.id.layout);
        buttonAdd = findViewById(R.id.buttonAdd);
        nombreAplicacion = findViewById(R.id.nombreAplicacion);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                Intent i = new Intent(ListaEspecifica.this, BuscadorProductos.class);
                b.putString("supermercado", supermercadoNombre);
                b.putString("nombreLista", nombreLista);
                b.putLong("fecha", fecha);
                i.putExtras(b);
                startActivity(i);
                new Thread(() -> {
                    gestor.actualizarListaProductos(productos);
                }).start();
            }
        });

        Thread t = new Thread(() -> {
            gestor = new Gestor(getApplicationContext());
            productos = gestor.getListaPorNombre(nombreLista, supermercadoNombre, fecha);
            mHandler.post(() -> actualizarVista());
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new Thread(() -> {
                    gestor.actualizarListaProductos(productos);
                    Intent intent = new Intent(ListaEspecifica.this, PrincipalListas.class);
                    startActivity(intent);
                }).start();
            }
        });
        nombreAplicacion.setText("Lista: " + nombreLista);
    }

    /**
     * Actualiza la vista de la lista de productos.
     */
    private void actualizarVista() {
        if (layout.getChildCount() > 0) layout.removeAllViews();
        for (Producto p : productos.getProductos()) {
            if (!p.isMarked()) {
                addProduct(p);
            }
        }
        addCentroEspecifico(productos);
        for (Producto p : productos.getProductos()) {
            if (p.isMarked()) {
                addProduct(p);
            }
        }
    }

    /**
     * Método para agregar un producto a la vista.
     *
     * @param p El producto a agregar.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void addProduct(Producto p) {
        View linea = new View(this);
        LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_lista_individual, null);
        TextView nombre = fila.findViewById(R.id.nombreProducto);
        TextView precio = fila.findViewById(R.id.precioProducto);
        TextView precioKilo = fila.findViewById(R.id.precioPorKilo);
        ImageView imagen = fila.findViewById(R.id.imagenProducto);
        nombre.setText(p.getName());
        crearPrecios(fila, p, precio);

        if (p.getPricePerKilo() == -1) precioKilo.setText("No aplica");
        else precioKilo.setText(p.getPricePerKilo() + "€/kilo");

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog menuDialog = new Dialog(ListaEspecifica.this);
                menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                menuDialog.setContentView(R.layout.imagen_lista);

                ImageView image = menuDialog.findViewById(R.id.IVimage);
                String url = p.getImage();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                Window window = menuDialog.getWindow();
                if (window != null) {
                    Picasso.get().load(url).into(image);
                    layoutParams.copyFrom(window.getAttributes());
                    int size = (int) (getResources().getDisplayMetrics().widthPixels * 0.75);
                    layoutParams.width = size;
                    layoutParams.height = size;
                    window.setAttributes(layoutParams);
                }

                menuDialog.show();

            }
        });

        fila.setPadding(
                fila.getPaddingLeft(),
                10,
                fila.getPaddingRight(),
                fila.getPaddingBottom());
        fila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setMarked(!p.isMarked()); // Toggle el marcado del producto.
                productos.getProductos().remove(p);
                productos.getProductos().add(p);
                actualizarVista();
            }
        });
        Picasso.get().load(p.getImage()).into(imagen);
        layout.addView(fila);
        addSeparatorLine(linea);
    }

    /**
     * Crea la vista de precios para un producto.
     *
     * @param fila   La fila en la que se encuentra el producto.
     * @param p      El producto.
     * @param precio El TextView donde se mostrará el precio.
     */
    private void crearPrecios(LinearLayout fila, Producto p, TextView precio) {
        DecimalFormat df = new DecimalFormat("#.##");
        double precioReal;

        Button botonSuma = fila.findViewById(R.id.sumaCantidad);
        Button botonResta = fila.findViewById(R.id.restaCantidad);

        precioReal = p.getAmount() * p.getPrice();
        String precioMostrar = p.getAmount() + "X" + p.getPrice() + "€=" + df.format(precioReal) + "€";

        precio.setText(precioMostrar);

        botonSuma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSumaCantidad(precio, p);
            }
        });
        botonResta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRestaCantidad(precio, p, fila);
            }
        });
    }

    /**
     * Agrega la vista del centro específico a la lista de productos.
     *
     * @param productos La lista de productos.
     */
    private void addCentroEspecifico(ListaCompra productos) {
        LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.centro_especifico, null);
        TextView marcado = fila.findViewById(R.id.tvMarcado);
        TextView sinMarcar = fila.findViewById(R.id.tvSinMarcar);
        TextView total = fila.findViewById(R.id.tvTotal);
        TextView media = fila.findViewById(R.id.tvMedia);

        media.setText(String.format("%s€", productos.getPrecioPromedio()));
        marcado.setText(String.format("%s€", productos.getPrecioMarcado()));
        sinMarcar.setText(String.format("%s€", productos.getPrecioSinMarcar()));
        total.setText(String.format("%s€", productos.getPrecioTotal()));
        layout.addView(fila);
    }

    /**
     * Acción cuando se hace clic en el botón de restar cantidad.
     *
     * @param precio El TextView donde se muestra el precio.
     * @param p      El producto.
     * @param fila   La fila del producto.
     */
    private void onClickRestaCantidad(TextView precio, Producto p, LinearLayout fila) {
        if (p.getAmount() == 1) {
            productos.getProductos().remove(p);
            layout.removeView(fila);
        }
        p.setAmount(p.getAmount() - 1);
        DecimalFormat df = new DecimalFormat("#.##");
        double precioReal;
        precioReal = p.getAmount() * p.getPrice();
        String precioMostrar = p.getAmount() + "X" + p.getPrice() + "€=" + df.format(precioReal) + "€";
        precio.setText(precioMostrar);
        actualizarVista();
    }

    /**
     * Acción cuando se hace clic en el botón de sumar cantidad.
     *
     * @param precio El TextView donde se muestra el precio.
     * @param p      El producto.
     */
    private void onClickSumaCantidad(TextView precio, Producto p) {
        p.setAmount(p.getAmount() + 1);

        DecimalFormat df = new DecimalFormat("#.##");
        double precioReal;
        precioReal = p.getAmount() * p.getPrice();
        String precioMostrar = p.getAmount() + "X" + p.getPrice() + "€=" + df.format(precioReal) + "€";
        precio.setText(precioMostrar);
        actualizarVista();
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
     * Agrega una línea separadora a la vista.
     *
     * @param linea La línea a agregar.
     */
    private void addSeparatorLine(View linea) {
        linea.setBackgroundColor(Color.LTGRAY);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4);
        params.setMargins(0, 5, 0, 5);
        linea.setLayoutParams(params);
        layout.addView(linea);
    }
}