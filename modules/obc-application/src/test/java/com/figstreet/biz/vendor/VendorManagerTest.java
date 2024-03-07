package com.figstreet.biz.vendor;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.Vendor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ VendorManager.class, VendorHistory.class, HibernateTransaction.class })
public class VendorManagerTest {
    @Test
    public void updateVendorTest() throws Exception {
        mockStatic(VendorHistory.class);
        mockStatic(HibernateTransaction.class);
        Vendor pVendor = mock(Vendor.class);
        Vendor pLastVendor = mock(Vendor.class);
        UsersID pBy = mock(UsersID.class);
        History vendorHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(VendorHistory.buildVendorHistory(pVendor, pLastVendor, pBy))
                .thenReturn(vendorHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pVendor).saveOrUpdate(pBy);
        List<String> result = VendorManager.updateVendor(pVendor, pLastVendor, pBy);
        verify(pVendor).saveOrUpdate(pBy);
        verify(trans).commit();
        verify(trans).close();
    }

    @Test
    public void updateVendorWithNullTest() throws Exception {
        mockStatic(VendorHistory.class);
        mockStatic(HibernateTransaction.class);
        Vendor pVendor = mock(Vendor.class);
        Vendor pLastVendor = mock(Vendor.class);
        UsersID pBy = mock(UsersID.class);
        History vendorHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(VendorHistory.buildVendorHistory(pVendor, pLastVendor, pBy))
                .thenReturn(vendorHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pVendor).saveOrUpdate(pBy);
        List<String> result = VendorManager.updateVendor(null, pLastVendor, pBy);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), "Vendor cannot be null");
    }
}
