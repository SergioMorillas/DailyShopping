package com.lista.listacompra.accesoDatos.apiSupermercados;

import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.util.ArrayList;
import java.util.Set;

public interface Supermercado {
    public static final String MERCADONA_API_URL = "https://7uzjkl1dj0-dsn.algolia.net/1/indexes/" +
            "products_prod_4315_es/query?x-algolia-application-id=7UZJKL1DJ0&x-algolia-api-key=" +
            "9d8f2e39e90df472b4f2e559a116fe17";
    public static final String ALCAMPO_API_URL = "https://www.compraonline.alcampo.es/api/v5/products/search?term=";
    public static final String DIA_API_URL = "https://www.dia.es/api/v1/search-back/search/reduced?q=";
    public abstract Set<ProductoBD> search(String product);
    default boolean exist(String product){
        boolean sw = false;
        try{
            if (this.search(product).size()>0) {
                sw = true;
            }
        } catch(Exception ex){}
        return sw;
    }
}