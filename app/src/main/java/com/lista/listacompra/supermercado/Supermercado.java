package com.lista.listacompra.supermercado;

import java.util.Map;

public interface Supermercado {
    public abstract Map<Double, String[]> busqueda(String producto);
}
