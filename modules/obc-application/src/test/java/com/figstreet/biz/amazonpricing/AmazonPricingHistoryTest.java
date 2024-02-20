package com.figstreet.biz.amazonpricing;

import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.amazonpricing.AmazonPricing;
import com.figstreet.data.amazonpricing.AmazonPricingID;
import com.figstreet.data.common.PriceCurrency;
import com.figstreet.data.common.ProductCondition;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
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
public class AmazonPricingHistoryTest {
     @Mock
     Path pJsonFile;
     @Mock
    AmazonPricing amazonPricing;
     @Mock
    AmazonPricing plastamazonPricing;


    String json = "{\"azpid\":\"1\",\"vpid\":\"2\",\"azp_product_condition\":\"NEW\",\"azp_offer_count\":3,\"azp_price_currency\":\"USD\",\"azp_buybox_item_price\":10.0,\"azp_buybox_shipping_price\":15.0,\"azp_buybox_fba\":true,\"azp_buybox_seller_amazon\":false,\"azp_secondary_item_price\":20.0,\"azp_secondary_shipping_price\":25.0,\"azp_secondary_fba\":true,\"azp_downloaded\":\"1969-12-31T19:00:00.000-0500\",\"azp_added_dt\":\"1969-12-31T19:00:00.000-0500\",\"azp_added_by\":\"100\"}";
    AmazonPricingID pricingID = new AmazonPricingID(1L);
    VendorProductID vendorProductID = new VendorProductID(2L);
    ProductCondition fProductCondition = ProductCondition.newInstance("new");
    int fOffertCount = 3;
    PriceCurrency priceCurrency = PriceCurrency.newInstance("USD");
    Double fBuyboxItemPrice = 10.0;
    Double fBuyboxShippingPrice = 15.0;
    boolean fBuyboxFBA = true;
    boolean fBuyboxSellerAmazon = false;
    Double fSecondaryItemPrice = 20.0;
    Double fSecondaryShippingPrice = 25.0;
    boolean fSecondaryFBA = true;
    Timestamp fDownloaded = new Timestamp(0);
    Timestamp fAdded = new Timestamp(0);
    UsersID fAddedBy = UsersID.ADMIN;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void newInstanceTest() {
        AmazonPricingHistory amazonPricingHistory = AmazonPricingHistory.newInstance(json);
        assertEquals(amazonPricingHistory.getVendorProductID(),vendorProductID );
        assertEquals(amazonPricingHistory.getProductCondition(),fProductCondition);
        assertEquals(amazonPricingHistory.getOfferCount(),fOffertCount);
        assertEquals(amazonPricingHistory.getPriceCurrency(),priceCurrency);
        assertEquals(amazonPricingHistory.getBuyboxItemPrice(),fBuyboxItemPrice);
        assertEquals(amazonPricingHistory.getBuyboxShippingPrice(),fBuyboxShippingPrice);
        assertEquals(amazonPricingHistory.getSecondaryFBA(),fBuyboxFBA);
        assertEquals(amazonPricingHistory.getSecondaryItemPrice(),fSecondaryItemPrice);
        assertEquals(amazonPricingHistory.getBuyboxSellerAmazon(),fBuyboxSellerAmazon);
        assertEquals(amazonPricingHistory.getSecondaryShippingPrice(),fSecondaryShippingPrice);
        assertEquals(amazonPricingHistory.getSecondaryFBA(),fSecondaryFBA);
        assertEquals(amazonPricingHistory.getDownloaded(),fDownloaded);
        assertEquals(amazonPricingHistory.getAdded(),fAdded);
        assertEquals(amazonPricingHistory.getAddedBy(),fAddedBy);
    }


