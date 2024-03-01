package com.lista.listacompra.supermercado;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Mercadona implements Supermercado {

    @Override
    public Map<Double, String[]> busqueda(String producto) {
        try {
            OkHttpClient cliente = new OkHttpClient();
            URL url = new URL(MERCADONA_API_URL);
            HttpsURLConnection conexion = (HttpsURLConnection) url.openConnection();
            String json = "{\"params\":\"query=" + producto
                    + "&clickAnalytics=true&analyticsTags=%5B%22web%22%5D&getRankingInfo=true&hitsPerPage=10\"}";
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setRequestProperty("Accept", "application/json");

            try(OutputStream os = conexion.getOutputStream()) {
                byte[] input = json.getBytes();
                os.write(input, 0, input.length);
            }
            String respuestaBajo;
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(conexion.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                respuestaBajo = response.toString();
                System.out.println(response.toString());
            }
            Request peticion = new Request.Builder()
                    .url(MERCADONA_API_URL)
                    .post(RequestBody.create(json,
                            MediaType.parse("application/json")))
                    .build();

            Response respuesta = cliente.newCall(peticion).execute();
            return devuelveMapa(respuestaBajo);
        } catch (Exception e) {
            System.err.println("Error buscando productos en el API del mercadona: " + e.getMessage());
            return null;
        }
    }

    private static Map<Double, String[]> devuelveMapa(String json) {
        Map<Double, String[]> productos = new HashMap<>();

        try {
            ObjectMapper mapeador = new ObjectMapper();
            JsonNode root = mapeador.readTree(json);

            JsonNode hitsNode = root.get("hits");
            Iterator<JsonNode> hitsIterator = hitsNode.elements();

            while (hitsIterator.hasNext()) {
                JsonNode hitNode = hitsIterator.next();

                Double precio = hitNode.path("price_instructions").path("unit_price").asDouble();
                String nombre = hitNode.path("display_name").asText();
                String url = hitNode.path("thumbnail").asText();

                String[] productInfo = {nombre, url};
                productos.put(precio, productInfo);
            }
        } catch (Exception e) {
            System.err.println("Error en la conversion del JSON a mapa: " + e.getMessage());
        }
        return productos;
    }
}
