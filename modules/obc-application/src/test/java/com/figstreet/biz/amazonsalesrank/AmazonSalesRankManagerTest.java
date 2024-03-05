package com.figstreet.biz.amazonsalesrank;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.amazonsalesrank.AmazonSalesRank;
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
@PrepareForTest({ AmazonSalesRankManager.class, AmazonSalesRankHistory.class, HibernateTransaction.class })
public class AmazonSalesRankManagerTest {
    @Test
    public void updateAmazonSalesRankTest() throws Exception {
        mockStatic(AmazonSalesRankHistory.class);
        mockStatic(HibernateTransaction.class);
        AmazonSalesRank pSalesRank = mock(AmazonSalesRank.class);
        AmazonSalesRank pLastSalesRank = mock(AmazonSalesRank.class);
        UsersID pBy = mock(UsersID.class);
        History amazonSalesRankHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(AmazonSalesRankHistory.buildAmazonSalesRankHistory(pSalesRank, pLastSalesRank, pBy))
                .thenReturn(amazonSalesRankHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pSalesRank).saveOrUpdate(pBy);
        List<String> result = AmazonSalesRankManager.updateAmazonSalesRank(pSalesRank, pLastSalesRank, pBy);
        verify(pSalesRank).saveOrUpdate(pBy);
        verify(trans).commit();
        verify(trans).close();
    }

    @Test
    public void updateAmazonSalesRankWithNullTest() throws Exception {
        mockStatic(AmazonSalesRankHistory.class);
        mockStatic(HibernateTransaction.class);
        AmazonSalesRank pSalesRank = mock(AmazonSalesRank.class);
        AmazonSalesRank pLastSalesRank = mock(AmazonSalesRank.class);
        UsersID pBy = mock(UsersID.class);
        History amazonSalesRankHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(AmazonSalesRankHistory.buildAmazonSalesRankHistory(pSalesRank, pLastSalesRank, pBy))
                .thenReturn(amazonSalesRankHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pSalesRank).saveOrUpdate(pBy);
        List<String> result = AmazonSalesRankManager.updateAmazonSalesRank(null, pLastSalesRank, pBy);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), "AmazonSalesRank cannot be null");
    }

}
