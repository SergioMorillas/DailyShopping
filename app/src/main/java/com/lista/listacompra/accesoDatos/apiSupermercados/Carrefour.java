package com.lista.listacompra.accesoDatos.apiSupermercados;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class Carrefour implements Supermercado {
    /**
     * Metodo heredado de la interfáz supermercado, busca un producto especifico en el API del mercadona
     * @param producto String que contiene el nombre del producto a buscar
     * @return Devuelve un set de {@link ProductoBD} con los que haya encontrado
     */
    @Override
    public Set<ProductoBD> search(String producto) {
        Set<ProductoBD> products = new HashSet<>();
        try {
            URL url = new URL(CARREFOUR_API_URL + producto);

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
    /**
     * Metodo que recibe una String con la busqueda de un supermercado y devuelve un set de {@link ProductoBD}
     * @param json el JSON con todos los productos del supermercado según el nombre
     * @return Un set de {@link ProductoBD}
     */
    private static Set<ProductoBD> returnProduct(String json) {
        Set<ProductoBD> products = new HashSet<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);
            JsonNode productNode = jsonNode
                    .path("content")
                    .path("docs");

            for (JsonNode node : productNode) //Iteramos por todos los nodos y rellenamos el arraylist
                products.add(creteProduct(node));

        } catch (Exception ex) {

        }
        return products;
    }
    /**
     * Metodo que en base a cada nodo del JSON devuelto por la busqueda de un prodcuto crea un {@link ProductoBD}
     * @param nodo El nodo JSON ya preparado con los datos de un producto
     * @return El objeto ProductoBD
     */
    private static ProductoBD creteProduct(JsonNode nodo) {
        String id = nodo.path("catalog_ref_id").asText();
        Double price = nodo
                .path("active_price").asDouble();
        Double pricePerKilo = calcularPrecioKilo(nodo);
        String name = nodo
                .path("display_name").asText();
        Double mass = nodo
                .path("average_weight").asDouble();
        String image = nodo
                .path("image_path").asText();

        ProductoBD p = new ProductoBD(id, image, name, price, pricePerKilo, mass, 1, false);
        return p;
    }

    private static double calcularPrecioKilo(JsonNode nodo) {
        double precio = nodo
                .path("active_price").asDouble();
        double peso = nodo
                .path("average_weight").asDouble();
        peso = peso/1000;
        return Math.round((precio/peso) * 100.0) / 100.0;


    }

}
