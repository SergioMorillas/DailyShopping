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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.R;
import com.lista.listacompra.modelo.Gestor;
import com.lista.listacompra.modelo.ListaCompra;
import com.lista.listacompra.modelo.SupermercadosDisponibles;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Actividad para crear nuevas listas de compra.
 */
public class CreadorListas extends AppCompatActivity {

    private EditText name;
    private CalendarView calendar;
    private Spinner supermarket;
    private Button accept, cancel;
    private Gestor gestor;
    long fechaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fechaSeleccionada = new Date().getTime();
        setContentView(R.layout.listas_creador);
        gestor = new Gestor(getApplicationContext());
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
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fechaSeleccionada = c.getTimeInMillis();
            }
        });
    }

    /**
     * Crea una nueva lista de compra.
     */
    private void createNewList() {
        String lName = name.getText().toString();
        String lSupermarket = (String) supermarket.getSelectedItem();
        long selectedDate = fechaSeleccionada;

        if (lName.isBlank()) {
            Toast.makeText(CreadorListas.this, "El nombre no puede estar vacío", Toast.LENGTH_LONG).show();
        } else {
            ListaCompra listaCompra = new ListaCompra(lName, selectedDate, lSupermarket, new HashSet());

            Thread t = new Thread(() -> {

                gestor.insertaLista(listaCompra);
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
            }

            name.setText("");
            navigateToListasPrincipal();
            Toast.makeText(getApplicationContext(), "La lista se ha creado correctamente", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Navega a la actividad PrincipalListas.
     */
    private void navigateToListasPrincipal() {
        Intent intent = new Intent(CreadorListas.this, PrincipalListas.class);
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
