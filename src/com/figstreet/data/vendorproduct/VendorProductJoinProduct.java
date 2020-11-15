package com.figstreet.data.vendorproduct;

import java.io.Serializable;

import com.figstreet.data.product.Product;

public class VendorProductJoinProduct implements Serializable
{
	private static final long serialVersionUID = 633187502100134944L;

	private VendorProduct fVendorProduct;
	private Product fProduct;

	public VendorProductJoinProduct(VendorProduct pVendorProduct, Product pProduct)
	{
		this.fVendorProduct = pVendorProduct;
		this.fProduct = pProduct;
	}

	public VendorProduct getVendorProduct()
	{
		return this.fVendorProduct;
	}

	public Product getProduct()
	{
		return this.fProduct;
	}

}
