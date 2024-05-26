package com.lista.listacompra.controlador;

import com.lista.listacompra.modelo.apiSupermercados.Supermercado;
import com.lista.listacompra.modelo.baseDatos.ProductoBD;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * La clase SupermercadosFactoria proporciona métodos para crear instancias de supermercados
 * y realizar búsquedas de productos en ellos.
 */
public class SupermercadosFactoria {
    /** El nombre del supermercado creado. */
    private String nombre;
    /** El supermercado creado. */
    private Supermercado supermercado;

    /**
     * Crea una instancia del supermercado especificado.
     *
     * @param smd El tipo de supermercado a crear.
     */
    public void crearSupermercado(SupermercadosDisponibles smd) {
        this.nombre = smd.name();
        try {
            Constructor<? extends Supermercado> constructor = smd.getClazz().getDeclaredConstructor();
            this.supermercado = constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el nombre del supermercado creado.
     *
     * @return El nombre del supermercado.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Realiza una búsqueda de productos en el supermercado.
     *
     * @param nombre El nombre del producto a buscar.
     * @return Un conjunto de productos encontrados en la búsqueda.
     */
    public Set<Producto> busqueda(String nombre) {
        Set<ProductoBD> productos;
        Set<Producto> productosAux = new HashSet<>();

        if (supermercado != null) {
            productos = supermercado.search(nombre);
            if (productos != null) {
                for (ProductoBD p : productos) {
                    productosAux.add(new Producto(p));
                }
            }
        }
        return productosAux;
    }

    /**
     * Verifica si un producto existe en el supermercado.
     *
     * @param nombre El nombre del producto a verificar.
     * @return true si el producto existe en el supermercado, false de lo contrario.
     */
    public boolean existe(String nombre) {
        return supermercado.exist(nombre);
    }
}
