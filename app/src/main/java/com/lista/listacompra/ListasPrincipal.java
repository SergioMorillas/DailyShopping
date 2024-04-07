package com.lista.listacompra;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.persistencia.AppDatabase;
import com.lista.listacompra.persistencia.ListaCompra;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;

public class ListasPrincipal extends AppCompatActivity {
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listas_principal);
        AppDatabase database = AppDatabase.getDatabase(this.getApplicationContext());
        ArrayList<ListaCompra> listas = (ArrayList<ListaCompra>)database.listaCompraDao().getAllListasCompra().getValue();
        layout = findViewById(R.id.linearLayout);
        insertarListasEnVistas(listas);
    }
    private void insertarListasEnVistas(ArrayList<ListaCompra> listas) {
        for (ListaCompra lista : listas) {
            LinearLayout fila = (LinearLayout) getLayoutInflater().inflate(R.layout.productos_lista, null);

            TextView nombreListaTextView = fila.findViewById(R.id.nombreLista);
            TextView supermercadoTextView = fila.findViewById(R.id.supermercado);
            TextView fechaTextView = fila.findViewById(R.id.fecha);

            nombreListaTextView.setText(lista.getNombre());
            supermercadoTextView.setText(lista.getSupermercado());
            fechaTextView.setText(new Date(lista.getFecha()).toString()); // Suponiendo que getFecha() devuelve un objeto LocalDate

            layout.addView(fila);

        }
    }

}
