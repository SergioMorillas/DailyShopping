package com.lista.listacompra.accesoDatos.baseDatos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Dao
public interface ListaCompraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListaCompra(ListaCompraBD listaCompra);

    @Query("SELECT * FROM listas_compra WHERE id = :id")
    ListaCompraBD getListaCompraById(int id);

    @Query("SELECT * FROM listas_compra WHERE nombre = :name")
    ListaCompraBD getListaCompraByName(String name);
    @Query("SELECT * FROM listas_compra")
    List<ListaCompraBD> getAllListasCompraList();

    default Set<ListaCompraBD> getAllListasCompra() {
        return new HashSet<>(getAllListasCompraList());
    }
    @Insert
    void insertListaCompras(Set<ListaCompraBD> listaCompra);

    @Insert
    void insertListaCompras(ListaCompraBD listaCompra);

    @Query("DELETE FROM listas_compra WHERE id = :id")
    void deleteListaCompra(int id);
    @Query("DELETE FROM listas_compra WHERE nombre = :nombre and supermercado = :supermercado and fecha = :fecha")
    void deleteListaCompra(String nombre, String supermercado, long fecha);

    @Query("SELECT * FROM listas_compra WHERE nombre LIKE '%' || :nombre || '%'")
    List<ListaCompraBD> getAllListasCompraNombreList(String nombre);
    default Set<ListaCompraBD> getAllListasCompraNombre(String nombre) {
        return new HashSet<>(getAllListasCompraNombre(nombre));
    }
}
