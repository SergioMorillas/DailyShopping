package com.lista.listacompra.logica;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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

import java.util.List;

/**
 * Actividad principal que muestra todas las listas de compras existentes.
 */
public class ListaEspecifica extends AppCompatActivity {

    private LinearLayout layout;
    private Button buttonAdd;
    private String nombreLista;
    private List<Producto> lista;
    private String supermercadoNombre;
    private TextView nombreAplicacion;
    private Gestor gestor;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

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
            ListaCompra listaCompra = gestor.getListaPorNombre(nombreLista);
            mHandler.post(() -> añadirObjeto(listaCompra));
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        nombreAplicacion.setText("Lista: " + nombreLista);
    }

    /**
     * @brief Inicializa las vistas de la actividad.
     */
    private void initializeViews() {
    }

    @SuppressLint("ClickableViewAccessibility")
    private void añadirObjeto(@NonNull ListaCompra productos) {
        for (Producto p : productos.getProductos()) {
            LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_lista_individual, null);
            TextView nombre = fila.findViewById(R.id.nombreProducto);
            TextView precio = fila.findViewById(R.id.precioProducto);
            TextView precioKilo = fila.findViewById(R.id.precioPorKilo);
            ImageView imagen = fila.findViewById(R.id.imagenProducto);

            nombre.setText(p.getName());
            precio.setText((String.format("%s€", p.getPrice())));

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

                            float decimoVista = v.getHeight() / 10;
                            float movimientoY = Math.abs(finY - empiezaY);

                            if (movimientoY > decimoVista) {
                                layout.removeView(fila);
                                productos.getProductos().remove(p);
                                new Thread(()->{
                                    gestor.actualizarListaProductos(productos);
                                }).start();

                                Toast.makeText(ListaEspecifica.this, "Se ha borrado el producto " + p.getName(), Toast.LENGTH_SHORT).show();
                            }
                    }
                    return true;
                }
            });
            Picasso.get().load(p.getImage()).into(imagen);
            layout.addView(fila);
        }
    }
}
