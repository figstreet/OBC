package com.figstreet.biz.vendoraddress;

import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.common.Country;
import com.figstreet.data.common.Region;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.Vendor;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendoraddress.VendorAddress;
import com.figstreet.data.vendoraddress.VendorAddressID;
import com.figstreet.data.vendoraddress.VendorAddressType;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
@RunWith(PowerMockRunner.class)
@PrepareForTest({JsonDataObject.class})
public class VendorAddressHistoryTest {

    VendorAddressID fVendorAddressID = new VendorAddressID(1);
    VendorID fVendorID = new VendorID(2);
    boolean fActive = true;
    boolean fPrimary = false;
    VendorAddressType fType = VendorAddressType.MAILING_ADDRESS;
    String fAddress1 = "address1";
    String fAddress2 = "address2";
    String fCity = "city1";
    Region fRegion = Region.newInstance("region");
    String fZip = "fzip";
    Country fCountry = Country.US;

    Timestamp fAdded = new Timestamp(1);
    UsersID fAddedBy = UsersID.ADMIN;

    String json = "{\"vdaid\":\"1\",\"vda_active\":true,\"vda_primary\":false,\"vda_type\":\"MAILING\",\"vda_addr1\":\"address1\",\"vda_addr2\":\"address2\",\"vda_city\":\"city1\",\"vda_region\":\"REGION\",\"vda_zip\":\"fzip\",\"vda_country\":\"US\",\"vda_added_dt\":\"1969-12-31T19:00:00.001-0500\",\"vda_added_by\":\"100\"}";

    @Mock
    VendorAddress lastvendor;

    @Mock
    VendorAddress vendor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(vendor.isActive()).thenReturn(fActive);
        when(vendor.getAddress1()).thenReturn(fAddress1);
        when(vendor.getAddress2()).thenReturn(fAddress2);
        when(vendor.getCity()).thenReturn(fCity);
        when(vendor.getCountry()).thenReturn(fCountry);
        when(vendor.getRegion()).thenReturn(fRegion);
        when(vendor.getType()).thenReturn(fType);
        when(vendor.getAdded()).thenReturn(fAdded);
        when(vendor.getAddedBy()).thenReturn(fAddedBy);
        when(vendor.getZip()).thenReturn(fZip);

        when(lastvendor.isActive()).thenReturn(fActive);
        when(lastvendor.getAddress1()).thenReturn(fAddress1);
        when(lastvendor.getAddress2()).thenReturn(fAddress2);
        when(lastvendor.getCity()).thenReturn(fCity);
        when(lastvendor.getCountry()).thenReturn(fCountry);
        when(lastvendor.getRegion()).thenReturn(fRegion);
        when(lastvendor.getType()).thenReturn(fType);
        when(lastvendor.getAdded()).thenReturn(fAdded);
        when(lastvendor.getAddedBy()).thenReturn(fAddedBy);
        when(lastvendor.getZip()).thenReturn(fZip);
    }

    @Test
    public void constructorTest() {
        VendorAddressHistory vendorHistory = new VendorAddressHistory(fVendorAddressID, vendor);
        assertEquals(vendorHistory.getAddress1(), fAddress1);
        assertEquals(vendorHistory.getAddress2(), fAddress2);
        assertEquals(vendorHistory.getCity(), fCity);
        assertEquals(vendorHistory.getCountry(), fCountry);
        assertEquals(vendorHistory.getType(), fType);
        assertEquals(vendorHistory.getZip(), fZip);
        assertEquals(vendorHistory.getRegion(), fRegion);
        assertEquals(vendorHistory.getAdded(), fAdded);
        assertEquals(vendorHistory.getAddedBy(), fAddedBy);
        System.out.println(vendorHistory.toJsonString());
    }



    @Test
    public void newInstanceFromJsonStringTest() {
        VendorAddressHistory vendorAddressHistory = VendorAddressHistory.newInstance(json);
        assertEquals(vendorAddressHistory.getAddress1(), fAddress1);
        assertEquals(vendorAddressHistory.getAddress2(), fAddress2);
        assertEquals(vendorAddressHistory.getCity(), fCity);
        assertEquals(vendorAddressHistory.getCountry(), fCountry);
        assertEquals(vendorAddressHistory.getType(), fType);
        assertEquals(vendorAddressHistory.getZip(), fZip);
        assertEquals(vendorAddressHistory.getRegion(), fRegion);
        assertEquals(vendorAddressHistory.getAdded(), fAdded);
        assertEquals(vendorAddressHistory.getAddedBy(), fAddedBy);
    }

    @Test
    public void hasChangedTest() throws Exception {
        when(lastvendor.getZip()).thenReturn("is zip");
        boolean expected = VendorAddressHistory.hasChanged(vendor, lastvendor);
        assertTrue(expected);
    }


    @Test
    public void hasChangedTestReturnFalse() throws Exception {
        boolean expected = VendorAddressHistory.hasChanged(vendor, lastvendor);
        assertFalse(expected);
    }

    @Test
    public void constructorTestWithNull() {
        VendorAddressHistory vendorHistory = new VendorAddressHistory(fVendorAddressID, null);
        assertNotNull(vendorHistory);
    }


    @Test
    public void buildProductHistoryTest() {
        when(lastvendor.getZip()).thenReturn("is zip");
        History history = VendorAddressHistory.buildVendorAddressHistory(vendor, lastvendor, fAddedBy);
        VendorAddressHistory productHistory = new  VendorAddressHistory(fVendorAddressID, lastvendor);
        assertEquals(productHistory.toJsonString().substring(13), history.getPriorValue().substring(1));
    }

    @Test
    public void getGsonWithFalseTest() throws IOException {
        VendorAddressHistory vendorHistory = new VendorAddressHistory(fVendorAddressID, lastvendor);
        Gson a = vendorHistory.getGson(false);
        String expected = json;
        assertEquals(a.toJson(vendorHistory), expected);
    }

    @Test
    public void getGsonWithTrueTest() throws IOException {
        VendorAddressHistory productHistory = new VendorAddressHistory(fVendorAddressID, lastvendor);
        Gson a = productHistory.getGson(true);
        String expected = json;
        assertEquals(a.toJson(productHistory), expected);
    }

    @Test
    public void getListTypeTest() {
        VendorAddressHistory vendorHistory = new VendorAddressHistory(fVendorAddressID, lastvendor);
        Type expected = vendorHistory.getListType();
        assertEquals(expected, new TypeToken<List<VendorAddressHistory>>() {}.getType());
    }

    @Test
    public void newInstanceTestPath() throws IOException {
        PowerMockito.mockStatic(JsonDataObject.class);
        VendorAddressHistory productHistory = mock(VendorAddressHistory.class);
        when(JsonDataObject.fromJson(any(Gson.class),any(Path.class), any(Class.class))).thenReturn(productHistory);
    }



}