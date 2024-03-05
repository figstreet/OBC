package com.figstreet.biz.amazonsalesrank;

import com.figstreet.data.amazonsalesrank.AmazonSalesRank;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AmazonSalesRankWithLastTest {

    @Test
    public void AmazonSalesRankWithLastTest() throws CloneNotSupportedException {
        AmazonSalesRank mockAmazonSalesRank = mock(AmazonSalesRank.class);
        when(mockAmazonSalesRank.clone()).thenReturn(mockAmazonSalesRank);
        AmazonSalesRankWithLast amazonSalesRankWithLast = new AmazonSalesRankWithLast(mockAmazonSalesRank);
        assertEquals(mockAmazonSalesRank, amazonSalesRankWithLast.getAmazonSalesRank());
        assertEquals(mockAmazonSalesRank, amazonSalesRankWithLast.getLastAmazonSalesRank());
    }
}
