package com.lista.listacompra.accesoDatos.baseDatos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.lista.listacompra.modelo.Producto;

import java.util.Objects;

/**
 * Entidad que representa un producto en la base de datos.
 * Esta clase se utiliza para almacenar información sobre productos en la tabla "productos".
 */
@Entity(tableName = "productos")
public class ProductoBD {

    /**
     * Identificador único del producto.
     */
    @PrimaryKey
    @ColumnInfo(name="id")
    @NonNull
    private String id;

    /**
     * URL de la imagen del producto.
     */
    @ColumnInfo(name = "image")
    private String image;

    /**
     * Nombre del producto.
     */
    @ColumnInfo(name = "name")
    private String name;

    /**
     * Precio del producto.
     */
    @ColumnInfo(name = "price")
    private double price;

    /**
     * Precio por kilo del producto.
     */
    @ColumnInfo(name = "price_per_kilo")
    private double pricePerKilo;

    /**
     * Peso del producto.
     */
    @ColumnInfo(name = "mass")
    private double mass;

    /**
     * Cantidad del producto.
     */
    @ColumnInfo(name = "amount")
    private int amount;

    /**
     * Indica si el producto está marcado.
     */
    @ColumnInfo(name = "marked")
    private boolean isMarked;

    /**
     * Constructor completo para crear un producto con todos sus atributos.
     *
     * @param id Identificador único del producto.
     * @param imagen URL de la imagen del producto.
     * @param nombre Nombre del producto.
     * @param precio Precio del producto.
     * @param precioKilo Precio por kilo del producto.
     * @param peso Peso del producto.
     * @param amount Cantidad del producto.
     * @param isMarked Indica si el producto está marcado.
     */
    public ProductoBD(String id, String imagen, String nombre, double precio, double precioKilo, double peso, int amount, boolean isMarked) {
        this.id = id;
        this.image = imagen;
        this.name = nombre;
        this.price = precio;
        this.pricePerKilo = precioKilo;
        this.mass = peso;
        this.amount = amount;
        this.isMarked = isMarked;
    }

    /**
     * Constructor que crea un producto a partir de una instancia de {@link Producto}.
     *
     * @param p Instancia de {@link Producto}.
     */
    public ProductoBD(Producto p) {
        this.image = p.getImage();
        this.name = p.getName();
        this.price = p.getPrice();
        this.pricePerKilo = p.getPricePerKilo();
        this.mass = p.getMass();
        this.amount = p.getAmount();
        this.isMarked = p.isMarked();
    }

    /**
     * Constructor simplificado para crear un producto con atributos básicos.
     *
     * @param id Identificador único del producto.
     * @param imagen URL de la imagen del producto.
     * @param nombre Nombre del producto.
     * @param precio Precio del producto.
     */
    public ProductoBD(String id, String imagen, String nombre, double precio) {
        this.id = id;
        this.image = imagen;
        this.name = nombre;
        this.price = precio;
        this.pricePerKilo = -1; // -1 Significa que no aplica para ese producto
        this.mass = -1;
        this.isMarked = false;
        this.amount = 1;
    }

    /**
     * Constructor que crea un producto con solo un identificador.
     *
     * @param id Identificador único del producto.
     */
    public ProductoBD(String id) {
        this.id = id;
    }

    /**
     * Constructor por defecto que crea un producto con un mensaje de no encontrado.
     */
    public ProductoBD() {
        this.name = "ProductoBD no encontrado";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPricePerKilo() {
        return pricePerKilo;
    }

    public void setPricePerKilo(double pricePerKilo) {
        this.pricePerKilo = pricePerKilo;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductoBD)) return false;
        ProductoBD producto = (ProductoBD) o;
        return Double.compare(producto.getPrice(), getPrice()) == 0 &&
                Double.compare(producto.getMass(), getMass()) == 0 &&
                Objects.equals(getName(), producto.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getMass());
    }

    @Override
    public String toString() {
        return "ProductoBD{" +
                "id='" + id + "'\n\t" +
                ", image='" + image + "'\n\t" +
                ", name='" + name + "'\n\t" +
                ", price=" + price + "\n\t" +
                ", pricePerKilo=" + pricePerKilo + "\n\t" +
                ", mass=" + mass + "\n\t" +
                ", amount=" + amount + "\n\t" +
                ", isMarked=" + isMarked + "\n\t" +
                '}';
    }
}
