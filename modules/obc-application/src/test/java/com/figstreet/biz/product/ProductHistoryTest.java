package com.figstreet.biz.product;

import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.common.MeasurementUnit;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.history.History;
import com.figstreet.data.product.Product;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.users.UsersID;

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
public class ProductHistoryTest {
    ProductID fProductID = new ProductID(1);
    boolean fActive = true;
    String name = "name";
    String shortDescription = "shortDescription";
    String longDescription = "longDescription";
    String upc = "upc";
    String sku = "sku";

    Double fLength = 1.0;
    Double fWidth = 2.0;
    Double fHeight = 3.0;
    Double fWeight = 4.0;
    MeasurementUnit fLengthUnit = MeasurementUnit.INCHES;
    MeasurementUnit fWidthUnit = MeasurementUnit.INCHES;
    MeasurementUnit fHeightUnit = MeasurementUnit.INCHES;
    MeasurementUnit fWeightUnit = MeasurementUnit.INCHES;
    String fImageUrl = "imageurl";
    Double fListPrice = 5.0;
    PriceCurrency fPriceCurrency = PriceCurrency.USD;
    Timestamp fAdded = new Timestamp(1);
    UsersID fAddedBy = UsersID.ADMIN;


    @Mock
    Product lastproduct;

    @Mock
    Product product;

    String json = "{\"prid\":\"1\",\"pr_active\":true,\"pr_name\":\"name\",\"pr_short_desc\":\"shortDescription\",\"pr_long_desc\":\"longDescription\",\"pr_upc\":\"upc\",\"pr_sku\":\"sku\",\"pr_length\":1.0,\"pr_width\":2.0,\"pr_height\":3.0,\"pr_weight\":4.0,\"pr_length_unit\":\"inches\",\"pr_width_unit\":\"inches\",\"pr_height_unit\":\"inches\",\"pr_weight_unit\":\"inches\",\"pr_image_url\":\"imageurl\",\"pr_list_price\":5.0,\"pr_price_currency\":\"USD\",\"pr_added_dt\":\"1969-12-31T19:00:00.001-0500\",\"pr_added_by\":\"100\"}";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(product.isActive()).thenReturn(fActive);
        when(product.getName()).thenReturn(name);
        when(product.getShortDescription()).thenReturn(shortDescription);
        when(product.getLongDescription()).thenReturn(longDescription);
        when(product.getUpc()).thenReturn(upc);
        when(product.getSku()).thenReturn(sku);
        when(product.getLength()).thenReturn(fLength);
        when(product.getWidth()).thenReturn(fWidth);
        when(product.getHeight()).thenReturn(fHeight);
        when(product.getWeight()).thenReturn(fWeight);
        when(product.getLengthUnit()).thenReturn(fLengthUnit);
        when(product.getWidthUnit()).thenReturn(fLengthUnit);
        when(product.getHeightUnit()).thenReturn(fHeightUnit);
        when(product.getWeightUnit()).thenReturn(fWeightUnit);
        when(product.getPriceCurrency()).thenReturn(fPriceCurrency);
        when(product.getListPrice()).thenReturn(fListPrice);
        when(product.getAdded()).thenReturn(fAdded);
        when(product.getAddedBy()).thenReturn(fAddedBy);

