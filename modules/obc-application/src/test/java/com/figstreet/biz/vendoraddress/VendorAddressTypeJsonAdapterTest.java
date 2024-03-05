package com.figstreet.biz.vendoraddress;

import com.figstreet.data.vendoraddress.VendorAddressID;
import com.figstreet.data.vendoraddress.VendorAddressType;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;


import java.io.IOException;

public class VendorAddressTypeJsonAdapterTest {
    @Test
    public void readTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(true);
        when(jsonReader.nextString()).thenReturn("1");
        VendorAddressTypeJsonAdapter adapter = new VendorAddressTypeJsonAdapter();
        VendorAddressType result = adapter.read(jsonReader);
        assertEquals(result, VendorAddressType.newInstance("1"));
    }

    @Test
    public void readReturningNullTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(false);
        VendorAddressTypeJsonAdapter adapter = new VendorAddressTypeJsonAdapter();
        VendorAddressType result = adapter.read(jsonReader);
        assertNull(result);
    }
}
