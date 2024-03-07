package com.figstreet.biz.vendor;

import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.Vendor;
import com.figstreet.data.vendor.VendorID;
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
public class VendorHistoryTest {
    VendorID fVendorID = new VendorID(1);
    boolean fActive = true;
    String fName = "name";
    String fWebsite = "website";
    Timestamp fAdded = new Timestamp(1);
    UsersID fAddedBy = UsersID.ADMIN;

    @Mock
    Vendor lastvendor;

    @Mock
    Vendor vendor;

    String json = "{\"vdid\":\"1\",\"vd_active\":true,\"vd_name\":\"name\",\"vd_website\":\"website\",\"vd_added_dt\":\"1969-12-31T19:00:00.001-0500\",\"vd_added_by\":\"100\"}";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(vendor.isActive()).thenReturn(fActive);
        when(vendor.getName()).thenReturn(fName);
        when(vendor.getWebsite()).thenReturn(fWebsite);
        when(vendor.getAdded()).thenReturn(fAdded);
        when(vendor.getAddedBy()).thenReturn(fAddedBy);

        when(lastvendor.isActive()).thenReturn(fActive);
        when(lastvendor.getName()).thenReturn("different");
        when(lastvendor.getWebsite()).thenReturn(fWebsite);
        when(lastvendor.getAdded()).thenReturn(fAdded);
        when(lastvendor.getAddedBy()).thenReturn(fAddedBy);
    }

    @Test
    public void constructorTest() {
        VendorHistory vendorHistory = new  VendorHistory(fVendorID, vendor);
        assertEquals(vendorHistory.getWebsite(), fWebsite);
        assertEquals(vendorHistory.isActive(), fActive);
        assertEquals(vendorHistory.getName(), fName);
        assertEquals(vendorHistory.getAddedBy(), fAddedBy);
        assertEquals(vendorHistory.getAdded(), fAdded);
    }

    @Test
    public void newInstanceFromJsonStringTest() {
        VendorHistory vendorHistory = VendorHistory.newInstance(json);
        assertEquals(vendorHistory.getName(), fName);
        assertEquals(vendorHistory.getWebsite(), fWebsite);
        assertEquals(vendorHistory.isActive(), fActive);
        assertEquals(vendorHistory.getAdded(), fAdded);
        assertEquals(vendorHistory.getAddedBy(), fAddedBy);
    }


    @Test
    public void hasChangedTest() throws Exception {
        when(lastvendor.getName()).thenReturn(fName);
        boolean expected = VendorHistory.hasChanged(vendor, lastvendor);
        assertFalse(expected);
    }


    @Test
    public void hasChangedTestReturnFalse() throws Exception {
        boolean expected = VendorHistory.hasChanged(vendor, lastvendor);
        assertTrue(expected);
    }


    @Test
    public void constructorTestWithNull() {
        VendorHistory vendorHistory = new  VendorHistory(fVendorID, null);
        assertNotNull(vendorHistory);
    }

    @Test
    public void buildProductHistoryTest() {
        History history = VendorHistory.buildVendorHistory(vendor, lastvendor, fAddedBy);
        VendorHistory vendorHistory = new VendorHistory(fVendorID, lastvendor);
        assertEquals(vendorHistory.toJsonString().substring(13), history.getPriorValue().substring(2));
    }

    @Test
    public void getGsonWithFalseTest() throws IOException {
        VendorHistory productHistory = new VendorHistory(fVendorID, lastvendor);
        Gson a = productHistory.getGson(false);
        String expected = "{\"vdid\":\"1\",\"vd_active\":true,\"vd_name\":\"different\",\"vd_website\":\"website\",\"vd_added_dt\":\"1969-12-31T19:00:00.001-0500\",\"vd_added_by\":\"100\"}";
        assertEquals(a.toJson(productHistory), expected);
    }

    @Test
    public void getGsonWithTrueTest() throws IOException {
        VendorHistory productHistory = new VendorHistory(fVendorID, lastvendor);
        Gson a = productHistory.getGson(true);
        String expected = "{\"vdid\":\"1\",\"vd_active\":true,\"vd_name\":\"different\",\"vd_website\":\"website\",\"vd_added_dt\":\"1969-12-31T19:00:00.001-0500\",\"vd_added_by\":\"100\"}";
        assertEquals(a.toJson(productHistory), expected);
    }


    @Test
    public void getListTypeTest() {
        VendorHistory vendorHistory = new VendorHistory(fVendorID, lastvendor);
        Type expected = vendorHistory.getListType();
        assertEquals(expected, new TypeToken<List<VendorHistory>>() {}.getType());
    }

    @Test
    public void newInstanceTestPath() throws IOException {
        PowerMockito.mockStatic(JsonDataObject.class);
        VendorHistory vendorHistory = mock(VendorHistory.class);
        when(JsonDataObject.fromJson(any(Gson.class),any(Path.class), any(Class.class))).thenReturn(vendorHistory);
    }


}
