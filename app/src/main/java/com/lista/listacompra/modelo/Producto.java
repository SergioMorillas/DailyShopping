package com.lista.listacompra.modelo;


import android.content.Context;

import com.lista.listacompra.accesoDatos.GestorBD;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.util.Objects;

public class Producto implements Comparable<Producto> {
    private String image;
    private String name;
    private double price;
    private double pricePerKilo;
    private double mass;
    private int amount;
    private boolean isMarked;
    private GestorBD gestorDB;
    public Producto(ProductoBD producto, Context context) {
        this.image = producto.getImage();
        this.name = producto.getName();
        this.price=producto.getPrice();
        this.pricePerKilo = producto.getPricePerKilo();
        this.mass = producto.getMass();
        this.amount = producto.getAmount();
        this.isMarked = producto.isMarked();
        gestorDB = new GestorBD(context);
    }    public Producto(ProductoBD producto) {
        this.image = producto.getImage();
        this.name = producto.getName();
        this.price=producto.getPrice();
        this.pricePerKilo = producto.getPricePerKilo();
        this.mass = producto.getMass();
        this.amount = producto.getAmount();
        this.isMarked = isMarked;
    }
    public Producto(Context context){

    }

    public Producto() {

    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getPricePerKilo() {
        return pricePerKilo;
    }

    public double getMass() {
        return mass;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount( int amount ) {
        this.amount=amount;
    }

    public boolean isMarked() {
        return isMarked;
    }
    public void setMarked(boolean marked){
        this.isMarked=marked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return Double.compare(producto.getPrice(), getPrice()) == 0 && Double.compare(producto.getMass(), getMass()) == 0 && Objects.equals(getImage(), producto.getImage()) && Objects.equals(getName(), producto.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImage(), getName(), getPrice(), getMass());
    }

    @Override
    public String toString() {
        return "Producto{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", pricePerKilo=" + pricePerKilo +
                ", mass=" + mass +
                '}';
    }

    @Override
    public int compareTo(Producto o) {
        int comparacionPrecio = Double.compare(this.getPrice(), o.getPrice());
        if (comparacionPrecio != 0) {
            return comparacionPrecio;
        }

        int comparacionPrecioPorKilo = Double.compare(this.getPricePerKilo(), o.getPricePerKilo());
        if (comparacionPrecioPorKilo != 0) {
            return comparacionPrecioPorKilo;
        }
        return this.getName().compareTo(o.getName());
    }
}
