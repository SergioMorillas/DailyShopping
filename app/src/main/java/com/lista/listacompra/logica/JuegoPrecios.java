package com.lista.listacompra.logica;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.github.javafaker.Faker;
import com.lista.listacompra.R;
import com.lista.listacompra.modelo.Producto;
import com.lista.listacompra.modelo.SupermercadosDisponibles;
import com.lista.listacompra.modelo.SupermercadosFactoria;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Actividad que representa el juego de adivinar el precio de un producto.
 */
public class JuegoPrecios extends AppCompatActivity {
    private static final double TOLERANCIA = 0.25; // Tolerancia de un 25%
    private Faker faker;
    private double precio;
    private EditText primerIntento, segundoIntento, tercerIntento;
    private ImageView imagenProducto, imagenPrimerIntento, imagenSegundoIntento, imagenTercerIntento, menu;
    private TextView nombreSupermercado, cuadroFinal;
    private Button comprobar, nuevoIntento;
    private ProgressBar loadingCircle;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.juego_precios);
        super.onCreate(savedInstanceState);
        faker = new Faker();
        instancias();
        new Thread (()->{
            SupermercadosFactoria superM = supermercadoAleatorio();
            Producto p = productoAleatorio(superM);

            mHandler.post(() -> {

                precio = p.getPrice();

                cuadroFinal.setVisibility(View.VISIBLE);
                nombreSupermercado.setText(superM.getNombre());
                Picasso.get().load(p.getImage()).into(imagenProducto);
                comprobar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stringPrecioIntento;
                        double precioIntento;
                        //Si es el primer intento
                        if (primerIntento.isEnabled()) primerIntento(p);
                        else if (!tercerIntento.isEnabled()) segundoIntento(p);
                        else tercerIntento(p);
                    }
                });
            });

        }).start();
        nuevoIntento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JuegoPrecios.this, JuegoPrecios.class);
                startActivity(i);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuDialog();
            }
        });
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(JuegoPrecios.this, PrincipalListas.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Inicializa las instancias de las vistas.
     */
    private void instancias() {
        primerIntento = findViewById(R.id.primerIntento);
        segundoIntento = findViewById(R.id.segundoIntento);
        tercerIntento = findViewById(R.id.tercerIntento);
        imagenProducto = findViewById(R.id.producto);
        imagenPrimerIntento = findViewById(R.id.imagenPrimerIntento);
        imagenSegundoIntento = findViewById(R.id.imagenSegundoIntento);
        imagenTercerIntento = findViewById(R.id.imagenTercerIntento);
        nombreSupermercado = findViewById(R.id.nombreSupermercado);
        cuadroFinal = findViewById(R.id.cuadroFinal);
        comprobar = findViewById(R.id.comprobar);
        nuevoIntento = findViewById(R.id.nuevo_intento);
        menu = findViewById(R.id.menu);
        loadingCircle = findViewById(R.id.loadingCircle);
    }

    /**
     * Lógica del primer intento del usuario para adivinar el precio.
     *
     * @param p Producto aleatorio generado para el juego.
     */
    private void primerIntento(Producto p) {
        double precioIntento;
        String stringPrecioIntento;
        try {
            stringPrecioIntento = primerIntento.getText().toString();
            stringPrecioIntento = stringPrecioIntento.replaceAll(",", ".");
            precioIntento = Double.parseDouble(stringPrecioIntento);

            int acierto = comprobarPrecioUsuario(precioIntento);
            if (acierto == 0) {
                Toast.makeText(JuegoPrecios.this, "El precio real es mas alto", Toast.LENGTH_LONG).show();
                imagenPrimerIntento.setImageResource(R.drawable.icono_flecha_arriba);
            } else if (acierto == 1) {
                Toast.makeText(JuegoPrecios.this, "El precio real es mas bajo", Toast.LENGTH_LONG).show();
                imagenPrimerIntento.setImageResource(R.drawable.icono_flecha_abajo);
            } else if (acierto == 2) {
                Toast.makeText(JuegoPrecios.this, "Has acertado el precio exacto, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(187, 165, 61)); //Dorado
                imagenPrimerIntento.setImageResource(R.drawable.icono_estrella_ganar_exacto);
                cuadroFinal.setText("Has acertado el precio exacto");
            } else {
                Toast.makeText(JuegoPrecios.this, "¡Bien! Te has quedado muy cerca, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(192, 192, 192)); //Plateado
                imagenPrimerIntento.setImageResource(R.drawable.icono_estrella_ganar_tolerancia);
                cuadroFinal.setText("¡Bien! Te has quedado muy cerca, el precio era " + p.getPrice() + "€");
            }
            if (acierto == 0 || acierto == 1) {
                segundoIntento.setEnabled(true);
            } else {
                comprobar.setEnabled(false);
            }
            primerIntento.setEnabled(false);
        } catch (NumberFormatException ex) {
            Toast.makeText(JuegoPrecios.this, "El valor introducido no es un numero", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Lógica del primer intento del usuario para adivinar el precio.
     *
     * @param p Producto aleatorio generado para el juego.
     */
    private void segundoIntento(Producto p) {
        double precioIntento;
        String stringPrecioIntento;
        try {
            stringPrecioIntento = segundoIntento.getText().toString();
            stringPrecioIntento = stringPrecioIntento.replaceAll(",", ".");
            precioIntento = Double.parseDouble(stringPrecioIntento);

            int acierto = comprobarPrecioUsuario(precioIntento);
            if (acierto == 0) {
                Toast.makeText(JuegoPrecios.this, "El precio real es mas alto", Toast.LENGTH_LONG).show();
                imagenSegundoIntento.setImageResource(R.drawable.icono_flecha_arriba);
            } else if (acierto == 1) {
                Toast.makeText(JuegoPrecios.this, "El precio real es mas bajo", Toast.LENGTH_LONG).show();
                imagenSegundoIntento.setImageResource(R.drawable.icono_flecha_abajo);
            } else if (acierto == 2) {
                Toast.makeText(JuegoPrecios.this, "Has acertado el precio exacto, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(187, 165, 61)); //Dorado
                imagenSegundoIntento.setImageResource(R.drawable.icono_estrella_ganar_exacto);
                cuadroFinal.setText("Has acertado el precio exacto");
            } else {
                Toast.makeText(JuegoPrecios.this, "¡Bien! Te has quedado muy cerca, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(192, 192, 192)); //Plateado
                imagenSegundoIntento.setImageResource(R.drawable.icono_estrella_ganar_tolerancia);
                cuadroFinal.setText("¡Bien! Te has quedado muy cerca, el precio era " + p.getPrice() + "€");
            }
            if (acierto == 0 || acierto == 1) {
                tercerIntento.setEnabled(true);
            } else {
                comprobar.setEnabled(false);
            }
            segundoIntento.setEnabled(false);
        } catch (NumberFormatException ex) {
            Toast.makeText(JuegoPrecios.this, "El valor introducido no es un numero", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Lógica del segundo intento del usuario para adivinar el precio.
     *
     * @param p Producto aleatorio generado para el juego.
     */
    private void tercerIntento(Producto p) {
        String stringPrecioIntento;
        double precioIntento;
        try {
            stringPrecioIntento = tercerIntento.getText().toString();
            precioIntento = Double.parseDouble(stringPrecioIntento);
            int acierto = comprobarPrecioUsuario(precioIntento);
            cuadroFinal.setVisibility(View.VISIBLE);
            if (acierto == 0) {
                Toast.makeText(JuegoPrecios.this, "Te has quedado corto, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(194, 24, 7)); //Rojo
                imagenTercerIntento.setImageResource(R.drawable.icono_flecha_arriba);
                cuadroFinal.setText(String.format("Te has quedado corto, el precio era %s€", p.getPrice()));
            } else if (acierto == 1) {
                Toast.makeText(JuegoPrecios.this, "Te has pasado, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(
                        194, 24, 7)); //Rojo
                imagenTercerIntento.setImageResource(R.drawable.icono_flecha_abajo);
                cuadroFinal.setText(String.format("Te has pasado, el precio era %s€", p.getPrice()));
            } else if (acierto == 2) {
                Toast.makeText(JuegoPrecios.this, "Has acertado el precio exacto, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(187, 165, 61)); //Dorado
                imagenTercerIntento.setImageResource(R.drawable.icono_estrella_ganar_exacto);
                cuadroFinal.setText(R.string.precio_acertado);
            } else {
                Toast.makeText(JuegoPrecios.this, "¡Bien! Te has quedado muy cerca, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(192, 192, 192)); //Plateado
                imagenTercerIntento.setImageResource(R.drawable.icono_estrella_ganar_tolerancia);
                cuadroFinal.setText(getString(R.string.te_has_quedado_cerca) + p.getPrice() + "€");
            }
            tercerIntento.setEnabled(false);
            comprobar.setEnabled(false);
        } catch (NumberFormatException ex) {
            Toast.makeText(JuegoPrecios.this, "El valor introducido no es un numero", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Genera un producto aleatorio.
     *
     * @param superM Supermercado del que se generará el producto.
     * @return Producto aleatorio generado.
     */
    private Producto productoAleatorio(SupermercadosFactoria superM) {
        mHandler.post(()->{loadingCircle.setVisibility(View.VISIBLE);});
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<Producto>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String alimento = faker.food().ingredient();
            alimento = alimento.contains(" ") ? alimento.split(" ")[0] : alimento;

            final String productoBuscado = alimento;

            Callable<Producto> task = () -> {
                if (superM.existe(productoBuscado)) {
                    return superM.busqueda(productoBuscado).iterator().next();
                } else {
                    return null;
                }
            };

            futures.add(executor.submit(task));
        }

        executor.shutdown();

        Producto p = null;
        for (Future<Producto> future : futures) {
            try {
                p = future.get();
                if (p != null) {
                    break;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        mHandler.post(()->{loadingCircle.setVisibility(View.GONE);});


        return p;
    }


    /**
     * Selecciona un supermercado aleatorio.
     *
     * @return Supermercado seleccionado aleatoriamente.
     */
    private SupermercadosFactoria supermercadoAleatorio() {
        SupermercadosFactoria superM = new SupermercadosFactoria();
        int longitud = SupermercadosDisponibles.getStringValues().length;
        int aleatorio = (int) (Math.random() * longitud);
        String nombreSupermercado = SupermercadosDisponibles.getStringValues()[aleatorio];
        superM.crearSupermercado(SupermercadosDisponibles.valueOf(nombreSupermercado));
        return superM;
    }

    /**
     * Comprueba si el precio introducido por el usuario está dentro de la tolerancia.
     *
     * @param precioUsuario Precio introducido por el usuario.
     * @return 0 si el precio es más bajo, 1 si es más alto, 2 si es exacto, 3 si está dentro de la tolerancia.
     */
    public int comprobarPrecioUsuario(double precioUsuario) {
        double limiteSuperior = precio * (1 + TOLERANCIA), limiteInferior = precio * (1 - TOLERANCIA);
        if (precioUsuario < limiteInferior) {
            return 0;
        } else if (precioUsuario > limiteSuperior) {
            return 1;
        } else if (precio == precioUsuario) {
            return 2;
        } else {
            return 3;
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
     * Método para el manejo del clic en la barra lateral.
     */
    public void onSideBarClick(View view) {
        showMenuDialog();
    }

    /**
     * Método para el manejo del clic en el botón de comparar productos.
     */
    public void onCompararButtonClick(View view) {
        Intent i = new Intent(this, ComparadorProductos.class);
        startActivity(i);
    }

    /**
     * Método para el manejo del clic en el botón de listas.
     */
    public void onListasButtonClick(View view) {
        Intent i = new Intent(this, PrincipalListas.class);
        startActivity(i);
    }

    /**
     * Método para el manejo del clic en el botón de juego.
     */
    public void onJuegoButtonClick(View view) {
        Intent i = new Intent(this, JuegoPrecios.class);
        startActivity(i);
    }
}
