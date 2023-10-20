package com.lista.listacompra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.lang.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button but;
    TextView text, valores, palabra;
    LinearLayout lin;
    int n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        but = (Button) findViewById(R.id.buttonSearch);
        text = (TextView) findViewById(R.id.textViewBusqueda);
        valores = (TextView) findViewById(R.id.guardaValores);
        palabra = (TextView) findViewById(R.id.editTextItem);
        lin = (LinearLayout) findViewById(R.id.linearLayout);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);

                ArrayList<String[]> productos = agregarElementoDesdeURL(palabra.getText().toString());
                    if (lin != null && lin.getChildCount() > 0)
                        lin.removeAllViews();
                for (String[] s:
                     productos) {
                    TextView nombrePrecio = new TextView(getApplicationContext());
                    ImageView imagen = new ImageView(getApplicationContext());
                    Picasso.get().load(s[1]).into(imagen);
                    nombrePrecio.setText(s[0]);
                    lin.addView(nombrePrecio);
                    lin.addView(imagen);
                }
            }
        });
    }

    private ArrayList<String[]> agregarElementoDesdeURL(String i) {
        ArrayList<String[]> productos = new ArrayList<>();
        try {
            String enlace = "https://www.compraonline.alcampo.es/api/v5/products/search?limit=10&offset=0&term=";
            URL url = new URL(enlace + i);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(stringBuilder.toString());
                JsonNode productNode = jsonNode
                        .path("entities")
                        .path("product");

                for (JsonNode node : productNode) {
                    String nombre = node.get("name").toString();
                    String precio = node.get("price").get("current").get("amount").toString();
                    String urlImagen = node.get("image").get("src").toString();
                    String total = "Nombre: " + nombre + "\n\tPrecio: " + precio + "€\n";
                    productos.add(new String[]{total, urlImagen});
                }
                return productos;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productos;
    }
}