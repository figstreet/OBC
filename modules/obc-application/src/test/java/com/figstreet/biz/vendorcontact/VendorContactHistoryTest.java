package com.figstreet.biz.vendorcontact;

import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.Vendor;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendorcontact.VendorContact;
import com.figstreet.data.vendorcontact.VendorContactID;
import com.figstreet.data.vendorcontact.VendorContactType;
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
public class VendorContactHistoryTest {
    VendorContactID fVendorContactID = new VendorContactID(1) ;
    VendorID fVendorID = new VendorID(1);
    boolean fActive = true;
    boolean fPrimary = false;
    VendorContactType fType = VendorContactType.SALES;
    String fEmail = "email";
    String fPhoneCountryCode = "phone";
    String fPhone1 = "phone1";
    String fPhone2 = "phone2";
    String fPhone3 = "phone3";
    String fPhoneExt = "phoneext";
    String fName1 = "name";
    String fName2 = "name1";

    Timestamp fAdded = new Timestamp(1);
    UsersID fAddedBy = UsersID.ADMIN;

    String json = "{\"vdcid\":\"1\",\"vdc_active\":true,\"vdc_primary\":false,\"vdc_type\":\"SALES\",\"vdc_email\":\"email\",\"vdc_country_code\":\"phone\",\"vdc_phone1\":\"phone1\",\"vdc_phone2\":\"phone2\",\"vdc_phone3\":\"phone3\",\"vdc_phone_ext\":\"phoneext\",\"vdc_contact_name1\":\"name\",\"vdc_contact_name2\":\"name1\",\"vdc_added_dt\":\"1969-12-31T19:00:00.001-0500\",\"vdc_added_by\":\"100\"}";

    @Mock
    VendorContact lastvendor;

    @Mock
    VendorContact vendor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(vendor.isActive()).thenReturn(fActive);
        when(vendor.isPrimary()).thenReturn(fPrimary);
        when(vendor.getType()).thenReturn(fType);
        when(vendor.getEmail()).thenReturn(fEmail);
        when(vendor.getName1()).thenReturn(fName1);
        when(vendor.getName2()).thenReturn(fName2);
        when(vendor.getPhone1()).thenReturn(fPhone1);
        when(vendor.getPhone2()).thenReturn(fPhone2);
        when(vendor.getPhone3()).thenReturn(fPhone3);
        when(vendor.getPhoneExt()).thenReturn(fPhoneExt);
        when(vendor.getPhoneCountryCode()).thenReturn(fPhoneCountryCode);
        when(vendor.getAdded()).thenReturn(fAdded);
        when(vendor.getAddedBy()).thenReturn(fAddedBy);

