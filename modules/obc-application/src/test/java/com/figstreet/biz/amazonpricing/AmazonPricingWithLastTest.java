package com.figstreet.biz.amazonpricing;

import com.figstreet.data.amazonpricing.AmazonPricing;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AmazonPricingWithLastTest {
    @Test
    public void AmaazonPricingWithLastTest() throws CloneNotSupportedException {
        AmazonPricing mockAmazonPricing = mock(AmazonPricing.class);
        when(mockAmazonPricing.clone()).thenReturn(mockAmazonPricing);
        AmazonPricingWithLast amazonPricingWithLast = new AmazonPricingWithLast(mockAmazonPricing);
        assertEquals(mockAmazonPricing, amazonPricingWithLast.getAmazonPricing());
        assertEquals(mockAmazonPricing, amazonPricingWithLast.getLastAmazonPricing());
    }
}
