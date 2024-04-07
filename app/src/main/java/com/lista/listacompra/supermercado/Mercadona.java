package com.lista.listacompra.supermercado;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.listacompra.persistencia.Producto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class Mercadona implements Supermercado {

    @Override
    public ArrayList<Producto> search(String producto) {
        try {
            URL url = new URL(MERCADONA_API_URL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            String json = "{\"params\":\"query=" + producto + "\"}";
            connection.setRequestMethod("POST"); // Añadimos el tipo de peticion, este API admite POST
            connection.setRequestProperty("Content-Type", "application/json"); // Añadimos el mime del contenido de la petición
            connection.setRequestProperty("Accept", "application/json");// Añadimos que mime aceptaremos, podría ser */*, pero al saber que es un JSON es mejor de esta manera
            connection.setDoOutput(true); // Añadimos que queremos poder enviar datos a través de la conexión
            try (OutputStream os = connection.getOutputStream()) {
                byte[] output = json.getBytes();
                os.write(output, 0, output.length); // Enviamos la petición
            }
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            return returnProducts(response.toString());
        } catch (Exception e) {
            System.err.println("Error buscando productos en el API del mercadona: " + e.getMessage());
            return null;
        }
    }

    private static Producto createProduct(JsonNode nodo) {

        String id = nodo
                .path("id").asText();
        double price = nodo
                .path("price_instructions")
                .path("unit_price").asDouble();
        double pricePerKilo = nodo
                .path("price_instructions")
                .path("reference_price").asDouble();
        String name = nodo
                .path("display_name").asText();
        double mass = nodo
                .path("price_instructions")
                .path("unit_size").asDouble();
        String image = nodo
                .path("thumbnail").asText();
        return new Producto(id, image, name, price, pricePerKilo, mass);

    }

    private static ArrayList<Producto> returnProducts(String json) {
        ArrayList<Producto> products = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            JsonNode hitsNode = root.get("hits");

            for (JsonNode node :
                    hitsNode) {
                products.add(createProduct(node));
            }
        } catch (Exception e) {
            System.err.println("Error en la conversion del JSON a mapa: " + e.getMessage());
        }
        return products;
    }
}