package com.lista.listacompra.accesoDatos.baseDatos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductoDao {
    @Insert
    void insertProducto(ProductoBD producto);

    @Query("SELECT * FROM productos WHERE id = :id")
    LiveData<ProductoBD> getProductoById(String id);

    @Query("SELECT * FROM productos")
    LiveData<List<ProductoBD>> getAllProductos();

    @Insert
    void insertProductos(List<ProductoBD> productos);

    @Insert
    void insertProductos(ProductoBD productos);

    @Query("DELETE FROM productos WHERE id = :id")
    void deleteProducto(String id);
}
