package com.lista.listacompra.modelo.baseDatos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.lista.listacompra.controlador.ListaCompra;
import com.lista.listacompra.controlador.Producto;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa una lista de compra en la base de datos.
 */
@Entity(
        tableName = "listas_compra",
        indices = {@Index(value = {"nombre", "fecha", "supermercado"}, unique = true)})
@TypeConverters({ConversorProducto.class})
public class ListaCompraBD {
    /**
     * Identificador único de la lista de compra.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    @NonNull
    private int id;

    /**
     * Nombre de la lista de compra.
     */
    @ColumnInfo(name = "nombre")
    private String nombre;

    /**
     * Fecha de creación de la lista de compra, en formato de timestamp.
     */
    @ColumnInfo(name = "fecha")
    private long fecha;

    /**
     * Supermercado donde se realizará la compra.
     */
    @ColumnInfo(name = "supermercado")
    private String supermercado;

    /**
     * Conjunto de productos incluidos en la lista de compra.
     */
    @ColumnInfo(name = "productos")
    private Set<ProductoBD> productos;

    /**
     * Constructor para crear una instancia de {@link ListaCompraBD}.
     *
     * @param nombre El nombre de la lista de compra.
     * @param fecha La fecha de creación de la lista de compra.
     * @param supermercado El supermercado asociado con la lista de compra.
     * @param productos El conjunto de productos incluidos en la lista de compra.
     */
    public ListaCompraBD(String nombre, long fecha, String supermercado, Set<ProductoBD> productos) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.supermercado = supermercado;
        this.productos = productos;
    }

    /**
     * Constructor para crear una instancia de {@link ListaCompraBD} a partir de un objeto {@link ListaCompra}.
     *
     * @param lista La lista de compra modelo.
     */
    public ListaCompraBD(ListaCompra lista) {
        this.nombre = lista.getNombre();
        this.fecha = lista.getFecha();
        this.supermercado = lista.getSupermercado();

        productos = new HashSet<>();
        Set<Producto> listaAux = lista.getProductos();
        for (Producto p : listaAux) productos.add(new ProductoBD(p));
    }

    /**
     * Obtiene el identificador único de la lista de compra.
     *
     * @return El identificador único.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único de la lista de compra.
     *
     * @param id El identificador único.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la lista de compra.
     *
     * @return El nombre de la lista de compra.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la lista de compra.
     *
     * @param nombre El nombre de la lista de compra.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la fecha de creación de la lista de compra.
     *
     * @return La fecha de creación en formato de timestamp.
     */
    public long getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de creación de la lista de compra.
     *
     * @param fecha La fecha de creación en formato de timestamp.
     */
    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el supermercado asociado con la lista de compra.
     *
     * @return El supermercado.
     */
    public String getSupermercado() {
        return supermercado;
    }

    /**
     * Establece el supermercado asociado con la lista de compra.
     *
     * @param supermercado El supermercado.
     */
    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }

    /**
     * Obtiene el conjunto de productos incluidos en la lista de compra.
     *
     * @return El conjunto de productos.
     */
    public Set<ProductoBD> getProductos() {
        return productos;
    }

    /**
     * Establece el conjunto de productos incluidos en la lista de compra.
     *
     * @param productos El conjunto de productos.
     */
    public void setProductos(Set<ProductoBD> productos) {
        this.productos = productos;
    }
}
