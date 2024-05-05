package com.lista.listacompra.logica;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
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
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * Aquí instanciamos los items de la vista para poder acceder a ellos, leemos los valores pasados como
     * parametro a la vista, como con el supermercado y el nombre de la lista, y creamos los metodos
     * onClick de la vista
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
            mHandler.post(() -> añadirObjeto());
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        nombreAplicacion.setText("Lista: " + nombreLista);
    }

    private void añadirObjeto() {
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
        EditText cantidad = fila.findViewById(R.id.cantidad);

        nombre.setText(p.getName());
        precio.setText((String.format("%s€", p.getPrice())));

        if (p.getPricePerKilo() == -1) precioKilo.setText("No aplica");
        else precioKilo.setText(p.getPricePerKilo() + "€/kilo");

        cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { //Al cambiarlo
                int cantidad = p.getAmount();
                String sCantidad = s.toString();
                try {
                    cantidad = Integer.parseInt(sCantidad);
                    if (String.valueOf(cantidad).equals(sCantidad)) {
                        Toast.makeText(ListaEspecifica.this,
                                        String.format("No se ha podido setear %s como cantidad, no puede ser un numero decimal", sCantidad),
                                        Toast.LENGTH_LONG)
                                .show();
                    }
                } catch (NumberFormatException formato) {
                    Toast.makeText(ListaEspecifica.this,
                                    String.format("No se ha podido setear %s como cantidad, no es un numero", sCantidad),
                                    Toast.LENGTH_LONG)
                            .show();
                }
                productos.getProductos().remove(p);
                p.setAmount(cantidad);
                productos.getProductos().add(p);
            }

            @Override
            public void afterTextChanged(Editable s) { //Despues de cambiarlo

            }
        });
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
                            añadirObjeto();

                            Toast.makeText(ListaEspecifica.this, "Se ha borrado el " +
                                    "producto " + p.getName(), Toast.LENGTH_SHORT).show();
                        } else if (movimientoY == 0) {
                            p.setMarked(!p.isMarked()); // Hacemos una puerta not sobre si esta marcado
                            productos.getProductos().remove(p);
                            productos.getProductos().add(p);
                            añadirObjeto();
                        }
                }
                return true;
            }
        });
        Picasso.get().load(p.getImage()).into(imagen);
        layout.addView(fila);
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
}
