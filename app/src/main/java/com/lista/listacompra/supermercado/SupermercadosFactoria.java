package com.lista.listacompra.supermercado;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SupermercadosFactoria {
    public static Supermercado crearSupermercado(SupermercadosDisponibles nombre) {
        try {
            Constructor<? extends Supermercado> constructor = nombre.getClazz().getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
