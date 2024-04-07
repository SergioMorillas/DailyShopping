package com.lista.listacompra.persistencia;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.lista.listacompra.persistencia.Producto;


import java.util.ArrayList;

@Entity(tableName = "listas_compra")
@TypeConverters(ConvertidorProducto.class)
public class ListaCompra {
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
    private ArrayList<Producto> productos;

    public ListaCompra(String nombre, long fecha, String supermercado, ArrayList<Producto> productos) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.supermercado = supermercado;
        this.productos = productos;
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

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
}
