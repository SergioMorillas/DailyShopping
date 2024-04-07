package com.lista.listacompra.supermercado;

import java.util.ArrayList;

public interface Supermercado {
    public static final String MERCADONA_API_URL = "https://7uzjkl1dj0-dsn.algolia.net/1/indexes/" +
            "products_prod_4315_es/query?x-algolia-application-id=7UZJKL1DJ0&x-algolia-api-key=" +
            "9d8f2e39e90df472b4f2e559a116fe17";
    public static final String ALCAMPO_API_URL = "https://www.compraonline.alcampo.es/api/v5/products/search?limit=100&offset=0&term=";
    public static final String DIA_API_URL = "https://www.dia.es/api/v1/search-back/search/reduced?q=";
    public abstract ArrayList<Product> search(String producto);
}
