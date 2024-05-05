package com.lista.listacompra.logica;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.javafaker.Faker;
import com.lista.listacompra.modelo.Producto;
import com.lista.listacompra.modelo.SupermercadosDisponibles;
import com.lista.listacompra.modelo.SupermercadosFactoria;

public class JuegoPrecios extends AppCompatActivity {
    private Faker faker;
    double precio, precioAcertadoArriba, precioAcertadoAbajo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        faker = new Faker();


    }




    private Producto productoAleatorio() {
        Producto p = null;
        SupermercadosFactoria superM = supermercadoAleatorio();
        for (int i = 0; i < 10; ) {
            String alimento = (faker.food().ingredient());
            alimento = (alimento.contains(" ")?alimento.split(" ")[0] : alimento);
            if (superM.existe(alimento)) return superM.busqueda(alimento).iterator().next();
        }
        return p;
    }
    private SupermercadosFactoria supermercadoAleatorio(){
        SupermercadosFactoria superM = new SupermercadosFactoria();
        int longitud = SupermercadosDisponibles.getStringValues().length;
        int aleatorio = (int) (Math.random() * longitud) + 1;
        String nombreSupermercado = SupermercadosDisponibles.getStringValues()[aleatorio];
        superM.crearSupermercado(SupermercadosDisponibles.valueOf(nombreSupermercado));
        return superM;
    }

    /**
     *
     * @param precioUsuario Precio introducido por el usuario
     * @return
     * <ul>
     *     <li>0. En caso de que el valor introducido por el usuario sea mas bajo que el precio
     *     real mas su tolerancia</li>
     *     <li>1. En caso de que el valor introducido por el usuario sea mas alto que el precio
     *     real mas su tolerancia</li>
     *     <li>2. En caso de que el valor introducido por el usuario sea igual que el precio
     *     real</li>
     *     <li>3. En caso de que el valor introducido por el usuario se encuentre entre la
     *     tolerancia del precio real</li>
     * </ul>
     */
    public int comprobarPrecioUsuario(double precioUsuario){
        if ((precio-precioUsuario)>(precio*0.1)){
            return 0;
        } else if ((precio - precioUsuario) < (precio - (precio * 1.1))) {
            return 1;
        } else if (precio == precioUsuario){
            return 2;
        } else {
            return 3;
        }
        // return Math.abs(precio-precioUsuario)<=(precio*0.1);
    }
}