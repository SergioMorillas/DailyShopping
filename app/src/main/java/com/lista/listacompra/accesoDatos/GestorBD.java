package com.lista.listacompra.accesoDatos;

import android.content.Context;

import com.lista.listacompra.accesoDatos.baseDatos.AppDatabase;
import com.lista.listacompra.accesoDatos.baseDatos.ListaCompraBD;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;
import com.lista.listacompra.modelo.ListaCompra;

import java.util.List;

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
    public List<ListaCompraBD>  getTodasListas(){
        return database.listaCompraDao().getAllListasCompra();
    }
}