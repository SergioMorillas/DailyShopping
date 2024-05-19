package com.lista.listacompra.accesoDatos.apiSupermercados;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class Mercadona implements Supermercado {
    /**
     * Metodo heredado de la interfáz supermercado, busca un producto especifico en el API del mercadona
     * @param producto String que contiene el nombre del producto a buscar
     * @return Devuelve un set de {@link ProductoBD} con los que haya encontrado
     */
    @Override
    public Set<ProductoBD> search(String producto) {
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

    /**
     * Metodo que recibe una String con la busqueda de un supermercado y devuelve un set de {@link ProductoBD}
     * @param json el JSON con todos los productos del supermercado según el nombre
     * @return Un set de {@link ProductoBD}
     */
    private static Set<ProductoBD> returnProducts(String json) {
        Set<ProductoBD> products = new HashSet<>();

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

    /**
     * Metodo que en base a cada nodo del JSON devuelto por la busqueda de un prodcuto crea un {@link ProductoBD}
     * @param nodo El nodo JSON ya preparado con los datos de un producto
     * @return El objeto ProductoBD
     */
    private static ProductoBD createProduct(JsonNode nodo) {

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
        return new ProductoBD(id, image, name, price, pricePerKilo, mass,1, false);

    }
}