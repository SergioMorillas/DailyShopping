package com.lista.listacompra.accesoDatos.apiSupermercados;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Alcampo implements Supermercado {
    @Override
    public ArrayList<ProductoBD> search(String producto) {
        ArrayList<ProductoBD> products = new ArrayList<>();
        try {
            URL url = new URL(ALCAMPO_API_URL + producto);

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

    private static ArrayList<ProductoBD> returnProduct(String json) {
        ArrayList<ProductoBD> products = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json.toString());
            JsonNode productNode = jsonNode
                    .path("entities")
                    .path("product");

            for (JsonNode node : productNode) //Iteramos por todos los nodos y rellenamos el arraylist
                products.add(creteProduct(node));

        } catch (Exception ex) {

        }
        return products;
    }

    private static ProductoBD creteProduct(JsonNode nodo) {
        String id = nodo.path("productId").asText();
        Double price = nodo
                .path("price")
                .path("current")
                .path("amount").asDouble();
        Double pricePerKilo = nodo
                .path("price")
                .path("unit")
                .path("current")
                .path("amount").asDouble();
        String name = nodo
                .path("name").asText();
        Double mass = nodo
                .path("size")
                .path("value").asDouble();
        String image = nodo
                .path("image")
                .path("src").asText();

        ProductoBD p = new ProductoBD(id, image, name, price, pricePerKilo, mass);
        return p;
    }
}