        when(lastproduct.isActive()).thenReturn(fActive);
        when(lastproduct.getName()).thenReturn(name);
        when(lastproduct.getShortDescription()).thenReturn(shortDescription);
        when(lastproduct.getLongDescription()).thenReturn(longDescription);
        when(lastproduct.getUpc()).thenReturn("different");
        when(lastproduct.getSku()).thenReturn(sku);
        when(lastproduct.getLength()).thenReturn(fLength);
        when(lastproduct.getWidth()).thenReturn(fWidth);
        when(lastproduct.getHeight()).thenReturn(fHeight);
        when(lastproduct.getWeight()).thenReturn(fWeight);
        when(lastproduct.getLengthUnit()).thenReturn(fLengthUnit);
        when(lastproduct.getWidthUnit()).thenReturn(fLengthUnit);
        when(lastproduct.getHeightUnit()).thenReturn(fHeightUnit);
        when(lastproduct.getWeightUnit()).thenReturn(fWeightUnit);
        when(lastproduct.getPriceCurrency()).thenReturn(fPriceCurrency);
        when(lastproduct.getListPrice()).thenReturn(fListPrice);
        when(lastproduct.getAdded()).thenReturn(fAdded);
        when(lastproduct.getAddedBy()).thenReturn(fAddedBy);
    }


    @Test
    public void newInstanceFromJsonStringTest() {
        ProductHistory productHistory = ProductHistory.newInstance(json);
        assertEquals(productHistory.isActive(), fActive);
        assertEquals(productHistory.getName(), name);
        assertEquals(productHistory.getShortDescription(), shortDescription);
        assertEquals(productHistory.getLongDescription(), longDescription);
        assertEquals(productHistory.getUpc(), upc);
        assertEquals(productHistory.getSku(), sku);
        assertEquals(productHistory.getLength(), fLength);
        assertEquals(productHistory.getWidth(), fWidth);
        assertEquals(productHistory.getHeight(), fHeight);
        assertEquals(productHistory.getWeight(), fWeight);
        assertEquals(productHistory.getLengthUnit().toString().toUpperCase(), fLengthUnit.toString().toUpperCase());
        assertEquals(productHistory.getWidthUnit().toString().toUpperCase(), fLengthUnit.toString().toUpperCase());
        assertEquals(productHistory.getHeightUnit().toString().toUpperCase(),fHeightUnit.toString().toUpperCase());
        assertEquals(productHistory.getWeightUnit().toString().toUpperCase(),fWeightUnit.toString().toUpperCase());
        assertEquals(productHistory.getPriceCurrency(), fPriceCurrency);
        assertEquals(productHistory.getListPrice(), fListPrice);
        assertEquals(productHistory.getAdded(), fAdded);
        assertEquals(productHistory.getAddedBy(), fAddedBy);
    }

    @Test
    public void hasChangedTest() throws Exception {
        when(lastproduct.getUpc()).thenReturn(upc);
        boolean expected = ProductHistory.hasChanged(product, lastproduct);
        assertFalse(expected);
    }


    @Test
    public void hasChangedTestReturnFalse() throws Exception {
        boolean expected = ProductHistory.hasChanged(product, lastproduct);
        assertTrue(expected);
    }

    @Test
    public void constructorTest() {
        ProductHistory productHistory = new  ProductHistory(fProductID, product);
        assertEquals(productHistory.isActive(), fActive);
        assertEquals(productHistory.getName(), name);
        assertEquals(productHistory.getShortDescription(), shortDescription);
        assertEquals(productHistory.getLongDescription(), longDescription);
        assertEquals(productHistory.getUpc(), upc);
        assertEquals(productHistory.getSku(), sku);
        assertEquals(productHistory.getLength(), fLength);
        assertEquals(productHistory.getWidth(), fWidth);
        assertEquals(productHistory.getHeight(), fHeight);
        assertEquals(productHistory.getWeight(), fWeight);
        assertEquals(productHistory.getLengthUnit().toString().toUpperCase(), fLengthUnit.toString().toUpperCase());
        assertEquals(productHistory.getWidthUnit().toString().toUpperCase(), fLengthUnit.toString().toUpperCase());
        assertEquals(productHistory.getHeightUnit().toString().toUpperCase(),fHeightUnit.toString().toUpperCase());
        assertEquals(productHistory.getWeightUnit().toString().toUpperCase(),fWeightUnit.toString().toUpperCase());
        assertEquals(productHistory.getPriceCurrency(), fPriceCurrency);
        assertEquals(productHistory.getListPrice(), fListPrice);
        assertEquals(productHistory.getAdded(), fAdded);
        assertEquals(productHistory.getAddedBy(), fAddedBy);
    }

    @Test
    public void constructorTestWithNull() {
        ProductHistory productHistory = new  ProductHistory(fProductID, null);
        assertNotNull(productHistory);
    }

    @Test
    public void buildProductHistoryTest() {
        History history = ProductHistory.buildProductHistory(product, lastproduct, fAddedBy);
        ProductHistory productHistory = new  ProductHistory(fProductID, lastproduct);
        assertEquals(productHistory.toJsonString().substring(13), history.getPriorValue().substring(2));
    }

    @Test
    public void getGsonWithFalseTest() throws IOException {
        ProductHistory productHistory = new ProductHistory(fProductID, lastproduct);
        Gson a = productHistory.getGson(false);
        String expected = "{\"prid\":\"1\",\"pr_active\":true,\"pr_name\":\"name\",\"pr_short_desc\":\"shortDescription\",\"pr_long_desc\":\"longDescription\",\"pr_upc\":\"different\",\"pr_sku\":\"sku\",\"pr_length\":1.0,\"pr_width\":2.0,\"pr_height\":3.0,\"pr_weight\":4.0,\"pr_length_unit\":\"inches\",\"pr_width_unit\":\"inches\",\"pr_height_unit\":\"inches\",\"pr_weight_unit\":\"inches\",\"pr_list_price\":5.0,\"pr_price_currency\":\"USD\",\"pr_added_dt\":\"1969-12-31T19:00:00.001-0500\",\"pr_added_by\":\"100\"}";
        assertEquals(a.toJson(productHistory), expected);
    }

    @Test
    public void getGsonWithTrueTest() throws IOException {
        ProductHistory productHistory = new ProductHistory(fProductID, lastproduct);
        Gson a = productHistory.getGson(true);
        String expected = "{\"prid\":\"1\",\"pr_active\":true,\"pr_name\":\"name\",\"pr_short_desc\":\"shortDescription\",\"pr_long_desc\":\"longDescription\",\"pr_upc\":\"different\",\"pr_sku\":\"sku\",\"pr_length\":1.0,\"pr_width\":2.0,\"pr_height\":3.0,\"pr_weight\":4.0,\"pr_length_unit\":\"inches\",\"pr_width_unit\":\"inches\",\"pr_height_unit\":\"inches\",\"pr_weight_unit\":\"inches\",\"pr_list_price\":5.0,\"pr_price_currency\":\"USD\",\"pr_added_dt\":\"1969-12-31T19:00:00.001-0500\",\"pr_added_by\":\"100\"}";
        assertEquals(a.toJson(productHistory), expected);
    }


    @Test
    public void getListTypeTest() {
        ProductHistory productHistory = new ProductHistory(fProductID, lastproduct);
        Type expected = productHistory.getListType();
        assertEquals(expected, new TypeToken<List<ProductHistory>>() {}.getType());
    }

    @Test
    public void newInstanceTestPath() throws IOException {
        PowerMockito.mockStatic(JsonDataObject.class);
        ProductHistory productHistory = mock(ProductHistory.class);
        when(JsonDataObject.fromJson(any(Gson.class),any(Path.class), any(Class.class))).thenReturn(productHistory);
    }


}
