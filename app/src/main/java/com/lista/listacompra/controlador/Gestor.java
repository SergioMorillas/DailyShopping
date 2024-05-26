package com.lista.listacompra.controlador;

import android.content.Context;

import com.lista.listacompra.modelo.GestorBD;
import com.lista.listacompra.modelo.baseDatos.ListaCompraBD;

import java.util.HashSet;
import java.util.Set;

/**
 * La clase Gestor proporciona métodos para interactuar con la base de datos y gestionar listas de compra.
 */
public class Gestor {
    /**
     * Gestor de la base de datos utilizado para realizar operaciones de lectura y escritura.
     */
    private final GestorBD gestorBD;

    /**
     * Construye un nuevo Gestor con el contexto proporcionado.
     *
     * @param context El contexto de la aplicación.
     */
    public Gestor(Context context) {
        this.gestorBD = new GestorBD(context);
    }

    /**
     * Recupera una ListaCompra por su nombre, supermercado y fecha.
     *
     * @param nombre      El nombre de la lista.
     * @param supermercado El nombre del supermercado.
     * @param fecha       La fecha de la lista.
     * @return La ListaCompra correspondiente, o null si no se encuentra.
     */
    public ListaCompra getListaPorNombre(String nombre, String supermercado, long fecha) {
        return new ListaCompra(gestorBD.getListaCompra(nombre, supermercado, fecha));
    }

    /**
     * Inserta una nueva ListaCompra en la base de datos.
     *
     * @param l La ListaCompra que se va a insertar.
     */
    public void insertaLista(ListaCompra l) {
        gestorBD.insertaLista(l);
    }

    /**
     * Obtiene todas las listas de compra almacenadas en la base de datos.
     *
     * @return Un conjunto de todas las listas de compra, o un conjunto vacío si no hay ninguna.
     */
    public Set<ListaCompra> getTodaslistas() {
        Set<ListaCompraBD> lista = gestorBD.getTodasListas();
        Set<ListaCompra> listaAux = new HashSet<>();
        if (lista != null) {
            for (ListaCompraBD listaCompra : lista) {
                listaAux.add(new ListaCompra(listaCompra));
            }
        }
        return listaAux;
    }

    /**
     * Busca listas de compra por su nombre.
     *
     * @param nombre El nombre de la lista de compra a buscar.
     * @return Un conjunto de listas de compra que coinciden con el nombre proporcionado.
     */
    public Set<ListaCompra> getBusquedaListasNombre(String nombre) {
        Set<ListaCompraBD> lista = gestorBD.getTodasListasNombre(nombre);
        Set<ListaCompra> listaAux = new HashSet<>();
        if (lista != null) {
            for (ListaCompraBD listaCompra : lista) {
                listaAux.add(new ListaCompra(listaCompra));
            }
        }
        return listaAux;
    }

    /**
     * Borra una lista de compra de la base de datos.
     *
     * @param nombre      El nombre de la lista de compra a borrar.
     * @param supermercado El nombre del supermercado asociado a la lista.
     * @param fecha       La fecha de la lista de compra a borrar.
     */
    public void borrarLista(String nombre, String supermercado, long fecha) {
        gestorBD.borrarLista(nombre, supermercado, fecha);
    }

    /**
     * Actualiza la información de una lista de compra en la base de datos.
     *
     * @param lista La ListaCompra con la información actualizada.
     */
    public void actualizarListaProductos(ListaCompra lista) {
        gestorBD.actualizarLista(lista);
    }
}
