package com.figstreet.biz.amazonsalesrank;

import com.figstreet.core.DateUtil;
import com.figstreet.data.amazonsalesrank.AmazonSalesRank;
import com.figstreet.data.common.AmazonSalesRankCategory;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProductID;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AmazonSalesRankWithLastTest {
    private AmazonSalesRank fAmazonSalesRank;

    private AmazonSalesRankWithLast fAmazonSalesRankWithLast;

    private final VendorProductID fVendorProductID = new VendorProductID(1L);
    private final UsersID fAddedBy = UsersID.ADMIN;
    private final Integer fPrimaryRank = new Integer(2);
    private final AmazonSalesRankCategory fPrimaryCategory = AmazonSalesRankCategory.GROCERY_AND_GOURMET_FOOD;
    private final Integer fSecondaryRank = new Integer(3);
    private final AmazonSalesRankCategory fSecondaryCategory = AmazonSalesRankCategory.TOYS_AND_GAMES;
    private final Timestamp fDownloaded = DateUtil.now();

    @Before
    public void beforeTest() throws CloneNotSupportedException {
        fAmazonSalesRank = new AmazonSalesRank(fVendorProductID, fAddedBy);
        fAmazonSalesRank.setPrimaryRank(fPrimaryRank);
        fAmazonSalesRank.setPrimarySalesRankCategory(fPrimaryCategory);
        fAmazonSalesRank.setSecondaryRank(fSecondaryRank);
        fAmazonSalesRank.setSecondarySalesRankCategory(fSecondaryCategory);
        fAmazonSalesRank.setDownloaded(fDownloaded);
        fAmazonSalesRankWithLast = new AmazonSalesRankWithLast(fAmazonSalesRank);
    }

    //TODO - add test methods for remaining member variables in Product

    @Test
    public void testVendorProductID() throws CloneNotSupportedException {
        assertEquals(fAmazonSalesRank, fAmazonSalesRankWithLast.getAmazonSalesRank());
        fAmazonSalesRank.setVendorProductID(new VendorProductID(0L));
        assertEquals(fVendorProductID, fAmazonSalesRankWithLast.getLastAmazonSalesRank().getVendorProductID());
        assertNotEquals(fAmazonSalesRankWithLast.getAmazonSalesRank().getVendorProductID(), fAmazonSalesRankWithLast.getLastAmazonSalesRank().getVendorProductID());
    }
    @Test
    public void testAddedBy() throws CloneNotSupportedException {
        assertEquals(fAmazonSalesRank, fAmazonSalesRankWithLast.getAmazonSalesRank());
        fAmazonSalesRank.setAddedBy(null);
        assertEquals(fAddedBy, fAmazonSalesRankWithLast.getLastAmazonSalesRank().getAddedBy());
        assertNotEquals(fAmazonSalesRankWithLast.getAmazonSalesRank().getAddedBy(), fAmazonSalesRankWithLast.getLastAmazonSalesRank().getAddedBy());
    }

    @Test
    public void testPrimaryRank() throws CloneNotSupportedException {
        assertEquals(fAmazonSalesRank, fAmazonSalesRankWithLast.getAmazonSalesRank());
        fAmazonSalesRank.setPrimaryRank( new Integer(5));
        assertEquals(fPrimaryRank, fAmazonSalesRankWithLast.getLastAmazonSalesRank().getPrimaryRank());
        assertNotEquals(fAmazonSalesRankWithLast.getAmazonSalesRank().getPrimaryRank(), fAmazonSalesRankWithLast.getLastAmazonSalesRank().getPrimaryRank());
    }

    @Test
    public void testPrimarySalesRankCategory() throws CloneNotSupportedException {
        assertEquals(fAmazonSalesRank, fAmazonSalesRankWithLast.getAmazonSalesRank());
        fAmazonSalesRank.setPrimarySalesRankCategory(AmazonSalesRankCategory.TOYS_AND_GAMES);
        assertEquals(fPrimaryCategory, fAmazonSalesRankWithLast.getLastAmazonSalesRank().getPrimarySalesRankCategory());
        assertNotEquals(fAmazonSalesRankWithLast.getAmazonSalesRank().getPrimarySalesRankCategory(), fAmazonSalesRankWithLast.getLastAmazonSalesRank().getPrimarySalesRankCategory());
    }

    @Test
    public void testSecundaryRank() throws CloneNotSupportedException {
        assertEquals(fAmazonSalesRank, fAmazonSalesRankWithLast.getAmazonSalesRank());
        fAmazonSalesRank.setSecondaryRank( new Integer(17));
        assertEquals(fSecondaryRank, fAmazonSalesRankWithLast.getLastAmazonSalesRank().getSecondaryRank());
        assertNotEquals(fAmazonSalesRankWithLast.getAmazonSalesRank().getSecondaryRank(), fAmazonSalesRankWithLast.getLastAmazonSalesRank().getSecondaryRank());
    }

    @Test
    public void testSecundarySalesRankCategory() throws CloneNotSupportedException {
        assertEquals(fAmazonSalesRank, fAmazonSalesRankWithLast.getAmazonSalesRank());
        fAmazonSalesRank.setSecondarySalesRankCategory(AmazonSalesRankCategory.GROCERY_AND_GOURMET_FOOD);
        assertEquals(fSecondaryCategory, fAmazonSalesRankWithLast.getLastAmazonSalesRank().getSecondarySalesRankCategory());
        assertNotEquals(fAmazonSalesRankWithLast.getAmazonSalesRank().getSecondarySalesRankCategory(), fAmazonSalesRankWithLast.getLastAmazonSalesRank().getSecondarySalesRankCategory());
    }

    @Test
    public void testDownload() throws CloneNotSupportedException {
        assertEquals(fAmazonSalesRank, fAmazonSalesRankWithLast.getAmazonSalesRank());
        fAmazonSalesRank.setDownloaded(DateUtil.now());
        assertEquals(fDownloaded, fAmazonSalesRankWithLast.getLastAmazonSalesRank().getDownloaded());
        assertNotEquals(fAmazonSalesRankWithLast.getAmazonSalesRank().getDownloaded(), fAmazonSalesRankWithLast.getLastAmazonSalesRank().getDownloaded());
    }


}
