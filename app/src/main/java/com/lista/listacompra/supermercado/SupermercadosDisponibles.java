package com.lista.listacompra.supermercado;

/**
 * Enumeracion que contiene todos los supermercados pertenecientes a la aplicación
 */
public enum SupermercadosDisponibles {
    Mercadona(Mercadona.class),
    Dia(Dia.class),
    Alcampo(Alcampo.class);

    private final Class<? extends Supermercado> clazz;

    SupermercadosDisponibles(Class<? extends Supermercado> clazz) {
        this.clazz = clazz;
    }
    public Class<? extends Supermercado> getClazz() {
        return clazz;
    }

    /**
     * Metodo que devuelve los supermercados
     * @return Un array de Strings conteniendo los nombres de los supermercados disponibles
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
