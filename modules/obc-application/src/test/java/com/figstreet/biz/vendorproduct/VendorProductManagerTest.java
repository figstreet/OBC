package com.figstreet.biz.vendorproduct;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorproduct.VendorProduct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ VendorProductManager.class, VendorProductHistory.class, HibernateTransaction.class })
public class VendorProductManagerTest {
    @Test
    public void updateVendorProductTest() throws Exception {
        mockStatic(VendorProductHistory.class);
        mockStatic(HibernateTransaction.class);
        VendorProduct pVendorProduct = mock(VendorProduct.class);
        VendorProduct pLastVendorProduct = mock(VendorProduct.class);
        UsersID pBy = mock(UsersID.class);
        History vendorProductHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(VendorProductHistory.buildVendorProductHistory(pVendorProduct, pLastVendorProduct, pBy))
                .thenReturn(vendorProductHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pVendorProduct).saveOrUpdate(pBy);
        List<String> result = VendorProductManager.updateVendorProduct(pVendorProduct, pLastVendorProduct, pBy);
        verify(pVendorProduct).saveOrUpdate(pBy);
        verify(trans).commit();
        verify(trans).close();
    }

    @Test
    public void updateVendorProductWithNullTest() throws Exception {
        mockStatic(VendorProductHistory.class);
        mockStatic(HibernateTransaction.class);
        VendorProduct pVendorProduct = mock(VendorProduct.class);
        VendorProduct pLastVendorProduct = mock(VendorProduct.class);
        UsersID pBy = mock(UsersID.class);
        History vendorProductHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(VendorProductHistory.buildVendorProductHistory(pVendorProduct, pLastVendorProduct, pBy))
                .thenReturn(vendorProductHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pVendorProduct).saveOrUpdate(pBy);
        List<String> result = VendorProductManager.updateVendorProduct(null, pLastVendorProduct, pBy);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), "VendorProduct cannot be null");
    }
}
