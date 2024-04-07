package com.lista.listacompra.supermercado;

import java.util.Objects;

public class Product implements Comparable<Product> {
    private String id;
    private String image;
    private String name;
    private double price;
    private double pricePerKilo;
    private double mass;

    public Product(String id, String imagen, String nombre, double precio, double precioKilo, double peso) {
        this.id = id;
        this.image = imagen;
        this.name = nombre;
        this.price = precio;
        this.pricePerKilo = precioKilo;
        this.mass = peso;
    }

    public Product(String id, String imagen, String nombre, double precio) {
        this.id = id;
        this.image = imagen;
        this.name = nombre;
        this.price = precio;
        this.pricePerKilo = -1; // -1 Significa que no aplica para ese producto
        this.mass = -1;
    }

    public Product(String id) {
        this.id = id;
    }
    public Product(){}

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
        if (!(o instanceof Product)) return false;
        Product producto = (Product) o;
        return getPrice() == producto.getPrice() && getMass() == producto.getMass() && Objects.equals(getName(), producto.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getMass());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + "'\n\t" +
                ", image='" + image + "'\n\t" +
                ", name='" + name + "'\n\t" +
                ", price=" + price + "\n\t" +
                ", pricePerKilo=" + pricePerKilo + "\n\t" +
                ", mass=" + mass + "\n\t" +
                '}';
    }

    @Override
    public int compareTo(Product o) { // >1 primero mayor, 0 iguales, <1 si el segundo mayor
        if (this.getPrice() - o.getPrice() == 0) {
            if (this.getPricePerKilo() - o.getPricePerKilo() == 0) {
                return 0;
            } else {
                return (int) (this.getPricePerKilo() - o.getPricePerKilo());
            }
        } else {
            return (int)(this.getPrice() - o.getPrice());
        }
    }
}
