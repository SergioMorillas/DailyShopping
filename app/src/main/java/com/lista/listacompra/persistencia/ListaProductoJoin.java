package com.lista.listacompra.persistencia;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "lista_producto_join",
        primaryKeys = {"listaCompraId", "productoId"},
        foreignKeys = {
                @ForeignKey(entity = ListaCompra.class,
                        parentColumns = "id",
                        childColumns = "listaCompraId"),
                @ForeignKey(entity = Producto.class,
                        parentColumns = "id",
                        childColumns = "productoId")
        })
public class ListaProductoJoin {
    @NonNull
    public int listaCompraId;
    @NonNull
    public String productoId;
}
