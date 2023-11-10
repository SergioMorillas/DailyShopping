package com.lista.listacompra;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatButton;

public class BotonDeslizable extends AppCompatButton {

    public BotonDeslizable(Context context) {
        super(context);
        init();
    }

    public BotonDeslizable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BotonDeslizable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setText("Desliza");

        setOnTouchListener((v, event) -> {
            int action = event.getAction();
            float x = event.getX();

            // Calcula el porcentaje del ancho del botón donde se encuentra el evento
            float percentage = x / getWidth();

            // Limita el porcentaje a estar entre 0 y 1
            percentage = Math.max(0, Math.min(1, percentage));

            // Calcula el color interpolando entre verde y rojo
            int green = (int) (255 * (1 - percentage));
            int red = (int) (255 * percentage);

            setBackgroundColor(Color.rgb(red, green, 0));

            return true;
        });
    }
}
