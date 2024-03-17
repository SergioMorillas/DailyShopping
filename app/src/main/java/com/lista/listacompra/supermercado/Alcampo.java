package com.lista.listacompra.supermercado;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class Alcampo implements Supermercado {
    @Override
    public ArrayList<Producto> busqueda(String producto) {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            URL url = new URL(ALCAMPO_API_URL + producto);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                    stringBuilder.append(line);
                productos = devuelveProductos(stringBuilder.toString());
                return productos;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productos;
    }

    private static ArrayList<Producto> devuelveProductos(String json) {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json.toString());
            JsonNode productNode = jsonNode
                    .path("entities")
                    .path("product");

            for (JsonNode node : productNode) //Iteramos por todos los nodos y rellenamos el arraylist
                productos.add(creaProducto(node));

        } catch (Exception ex) {

        }
        return productos;
    }

    private static Producto creaProducto(JsonNode nodo) {
        String id = nodo.path("productId").asText();
        Double precio = nodo
                .path("price")
                .path("current")
                .path("amount").asDouble();
        Double precioKilo = nodo
                .path("price")
                .path("unit")
                .path("current")
                .path("amount").asDouble();
        String nombre = nodo
                .path("name").asText();
        Double peso = nodo
                .path("size")
                .path("value").asDouble();
        String imagen = nodo
                .path("image")
                .path("src").asText();

        Producto p = new Producto(id, imagen, nombre, precio, precioKilo, peso);
        return p;
    }


    /**
     * Metodo que me elimina las comillas de los diferentes campos de la petición, ya que sin esto
     * las imagenes no se mostrarian
     *
     * @param entrada
     * @return
     */
    private static String modificaString(String entrada) {
        if (entrada.startsWith("\"") && entrada.endsWith("\"")) {
            return entrada.substring(1, entrada.length() - 1);
        } else {
            return entrada;
        }
    }
}
