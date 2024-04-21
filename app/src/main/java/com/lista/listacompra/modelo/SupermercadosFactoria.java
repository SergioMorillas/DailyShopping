package com.lista.listacompra.modelo;

import com.lista.listacompra.accesoDatos.apiSupermercados.Supermercado;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
    public List<Producto> busqueda(String nombre){
        List<ProductoBD> productos=new ArrayList<>();
        List<Producto> productosAux=new ArrayList<>();

        if(supermercado!=null){
            productos=supermercado.search(nombre);
            for (ProductoBD p :productos ) {
                productosAux.add(new Producto(p));
            }
        }

        return productosAux;
    }
}
