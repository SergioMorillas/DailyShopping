package com.lista.listacompra.modelo.baseDatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Data Access Object (DAO) para la entidad {@link ListaCompraBD}.
 * Proporciona métodos para interactuar con la base de datos.
 */
@Dao
public interface ListaCompraDao {

    /**
     * Inserta una lista de compra en la base de datos.
     * Si la lista ya existe, la reemplaza.
     *
     * @param listaCompra La lista de compra a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListaCompra(ListaCompraBD listaCompra);

    /**
     * Obtiene una lista de compra por su identificador.
     *
     * @param id El identificador de la lista de compra.
     * @return La lista de compra con el identificador dado.
     */
    @Query("SELECT * FROM listas_compra WHERE id = :id")
    ListaCompraBD getListaCompraById(int id);

    /**
     * Obtiene una lista de compra por su nombre, supermercado y fecha.
     *
     * @param nombre El nombre de la lista de compra.
     * @param supermercado El supermercado asociado con la lista de compra.
     * @param fecha La fecha de creación de la lista de compra.
     * @return La lista de compra con los parámetros dados.
     */
    @Query("SELECT * FROM listas_compra WHERE nombre = :nombre AND supermercado = :supermercado AND fecha = :fecha")
    ListaCompraBD getListaCompraByName(String nombre, String supermercado, long fecha);

    /**
     * Obtiene todas las listas de compra en la base de datos como una lista.
     *
     * @return Una lista de todas las listas de compra.
     */
    @Query("SELECT * FROM listas_compra")
    List<ListaCompraBD> getAllListasCompraList();

    /**
     * Obtiene todas las listas de compra en la base de datos como un conjunto.
     *
     * @return Un conjunto de todas las listas de compra.
     */
    default Set<ListaCompraBD> getAllListasCompra() {
        return new HashSet<>(getAllListasCompraList());
    }

    /**
     * Inserta un conjunto de listas de compra en la base de datos.
     *
     * @param listaCompra El conjunto de listas de compra a insertar.
     */
    @Insert
    void insertListaCompras(Set<ListaCompraBD> listaCompra);

    /**
     * Inserta una lista de compra en la base de datos.
     *
     * @param listaCompra La lista de compra a insertar.
     */
    @Insert
    void insertListaCompras(ListaCompraBD listaCompra);

    /**
     * Elimina una lista de compra por su identificador.
     *
     * @param id El identificador de la lista de compra a eliminar.
     */
    @Query("DELETE FROM listas_compra WHERE id = :id")
    void deleteListaCompra(int id);

    /**
     * Elimina una lista de compra por su nombre, supermercado y fecha.
     *
     * @param nombre El nombre de la lista de compra.
     * @param supermercado El supermercado asociado con la lista de compra.
     * @param fecha La fecha de creación de la lista de compra.
     */
    @Query("DELETE FROM listas_compra WHERE nombre = :nombre AND supermercado = :supermercado AND fecha = :fecha")
    void deleteListaCompra(String nombre, String supermercado, long fecha);
}
