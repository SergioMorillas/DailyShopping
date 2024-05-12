package com.lista.listacompra.modelo;

import android.content.Context;


import com.lista.listacompra.accesoDatos.GestorBD;
import com.lista.listacompra.accesoDatos.baseDatos.ListaCompraBD;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class ListaCompra {
    private String nombre;
    private long fecha;
    private String supermercado;
    private Set<Producto> productos;
    private GestorBD gestorBD;

    public ListaCompra(ListaCompraBD lista, Context context) {
        this.nombre = lista.getNombre();
        this.fecha = lista.getFecha();
        this.supermercado = lista.getSupermercado();
        gestorBD = new GestorBD(context);
        productos = new HashSet<>();

        Set<ProductoBD> listaAux = lista.getProductos();
        if (listaAux!=null) for (ProductoBD pBD : listaAux) productos.add(new Producto(pBD));

    }
    public ListaCompra(ListaCompraBD lista) {
        this.nombre = lista.getNombre();
        this.fecha = lista.getFecha();
        this.supermercado = lista.getSupermercado();
        productos = new HashSet<>();

        Set<ProductoBD> listaAux = lista.getProductos();
        if (listaAux!=null) for (ProductoBD pBD : listaAux) productos.add(new Producto(pBD));
    }
    public ListaCompra(Context context) {
        gestorBD = new GestorBD(context);
    }

    public ListaCompra(String nombre, long fecha, String supermercado, Set productos) {
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

    public Set<Producto> getProductos() {
        return productos;
    }

    public boolean addProducto(Producto p){
        Set<Producto> lista = this.getProductos();
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

    public String getPrecioTotal(){
        DecimalFormat df = new DecimalFormat("#.##");
        double suma = 0;
        for (Producto p : productos) {
             suma += p.getPrice()*p.getAmount();;
        }
        return df.format(suma);
    }
    public String getPrecioMarcado(){
        DecimalFormat df = new DecimalFormat("#.##");
        double suma = 0;
        for (Producto p : productos) {
            if (p.isMarked()) suma += p.getPrice()*p.getAmount();
        }
        return df.format(suma);
    }
    public String getPrecioSinMarcar(){
        double suma = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        for (Producto p : productos) {
            if (!p.isMarked()) suma += p.getPrice()*p.getAmount();
        }
        return df.format(suma);
    }

}
