package com.lista.listacompra.modelo;

import com.lista.listacompra.accesoDatos.apiSupermercados.BM;
import com.lista.listacompra.accesoDatos.apiSupermercados.Carrefour;
import com.lista.listacompra.accesoDatos.apiSupermercados.Supermercado;
import com.lista.listacompra.accesoDatos.apiSupermercados.Mercadona;
import com.lista.listacompra.accesoDatos.apiSupermercados.Dia;
import com.lista.listacompra.accesoDatos.apiSupermercados.Alcampo;

/**
 * Enumeración que contiene todos los supermercados disponibles en la aplicación.
 */
public enum SupermercadosDisponibles {
    Mercadona(Mercadona.class),
    Dia(Dia.class),
    Alcampo(Alcampo.class),
    Carrefour(Carrefour.class),
    BM(BM.class);

    /**
     * Clase asociada con el supermercado.
     */
    private final Class<? extends Supermercado> clazz;

    /**
     * Constructor de SupermercadosDisponibles.
     *
     * @param clazz La clase asociada con el supermercado.
     */
    SupermercadosDisponibles(Class<? extends Supermercado> clazz) {
        this.clazz = clazz;
    }

    /**
     * Obtiene la clase asociada con el supermercado.
     *
     * @return La clase asociada con el supermercado.
     */
    public Class<? extends Supermercado> getClazz() {
        return clazz;
    }

    /**
     * Obtiene los nombres de todos los supermercados disponibles.
     *
     * @return Un array de Strings conteniendo los nombres de los supermercados disponibles.
     */
    public static String[] getStringValues() {
        SupermercadosDisponibles[] supermarket = SupermercadosDisponibles.values();
        String[] names = new String[supermarket.length];
        for (int i = 0; i < supermarket.length; i++) {
            names[i] = supermarket[i].toString();
        }
        return names;
    }
}
