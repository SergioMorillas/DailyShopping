package com.lista.listacompra.modelo;

import com.lista.listacompra.accesoDatos.apiSupermercados.Supermercado;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class SupermercadosFactoria {
    String nombre;
    Supermercado supermercado;
    public void crearSupermercado(SupermercadosDisponibles smd) {
        this.nombre = smd.name();
        try {
            Constructor<? extends Supermercado> constructor = smd.getClazz().getDeclaredConstructor();
            this.supermercado= constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public String getNombre(){
        return nombre;
    }
    public Set<Producto> busqueda(String nombre){
        Set<ProductoBD> productos;
        Set<Producto> productosAux=new HashSet<>();

        if(supermercado!=null){
            productos=supermercado.search(nombre);
            for (ProductoBD p :productos ) {
                productosAux.add(new Producto(p));
            }
        }

        return productosAux;
    }
}
