package com.lista.listacompra.persistencia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ListaCompraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListaCompra(ListaCompra listaCompra);

    @Query("SELECT * FROM listas_compra WHERE id = :id")
    LiveData<ListaCompra> getListaCompraById(int id);

    @Query("SELECT * FROM listas_compra")
    LiveData<List<ListaCompra>> getAllListasCompra();

    @Insert
    void insertListaCompras(List<ListaCompra> listaCompra);

    @Insert
    void insertListaCompras(ListaCompra listaCompra);

    @Query("DELETE FROM listas_compra WHERE id = :id")
    void deleteListaCompra(int id);

}
