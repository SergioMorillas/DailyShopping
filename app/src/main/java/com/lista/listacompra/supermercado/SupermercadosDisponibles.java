package com.lista.listacompra.supermercado;

public enum SupermercadosDisponibles {
    Mercadona,
    Dia,
    Alcampo;

    /**
     *
     * @return
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
