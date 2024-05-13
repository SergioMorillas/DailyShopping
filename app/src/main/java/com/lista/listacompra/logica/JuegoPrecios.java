package com.lista.listacompra.logica;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.concurrent.*;

import java.util.ArrayList;
import java.util.List;


public class JuegoPrecios extends AppCompatActivity {
    private static final double TOLERANCIA = 0.25; // Tolerancia de un 25%
    private Faker faker;
    private double precio;
    private EditText primerIntento, segundoIntento;
    private ImageView imagenProducto, imagenPrimerIntento, imagenSegundoIntento, menu;
    private TextView nombreSupermercado, cuadroFinal;
    private Button comprobar, nuevoIntento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.juego_precios);
        super.onCreate(savedInstanceState);
        faker = new Faker();
        instancias();
        SupermercadosFactoria superM = supermercadoAleatorio();
        Producto p = productoAleatorio(superM);

        precio = p.getPrice();
        cuadroFinal.setVisibility(View.VISIBLE);
        nombreSupermercado.setText(superM.getNombre());
        Picasso.get().load(p.getImage()).into(imagenProducto);
        nuevoIntento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JuegoPrecios.this, JuegoPrecios.class);
                startActivity(i);
            }
        });
        comprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringPrecioIntento;
                double precioIntento;
                //Si es el primer intento
                if (!segundoIntento.isEnabled()) primerIntento(p);
                else segundoIntento(p);
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

    private void instancias() {
        primerIntento = findViewById(R.id.primerIntento);
        segundoIntento = findViewById(R.id.segundoIntento);
        imagenProducto = findViewById(R.id.producto);
        imagenPrimerIntento = findViewById(R.id.imagenPrimerIntento);
        imagenSegundoIntento = findViewById(R.id.imagenSegundoIntento);
        nombreSupermercado = findViewById(R.id.nombreSupermercado);
        cuadroFinal = findViewById(R.id.cuadroFinal);
        comprobar = findViewById(R.id.comprobar);
        nuevoIntento = findViewById(R.id.nuevo_intento);
        menu = findViewById(R.id.menu);
    }

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
            }
            primerIntento.setEnabled(false);
        } catch (NumberFormatException ex) {
            Toast.makeText(JuegoPrecios.this, "El valor introducido no es un numero", Toast.LENGTH_LONG).show();
        }
    }

    private void segundoIntento(Producto p) {
        String stringPrecioIntento;
        double precioIntento;
        try {
            stringPrecioIntento = segundoIntento.getText().toString();
            precioIntento = Double.parseDouble(stringPrecioIntento);
            int acierto = comprobarPrecioUsuario(precioIntento);
            cuadroFinal.setVisibility(View.VISIBLE);
            if (acierto == 0) {
                Toast.makeText(JuegoPrecios.this, "Te has quedado corto, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(194, 24, 7)); //Rojo
                imagenSegundoIntento.setImageResource(R.drawable.icono_flecha_arriba);
                cuadroFinal.setText(String.format("Te has quedado corto, el precio era %s€", p.getPrice()));
            } else if (acierto == 1) {
                Toast.makeText(JuegoPrecios.this, "Te has pasado, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(194, 24, 7)); //Rojo
                imagenSegundoIntento.setImageResource(R.drawable.icono_flecha_abajo);
                cuadroFinal.setText(String.format("Te has pasado, el precio era %s€", p.getPrice()));
            } else if (acierto == 2) {
                Toast.makeText(JuegoPrecios.this, "Has acertado el precio exacto, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(187, 165, 61)); //Dorado
                imagenSegundoIntento.setImageResource(R.drawable.icono_estrella_ganar_exacto);
                cuadroFinal.setText(R.string.precio_acertado);
            } else {
                Toast.makeText(JuegoPrecios.this, "¡Bien! Te has quedado muy cerca, el precio era " + p.getPrice() + "€", Toast.LENGTH_LONG).show();
                cuadroFinal.setTextColor(Color.rgb(192, 192, 192)); //Plateado
                imagenSegundoIntento.setImageResource(R.drawable.icono_estrella_ganar_tolerancia);
                cuadroFinal.setText(getString(R.string.te_has_quedado_cerca) + p.getPrice() + "€");
            }
            segundoIntento.setEnabled(false);
        } catch (NumberFormatException ex) {
            Toast.makeText(JuegoPrecios.this, "El valor introducido no es un numero", Toast.LENGTH_LONG).show();
        }
    }


    private Producto productoAleatorio(SupermercadosFactoria superM) {
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

        return p;
    }


    private SupermercadosFactoria supermercadoAleatorio() {
        SupermercadosFactoria superM = new SupermercadosFactoria();
        int longitud = SupermercadosDisponibles.getStringValues().length;
        int aleatorio = (int) (Math.random() * longitud);
        String nombreSupermercado = SupermercadosDisponibles.getStringValues()[aleatorio];
        superM.crearSupermercado(SupermercadosDisponibles.valueOf(nombreSupermercado));
        return superM;
    }

    /**
     * @param precioUsuario Precio introducido por el usuario
     * @return <ul>
     * <li>0. En caso de que el valor introducido por el usuario sea mas bajo que el precio
     * real mas su tolerancia</li>
     * <li>1. En caso de que el valor introducido por el usuario sea mas alto que el precio
     * real mas su tolerancia</li>
     * <li>2. En caso de que el valor introducido por el usuario sea igual que el precio
     * real</li>
     * <li>3. En caso de que el valor introducido por el usuario se encuentre entre la
     * tolerancia del precio real</li>
     * </ul>
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
     * @brief Muestra un diálogo de menú.
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

    public void onSideBarClick(View view) {
        showMenuDialog();
    }

    public void onCompararButtonClick(View view) {
        Intent i = new Intent(this, ComparadorProductos.class);
        startActivity(i);
    }

    public void onListasButtonClick(View view) {
        Intent i = new Intent(this, PrincipalListas.class);
        startActivity(i);
    }

    public void onJuegoButtonClick(View view) {
        Intent i = new Intent(this, JuegoPrecios.class);
        startActivity(i);
    }
}