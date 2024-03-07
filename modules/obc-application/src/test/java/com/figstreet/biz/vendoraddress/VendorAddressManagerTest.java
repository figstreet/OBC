package com.figstreet.biz.vendoraddress;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendoraddress.VendorAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ VendorAddressManager.class, VendorAddressHistory.class, HibernateTransaction.class })
public class VendorAddressManagerTest {
    @Test
    public void updateVendorAddressTest() throws Exception {
        mockStatic(VendorAddressHistory.class);
        mockStatic(HibernateTransaction.class);
        VendorAddress pVendorAddress = mock(VendorAddress.class);
        VendorAddress pLastVendorAddress = mock(VendorAddress.class);
        UsersID pBy = mock(UsersID.class);
        History vendorAddressHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(VendorAddressHistory.buildVendorAddressHistory(pVendorAddress, pLastVendorAddress, pBy))
                .thenReturn(vendorAddressHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pVendorAddress).saveOrUpdate(pBy);
        List<String> result = VendorAddressManager.updateVendorAddress(pVendorAddress, pLastVendorAddress, pBy);
        verify(pVendorAddress).saveOrUpdate(pBy);
        verify(trans).commit();
        verify(trans).close();
    }

    @Test
    public void updateVendorAddressWithNullTest() throws Exception {
        mockStatic(VendorAddressHistory.class);
        mockStatic(HibernateTransaction.class);
        VendorAddress pVendorAddress = mock(VendorAddress.class);
        VendorAddress pLastVendorAddress = mock(VendorAddress.class);
        UsersID pBy = mock(UsersID.class);
        History vendorAddressHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(VendorAddressHistory.buildVendorAddressHistory(pVendorAddress, pLastVendorAddress, pBy))
                .thenReturn(vendorAddressHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pVendorAddress).saveOrUpdate(pBy);
        List<String> result = VendorAddressManager.updateVendorAddress(null, pLastVendorAddress, pBy);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), "VendorAddress cannot be null");
    }
}
