package com.lista.listacompra.modelo.baseDatos;

import androidx.room.TypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Set;

/**
 * Convertidor para la entidad {@link ProductoBD} que convierte entre
 * JSON y un conjunto de objetos {@link ProductoBD}.
 */
public class ConversorProducto {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Convierte una cadena JSON en un conjunto de objetos {@link ProductoBD}.
     *
     * @param value La cadena JSON que representa el conjunto de productos.
     * @return Un conjunto de objetos {@link ProductoBD} o {@code null} si ocurre un error.
     */
    @TypeConverter
    public static Set<ProductoBD> fromJson(String value) {
        if (value == null) {
            return null;
        }

        try {
            return objectMapper.readValue(value, new TypeReference<Set<ProductoBD>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convierte un conjunto de objetos {@link ProductoBD} en una cadena JSON.
     *
     * @param productos El conjunto de productos a convertir.
     * @return Una cadena JSON que representa el conjunto de productos o {@code null} si ocurre un error.
     */
    @TypeConverter
    public static String toJson(Set<ProductoBD> productos) {
        if (productos == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(productos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
