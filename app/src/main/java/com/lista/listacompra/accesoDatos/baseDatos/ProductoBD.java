package com.lista.listacompra.accesoDatos.baseDatos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.lista.listacompra.modelo.Producto;

import java.util.Objects;
@Entity(tableName = "productos")
public class ProductoBD{
    @PrimaryKey
    @ColumnInfo(name="id")
    @NonNull
    private String id;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "price_per_kilo")
    private double pricePerKilo;

    @ColumnInfo(name = "mass")
    private double mass;




    public ProductoBD(String id, String imagen, String nombre, double precio, double precioKilo, double peso) {
        this.id = id;
        this.image = imagen;
        this.name = nombre;
        this.price = precio;
        this.pricePerKilo = precioKilo;
        this.mass = peso;
    }
    public ProductoBD(Producto p) {
        this.image = p.getImage();
        this.name = p.getName();
        this.price = p.getPrice();
        this.pricePerKilo = p.getPricePerKilo();
        this.mass = p.getMass();
    }

    public ProductoBD(String id, String imagen, String nombre, double precio) {
        this.id = id;
        this.image = imagen;
        this.name = nombre;
        this.price = precio;
        this.pricePerKilo = -1; // -1 Significa que no aplica para ese producto
        this.mass = -1;
    }

    public ProductoBD(String id) {
        this.id = id;
    }
    public ProductoBD(){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductoBD)) return false;
        ProductoBD producto = (ProductoBD) o;
        return getPrice() == producto.getPrice() && getMass() == producto.getMass() && Objects.equals(getName(), producto.getName());
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
                '}';
    }

}
