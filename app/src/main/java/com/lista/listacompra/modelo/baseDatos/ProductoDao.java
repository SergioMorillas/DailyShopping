package com.lista.listacompra.modelo.baseDatos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) para la entidad {@link ProductoBD}.
 * Proporciona métodos para interactuar con la tabla "productos" en la base de datos.
 */
@Dao
public interface ProductoDao {

    /**
     * Inserta un producto en la base de datos.
     *
     * @param producto El producto a insertar.
     */
    @Insert
    void insertProducto(ProductoBD producto);

    /**
     * Obtiene un producto por su identificador de forma asincrónica.
     *
     * @param id El identificador del producto.
     * @return LiveData que emite el producto correspondiente al identificador dado.
     */
    @Query("SELECT * FROM productos WHERE id = :id")
    LiveData<ProductoBD> getProductoById(String id);

    /**
     * Obtiene todos los productos de la base de datos de forma asincrónica.
     *
     * @return LiveData que emite una lista de todos los productos.
     */
    @Query("SELECT * FROM productos")
    LiveData<List<ProductoBD>> getAllProductos();

    /**
     * Inserta una lista de productos en la base de datos.
     *
     * @param productos La lista de productos a insertar.
     */
    @Insert
    void insertProductos(List<ProductoBD> productos);

    /**
     * Inserta un producto en la base de datos.
     *
     * @param producto El producto a insertar.
     */
    @Insert
    void insertProductos(ProductoBD producto);

    /**
     * Elimina un producto de la base de datos por su identificador.
     *
     * @param id El identificador del producto a eliminar.
     */
    @Query("DELETE FROM productos WHERE id = :id")
    void deleteProducto(String id);
}