        when(lastvendor.isActive()).thenReturn(fActive);
        when(lastvendor.isPrimary()).thenReturn(fPrimary);
        when(lastvendor.getType()).thenReturn(fType);
        when(lastvendor.getEmail()).thenReturn(fEmail);
        when(lastvendor.getName1()).thenReturn(fName1);
        when(lastvendor.getName2()).thenReturn(fName2);
        when(lastvendor.getPhone1()).thenReturn(fPhone1);
        when(lastvendor.getPhone2()).thenReturn(fPhone2);
        when(lastvendor.getPhone3()).thenReturn(fPhone3);
        when(lastvendor.getPhoneExt()).thenReturn(fPhoneExt);
        when(lastvendor.getPhoneCountryCode()).thenReturn(fPhoneCountryCode);
        when(lastvendor.getAdded()).thenReturn(fAdded);
        when(lastvendor.getAddedBy()).thenReturn(fAddedBy);


    }

    @Test
    public void constructorTest() {
        VendorContactHistory vendorHistory = new VendorContactHistory(fVendorContactID, vendor);
        assertEquals(vendorHistory.isActive(),fActive);
        assertEquals(vendorHistory.isPrimary(),fPrimary);
        assertEquals(vendorHistory.getType(),fType);
        assertEquals(vendorHistory.getEmail(),fEmail);
        assertEquals(vendorHistory.getName1(),fName1);
        assertEquals(vendorHistory.getName2(),fName2);
        assertEquals(vendorHistory.getPhone1(),fPhone1);
        assertEquals(vendorHistory.getPhone2(),fPhone2);
        assertEquals(vendorHistory.getPhone3(),fPhone3);
        assertEquals(vendorHistory.getPhoneExt(),fPhoneExt);
        assertEquals(vendorHistory.getPhoneCountryCode(),fPhoneCountryCode);
        assertEquals(vendorHistory.getAdded(),fAdded);
        assertEquals(vendorHistory.getAddedBy(),fAddedBy);
        System.out.println(vendorHistory.toJsonString());
    }


    @Test
    public void newInstanceFromJsonStringTest() {
        VendorContactHistory vendorHistory = VendorContactHistory.newInstance(json);
        assertEquals(vendorHistory.isActive(),fActive);
        assertEquals(vendorHistory.isPrimary(),fPrimary);
        assertEquals(vendorHistory.getType(),fType);
        assertEquals(vendorHistory.getEmail(),fEmail);
        assertEquals(vendorHistory.getName1(),fName1);
        assertEquals(vendorHistory.getName2(),fName2);
        assertEquals(vendorHistory.getPhone1(),fPhone1);
        assertEquals(vendorHistory.getPhone2(),fPhone2);
        assertEquals(vendorHistory.getPhone3(),fPhone3);
        assertEquals(vendorHistory.getPhoneExt(),fPhoneExt);
        assertEquals(vendorHistory.getPhoneCountryCode(),fPhoneCountryCode);
        assertEquals(vendorHistory.getAdded(),fAdded);
        assertEquals(vendorHistory.getAddedBy(),fAddedBy);
    }

    @Test
    public void hasChangedTest() throws Exception {
        when(lastvendor.getName1()).thenReturn("is name");
        boolean expected = VendorContactHistory.hasChanged(vendor, lastvendor);
        assertTrue(expected);
    }


    @Test
    public void hasChangedTestReturnFalse() throws Exception {
        boolean expected = VendorContactHistory.hasChanged(vendor, lastvendor);
        assertFalse(expected);
    }

    @Test
    public void constructorTestWithNull() {
        VendorContactHistory vendorHistory = new VendorContactHistory(fVendorContactID, null);
        assertNotNull(vendorHistory);
    }


    @Test
    public void buildProductHistoryTest() {
        when(lastvendor.getName1()).thenReturn("different name");
        History history = VendorContactHistory.buildVendorContactHistory(vendor, lastvendor, fAddedBy);
        VendorContactHistory vendorHistory = new  VendorContactHistory(fVendorContactID, lastvendor);
        assertEquals(vendorHistory.toJsonString().substring(13), history.getPriorValue().substring(1));
    }

    @Test
    public void getGsonWithFalseTest() throws IOException {
        VendorContactHistory vendorHistory = new VendorContactHistory(fVendorContactID, lastvendor);
        Gson a = vendorHistory.getGson(false);
        String expected = json;
        assertEquals(a.toJson(vendorHistory), expected);
    }

    @Test
    public void getGsonWithTrueTest() throws IOException {
        VendorContactHistory vendorHistory = new VendorContactHistory(fVendorContactID, lastvendor);
        Gson a = vendorHistory.getGson(true);
        String expected = json;
        assertEquals(a.toJson(vendorHistory), expected);
    }

    @Test
    public void getListTypeTest() {
        VendorContactHistory vendorHistory = new VendorContactHistory(fVendorContactID, lastvendor);
        Type expected = vendorHistory.getListType();
        assertEquals(expected, new TypeToken<List<VendorContactHistory>>() {}.getType());
    }

    @Test
    public void newInstanceTestPath() throws IOException {
        PowerMockito.mockStatic(JsonDataObject.class);
        VendorContactHistory vendorHistory = mock(VendorContactHistory.class);
        when(JsonDataObject.fromJson(any(Gson.class),any(Path.class), any(Class.class))).thenReturn(vendorHistory);
    }

}
