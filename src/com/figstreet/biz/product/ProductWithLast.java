package com.figstreet.biz.product;

import com.figstreet.data.product.Product;

public class ProductWithLast
{
	private Product fProduct;
	private Product fLastProduct;

	public ProductWithLast(Product pProduct) throws CloneNotSupportedException
	{
		this.fProduct = pProduct;
		this.fLastProduct = (Product)pProduct.clone();
	}

	public Product getProduct()
	{
		return this.fProduct;
	}

	public Product getLastProduct()
	{
		return this.fLastProduct;
	}

}
