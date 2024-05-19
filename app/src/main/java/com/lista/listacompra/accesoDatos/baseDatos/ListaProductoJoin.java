package com.lista.listacompra.accesoDatos.baseDatos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

/**
 * Entidad que representa la relación entre una lista de compra y un producto en la base de datos.
 * Esta clase utiliza claves foráneas para enlazar {@link ListaCompraBD} y {@link ProductoBD}.
 */
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

    /**
     * Identificador de la lista de compra.
     * Este campo es una clave primaria y una clave foránea que referencia el identificador de {@link ListaCompraBD}.
     */
    @NonNull
    public int listaCompraId;

    /**
     * Identificador del producto.
     * Este campo es una clave primaria y una clave foránea que referencia el identificador de {@link ProductoBD}.
     */
    @NonNull
    public String productoId;
}
