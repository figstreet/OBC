package com.figstreet.biz.vendorproduct;

import com.figstreet.data.vendorproduct.VendorProduct;

public class VendorProductWithLast
{
	private VendorProduct fVendorProduct;
	private VendorProduct fLastVendorProduct;

	public VendorProductWithLast(VendorProduct pVendorProduct) throws CloneNotSupportedException
	{
		this.fVendorProduct = pVendorProduct;
		this.fLastVendorProduct = (VendorProduct)pVendorProduct.clone();
	}

	public VendorProduct getVendorProduct()
	{
		return this.fVendorProduct;
	}

	public VendorProduct getLastVendorProduct()
	{
		return this.fLastVendorProduct;
	}

}
