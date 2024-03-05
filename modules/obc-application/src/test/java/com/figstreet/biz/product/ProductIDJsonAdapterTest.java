package com.figstreet.biz.product;

import com.figstreet.data.product.ProductID;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;


import java.io.IOException;

public class ProductIDJsonAdapterTest {
    @Test
    public void readTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(true);
        when(jsonReader.nextString()).thenReturn("1");
        ProductIDJsonAdapter adapter = new ProductIDJsonAdapter();
        ProductID result = adapter.read(jsonReader);
        assertEquals(1L, (long) result.getValue());
    }

    @Test
    public void readReturningNullTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(false);
        ProductIDJsonAdapter adapter = new ProductIDJsonAdapter();
        ProductID result = adapter.read(jsonReader);
        assertNull(result);
    }

}
