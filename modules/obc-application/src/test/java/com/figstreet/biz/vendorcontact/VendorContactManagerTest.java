package com.figstreet.biz.vendorcontact;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.history.History;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendorcontact.VendorContact;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ VendorContactManager.class, VendorContactHistory.class, HibernateTransaction.class })
public class VendorContactManagerTest {
    @Test
    public void updateVendorContactTest() throws Exception {
        mockStatic(VendorContactHistory.class);
        mockStatic(HibernateTransaction.class);
        VendorContact pVendorContact = mock(VendorContact.class);
        VendorContact pLastVendorContact = mock(VendorContact.class);
        UsersID pBy = mock(UsersID.class);
        History vendorContactHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(VendorContactHistory.buildVendorContactHistory(pVendorContact, pLastVendorContact, pBy))
                .thenReturn(vendorContactHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pVendorContact).saveOrUpdate(pBy);
        List<String> result = VendorContactManager.updateVendorContact(pVendorContact, pLastVendorContact, pBy);
        verify(pVendorContact).saveOrUpdate(pBy);
        verify(trans).commit();
        verify(trans).close();
    }

    @Test
    public void updateVendorContactWithNullTest() throws Exception {
        mockStatic(VendorContactHistory.class);
        mockStatic(HibernateTransaction.class);
        VendorContact pVendorContact = mock(VendorContact.class);
        VendorContact pLastVendorContact = mock(VendorContact.class);
        UsersID pBy = mock(UsersID.class);
        History vendorContactHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(VendorContactHistory.buildVendorContactHistory(pVendorContact, pLastVendorContact, pBy))
                .thenReturn(vendorContactHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pVendorContact).saveOrUpdate(pBy);
        List<String> result = VendorContactManager.updateVendorContact(null, pLastVendorContact, pBy);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), "VendorContact cannot be null");
    }
}
