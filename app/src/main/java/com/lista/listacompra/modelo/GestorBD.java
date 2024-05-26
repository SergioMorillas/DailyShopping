package com.lista.listacompra.modelo;

import android.content.Context;
import android.util.Log;

import com.lista.listacompra.modelo.baseDatos.AppDatabase;
import com.lista.listacompra.modelo.baseDatos.ListaCompraBD;
import com.lista.listacompra.controlador.ListaCompra;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase GestorBD: Administra operaciones relacionadas con la base de datos de la lista de compras.
 */
public class GestorBD {
    Context contexto;
    AppDatabase database;

    /**
     * Constructor de la clase GestorBD.
     *
     * @param contextoAplicacion El contexto de la aplicación.
     */
    public GestorBD(Context contextoAplicacion) {
        this.contexto = contextoAplicacion;
        database = AppDatabase.getDatabase(this.contexto);
    }

    /**
     * Obtiene una lista de compra por su nombre, supermercado y fecha.
     *
     * @param nombre      El nombre de la lista de compra.
     * @param supermercado El supermercado de la lista de compra.
     * @param fecha       La fecha de la lista de compra.
     * @return La lista de compra correspondiente a los parámetros dados.
     */
    public ListaCompraBD getListaCompra(String nombre, String supermercado, long fecha) {
        return database.listaCompraDao().getListaCompraByName(nombre, supermercado, fecha);
    }

    /**
     * Inserta una lista de compra en la base de datos.
     *
     * @param lista La lista de compra a insertar.
     */
    public void insertaLista(ListaCompra lista) {
        database.listaCompraDao().insertListaCompra(new ListaCompraBD(lista));
    }

    /**
     * Obtiene todas las listas de compra de la base de datos.
     *
     * @return Un conjunto de todas las listas de compra.
     */
    public Set<ListaCompraBD> getTodasListas() {
        return database.listaCompraDao().getAllListasCompra();
    }

    /**
     * Obtiene todas las listas de compra que contienen un nombre específico.
     *
     * @param nombre El nombre a buscar en las listas de compra.
     * @return Un conjunto de listas de compra que contienen el nombre especificado.
     */
    public Set<ListaCompraBD> getTodasListasNombre(String nombre) {
        Set<ListaCompraBD> aux = new HashSet<>();
        for (ListaCompraBD lista : database.listaCompraDao().getAllListasCompra()) {
            if (lista.getNombre().toLowerCase().contains(nombre.toLowerCase())) aux.add(lista);
        }
        return aux;
    }

    /**
     * Borra una lista de compra de la base de datos por su nombre, supermercado y fecha.
     *
     * @param nombre      El nombre de la lista de compra.
     * @param supermercado El supermercado de la lista de compra.
     * @param fecha       La fecha de la lista de compra.
     */
    public void borrarLista(String nombre, String supermercado, long fecha) {
        database.listaCompraDao().deleteListaCompra(nombre, supermercado, fecha);
    }

    /**
     * Actualiza una lista de compra en la base de datos.
     *
     * @param lista La lista de compra actualizada.
     */
    public void actualizarLista(ListaCompra lista) {
        try {
            database.runInTransaction(new Runnable() {
                @Override
                public void run() {
                    database.listaCompraDao().deleteListaCompra(lista.getNombre(),
                            lista.getSupermercado(),
                            lista.getFecha());
                    database.listaCompraDao().insertListaCompra(new ListaCompraBD(lista));
                }
            });
        } catch (Exception ex) {
            Log.e("Excepcion", "Ha habido una excepcion" + ex.getMessage());
            throw ex;
        }
    }
}
