package com.lista.listacompra.accesoDatos.baseDatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ListaCompraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListaCompra(ListaCompraBD listaCompra);

    @Query("SELECT * FROM listas_compra WHERE id = :id")
    ListaCompraBD getListaCompraById(int id);

    @Query("SELECT * FROM listas_compra WHERE nombre = :name")
    ListaCompraBD getListaCompraByName(String name);
    @Query("SELECT * FROM listas_compra")
    List<ListaCompraBD> getAllListasCompra();

    @Insert
    void insertListaCompras(List<ListaCompraBD> listaCompra);

    @Insert
    void insertListaCompras(ListaCompraBD listaCompra);

    @Query("DELETE FROM listas_compra WHERE id = :id")
    void deleteListaCompra(int id);

}