    @Test
    public void hasChangedTest() {
        when(amazonPricing.getVendorProductID()).thenReturn(vendorProductID);
        when(amazonPricing.getVendorProductID()).thenReturn(vendorProductID );
        when(amazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(amazonPricing.getOfferCount()).thenReturn(fOffertCount);
        when(amazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(amazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(amazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(amazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(amazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(amazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(amazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(amazonPricing.getAdded()).thenReturn(fAdded);
        when(amazonPricing.getAddedBy()).thenReturn(fAddedBy);
        when(plastamazonPricing.getVendorProductID()).thenReturn(vendorProductID);
        when(plastamazonPricing.getVendorProductID()).thenReturn(vendorProductID );
        when(plastamazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(plastamazonPricing.getOfferCount()).thenReturn(fOffertCount);
        when(plastamazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(plastamazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(plastamazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(plastamazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(plastamazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(plastamazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(plastamazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(plastamazonPricing.getAdded()).thenReturn(fAdded);
        when(plastamazonPricing.getAddedBy()).thenReturn(fAddedBy);
        boolean expected = AmazonPricingHistory.hasChanged(amazonPricing, plastamazonPricing);
        assertFalse(expected);
    }


    @Test
    public void hasChangedReturningTrueTest() {
        when(amazonPricing.getVendorProductID()).thenReturn(vendorProductID);
        when(amazonPricing.getVendorProductID()).thenReturn(vendorProductID );
        when(amazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(amazonPricing.getOfferCount()).thenReturn(fOffertCount);
        when(amazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(amazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(amazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(amazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(amazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(amazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(amazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(amazonPricing.getAdded()).thenReturn(fAdded);
        when(amazonPricing.getAddedBy()).thenReturn(fAddedBy);
        when(plastamazonPricing.getVendorProductID()).thenReturn(vendorProductID);
        when(plastamazonPricing.getVendorProductID()).thenReturn(vendorProductID );
        when(plastamazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(plastamazonPricing.getOfferCount()).thenReturn(10); //different
        when(plastamazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(plastamazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(plastamazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(plastamazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(plastamazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(plastamazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(plastamazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(plastamazonPricing.getAdded()).thenReturn(fAdded);
        when(plastamazonPricing.getAddedBy()).thenReturn(fAddedBy);
        boolean expected = AmazonPricingHistory.hasChanged(amazonPricing, plastamazonPricing);
        assertTrue(expected);
    }

    @Test
    public void AmazonPricingHistoryConstrucctorTest() {
        when(plastamazonPricing.getVendorProductID()).thenReturn(vendorProductID);
        when(plastamazonPricing.getVendorProductID()).thenReturn(vendorProductID );
        when(plastamazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(plastamazonPricing.getOfferCount()).thenReturn(fOffertCount); //different
        when(plastamazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(plastamazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(plastamazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(plastamazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(plastamazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(plastamazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(plastamazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(plastamazonPricing.getAdded()).thenReturn(fAdded);
        when(plastamazonPricing.getAddedBy()).thenReturn(fAddedBy);
        AmazonPricingHistory amazonPricingHistory = new  AmazonPricingHistory(pricingID, plastamazonPricing);

        assertEquals(amazonPricingHistory.getVendorProductID(),vendorProductID );
        assertEquals(amazonPricingHistory.getProductCondition(),fProductCondition);
        assertEquals(amazonPricingHistory.getOfferCount(),fOffertCount);
        assertEquals(amazonPricingHistory.getPriceCurrency(),priceCurrency);
        assertEquals(amazonPricingHistory.getBuyboxItemPrice(),fBuyboxItemPrice);
        assertEquals(amazonPricingHistory.getBuyboxShippingPrice(),fBuyboxShippingPrice);
        assertEquals(amazonPricingHistory.getSecondaryItemPrice(),fSecondaryItemPrice);
        assertEquals(amazonPricingHistory.getBuyboxSellerAmazon(),fBuyboxSellerAmazon);
        assertEquals(amazonPricingHistory.getSecondaryShippingPrice(),fSecondaryShippingPrice);
        assertEquals(amazonPricingHistory.getDownloaded(),fDownloaded);
        assertEquals(amazonPricingHistory.getAdded(),fAdded);
        assertEquals(amazonPricingHistory.getAddedBy(),fAddedBy);
    }

    @Test
    public void AmazonPricingHistoryConstructorWithNullTest() {
        AmazonPricingID pricingID = new AmazonPricingID(1L);
        AmazonPricingHistory amazonPricingHistory = new  AmazonPricingHistory(pricingID, null);
        assertNotNull(amazonPricingHistory);
    }


    @Test
    public void buildAmazonPricingHistoryTest() {
        AmazonPricing amazonPricing = mock(AmazonPricing.class);
        AmazonPricing plastamazonPricing = mock(AmazonPricing.class);
        when(amazonPricing.getVendorProductID()).thenReturn(vendorProductID);
        when(amazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(amazonPricing.getOfferCount()).thenReturn(fOffertCount);
        when(amazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(amazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(amazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(amazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(amazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(amazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(amazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(amazonPricing.getAdded()).thenReturn(fAdded);
        when(amazonPricing.getAddedBy()).thenReturn(fAddedBy);

        when(plastamazonPricing.getVendorProductID()).thenReturn(vendorProductID );
        when(plastamazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(plastamazonPricing.getOfferCount()).thenReturn(10); //different
        when(plastamazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(plastamazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(plastamazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(plastamazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(plastamazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(plastamazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(plastamazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(plastamazonPricing.getAdded()).thenReturn(fAdded);
        when(plastamazonPricing.getAddedBy()).thenReturn(fAddedBy);
        History history = AmazonPricingHistory.buildAmazonPricingHistory(amazonPricing, plastamazonPricing, fAddedBy);
        AmazonPricingHistory amazonPricingHistory = new  AmazonPricingHistory(pricingID, plastamazonPricing);
        assertEquals(amazonPricingHistory.toJsonString().substring(14), history.getPriorValue().substring(2));
    }

    @Test
    public void getGsonWithFalseTest() throws IOException {
        when(amazonPricing.getVendorProductID()).thenReturn(vendorProductID);
        when(amazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(amazonPricing.getOfferCount()).thenReturn(fOffertCount);
        when(amazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(amazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(amazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(amazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(amazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(amazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(amazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(amazonPricing.getAdded()).thenReturn(fAdded);
        when(amazonPricing.getAddedBy()).thenReturn(fAddedBy);

        when(plastamazonPricing.getVendorProductID()).thenReturn(vendorProductID );
        when(plastamazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(plastamazonPricing.getOfferCount()).thenReturn(10); //different
        when(plastamazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(plastamazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(plastamazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(plastamazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(plastamazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(plastamazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(plastamazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(plastamazonPricing.getAdded()).thenReturn(fAdded);
        when(plastamazonPricing.getAddedBy()).thenReturn(fAddedBy);
        AmazonPricingHistory amazonPricingHistory = new  AmazonPricingHistory(pricingID, plastamazonPricing);
        Gson a = amazonPricingHistory.getGson(false);
        String expected = "{\"azpid\":\"1\",\"vpid\":\"2\",\"azp_product_condition\":\"NEW\",\"azp_offer_count\":10,\"azp_price_currency\":\"USD\",\"azp_buybox_item_price\":10.0,\"azp_buybox_shipping_price\":15.0,\"azp_buybox_fba\":false,\"azp_buybox_seller_amazon\":false,\"azp_secondary_item_price\":20.0,\"azp_secondary_shipping_price\":25.0,\"azp_secondary_fba\":false,\"azp_downloaded\":\"1969-12-31T19:00:00.000-0500\",\"azp_added_dt\":\"1969-12-31T19:00:00.000-0500\",\"azp_added_by\":\"100\"}";
        assertEquals(a.toJson(amazonPricingHistory), expected);
    }

    @Test
    public void getGsonWithTrueTest() throws IOException {
        when(amazonPricing.getVendorProductID()).thenReturn(vendorProductID);
        when(amazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(amazonPricing.getOfferCount()).thenReturn(fOffertCount);
        when(amazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(amazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(amazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(amazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(amazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(amazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(amazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(amazonPricing.getAdded()).thenReturn(fAdded);
        when(amazonPricing.getAddedBy()).thenReturn(fAddedBy);

        when(plastamazonPricing.getVendorProductID()).thenReturn(vendorProductID );
        when(plastamazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(plastamazonPricing.getOfferCount()).thenReturn(10); //different
        when(plastamazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(plastamazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(plastamazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(plastamazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(plastamazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(plastamazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(plastamazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(plastamazonPricing.getAdded()).thenReturn(fAdded);
        when(plastamazonPricing.getAddedBy()).thenReturn(fAddedBy);
        AmazonPricingHistory amazonPricingHistory = new  AmazonPricingHistory(pricingID, plastamazonPricing);
        Gson a = amazonPricingHistory.getGson(true);
        String expected = "{\"azpid\":\"1\",\"vpid\":\"2\",\"azp_product_condition\":\"NEW\",\"azp_offer_count\":10,\"azp_price_currency\":\"USD\",\"azp_buybox_item_price\":10.0,\"azp_buybox_shipping_price\":15.0,\"azp_buybox_fba\":false,\"azp_buybox_seller_amazon\":false,\"azp_secondary_item_price\":20.0,\"azp_secondary_shipping_price\":25.0,\"azp_secondary_fba\":false,\"azp_downloaded\":\"1969-12-31T19:00:00.000-0500\",\"azp_added_dt\":\"1969-12-31T19:00:00.000-0500\",\"azp_added_by\":\"100\"}";
        assertEquals(a.toJson(amazonPricingHistory), expected);
    }

    @Test
    public void getListTypeTest() {
        when(amazonPricing.getVendorProductID()).thenReturn(vendorProductID);
        when(amazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(amazonPricing.getOfferCount()).thenReturn(fOffertCount);
        when(amazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(amazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(amazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(amazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(amazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(amazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(amazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(amazonPricing.getAdded()).thenReturn(fAdded);
        when(amazonPricing.getAddedBy()).thenReturn(fAddedBy);

        when(plastamazonPricing.getVendorProductID()).thenReturn(vendorProductID );
        when(plastamazonPricing.getProductCondition()).thenReturn(fProductCondition);
        when(plastamazonPricing.getOfferCount()).thenReturn(10); //different
        when(plastamazonPricing.getPriceCurrency()).thenReturn(priceCurrency);
        when(plastamazonPricing.getBuyboxItemPrice()).thenReturn(fBuyboxItemPrice);
        when(plastamazonPricing.getBuyboxShippingPrice()).thenReturn(fBuyboxShippingPrice);
        when(plastamazonPricing.getSecondaryItemPrice()).thenReturn(fSecondaryItemPrice);
        when(plastamazonPricing.getBuyboxSellerAmazon()).thenReturn(fBuyboxSellerAmazon);
        when(plastamazonPricing.getSecondaryShippingPrice()).thenReturn(fSecondaryShippingPrice);
        when(plastamazonPricing.getDownloaded()).thenReturn(fDownloaded);
        when(plastamazonPricing.getAdded()).thenReturn(fAdded);
        when(plastamazonPricing.getAddedBy()).thenReturn(fAddedBy);
        AmazonPricingHistory amazonPricingHistory = new  AmazonPricingHistory(pricingID, plastamazonPricing);
        Type expected = amazonPricingHistory.getListType();
        assertEquals(expected, new TypeToken<List<AmazonPricingHistory>>() {}.getType());
    }

    @Test
    public void newInstanceTestPath() throws IOException {
        PowerMockito.mockStatic(JsonDataObject.class);
        AmazonPricingHistory amazonPricingHistory = mock(AmazonPricingHistory.class);
        when(JsonDataObject.fromJson(any(Gson.class),any(Path.class), any(Class.class))).thenReturn(amazonPricingHistory);
    }

}