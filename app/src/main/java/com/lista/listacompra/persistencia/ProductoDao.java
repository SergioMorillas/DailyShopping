package com.lista.listacompra.persistencia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductoDao {
    @Insert
    void insertProducto(Producto producto);

    @Query("SELECT * FROM productos WHERE id = :id")
    LiveData<Producto> getProductoById(String id);

    @Query("SELECT * FROM productos")
    LiveData<List<Producto>> getAllProductos();

    @Insert
    void insertProductos(List<Producto> productos);

    @Insert
    void insertProductos(Producto productos);

    @Query("DELETE FROM productos WHERE id = :id")
    void deleteProducto(String id);
}
