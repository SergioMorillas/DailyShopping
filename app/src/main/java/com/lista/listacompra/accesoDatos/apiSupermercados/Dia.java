package com.lista.listacompra.accesoDatos.apiSupermercados;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class Dia implements Supermercado {
    @Override
    public Set<ProductoBD> search(String producto) {
        Set<ProductoBD> products = new HashSet<>();
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

    private static Set<ProductoBD> returnProduct(String json) {
        Set<ProductoBD> products = new HashSet<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json.toString());
            JsonNode productNode = jsonNode
                    .path("search_items");

            for (JsonNode node : productNode) //Iteramos por todos los nodos y rellenamos el Set
                products.add(creteProduct(node));

        } catch (Exception ex) {

        }
        return products;
    }

    private static ProductoBD creteProduct(JsonNode nodo) {
        String id = nodo.path("object_id").asText();
        Double price = nodo
                .path("prices")
                .path("price").asDouble();
        // Double pricePerKilo = nodo No incluye el precio por kilo
        //        .path("prices")
        //        .path("price").asDouble();
        String name = nodo
                .path("display_name").asText();
        //Double mass = nodo El peso lo incluye en el nombre
        //        .path("size")
        //        .path("value").asDouble();
        String image = "https://www.dia.es" + nodo
                .path("image").asText();

        ProductoBD p = new ProductoBD(id, image, name, price);
        return p;
    }
}
