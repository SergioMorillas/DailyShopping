package com.lista.listacompra.supermercado;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Dia extends Supermercado {
    @Override
    public ArrayList<Product> search(String producto) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            URL url = new URL(DIA_API_URL + producto);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                    stringBuilder.append(line);
                products = returnProduct(stringBuilder.toString());
                return products;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    private static ArrayList<Product> returnProduct(String json) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json.toString());
            JsonNode productNode = jsonNode
                    .path("search_items");

            for (JsonNode node : productNode) //Iteramos por todos los nodos y rellenamos el arraylist
                products.add(creteProduct(node));

        } catch (Exception ex) {

        }
        return products;
    }

    private static Product creteProduct(JsonNode nodo) {
        String id = nodo.path("object_id").asText();
        Double price = nodo
                .path("prices")
                .path("price").asDouble();
        // Double pricePerKilo = nodo No incluye el precio por kilo
        //        .path("prices")
        //        .path("price").asDouble();
        String name = nodo
                .path("displayName").asText();
        //Double mass = nodo El peso lo incluye en el nombre
        //        .path("size")
        //        .path("value").asDouble();
        String image = "https://www.dia.es" + nodo
                .path("image").asText();

        Product p = new Product(id, image, name, price);
        return p;
    }
}
