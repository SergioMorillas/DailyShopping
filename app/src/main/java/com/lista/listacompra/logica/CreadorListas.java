package com.lista.listacompra.logica;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lista.listacompra.R;
import com.lista.listacompra.modelo.Gestor;
import com.lista.listacompra.modelo.ListaCompra;
import com.lista.listacompra.modelo.SupermercadosDisponibles;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;

/**
 * Actividad para crear nuevas listas de compra.
 */
public class CreadorListas extends AppCompatActivity {

    private EditText name;
    private CalendarView calendar;
    private Spinner supermarket;
    private Button accept, cancel;
    private Gestor gestor;
    private long fechaSeleccionada;
    private final Handler mHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establecer configuración local en español
        Locale locale = new Locale("es");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Configurar la fecha actual y establecer la vista
        setFechaActual();
        setContentView(R.layout.listas_creador);
        gestor = new Gestor(getApplicationContext());
        initializeVariables();
        setupListeners();
        // Configurar el botón de retroceso
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToListasPrincipal();
            }
        });

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
                // Seteamos horas minutos y segundos a 0, para que la
                // fecha sea solo el dia y bajarle la precision
                setFecha(year, month, dayOfMonth);
            }

        });
    }

    // Métodos auxiliares para trabajar con fechas

    private void setFechaActual() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        setFecha(year, month, dayOfMonth);
    }

    private void setFecha(int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        fechaSeleccionada = c.getTimeInMillis();
    }

    private long getFechaActual() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
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
        } else if (fechaSeleccionada < getFechaActual()) { // Si la fecha es anterior al día de hoy falla
            Toast.makeText(CreadorListas.this, "La fecha no puede ser anterior al día de hoy", Toast.LENGTH_LONG).show();
            calendar.setDate(getFechaActual());
            fechaSeleccionada = getFechaActual();
        } else {
            ListaCompra listaCompra = new ListaCompra(lName, selectedDate, lSupermarket, new HashSet());

            Thread t = new Thread(() -> {
                try {
                    // Si el get lista devuelve null significa que no existe, por lo tanto podemos crearlo
                    ListaCompra listaCompra1 = gestor.getListaPorNombre(lName, lSupermarket, selectedDate);
                    mHandler.post(() -> Toast.makeText(CreadorListas.this,
                            "Ya existe una lista con esos datos",
                            Toast.LENGTH_LONG).show());
                } catch (NullPointerException npe) {
                    gestor.insertaLista(listaCompra);
                    mHandler.post(() -> Toast.makeText(getApplicationContext(),
                            "La lista se ha creado correctamente",
                            Toast.LENGTH_LONG).show());
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
            }

            name.setText("");
            navigateToListasPrincipal();
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
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            spinner.setBackgroundColor(Color.WHITE);
        }
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
     * Maneja el clic en la barra lateral para mostrar el menú.
     * @param view La vista del botón de la barra lateral.
     */
    public void onSideBarClick(View view) {
        showMenuDialog();
    }

    /**
     * Maneja el clic en el botón de comparar productos.
     * @param view La vista del botón de comparar productos.
     */
    public void onCompararButtonClick(View view) {
        Intent i = new Intent(this, ComparadorProductos.class);
        startActivity(i);
    }

    /**
     * Maneja el clic en el botón de listas.
     * @param view La vista del botón de listas.
     */
    public void onListasButtonClick(View view) {
        Intent i = new Intent(this, PrincipalListas.class);
        startActivity(i);
    }

    /**
     * Maneja el clic en el botón de juego.
     * @param view La vista del botón de juego.
     */
    public void onJuegoButtonClick(View view) {
        Intent i = new Intent(this, JuegoPrecios.class);
        startActivity(i);
    }
}

