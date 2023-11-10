package com.lista.listacompra.supermercado;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Alcampo implements Supermercado {

    @Override
    public Map<Double, String[]> busqueda(String producto) {
        HashMap<Double, String[]> productos = new HashMap<>();
        try {
            String enlace = "https://www.compraonline.alcampo.es/api/v5/products/search?limit=10&offset=0&term=";
            URL url = new URL(enlace + producto);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                    stringBuilder.append(line);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(stringBuilder.toString());
                JsonNode productNode = jsonNode
                        .path("entities")
                        .path("product");

                for (JsonNode node : productNode) {
                    String nombre = modificaString(node.get("name").toString());
                    String precio = modificaString(node.get("price").get("current").get("amount").toString());
                    String urlImagen = modificaString(node.get("image").get("src").toString());
                    productos.put(Double.parseDouble(precio), new String[]{nombre, urlImagen});
                }
                return productos;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productos;
    }


    private String modificaString(String entrada) {
        if (entrada.startsWith("\"") && entrada.endsWith("\"")) {
            return entrada.substring(1, entrada.length() - 1);
        } else {
            return entrada;
        }
    }
}
