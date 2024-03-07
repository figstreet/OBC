package com.figstreet.biz.product;

import com.figstreet.core.HibernateTransaction;
import com.figstreet.data.history.History;
import com.figstreet.data.product.Product;
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
@PrepareForTest({ ProductManager.class, ProductHistory.class, HibernateTransaction.class })
public class ProductManagerTest {
    @Test
    public void updateProductTest() throws Exception {
        mockStatic(ProductHistory.class);
        mockStatic(HibernateTransaction.class);
        Product pProduct = mock(Product.class);
        Product pLastProduct = mock(Product.class);
        UsersID pBy = mock(UsersID.class);
        History productHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(ProductHistory.buildProductHistory(pProduct, pLastProduct, pBy))
                .thenReturn(productHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pProduct).saveOrUpdate(pBy);
        List<String> result = ProductManager.updateProduct(pProduct, pLastProduct, pBy);
        verify(pProduct).saveOrUpdate(pBy);
        verify(trans).commit();
        verify(trans).close();
    }

    @Test
    public void updateProductWithNullTest() throws Exception {
        mockStatic(ProductHistory.class);
        mockStatic(HibernateTransaction.class);
        Product pProduct = mock(Product.class);
        Product pLastProduct = mock(Product.class);
        UsersID pBy = mock(UsersID.class);
        History productHistory = mock(History.class);
        HibernateTransaction trans = mock(HibernateTransaction.class);
        when(ProductHistory.buildProductHistory(pProduct, pLastProduct, pBy))
                .thenReturn(productHistory);
        when(HibernateTransaction.open()).thenReturn(trans);
        doNothing().when(pProduct).saveOrUpdate(pBy);
        List<String> result = ProductManager.updateProduct(null, pLastProduct, pBy);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), "Product cannot be null");
    }
}
