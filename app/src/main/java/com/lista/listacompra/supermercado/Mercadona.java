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
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Mercadona implements Supermercado {

    @Override
    public ArrayList<Producto> busqueda(String producto) {
        try {
            OkHttpClient cliente = new OkHttpClient();
            URL url = new URL(MERCADONA_API_URL);
            HttpsURLConnection conexion = (HttpsURLConnection) url.openConnection();
            String json = "{\"params\":\"query=" + producto + "&hitsPerPage=10\"}";
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setRequestProperty("Accept", "application/json");

            try (OutputStream os = conexion.getOutputStream()) {
                byte[] input = json.getBytes();
                os.write(input, 0, input.length);
            }
            String respuestaBajo;
            try (BufferedReader br = new BufferedReader(
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
            return devuelveProductos(respuestaBajo);
        } catch (Exception e) {
            System.err.println("Error buscando productos en el API del mercadona: " + e.getMessage());
            return null;
        }
    }

    private static Producto creaProducto(JsonNode nodo) {
        String id = nodo
                .path("id").asText();
        Double precio = nodo
                .path("price_instructions")
                .path("unit_price").asDouble();
        Double precioKilo = nodo
                .path("price_instructions")
                .path("reference_price").asDouble();
        String nombre = nodo
                .path("display_name").asText();
        Double peso = nodo
                .path("price_instructions")
                .path("unit_size").asDouble();
        String imagen = nodo
                .path("thumbnail").asText();
        Producto p = new Producto(id, imagen, nombre, precio, precioKilo, peso);
        return p;
    }

    private static ArrayList<Producto> devuelveProductos(String json) {
        ArrayList<Producto> productos = new ArrayList<>();

        try {
            ObjectMapper mapeador = new ObjectMapper();
            JsonNode root = mapeador.readTree(json);

            JsonNode hitsNode = root.get("hits");
            Iterator<JsonNode> hitsIterator = hitsNode.elements();

            while (hitsIterator.hasNext()) {
                JsonNode nodo = hitsIterator.next();
                productos.add(creaProducto(nodo));
            }
        } catch (Exception e) {
            System.err.println("Error en la conversion del JSON a mapa: " + e.getMessage());
        }
        return productos;
    }
}