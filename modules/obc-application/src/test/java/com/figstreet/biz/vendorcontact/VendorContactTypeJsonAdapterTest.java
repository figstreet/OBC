package com.figstreet.biz.vendorcontact;


import com.figstreet.data.vendoraddress.VendorAddressType;
import com.figstreet.data.vendorcontact.VendorContactType;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class VendorContactTypeJsonAdapterTest {
    @Test
    public void readTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(true);
        when(jsonReader.nextString()).thenReturn("1");
        VendorContactTypeJsonAdapter adapter = new VendorContactTypeJsonAdapter();
        VendorContactType result = adapter.read(jsonReader);
        assertEquals(result, VendorContactType.newInstance("1"));
    }

    @Test
    public void readReturningNullTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(false);
        VendorContactTypeJsonAdapter adapter = new VendorContactTypeJsonAdapter();
        VendorContactType result = adapter.read(jsonReader);
        assertNull(result);
    }
}
