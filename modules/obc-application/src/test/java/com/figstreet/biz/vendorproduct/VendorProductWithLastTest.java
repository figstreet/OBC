package com.figstreet.biz.vendorproduct;

import com.figstreet.data.vendorproduct.VendorProduct;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class VendorProductWithLastTest {
    @Test
    public void VendorProductWithLastTest() throws CloneNotSupportedException {
        VendorProduct mockVendorProduct = mock(VendorProduct.class);
        when(mockVendorProduct.clone()).thenReturn(mockVendorProduct);
        VendorProductWithLast vendorProductWithLast = new VendorProductWithLast(mockVendorProduct);
        assertEquals(mockVendorProduct, vendorProductWithLast.getVendorProduct());
        assertEquals(mockVendorProduct, vendorProductWithLast.getLastVendorProduct());
    }
}
