package com.figstreet.biz.vendorcontact;

import com.figstreet.data.vendorcontact.VendorContactID;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
public class VendorContactIDJsonAdapterTest {
    @Test
    public void readTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(true);
        when(jsonReader.nextString()).thenReturn("1");
        VendorContactIDJsonAdapter adapter = new VendorContactIDJsonAdapter();
        VendorContactID result = adapter.read(jsonReader);
        assertEquals(1L, (long) result.getValue());
    }

    @Test
    public void readReturningNullTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(false);
        VendorContactIDJsonAdapter adapter = new VendorContactIDJsonAdapter();
        VendorContactID result = adapter.read(jsonReader);
        assertNull(result);
    }
}
