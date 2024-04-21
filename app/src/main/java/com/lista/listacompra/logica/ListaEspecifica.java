package com.lista.listacompra.logica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_especifica);
        nombreLista = this.getIntent().getStringExtra("nombreLista");
        supermercadoNombre = this.getIntent().getStringExtra("supermercado");

        initializeViews();
        ListaCompra listaCompra = gestor.getListaPorNombre(nombreLista);
        lista = listaCompra.getProductos();
        nombreAplicacion.setText("Lista: "+ nombreLista);
    }

    /**
     * @brief Inicializa las vistas de la actividad.
     */
    private void initializeViews() {
        layout = findViewById(R.id.linearLayout);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                Intent i = new Intent(ListaEspecifica.this, BuscadorProductos.class);
                b.putString("supermercado", supermercadoNombre);
                i.putExtras(b);
                startActivity(i);
            }
        });
        nombreAplicacion = findViewById(R.id.nombreAplicacion);
    }

    private void añadirObjeto(List<Producto> productos) {
        for (Producto p : productos) {
            LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_lista, null);
            TextView nombre = fila.findViewById(R.id.nombreProducto);
            TextView precio = fila.findViewById(R.id.precioProducto);
            TextView precioKilo = fila.findViewById(R.id.precioPorKilo);
            ImageView imagen = fila.findViewById(R.id.imageProducto);

            nombre.setText(p.getName());

            precio.setText((String.format("%s€", p.getPrice())));

            if (p.getPricePerKilo() == -1) precioKilo.setText("No aplica");
            else precioKilo.setText(p.getPricePerKilo() + "€/kilo");

            Picasso.get().load(p.getImage()).into(imagen);

            layout.addView(fila);
        }

    }

}
