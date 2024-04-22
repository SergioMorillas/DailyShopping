package com.lista.listacompra.modelo;

import android.content.Context;

import com.lista.listacompra.accesoDatos.GestorBD;
import com.lista.listacompra.accesoDatos.baseDatos.ListaCompraBD;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

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

    public List<ListaCompra> getTodaslistas() {
        List<ListaCompraBD> lista = gestorBD.getTodasListas();
        List<ListaCompra> listaAux = new ArrayList<>();
        if (lista != null) {
            for (ListaCompraBD listaCompra : lista) {
                listaAux.add(new ListaCompra(listaCompra));
            }
        }
        return listaAux;
    }
}