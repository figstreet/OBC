package com.figstreet.biz.vendorproduct;

import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.common.AmazonMarketplace;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProduct;
import com.figstreet.data.vendorproduct.VendorProductID;
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
public class VendorProductHistoryTest {

    VendorProductID fVendorProductID = new VendorProductID(1L);
    boolean fActive = true;
    double fPrice = 1.0;
    int fQuantity = 2;
    Double fAlternativePrice = 3.0;
    Integer fMinimumOrderQuantity = 4;
    boolean fAvailableOnline = false;
    String fVendorIdentifier = "vendorid";
    AmazonMarketplace fAmazonMarketplace = AmazonMarketplace.UNITED_STATES;
    PriceCurrency fPriceCurrency = PriceCurrency.USD;
    Timestamp fAdded = new Timestamp(1);
    UsersID fAddedBy = UsersID.ADMIN;

    String json = "{\"vpid\":\"1\",\"vp_active\":true,\"vp_price\":1.0,\"vp_quantity\":2,\"vp_alternative_price\":3.0,\"vp_min_order_quantity\":4,\"vp_available_online\":false,\"vp_vendor_identifier\":\"vendorid\",\"vp_amazon_marketplace\":\"ATVPDKIKX0DER\",\"vp_price_currency\":\"USD\",\"vp_added_dt\":\"1969-12-31T19:00:00.001-0500\",\"vp_added_by\":\"100\"}";

    @Mock
    VendorProduct lastvendor;
    @Mock
    VendorProduct vendor;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(vendor.getAdded()).thenReturn(fAdded);
        when(vendor.getAddedBy()).thenReturn(fAddedBy);
        when(vendor.isActive()).thenReturn(fActive);
        when(vendor.getPrice()).thenReturn(fPrice);
        when(vendor.getQuantity()).thenReturn(fQuantity);
        when(vendor.getAlternativePrice()).thenReturn(fAlternativePrice);
        when(vendor.getMinimumOrderQuantity()).thenReturn(fMinimumOrderQuantity);
        when(vendor.isAvailableOnline()).thenReturn(fAvailableOnline);
        when(vendor.getVendorIdentifier()).thenReturn(fVendorIdentifier);
        when(vendor.getAmazonMarketplace()).thenReturn(fAmazonMarketplace);
        when(vendor.getPriceCurrency()).thenReturn(fPriceCurrency);

