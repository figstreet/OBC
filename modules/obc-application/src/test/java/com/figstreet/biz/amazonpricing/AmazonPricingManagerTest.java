package com.figstreet.biz.amazonpricing;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.amazonpricing.AmazonPricing;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AmazonPricingManager.class, AmazonPricingHistory.class, HibernateTransaction.class })
public class AmazonPricingManagerTest {
    @Test
    public void updateAmazonPricingTest() throws Exception {
        mockStatic(AmazonPricingHistory.class);
        mockStatic(HibernateTransaction.class);
        AmazonPricing pAmazonPricing = mock(AmazonPricing.class);
        AmazonPricing pLastAmazonPricing = mock(AmazonPricing.class);
        UsersID pBy = mock(UsersID.class);
        History amazonPricingHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(AmazonPricingHistory.buildAmazonPricingHistory(pAmazonPricing, pLastAmazonPricing, pBy))
                .thenReturn(amazonPricingHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pAmazonPricing).saveOrUpdate(pBy);
        List<String> result = AmazonPricingManager.updateAmazonPricing(pAmazonPricing, pLastAmazonPricing, pBy);
        verify(pAmazonPricing).saveOrUpdate(pBy);
        verify(trans).commit();
        verify(trans).close();
    }

    @Test
    public void updateAmazonPricingWithNullTest() throws Exception {
        mockStatic(AmazonPricingHistory.class);
        mockStatic(HibernateTransaction.class);
        AmazonPricing pAmazonPricing = mock(AmazonPricing.class);
        AmazonPricing pLastAmazonPricing = mock(AmazonPricing.class);
        UsersID pBy = mock(UsersID.class);
        History amazonPricingHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(AmazonPricingHistory.buildAmazonPricingHistory(pAmazonPricing, pLastAmazonPricing, pBy))
                .thenReturn(amazonPricingHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pAmazonPricing).saveOrUpdate(pBy);
        List<String> result = AmazonPricingManager.updateAmazonPricing(null, pLastAmazonPricing, pBy);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), "AmazonPricing cannot be null");
    }
}
