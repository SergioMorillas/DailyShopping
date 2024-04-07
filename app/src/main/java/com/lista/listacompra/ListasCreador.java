package com.lista.listacompra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.persistencia.AppDatabase;
import com.lista.listacompra.persistencia.ListaCompra;
import com.lista.listacompra.supermercado.SupermercadosDisponibles;

import java.util.ArrayList;

public class ListasCreador extends AppCompatActivity {
    private EditText name;
    private CalendarView calendar;
    private Spinner supermarket;
    private Button accept, cancel;
    private AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listas_creador);
        initializeVariables();
        database = AppDatabase.getDatabase(getApplicationContext());
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lName = name.getText().toString();
                String lSupermarket = (String) supermarket.getSelectedItem();
                long selectedDate = calendar.getDate();

                ListaCompra listaCompra = new ListaCompra(lName, selectedDate, lSupermarket, new ArrayList<>());

                new Thread(() -> {
                    database.listaCompraDao().insertListaCompra(listaCompra);
                }).start();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListasCreador.this, ListasPrincipal.class);
                startActivity(intent);
            }
        });
    }

    private void initializeVariables() {
        name = this.findViewById(R.id.etName);
        calendar = this.findViewById(R.id.cvCalendar);
        supermarket = this.findViewById(R.id.spnSupermarket);
        accept = this.findViewById(R.id.buttonCreate);
        cancel = this.findViewById(R.id.buttonCancel);
        calendar.setDate(System.currentTimeMillis()); //Ponemos en el calendario la fecha actual
        initializeSpinner(supermarket, SupermercadosDisponibles.getStringValues());
    }

    public void initializeSpinner(Spinner spinner, String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

}
