package com.lista.listacompra.logica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.R;
import com.lista.listacompra.accesoDatos.baseDatos.ListaCompraBD;
import com.lista.listacompra.modelo.Gestor;
import com.lista.listacompra.modelo.ListaCompra;
import com.lista.listacompra.modelo.SupermercadosDisponibles;

import java.util.ArrayList;

/**
 * Actividad para crear nuevas listas de compra.
 */
public class ListasCreador extends AppCompatActivity {

    private EditText name;
    private CalendarView calendar;
    private Spinner supermarket;
    private Button accept, cancel;
    private Gestor gestor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listas_creador);
        initializeVariables();
        setupListeners();
    }

    /**
     * Inicializa las variables de la actividad.
     */
    private void initializeVariables() {
        name = findViewById(R.id.txtBuscador);
        calendar = findViewById(R.id.cvCalendar);
        supermarket = findViewById(R.id.spnSupermarket);
        accept = findViewById(R.id.buttonCreate);
        cancel = findViewById(R.id.buttonCancel);
        calendar.setDate(System.currentTimeMillis());
        initializeSpinner(supermarket, SupermercadosDisponibles.getStringValues());
    }

    /**
     * Configura los listeners para los botones de la actividad.
     */
    private void setupListeners() {
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewList();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToListasPrincipal();
            }
        });
    }

    /**
     * Crea una nueva lista de compra.
     */
    private void createNewList() {
        String lName = name.getText().toString();
        String lSupermarket = (String) supermarket.getSelectedItem();
        long selectedDate = calendar.getDate();

        if (lName.length() >= 9) {
            Toast.makeText(ListasCreador.this, "El nombre no puede tener más de 9 caracteres", Toast.LENGTH_LONG).show();
        } else if (lName.isBlank()) {
            Toast.makeText(ListasCreador.this, "El nombre no puede estar vacío", Toast.LENGTH_LONG).show();
        } else {
            ListaCompra listaCompra = new ListaCompra(lName, selectedDate, lSupermarket, new ArrayList<>());

            new Thread(() -> {
                gestor.insertaLista(listaCompra);
            }).start();
            name.setText("");
            navigateToListasPrincipal();
            Toast.makeText(getApplicationContext(), "La lista se ha creado correctamente", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Navega a la actividad ListasPrincipal.
     */
    private void navigateToListasPrincipal() {
        Intent intent = new Intent(ListasCreador.this, ListasPrincipal.class);
        startActivity(intent);
    }

    /**
     * Inicializa el spinner con los datos proporcionados.
     *
     * @param spinner El spinner a inicializar.
     * @param data    Los datos para inicializar el spinner.
     */
    public void initializeSpinner(Spinner spinner, String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
