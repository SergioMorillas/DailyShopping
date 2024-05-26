package com.lista.listacompra;

import org.junit.Test;

import static org.junit.Assert.*;

import com.lista.listacompra.modelo.apiSupermercados.Mercadona;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    Mercadona m = new Mercadona();
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}