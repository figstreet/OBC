package com.figstreet.biz.common;

import com.figstreet.data.common.ProductCondition;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
public class ProductConditionJsonAdapterTest {
    @Test
    public void readTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(true);
        when(jsonReader.nextString()).thenReturn("1");
        ProductConditionJsonAdapter adapter = new ProductConditionJsonAdapter();
        ProductCondition result = adapter.read(jsonReader);
        assertEquals("1", result.getValue());
    }

    @Test
    public void readReturningNullTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(false);
        ProductConditionJsonAdapter adapter = new ProductConditionJsonAdapter();
        ProductCondition result = adapter.read(jsonReader);
        assertNull(result);
    }
}
