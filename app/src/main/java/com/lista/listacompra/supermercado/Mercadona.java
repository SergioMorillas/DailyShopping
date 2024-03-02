package com.lista.listacompra.supermercado;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Mercadona implements Supermercado {
    private static final String MERCADONA_API_URL = "https://7uzjkl1dj0-dsn.algolia.net/1/indexes/" + "products_prod_4315_es/query?x-algolia-application-id=7UZJKL1DJ0&x-algolia-api-key=" + "9d8f2e39e90df472b4f2e559a116fe17";

    public Mercadona() {
    }

    @Override
    public Map<Double, String[]> busqueda(String producto) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(MERCADONA_API_URL)
                    .post(RequestBody.create(
                    "{\"params\":\"query=" + producto + "&hitsPerPage=10\"}",
                            MediaType.parse("application/json")))
                    .build();

            Response response = client.newCall(request).execute();
            return devuelveMapa(response.body().string());
        } catch (Exception e) {
            System.err.println("Error buscando productos en el API del mercadona: " + e.getMessage());
            return null;
        }
    }

    private static Map<Double, String[]> devuelveMapa(String json) {
        Map<Double, String[]> productMap = new HashMap<>();

        try {
            ObjectMapper mapeador = new ObjectMapper();
            JsonNode raiz = mapeador.readTree(json);

            JsonNode hits = raiz.get("hits");
            Iterator<JsonNode> hitsIterator = hits.elements();

            while (hitsIterator.hasNext()) {
                JsonNode hitNode = hitsIterator.next();

                Double precio = hitNode.path("price_instructions").path("unit_price").asDouble();
                String nombre = hitNode.path("display_name").asText();
                String descripcion = hitNode.path("thumbnail").asText();

                String[] productInfo = {nombre, descripcion};
                productMap.put(precio, productInfo);
            }
        } catch (Exception e) {
            System.err.println("Error en la conversion del JSON a mapa: " + e.getMessage());
        }

        return productMap;
    }
    //TODO: Cambiar la clave del producto del precio a la URL
    public static void main(String[] args) {
        Mercadona mer = new Mercadona();
        Map m1 = mer.busqueda("pan");
        Alcampo alc = new Alcampo();
        Map m2 =alc.busqueda("pan");
        imprimirMapa(m1);
        imprimirMapa(m2);
    }
    public static void imprimirMapa(Map<Double, String[]> mapa) {
        for (Map.Entry<Double, String[]> entry : mapa.entrySet()) {
            Double clave = entry.getKey();
            String[] valores = entry.getValue();

            System.out.println("Clave: " + clave);

            System.out.print("Valores: ");
            for (String valor : valores) {
                System.out.print(valor + " ");
            }
            System.out.println();
        }
    }

}
