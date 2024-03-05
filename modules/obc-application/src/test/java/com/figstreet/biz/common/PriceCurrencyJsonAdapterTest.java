package com.figstreet.biz.common;

import com.figstreet.data.common.PriceCurrency;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
public class PriceCurrencyJsonAdapterTest {
    @Test
    public void readTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(true);
        when(jsonReader.nextString()).thenReturn("1");
        PriceCurrencyJsonAdapter adapter = new PriceCurrencyJsonAdapter();
        PriceCurrency result = adapter.read(jsonReader);
        assertEquals("1", result.getValue());
    }

    @Test
    public void readReturningNullTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(false);
        PriceCurrencyJsonAdapter adapter = new PriceCurrencyJsonAdapter();
        PriceCurrency result = adapter.read(jsonReader);
        assertNull(result);
    }
}
