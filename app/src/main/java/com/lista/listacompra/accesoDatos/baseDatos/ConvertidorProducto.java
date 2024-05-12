package com.lista.listacompra.accesoDatos.baseDatos;

import androidx.room.TypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Set;

public class ConvertidorProducto {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
