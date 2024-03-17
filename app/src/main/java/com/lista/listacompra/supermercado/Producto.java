package com.lista.listacompra.supermercado;

import java.util.Objects;

public class Producto implements Comparable<Producto> {
    private String id;
    private String imagen;
    private String nombre;
    private double precio;
    private double precioKilo;
    private double peso;

    public Producto(String id, String imagen, String nombre, double precio, double precioKilo, double peso) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.precio = precio;
        this.precioKilo = precioKilo;
        this.peso = peso;
    }

    public Producto(String id, String imagen, String nombre, double precio) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.precio = precio;
        this.precioKilo = -1; // -1 Significa que no aplica para ese producto
        this.peso = -1;
    }

    public Producto(String id) {
        this.id = id;
    }
    public Producto(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPrecioKilo() {
        return precioKilo;
    }

    public void setPrecioKilo(double precioKilo) {
        this.precioKilo = precioKilo;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return getPrecio() == producto.getPrecio() && getPeso() == producto.getPeso() && Objects.equals(getNombre(), producto.getNombre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombre(), getPrecio(), getPeso());
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", imagen='" + imagen + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", precioKilo=" + precioKilo +
                ", peso=" + peso +
                '}';
    }

    @Override
    public int compareTo(Producto o) { // >1 primero mayor, 0 iguales, <1 si el segundo mayor
        if (this.getPrecio() - o.getPrecio() == 0) {
            if (this.getPrecioKilo() - o.getPrecioKilo() == 0) {
                return 0;
            } else {
                return (int) (this.getPrecioKilo() - o.getPrecioKilo());
            }
        } else {
            return (int)(this.getPrecio() - o.getPrecio());
        }
    }
}