        when(lastvendor.getAdded()).thenReturn(fAdded);
        when(lastvendor.getAddedBy()).thenReturn(fAddedBy);
        when(lastvendor.isActive()).thenReturn(fActive);
        when(lastvendor.getPrice()).thenReturn(fPrice);
        when(lastvendor.getQuantity()).thenReturn(fQuantity);
        when(lastvendor.getAlternativePrice()).thenReturn(fAlternativePrice);
        when(lastvendor.getMinimumOrderQuantity()).thenReturn(fMinimumOrderQuantity);
        when(lastvendor.isAvailableOnline()).thenReturn(fAvailableOnline);
        when(lastvendor.getVendorIdentifier()).thenReturn(fVendorIdentifier);
        when(lastvendor.getAmazonMarketplace()).thenReturn(fAmazonMarketplace);
        when(lastvendor.getPriceCurrency()).thenReturn(fPriceCurrency);
    }

    @Test
    public void constructorTest() {
        VendorProductHistory vendorHistory = new VendorProductHistory(fVendorProductID, vendor);
        assertEquals(vendorHistory.getAdded(),fAdded);
        assertEquals(vendorHistory.getAddedBy(),fAddedBy);
        assertEquals(vendorHistory.isActive(),fActive);
        assertEquals(vendorHistory.getPrice(), (Object) fPrice);
        assertEquals(vendorHistory.getQuantity(), (Object) fQuantity);
        assertEquals(vendorHistory.getAlternativePrice(),fAlternativePrice);
        assertEquals(vendorHistory.getMinimumOrderQuantity(),fMinimumOrderQuantity);
        assertEquals(vendorHistory.isAvailableOnline(),fAvailableOnline);
        assertEquals(vendorHistory.getVendorIdentifier(),fVendorIdentifier);
        assertEquals(vendorHistory.getAmazonMarketplace(),fAmazonMarketplace);
        assertEquals(vendorHistory.getPriceCurrency(),fPriceCurrency);
    }

    @Test
    public void newInstanceFromJsonStringTest() {
        VendorProductHistory vendorHistory = VendorProductHistory.newInstance(json);
        assertEquals(vendorHistory.getAdded(),fAdded);
        assertEquals(vendorHistory.getAddedBy(),fAddedBy);
        assertEquals(vendorHistory.isActive(),fActive);
        assertEquals(vendorHistory.getPrice(), (Object) fPrice);
        assertEquals(vendorHistory.getQuantity(), (Object) fQuantity);
        assertEquals(vendorHistory.getAlternativePrice(),fAlternativePrice);
        assertEquals(vendorHistory.getMinimumOrderQuantity(),fMinimumOrderQuantity);
        assertEquals(vendorHistory.isAvailableOnline(),fAvailableOnline);
        assertEquals(vendorHistory.getVendorIdentifier(),fVendorIdentifier);
        assertEquals(vendorHistory.getAmazonMarketplace(),fAmazonMarketplace);
        assertEquals(vendorHistory.getPriceCurrency(),fPriceCurrency);
    }

    @Test
    public void hasChangedTest() throws Exception {
        when(lastvendor.getVendorIdentifier()).thenReturn("is different");
        boolean expected = VendorProductHistory.hasChanged(vendor, lastvendor);
        assertTrue(expected);
    }


    @Test
    public void hasChangedTestReturnFalse() throws Exception {
        boolean expected = VendorProductHistory.hasChanged(vendor, lastvendor);
        assertFalse(expected);
    }

    @Test
    public void constructorTestWithNull() {
        VendorProductHistory vendorHistory = new VendorProductHistory(fVendorProductID, null);
        assertNotNull(vendorHistory);
    }


    @Test
    public void buildProductHistoryTest() {
        when(lastvendor.getVendorIdentifier()).thenReturn("is different");
        History history = VendorProductHistory.buildVendorProductHistory(vendor, lastvendor, fAddedBy);
        VendorProductHistory vendorHistory = new  VendorProductHistory(fVendorProductID, lastvendor);
        assertEquals(vendorHistory.toJsonString().substring(13), history.getPriorValue().substring(2));
    }

    @Test
    public void getGsonWithFalseTest() throws IOException {
        VendorProductHistory vendorHistory = new VendorProductHistory(fVendorProductID, lastvendor);
        Gson a = vendorHistory.getGson(false);
        String expected = json;
        assertEquals(a.toJson(vendorHistory), expected);
    }

    @Test
    public void getGsonWithTrueTest() throws IOException {
        VendorProductHistory vendorHistory = new VendorProductHistory(fVendorProductID, lastvendor);
        Gson a = vendorHistory.getGson(true);
        String expected = json;
        assertEquals(a.toJson(vendorHistory), expected);
    }

    @Test
    public void getListTypeTest() {
        VendorProductHistory vendorHistory = new VendorProductHistory(fVendorProductID, lastvendor);
        Type expected = vendorHistory.getListType();
        assertEquals(expected, new TypeToken<List<VendorProductHistory>>() {}.getType());
    }

    @Test
    public void newInstanceTestPath() throws IOException {
        PowerMockito.mockStatic(JsonDataObject.class);
        VendorProductHistory vendorHistory = mock(VendorProductHistory.class);
        when(JsonDataObject.fromJson(any(Gson.class),any(Path.class), any(Class.class))).thenReturn(vendorHistory);
    }




}
