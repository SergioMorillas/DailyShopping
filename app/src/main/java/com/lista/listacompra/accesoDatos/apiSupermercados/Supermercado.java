package com.lista.listacompra.accesoDatos.apiSupermercados;

import com.lista.listacompra.accesoDatos.baseDatos.ProductoBD;

import java.util.ArrayList;
import java.util.Set;

public interface Supermercado {
    /** URL del API del mercadona*/
    String MERCADONA_API_URL = "https://7uzjkl1dj0-dsn.algolia.net/1/indexes/" +
            "products_prod_4315_es/query?x-algolia-application-id=7UZJKL1DJ0&x-algolia-api-key=" +
            "9d8f2e39e90df472b4f2e559a116fe17";

    /** URL del API del alcampo*/
    String ALCAMPO_API_URL = "https://www.compraonline.alcampo.es/api/v5/products/search?term=";

    /** URL del API del dia*/
    String DIA_API_URL = "https://www.dia.es/api/v1/search-back/search/reduced?q=";

    String CARREFOUR_API_URL = "https://www.carrefour.es/search-api/query/v1/search?lang=es&query=";
    String BM_API_URL = "https://www.online.bmsupermercados.es/api/rest/V1.0/catalog/searcher/products?q=";

    /**
     * Metodo que busca un producto especifico en el API del mercadona
     *  @param product String que contiene el nombre del producto a buscar
     *  @return Devuelve un set de {@link ProductoBD} con los que haya encontrado
     */
    Set<ProductoBD> search(String product);

    /**
     * Metodo por defecto que devuelve si un producto existe en un supermercado o no
     *  @param product String que contiene el nombre del producto a buscar
     * @return true en caso de que exista, false en caso contrario
     */
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