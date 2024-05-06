package com.lista.listacompra.logica;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.R;
import com.lista.listacompra.modelo.Gestor;
import com.lista.listacompra.modelo.ListaCompra;
import com.lista.listacompra.modelo.Producto;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;


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

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * Metodo heredado de AppCompat, sirve para ejecutar una acción al parar la acción actual,
     * por ejemplo al cambiar de pantalla o al cerrar la aplicación.
     * Actualiza la lista de productos para asegurar que los productos mantienen la cantidad y el
     * marcado
     */
    @Override
    protected void onStop() {
        new Thread(() -> {
            gestor.actualizarListaProductos(productos);
        }).start();
        super.onStop();
    }

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *                           Aquí instanciamos los items de la vista para poder acceder a ellos, leemos los valores pasados como
     *                           parametro a la vista, como con el supermercado y el nombre de la lista, y creamos los metodos
     *                           onClick de la vista
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_especifica);
        nombreLista = this.getIntent().getStringExtra("nombreLista");
        supermercadoNombre = this.getIntent().getStringExtra("supermercado");
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
                i.putExtras(b);
                startActivity(i);
            }
        });

        Thread t = new Thread(() -> {
            gestor = new Gestor(getApplicationContext());
            productos = gestor.getListaPorNombre(nombreLista);
            mHandler.post(() -> actualizarVista());
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        nombreAplicacion.setText("Lista: " + nombreLista);
    }

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

    @SuppressLint("ClickableViewAccessibility")
    private void addProduct(Producto p) {
        LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_lista_individual, null);
        TextView nombre = fila.findViewById(R.id.nombreProducto);
        TextView precio = fila.findViewById(R.id.precioProducto);
        TextView precioKilo = fila.findViewById(R.id.precioPorKilo);
        ImageView imagen = fila.findViewById(R.id.imagenProducto);
        nombre.setText(p.getName());
        crearPrecios(fila, p, precio);

        if (p.getPricePerKilo() == -1) precioKilo.setText("No aplica");
        else precioKilo.setText(p.getPricePerKilo() + "€/kilo");

        fila.setOnTouchListener(new View.OnTouchListener() {
            private float empiezaY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        empiezaY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        float finY = event.getY();

                        float veinteavo = v.getHeight() / 20;
                        float movimientoY = Math.abs(finY - empiezaY);

                        if (movimientoY > veinteavo) {
                            productos.getProductos().remove(p);
                            actualizarVista();

                            Toast.makeText(ListaEspecifica.this, "Se ha borrado el " +
                                    "producto " + p.getName(), Toast.LENGTH_SHORT).show();
                        } else if (movimientoY == 0) {
                            p.setMarked(!p.isMarked()); // Hacemos una puerta not sobre si esta marcado
                            productos.getProductos().remove(p);
                            productos.getProductos().add(p);
                            actualizarVista();
                        }
                }
                return true;
            }
        });
        Picasso.get().load(p.getImage()).into(imagen);
        layout.addView(fila);
    }

    private void crearPrecios(LinearLayout fila, Producto p, TextView precio) {
        DecimalFormat df = new DecimalFormat("#.##");
        double precioReal;

        Button botonSuma = fila.findViewById(R.id.sumaCantidad);
        Button botonResta = fila.findViewById(R.id.restaCantidad);

        precioReal = p.getAmount() * p.getPrice();
        String precioMostrar = p.getAmount() + "X" + p.getPrice() + "€=" + df.format(precioReal) +"€";

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

    private void addCentroEspecifico(ListaCompra productos) {
        LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.centro_especifico, null);
        TextView marcado = fila.findViewById(R.id.tvMarcado);
        TextView sinMarcar = fila.findViewById(R.id.tvSinMarcar);
        TextView total = fila.findViewById(R.id.tvTotal);

        marcado.setText(String.format("%s€", productos.getPrecioMarcado()));
        sinMarcar.setText(String.format("%s€", productos.getPrecioSinMarcar()));
        total.setText(String.format("%s€", productos.getPrecioTotal()));
        layout.addView(fila);
    }



    private void onClickRestaCantidad(TextView precio, Producto p, LinearLayout fila) {
        if (p.getAmount()==1){
            productos.getProductos().remove(p);
            layout.removeView(fila);
        }
        p.setAmount(p.getAmount() - 1);
        new Thread(() -> {
            gestor.actualizarListaProductos(productos);
        }).start();

        DecimalFormat df = new DecimalFormat("#.##");
        double precioReal;
        precioReal = p.getAmount() * p.getPrice();
        String precioMostrar = p.getAmount() + "X" + p.getPrice() + "€=" + df.format(precioReal) +"€";
        precio.setText(precioMostrar);
        actualizarVista();
    }

    private void onClickSumaCantidad(TextView precio, Producto p) {
        p.setAmount(p.getAmount() + 1);
        new Thread(() -> {
            gestor.actualizarListaProductos(productos);
        }).start();

        DecimalFormat df = new DecimalFormat("#.##");
        double precioReal;
        precioReal = p.getAmount() * p.getPrice();
        String precioMostrar = p.getAmount() + "X" + p.getPrice() + "€=" + df.format(precioReal) +"€";
        precio.setText(precioMostrar);
        actualizarVista();

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

    public void onSideBarClick(View view) {
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

