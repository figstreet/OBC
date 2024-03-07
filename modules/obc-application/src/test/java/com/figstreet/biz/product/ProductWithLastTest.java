package com.figstreet.biz.product;

import com.figstreet.data.product.Product;
import com.figstreet.data.users.UsersID;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ProductWithLastTest {
    private Product fProduct;
    private ProductWithLast fProductWithLast;


    private final String fName = "name";
    private final String fUpc = "upc";
    private final String fSku = "sku";
    private final UsersID fUsersID = UsersID.ADMIN;
    private final String fShortDesc = "shortDescription";
    private final String fLongDesc = "longDescription";

    @Before
    public void beforeTest() throws CloneNotSupportedException {
        fProduct = new Product(fName, fUpc, fSku, fUsersID);
        fProduct.setShortDescription(fShortDesc);
        fProduct.setLongDescription(fLongDesc);
        fProductWithLast = new ProductWithLast(fProduct);
    }

    //TODO - add test methods for remaining member variables in Product

    @Test
    public void testName() throws CloneNotSupportedException {
        assertEquals(fProduct, fProductWithLast.getProduct());
        fProduct.setName("test");
        assertEquals(fName, fProductWithLast.getLastProduct().getName());
        assertNotEquals(fProductWithLast.getProduct().getName(), fProductWithLast.getLastProduct().getName());
    }

    @Test
    public void testActive() throws CloneNotSupportedException {
        assertEquals(fProduct, fProductWithLast.getProduct());
        fProduct.setActive(false);
        assertTrue(fProductWithLast.getLastProduct().isActive());
        assertNotEquals(fProductWithLast.getProduct().isActive(), fProductWithLast.getLastProduct().isActive());
    }

    @Test
    public void testUpc() throws CloneNotSupportedException {
        assertEquals(fProduct, fProductWithLast.getProduct());
        fProduct.setUpc("test");
        assertEquals(fUpc, fProductWithLast.getLastProduct().getUpc());
        assertNotEquals(fProductWithLast.getProduct().getUpc(), fProductWithLast.getLastProduct().getUpc());
    }

    @Test
    public void testSku() throws CloneNotSupportedException {
        assertEquals(fProduct, fProductWithLast.getProduct());
        fProduct.setSku("test");
        assertEquals(fSku, fProductWithLast.getLastProduct().getSku());
        assertNotEquals(fProductWithLast.getProduct().getSku(), fProductWithLast.getLastProduct().getSku());
    }

    @Test
    public void testShortDescription() throws CloneNotSupportedException {
        assertEquals(fProduct, fProductWithLast.getProduct());
        fProduct.setShortDescription("test");
        assertEquals(fShortDesc, fProductWithLast.getLastProduct().getShortDescription());
        assertNotEquals(fProductWithLast.getProduct().getShortDescription(), fProductWithLast.getLastProduct().getShortDescription());
    }

    @Test
    public void testLongDescription() throws CloneNotSupportedException {
        assertEquals(fProduct, fProductWithLast.getProduct());
        fProduct.setLongDescription("test");
        assertEquals(fLongDesc, fProductWithLast.getLastProduct().getLongDescription());
        assertNotEquals(fProductWithLast.getProduct().getLongDescription(), fProductWithLast.getLastProduct().getLongDescription());
    }
}
