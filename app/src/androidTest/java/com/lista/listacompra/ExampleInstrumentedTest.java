package com.lista.listacompra;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import com.lista.listacompra.controlador.ListaCompra;
import com.lista.listacompra.controlador.Producto;
import com.lista.listacompra.controlador.SupermercadosDisponibles;
import com.lista.listacompra.controlador.SupermercadosFactoria;
import com.lista.listacompra.modelo.GestorBD;
import com.lista.listacompra.modelo.apiSupermercados.Supermercado;
import com.lista.listacompra.modelo.baseDatos.ProductoBD;

import java.util.HashSet;
import java.util.Set;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.lista.listacompra", appContext.getPackageName());
    }

    @Test
    public void apiCall(){
        boolean prueba = true;
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        for (int i = 0; i<10;i++){
            SupermercadosFactoria superM = supermercadoAleatorio();
            if (superM.busqueda("patata")==null){
                prueba = false;
            }
        }
        assertTrue(prueba);
    }

    @Test
    public void transformModelToBD(){
        Producto p = supermercadoAleatorio().busqueda("patata").iterator().next();
        ProductoBD pbd = new ProductoBD(p);
        Producto p1 = new Producto(pbd);
        assertEquals(p1, p);
    }

    @Test
    public void saveData(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        GestorBD gestor = new GestorBD(appContext);

        ListaCompra lista = new ListaCompra(
                "nombre",
                1716768000000l,
                "Mercadona",
                new HashSet<>());
        Producto p = supermercadoAleatorio().busqueda("patata").iterator().next();
        lista.addProducto(p);
        gestor.insertaLista(lista);

        ProductoBD p1 = gestor.getListaCompra(
                "nombre",
                "Mercadona",
                1716768000000l).getProductos().iterator().next();

        assertEquals(p, new Producto(p1));
    }

    private SupermercadosFactoria supermercadoAleatorio() {
        SupermercadosFactoria superM = new SupermercadosFactoria();
        int longitud = SupermercadosDisponibles.getStringValues().length;
        int aleatorio = (int) (Math.random() * longitud);
        String nombreSupermercado = SupermercadosDisponibles.getStringValues()[aleatorio];
        superM.crearSupermercado(SupermercadosDisponibles.valueOf(nombreSupermercado));
        return superM;
    }
}