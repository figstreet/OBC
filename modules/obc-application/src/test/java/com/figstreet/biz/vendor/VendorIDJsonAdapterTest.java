package com.figstreet.biz.vendor;

import com.figstreet.data.vendor.VendorID;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class VendorIDJsonAdapterTest {
    @Test
    public void readTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(true);
        when(jsonReader.nextString()).thenReturn("1");
        VendorIDJsonAdapter adapter = new VendorIDJsonAdapter();
        VendorID result = adapter.read(jsonReader);
        assertEquals(1L, (long) result.getValue());
    }

    @Test
    public void readReturningNullTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(false);
        VendorIDJsonAdapter adapter = new VendorIDJsonAdapter();
        VendorID result = adapter.read(jsonReader);
        assertNull(result);
    }
}
