package com.lista.listacompra.accesoDatos.baseDatos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "lista_producto_join",
        primaryKeys = {"listaCompraId", "productoId"},
        foreignKeys = {
                @ForeignKey(entity = ListaCompraBD.class,
                        parentColumns = "id",
                        childColumns = "listaCompraId"),
                @ForeignKey(entity = ProductoBD.class,
                        parentColumns = "id",
                        childColumns = "productoId")
        })
public class ListaProductoJoin {
    @NonNull
    public int listaCompraId;
    @NonNull
    public String productoId;
}
