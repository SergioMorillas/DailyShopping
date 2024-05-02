package com.lista.listacompra.accesoDatos;

import android.content.Context;

import com.lista.listacompra.accesoDatos.baseDatos.AppDatabase;
import com.lista.listacompra.accesoDatos.baseDatos.ListaCompraBD;
import com.lista.listacompra.modelo.ListaCompra;

import java.util.Set;

public class GestorBD {
    Context contexto;
    AppDatabase database;

    public GestorBD(Context contextoAplicacion) {
        this.contexto = contextoAplicacion;
        database = AppDatabase.getDatabase(this.contexto);
    }

    public ListaCompraBD getListaCompra(String nombre) {
        return database.listaCompraDao().getListaCompraByName(nombre);
    }
    public void insertaLista(ListaCompra l){
        database.listaCompraDao().insertListaCompra(new ListaCompraBD(l));
    }
    public Set<ListaCompraBD> getTodasListas(){
        return database.listaCompraDao().getAllListasCompra();
    }

    public Set<ListaCompraBD> getTodasListasNombre(String nombre) {
        return database.listaCompraDao().getAllListasCompraNombre(nombre);

    }

    public void borrarLista(String nombre, String supermercado, long fecha) {
        database.listaCompraDao().deleteListaCompra(nombre, supermercado, fecha);
    }

    public void actualizarLista(ListaCompra lista) {
        try {
            database.runInTransaction(new Runnable() {
                @Override
                public void run() {
                    database.listaCompraDao().deleteListaCompra(lista.getNombre(),
                            lista.getSupermercado(),
                            lista.getFecha());
                    database.listaCompraDao().insertListaCompra(new ListaCompraBD(lista));
                }
            });
        } catch (Exception ex) {
            throw ex;
        }

    }
}