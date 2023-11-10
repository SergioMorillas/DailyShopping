package com.lista.listacompra;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class Listas extends AppCompatActivity {
    Button buttonAdd;
    LinearLayout layout;
    ActivityResultLauncher<Intent> launcher;
    private float x1, x2;
    static final int MIN_DISTANCE = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listas);
        buttonAdd = findViewById(R.id.buttonAdd);
        layout = findViewById(R.id.layout);

        initializeLauncher();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::manejarResultadoActividad);
    }

    /**
     * Maneja el resultado de la actividad lanzada.
     *
     * @param result El resultado de la actividad lanzada.
     */
    private void manejarResultadoActividad(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) { // Si el resultado es ok creamos el text view con los datos de la lista, la linea vacia y configuramos el movimiento de la misma
            TextView miLista = crearTextView(result.getData().getStringExtra("nombreLista"));
            View linea = crearVistaLinea("#BEE9E8");
            configurarTouchListener(miLista, linea);
            agregarVistasAlLayout(miLista, linea);
        } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
            mostrarToast("Cancelado");
        } else {
            mostrarToast("No se ha podido crear la lista de la compra");
        }
    }

    /**
     * Crea un TextView con el texto proporcionado.
     *
     * @param texto El texto para el TextView.
     * @return El TextView creado.
     */
    private TextView crearTextView(String texto) {
        TextView miLista = new TextView(layout.getContext());
        miLista.setText(texto);
        miLista.setTextSize(20);
        miLista.setClickable(true);
        return miLista;
    }

    /**
     * Crea una línea para separar elementos.
     *
     * @return La vista de línea creada.
     */
    private View crearVistaLinea(String color) {
        View linea = new View(layout.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 2);
        params.setMargins(0, 0, 0, 25);
        linea.setLayoutParams(params);
        linea.setBackgroundColor(Color.parseColor(color));
        return linea;
    }

    /**
     * Configuramos el touch listener, para que se pueda deslizar
     *
     * @param miLista
     * @param linea
     */
    @SuppressLint("ClickableViewAccessibility")
    private void configurarTouchListener(TextView miLista, View linea) {
        miLista.setOnTouchListener((v, event) -> {
            manejarAccionesTouch(miLista, linea, event);
            return true;
        });
    }

    /**
     * Creamos los movimientos al pulsar
     *
     * @param miLista TextView que queremos hacer que se mueva
     * @param linea La linea que separa los TextViews
     * @param event El evento de movimiento que realizamos al deslizar
     */
    private void manejarAccionesTouch(TextView miLista, View linea, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                manejarAccionMove(miLista, event);
                break;

            case MotionEvent.ACTION_UP:
                manejarAccionUp(miLista, linea, event);
                break;
        }
    }

    /**
     * Creamos las acciones de movimiento
     * @param miLista TextView que queremos hacer que se mueva
     * @param event El evento de movimiento que realizamos al deslizar
     */
    private void manejarAccionMove(TextView miLista, MotionEvent event) {
        float deltaX = event.getX() - x1;
        miLista.setTranslationX(deltaX);
        float porcentaje = deltaX / miLista.getWidth();

        porcentaje = Math.max(-1, Math.min(1, porcentaje));

        int rojo, verde;
        if (porcentaje > 0) {
            rojo = (int) (150 * Math.abs(porcentaje));
            verde = 0;
        } else {
            rojo = 0;
            verde = (int) (150 * Math.abs(porcentaje));
        }
        miLista.setBackgroundColor(Color.rgb(rojo, verde, 0));
    }

    /**
     * El manejador que se activa al soltar el text view
     * @param miLista TextView que queremos hacer que se mueva
     * @param linea La linea que separa los TextViews
     * @param event El evento de movimiento que realizamos al deslizar
     */
    private void manejarAccionUp(TextView miLista, View linea, MotionEvent event) {
        x2 = event.getX();
        float totalDeltaX = x2 - x1;
        if (Math.abs(totalDeltaX) > MIN_DISTANCE) {
            removerVistas(miLista, linea);
        } else {
            resetear(miLista);
        }
    }

    /**
     * En caso de que hayamos pasado del limite borra la vista
     * @param miLista TextView que queremos hacer que se mueva
     * @param linea La linea que separa los TextViews
     */
    private void removerVistas(TextView miLista, View linea) {
        layout.removeView(miLista);
        layout.removeView(linea);
        mostrarToast("Se ha eliminado la lista\n" + miLista.getText());
    }

    /**
     * Si no hemos pasado del limite resetea la transicion y los colores
     * @param miLista
     */
    private void resetear(TextView miLista) {
        ValueAnimator resetAnimator = ValueAnimator.ofFloat(miLista.getTranslationX(), 0);
        resetAnimator.setDuration(300);
        resetAnimator.addUpdateListener(animation -> {
            miLista.setTranslationX((float) animation.getAnimatedValue());
        });
        resetAnimator.start();

        miLista.setBackgroundColor(Color.TRANSPARENT);
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void agregarVistasAlLayout(TextView miLista, View linea) {
        layout.addView(miLista);
        layout.addView(linea);
    }


    public void nuevaLista(View view) {
        Intent i = new Intent(this, CreaLista.class);
        launcher.launch(i);
    }
}
