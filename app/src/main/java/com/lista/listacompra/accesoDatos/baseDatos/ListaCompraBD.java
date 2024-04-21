package com.lista.listacompra.accesoDatos.baseDatos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.lista.listacompra.modelo.ListaCompra;
import com.lista.listacompra.modelo.Producto;

import java.util.List;

@Entity(
        tableName = "listas_compra",
        indices = {@Index(value = {"nombre", "fecha", "supermercado"}, unique = true)})
@TypeConverters({ConvertidorProducto.class})
public class ListaCompraBD {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    @NonNull
    private int id;

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "fecha")
    private long fecha;

    @ColumnInfo(name = "supermercado")
    private String supermercado;

    @ColumnInfo(name = "productos")
    private List<ProductoBD> productos;


    public ListaCompraBD(String nombre, long fecha, String supermercado, List<ProductoBD> productos) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.supermercado = supermercado;
        this.productos = productos;
    }
    public ListaCompraBD(ListaCompra lista) {
        this.nombre = lista.getNombre();
        this.fecha = lista.getFecha();
        this.supermercado = lista.getSupermercado();

        List<Producto> listaAux = lista.getProductos();
        for (Producto p : listaAux) productos.add(new ProductoBD(p));

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }

    public List<ProductoBD> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoBD> productos) {
        this.productos = productos;
    }
}
