package com.lista.listacompra.modelo;

import android.content.Context;

import com.lista.listacompra.accesoDatos.GestorBD;
import com.lista.listacompra.accesoDatos.baseDatos.ListaCompraBD;

import java.util.HashSet;
import java.util.IllegalFormatCodePointException;
import java.util.Set;

public class Gestor {
    private GestorBD gestorBD;

    public Gestor(Context context) {
        this.gestorBD = new GestorBD(context);
    }

    public ListaCompra getListaPorNombre(String nombre) {
        return new ListaCompra(gestorBD.getListaCompra(nombre));
    }

    public void insertaLista(ListaCompra l) {
        gestorBD.insertaLista(l);
    }

    public Set<ListaCompra> getTodaslistas() {
        Set<ListaCompraBD> lista = gestorBD.getTodasListas();
        Set<ListaCompra> listaAux = new HashSet<>();
        if (lista != null) {
            for (ListaCompraBD listaCompra : lista) {
                listaAux.add(new ListaCompra(listaCompra));
            }
        }
        return listaAux;
    }

    public Set<ListaCompra> getBusquedaListasNombre(String nombre) {
        Set<ListaCompraBD> lista = gestorBD.getTodasListasNombre(nombre);
        Set<ListaCompra> listaAux = new HashSet<>();
        if (lista != null) {
            for (ListaCompraBD listaCompra : lista) {
                listaAux.add(new ListaCompra(listaCompra));
            }
        }
        return listaAux;
    }

    public void borrarLista(String nombre, String supermercado, long fecha){
        gestorBD.borrarLista(nombre, supermercado, fecha);
    }
    public void actualizarListaProductos(ListaCompra lista){
        gestorBD.actualizarLista(lista);
    }

}