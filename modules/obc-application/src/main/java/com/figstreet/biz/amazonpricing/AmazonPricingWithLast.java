package com.figstreet.biz.amazonpricing;

import com.figstreet.data.amazonpricing.AmazonPricing;

public class AmazonPricingWithLast
{
	private AmazonPricing fAmazonPricing;
	private AmazonPricing fLastAmazonPricing;

	public AmazonPricingWithLast(AmazonPricing pAmazonPricing) throws CloneNotSupportedException
	{
		this.fAmazonPricing = pAmazonPricing;
		this.fLastAmazonPricing = (AmazonPricing)pAmazonPricing.clone();
	}

	public AmazonPricing getAmazonPricing()
	{
		return this.fAmazonPricing;
	}

	public AmazonPricing getLastAmazonPricing()
	{
		return this.fLastAmazonPricing;
	}

}
