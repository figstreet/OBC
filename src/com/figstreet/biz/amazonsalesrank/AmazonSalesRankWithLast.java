package com.figstreet.biz.amazonsalesrank;

import com.figstreet.data.amazonsalesrank.AmazonSalesRank;

public class AmazonSalesRankWithLast
{
	private AmazonSalesRank fAmazonSalesRank;
	private AmazonSalesRank fLastAmazonSalesRank;

	public AmazonSalesRankWithLast(AmazonSalesRank pAmazonSalesRank) throws CloneNotSupportedException
	{
		this.fAmazonSalesRank = pAmazonSalesRank;
		this.fLastAmazonSalesRank = (AmazonSalesRank)pAmazonSalesRank.clone();
	}

	public AmazonSalesRank getAmazonSalesRank()
	{
		return this.fAmazonSalesRank;
	}

	public AmazonSalesRank getLastAmazonSalesRank()
	{
		return this.fLastAmazonSalesRank;
	}

}
