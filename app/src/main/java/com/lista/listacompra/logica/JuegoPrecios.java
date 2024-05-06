package com.lista.listacompra.logica;

import android.app.Dialog;
import android.content.Intent;
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
    private Faker faker;
    double precio;
    EditText primerIntento, segundoIntento;
    ImageView imagenProducto, imagenPrimerIntento, imagenSegundoIntento;
    TextView nombreSupermercado;
    Button comprobar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.juego_precios);
        super.onCreate(savedInstanceState);
        faker = new Faker();
        primerIntento = findViewById(R.id.primerIntento);
        segundoIntento = findViewById(R.id.segundoIntento);
        imagenProducto = findViewById(R.id.producto);
        imagenPrimerIntento = findViewById(R.id.imagenPrimerIntento);
        imagenSegundoIntento = findViewById(R.id.imagenSegundoIntento);
        nombreSupermercado = findViewById(R.id.nombreSupermercado);
        comprobar = findViewById(R.id.comprobar);
        SupermercadosFactoria superM = supermercadoAleatorio();
        Producto p = productoAleatorio(superM);


        nombreSupermercado.setText(superM.getNombre());
        Picasso.get().load(p.getImage()).into(imagenProducto);
        comprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringPrecioIntento;
                double precioIntento;

                if (!segundoIntento.isEnabled()) { //Si es el primer intento
                    try {
                        stringPrecioIntento = primerIntento.getText().toString();
                        precioIntento = Double.parseDouble(stringPrecioIntento);
                        int acierto = comprobarPrecioUsuario(precioIntento);
                        if (acierto == 0) {
                            Toast.makeText(JuegoPrecios.this, "El precio real es mas alto", Toast.LENGTH_LONG).show();
                        } else if (acierto == 1) {
                            Toast.makeText(JuegoPrecios.this, "El precio real es mas bajo", Toast.LENGTH_LONG).show();
                        } else if (acierto == 2) {
                            Toast.makeText(JuegoPrecios.this, "Has acertado el precio exacto", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(JuegoPrecios.this, "Has acertado el precio con la tolerancia", Toast.LENGTH_LONG).show();
                        }
                    } catch (NumberFormatException ex) {
                        Toast.makeText(JuegoPrecios.this, "El valor introducido no es un numero", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        stringPrecioIntento = segundoIntento.getText().toString();
                        precioIntento = Double.parseDouble(stringPrecioIntento);
                        int acierto = comprobarPrecioUsuario(precioIntento);
                        if (acierto == 0) {
                            Toast.makeText(JuegoPrecios.this, "El precio real era: " + p.getPrice(), Toast.LENGTH_LONG).show();
                        } else if (acierto == 1) {
                            Toast.makeText(JuegoPrecios.this, "El precio real era: " + p.getPrice(), Toast.LENGTH_LONG).show();
                        } else if (acierto == 2) {
                            Toast.makeText(JuegoPrecios.this, "Has acertado el precio exacto", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(JuegoPrecios.this, "Has acertado el precio con la tolerancia", Toast.LENGTH_LONG).show();
                        }
                    } catch (NumberFormatException ex) {
                        Toast.makeText(JuegoPrecios.this, "El valor introducido no es un numero", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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
        int aleatorio = (int) (Math.random() * longitud) ;
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
        if (!((precio - precioUsuario) > (precio * 0.1))) { //Comprueba si el precio del usuario es demasiado bajo, true en caso afirmativo
            return 0;
        } else if (!((precio - precioUsuario) < (precio - (precio * 1.1)))) {//Comprueba si el precio del usuario es demasiado alto, true en caso afirmativo
            return 1;
        } else if (precio == precioUsuario) {//Comprueba si el precio del usuario es el precio real
            return 2;
        } else { // Esta en el rango ofrecido
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