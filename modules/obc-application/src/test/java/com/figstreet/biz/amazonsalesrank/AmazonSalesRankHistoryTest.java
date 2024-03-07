package com.figstreet.biz.amazonsalesrank;

import com.figstreet.core.json.JsonDataObject;
import com.figstreet.data.amazonsalesrank.AmazonSalesRank;
import com.figstreet.data.amazonsalesrank.AmazonSalesRankID;
import com.figstreet.data.common.AmazonSalesRankCategory;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProductID;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.checkerframework.checker.units.qual.A;
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
public class AmazonSalesRankHistoryTest {
    AmazonSalesRankID fAmazonSalesRankID = new AmazonSalesRankID(1L);
    VendorProductID fVendorProductID = new VendorProductID(1L);
    Integer fPrimaryRank = 2;
    AmazonSalesRankCategory fPrimarySalesRankCategory = AmazonSalesRankCategory.GROCERY_AND_GOURMET_FOOD;
    Integer fSecondaryRank = 3;
    AmazonSalesRankCategory fSecondarySalesRankCategory = AmazonSalesRankCategory.TOYS_AND_GAMES;
    Timestamp fDownloaded = new Timestamp(0);
    Timestamp fAdded = new Timestamp(0);
    UsersID fAddedBy = UsersID.ADMIN;
    String json = "{\"azsrid\":\"1\",\"azsr_primary_rank\":2,\"azsr_primary_rank_category\":\"grocery_d-o-w\",\"azsr_secondary_rank\":3,\"azsr_secondary_rank_category\":{},\"azsr_added_dt\":\"1969-12-31T19:00:00.000-0500\",\"azsr_added_by\":\"100\"}";

    @Mock
    Path pJsonFile;
    @Mock
    AmazonSalesRank amazonSalesRank;
    @Mock
    AmazonSalesRank plastamazonSalesRank;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(amazonSalesRank.getPrimaryRank()).thenReturn(fPrimaryRank);
        when(amazonSalesRank.getPrimarySalesRankCategory()).thenReturn(fPrimarySalesRankCategory);
        when(amazonSalesRank.getSecondaryRank()).thenReturn(fSecondaryRank);
        when(amazonSalesRank.getAdded()).thenReturn(fAdded);
        when(amazonSalesRank.getAddedBy()).thenReturn(fAddedBy);
        when(amazonSalesRank.getSecondarySalesRankCategory()).thenReturn(fSecondarySalesRankCategory);

        when(plastamazonSalesRank.getPrimaryRank()).thenReturn(fPrimaryRank);
        when(plastamazonSalesRank.getPrimarySalesRankCategory()).thenReturn(fPrimarySalesRankCategory);
        when(plastamazonSalesRank.getSecondaryRank()).thenReturn(fSecondaryRank);
        when(plastamazonSalesRank.getAdded()).thenReturn(fAdded);
        when(plastamazonSalesRank.getAddedBy()).thenReturn(fAddedBy);
        when(plastamazonSalesRank.getSecondarySalesRankCategory()).thenReturn(fSecondarySalesRankCategory);
    }


    @Test
    public void AmazonSalesRankConstructorTest() {
        AmazonSalesRankHistory amazonSalesRankHistory = new AmazonSalesRankHistory(fAmazonSalesRankID, plastamazonSalesRank);
        assertEquals(amazonSalesRankHistory.getPrimaryRank(),(Object) fPrimaryRank);
        assertEquals(amazonSalesRankHistory.getPrimarySalesRankCategory(),fPrimarySalesRankCategory);
        assertEquals(amazonSalesRankHistory.getSecondaryRank(),(Object) fSecondaryRank);
        assertEquals(amazonSalesRankHistory.getAdded(),fAdded);
        assertEquals(amazonSalesRankHistory.getAddedBy(),fAddedBy);
        assertEquals(amazonSalesRankHistory.getSecundarySalesRankCategory(),fSecondarySalesRankCategory);
    }


    @Test
    public void hasChangedTest() throws Exception {
        when(plastamazonSalesRank.getPrimaryRank()).thenReturn(19);
        boolean expected = AmazonSalesRankHistory.hasChanged(amazonSalesRank, plastamazonSalesRank);
        assertTrue(expected);
    }


    @Test
    public void hasChangedTestReturnFalse() throws Exception {
        boolean expected = AmazonSalesRankHistory.hasChanged(amazonSalesRank, plastamazonSalesRank);
        assertFalse(expected);
    }


    @Test
    public void constructorTestWithNull() {
        when(plastamazonSalesRank.getPrimaryRank()).thenReturn(19);
        AmazonSalesRankHistory amazonSalesRankHistory = new AmazonSalesRankHistory(fAmazonSalesRankID, null);
        assertNotNull(amazonSalesRankHistory);
    }

    @Test
    public void buildProductHistoryTest() {
        when(plastamazonSalesRank.getPrimaryRank()).thenReturn(19);
        History history = AmazonSalesRankHistory.buildAmazonSalesRankHistory(amazonSalesRank, plastamazonSalesRank, fAddedBy);
        AmazonSalesRankHistory amazonSalesRankHistory = new AmazonSalesRankHistory(fAmazonSalesRankID, plastamazonSalesRank);
        assertEquals(amazonSalesRankHistory.toJsonString().substring(15), history.getPriorValue().substring(2));
    }

    @Test
    public void getGsonWithFalseTest() throws IOException {
        AmazonSalesRankHistory amazonSalesRankHistory = new AmazonSalesRankHistory(fAmazonSalesRankID, plastamazonSalesRank);
        Gson a = amazonSalesRankHistory.getGson(false);
        String expected = json;
        assertEquals(a.toJson(amazonSalesRankHistory), expected);
    }

    @Test
    public void getGsonWithTrueTest() throws IOException {
        AmazonSalesRankHistory productHistory = new AmazonSalesRankHistory(fAmazonSalesRankID, plastamazonSalesRank);
        Gson a = productHistory.getGson(true);
        String expected = json;
        assertEquals(a.toJson(productHistory), expected);
    }


    @Test
    public void getListTypeTest() {
        AmazonSalesRankHistory amazonSalesRankHistory = new AmazonSalesRankHistory(fAmazonSalesRankID, plastamazonSalesRank);
        Type expected = amazonSalesRankHistory.getListType();
        assertEquals(expected, new TypeToken<List<AmazonSalesRankHistory>>() {}.getType());
    }

    @Test
    public void newInstanceTestPath() throws IOException {
        PowerMockito.mockStatic(JsonDataObject.class);
        AmazonSalesRankHistory amazonSalesRankHistory = mock(AmazonSalesRankHistory.class);
        when(JsonDataObject.fromJson(any(Gson.class),any(Path.class), any(Class.class))).thenReturn(amazonSalesRankHistory);
    }

}
