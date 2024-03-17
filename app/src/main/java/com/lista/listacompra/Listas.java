package com.lista.listacompra;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class Listas extends AppCompatActivity {
    Button buttonAdd;
    LinearLayout layout;
    ActivityResultLauncher<Intent> launcher;
    private float x1, x2;
    static final int MIN_DISTANCE = 150;

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
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                TextView miLista = new TextView(layout.getContext());
                miLista.setText(result.getData().getStringExtra("nombreLista"));
                miLista.setTextSize(20);
                miLista.setClickable(true);
                View lineaPlateada = new View(layout.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                params.setMargins(0, 0, 0, 25);
                lineaPlateada.setLayoutParams(params);
                lineaPlateada.setBackgroundColor(Color.parseColor("#BEE9E8"));

                miLista.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                x1 = event.getX();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                float deltaX = event.getX() - x1;

                                ObjectAnimator animator = ObjectAnimator.ofFloat(miLista, "translationX", deltaX);
                                animator.setDuration(0);
                                animator.start();
                                float percentage = deltaX / v.getWidth();
                                percentage = Math.max(-1, Math.min(1, percentage));
                                int green = (int) (255 * (1 - Math.abs(percentage+25)));
                                int red = (int) (255 * Math.abs(percentage+25));
                                miLista.setBackgroundColor(Color.rgb(red, green, 0));

                                break;
                            case MotionEvent.ACTION_UP:
                                x2 = event.getX();
                                float totalDeltaX = x2 - x1;
                                if (Math.abs(totalDeltaX) > MIN_DISTANCE) {
                                    layout.removeView(miLista);
                                    layout.removeView(lineaPlateada);
                                } else if (Math.abs(totalDeltaX) == 0){
                                    Intent i = new Intent(getBaseContext(), Productos.class);
                                    launcher.launch(i);
                                }
                                ObjectAnimator resetAnimator = ObjectAnimator.ofFloat(miLista, "translationX", 0);
                                resetAnimator.setDuration(1000); // Duración de la animación de regreso
                                resetAnimator.start();
                                miLista.setBackgroundColor(Color.TRANSPARENT);
                                break;
                        }
                        return true;
                    }
                });
                layout.addView(miLista);
                layout.addView(lineaPlateada);

            } else if (result.getResultCode() == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "No se ha podido crear la lista de la compra", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void nuevaLista(View view) {
        Intent i = new Intent(this, CreaLista.class);
        launcher.launch(i);
    }
}
