package com.figstreet.biz.product;

import com.figstreet.data.product.Product;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ProductWithLastTest {
    @Test
    public void ProductWithLastTest() throws CloneNotSupportedException {
        Product mockProduct = mock(Product.class);
        when(mockProduct.clone()).thenReturn(mockProduct);
        ProductWithLast productWithLast = new ProductWithLast(mockProduct);
        assertEquals(mockProduct, productWithLast.getProduct());
        assertEquals(mockProduct, productWithLast.getLastProduct());
    }
}
