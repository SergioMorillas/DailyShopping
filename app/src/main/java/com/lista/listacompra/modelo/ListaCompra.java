package com.lista.listacompra.modelo;

import android.content.Context;


import com.lista.listacompra.accesoDatos.GestorBD;
import com.lista.listacompra.accesoDatos.baseDatos.ListaCompraBD;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListaCompra {
    private String nombre;
    private long fecha;
    private String supermercado;
    private ArrayList<Producto> productos;
    private GestorBD gestorBD;

    public ListaCompra(ListaCompraBD lista, Context context) {
        this.nombre = lista.getNombre();
        this.fecha = lista.getFecha();
        this.supermercado = lista.getSupermercado();
        gestorBD = new GestorBD(context);

        List<ProductoBD> listaAux = lista.getProductos();
        for (ProductoBD pBD : listaAux) productos.add(new Producto(pBD));

    }
    public ListaCompra(ListaCompraBD lista) {
        this.nombre = lista.getNombre();
        this.fecha = lista.getFecha();
        this.supermercado = lista.getSupermercado();

        List<ProductoBD> listaAux = lista.getProductos();
        for (ProductoBD pBD : listaAux) productos.add(new Producto(pBD));
    }
    public ListaCompra(Context context) {
        gestorBD = new GestorBD(context);
    }

    public ListaCompra(String nombre, long fecha, String supermercado, ArrayList productos) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.supermercado = supermercado;
        this.productos = productos;

    }

    public String getNombre() {
        return nombre;
    }

    public long getFecha() {
        return fecha;
    }

    public String getSupermercado() {
        return supermercado;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public boolean addProducto(Producto p){
        ArrayList<Producto> lista = this.getProductos();
        if (lista != null ) lista.add(p);
        else return false;
        return true;
    }
    public void setGestorDB(GestorBD gestorBD){
        this.gestorBD = gestorBD;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListaCompra)) return false;
        ListaCompra that = (ListaCompra) o;
        return getFecha() == that.getFecha() && Objects.equals(getNombre(), that.getNombre()) && Objects.equals(getSupermercado(), that.getSupermercado());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombre(), getFecha(), getSupermercado());
    }

    @Override
    public String toString() {
        return "ListaCompra{" +
                "nombre='" + nombre + '\'' +
                ", fecha=" + fecha +
                ", supermercado='" + supermercado + '\'' +
                ", productos=" + productos +
                '}';
    }

}
