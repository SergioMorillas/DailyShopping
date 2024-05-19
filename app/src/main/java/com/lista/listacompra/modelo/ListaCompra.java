package com.lista.listacompra.modelo;

import android.content.Context;

import com.lista.listacompra.accesoDatos.GestorBD;
import com.lista.listacompra.accesoDatos.baseDatos.ListaCompraBD;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * La clase ListaCompra modela una lista de compras, que incluye un nombre, fecha, supermercado
 * y un conjunto de productos. También proporciona métodos para calcular el precio total,
 * precio marcado y precio sin marcar de los productos en la lista.
 */
public class ListaCompra {
    /**
     * El nombre de la lista de compras.
     */
    private String nombre;

    /**
     * La fecha en que se creó la lista de compras.
     */
    private long fecha;

    /**
     * El nombre del supermercado asociado con la lista de compras.
     */
    private String supermercado;

    /**
     * Conjunto de productos en la lista de compras.
     */
    private Set<Producto> productos;

    /**
     * Gestor de base de datos asociado con la lista de compras.
     */
    private GestorBD gestorBD;

    /**
     * Constructor que inicializa una ListaCompra a partir de un objeto ListaCompraBD y un Contexto.
     *
     * @param lista   El objeto ListaCompraBD del cual se inicializa la lista de compras.
     * @param context El contexto de la aplicación Android.
     */
    public ListaCompra(ListaCompraBD lista, Context context) {
        this.nombre = lista.getNombre();
        this.fecha = lista.getFecha();
        this.supermercado = lista.getSupermercado();
        gestorBD = new GestorBD(context);
        productos = new HashSet<>();

        Set<ProductoBD> listaAux = lista.getProductos();
        if (listaAux != null) for (ProductoBD pBD : listaAux) productos.add(new Producto(pBD));

    }

    /**
     * Constructor que inicializa una ListaCompra a partir de un objeto ListaCompraBD.
     *
     * @param lista El objeto ListaCompraBD del cual se inicializa la lista de compras.
     */
    public ListaCompra(ListaCompraBD lista) {
        this.nombre = lista.getNombre();
        this.fecha = lista.getFecha();
        this.supermercado = lista.getSupermercado();
        productos = new HashSet<>();

        Set<ProductoBD> listaAux = lista.getProductos();
        if (listaAux != null) for (ProductoBD pBD : listaAux) productos.add(new Producto(pBD));
    }

    /**
     * Constructor que inicializa una ListaCompra con un Contexto.
     *
     * @param context El contexto de la aplicación Android.
     */
    public ListaCompra(Context context) {
        gestorBD = new GestorBD(context);
    }

    /**
     * Constructor que inicializa una ListaCompra con nombre, fecha, supermercado y conjunto de productos.
     *
     * @param nombre       El nombre de la lista de compras.
     * @param fecha        La fecha en que se creó la lista de compras.
     * @param supermercado El nombre del supermercado asociado con la lista de compras.
     * @param productos    Conjunto de productos en la lista de compras.
     */
    public ListaCompra(String nombre, long fecha, String supermercado, Set<Producto> productos) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.supermercado = supermercado;
        this.productos = productos;
    }

    /**
     * Obtiene el nombre de la lista de compras.
     *
     * @return El nombre de la lista de compras.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la fecha en que se creó la lista de compras.
     *
     * @return La fecha en que se creó la lista de compras.
     */
    public long getFecha() {
        return fecha;
    }

    /**
     * Obtiene el nombre del supermercado asociado con la lista de compras.
     *
     * @return El nombre del supermercado asociado con la lista de compras.
     */
    public String getSupermercado() {
        return supermercado;
    }

    /**
     * Obtiene el conjunto de productos en la lista de compras.
     *
     * @return Conjunto de productos en la lista de compras.
     */
    public Set<Producto> getProductos() {
        return productos;
    }

    /**
     * Agrega un producto a la lista de compras.
     *
     * @param p El producto a agregar.
     * @return true si se agrega correctamente, false de lo contrario.
     */
    public boolean addProducto(Producto p) {
        Set<Producto> lista = this.getProductos();
        if (lista != null) lista.add(p);
        else return false;
        return true;
    }

    /**
     * Establece el gestor de base de datos asociado con la lista de compras.
     *
     * @param gestorBD El gestor de base de datos a establecer.
     */
    public void setGestorDB(GestorBD gestorBD) {
        this.gestorBD = gestorBD;
    }

    /**
     * Comprueba si dos objetos ListaCompra son iguales.
     *
     * @param o El objeto a comparar.
     * @return true si los objetos son iguales, false de lo contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListaCompra)) return false;
        ListaCompra that = (ListaCompra) o;
        return getFecha() == that.getFecha() && Objects.equals(getNombre(), that.getNombre()) && Objects.equals(getSupermercado(), that.getSupermercado());
    }

    /**
     * Genera un código hash para un objeto ListaCompra.
     *
     * @return El código hash del objeto ListaCompra.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getNombre(), getFecha(), getSupermercado());
    }

    /**
     * Devuelve una representación en cadena de la lista de compras.
     *
     * @return Una representación en cadena de la lista de compras.
     */
    @Override
    public String toString() {
        return "ListaCompra{" +
                "nombre='" + nombre + '\'' +
                ", fecha=" + fecha +
                ", supermercado='" + supermercado + '\'' +
                ", productos=" + productos +
                '}';
    }

    /**
     * Calcula el precio total de todos los productos en la lista de compras.
     *
     * @return El precio total de todos los productos en la lista de compras.
     */
    public String getPrecioTotal() {
        DecimalFormat df = new DecimalFormat("#.##");
        double suma = 0;
        for (Producto p : productos) {
            suma += p.getPrice() * p.getAmount();
        }
        return df.format(suma);
    }

    /**
     * Calcula el precio total de los productos marcados en la lista de compras.
     *
     * @return El precio total de los productos marcados en la lista de compras.
     */
    public String getPrecioMarcado() {
        DecimalFormat df = new DecimalFormat("#.##");
        double suma = 0;
        for (Producto p : productos) {
            if (p.isMarked()) suma += p.getPrice() * p.getAmount();
        }
        return df.format(suma);
    }

    /**
     * Calcula el precio total de los productos no marcados en la lista de compras.
     *
     * @return El precio total de los productos no marcados en la lista de compras.
     */
    public String getPrecioSinMarcar() {
        double suma = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        for (Producto p : productos) {
            if (!p.isMarked()) suma += p.getPrice() * p.getAmount();
        }
        return df.format(suma);
    }

    /**
     * Calcula el precio promedio de una lista de la compra
     * @return El precio medio de todos los productos en la lista de la compra
     */
    public String getPrecioPromedio() {
        double precio = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        if (!(this.getProductos().size() == 0))
            precio = Double.parseDouble(getPrecioTotal())/(double)(this.getProductos().size());
        return df.format(precio);
    }

}
