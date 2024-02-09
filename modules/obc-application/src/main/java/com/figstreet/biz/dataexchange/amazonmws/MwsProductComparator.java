package com.figstreet.biz.dataexchange.amazonmws;

import java.io.Serializable;
import java.util.Comparator;

import com.figstreet.core.CompareUtil;

public class MwsProductComparator implements Comparator<MwsProduct>, Serializable
{
	private static final long serialVersionUID = 5505261149559000563L;

	public enum SortOrder
	{
		PRIMARY_SALES_RANK(new PrimarySalesRankComparator());

		private Comparator<MwsProduct> fComparator;

		SortOrder(Comparator<MwsProduct> comparator)
		{
			this.fComparator = comparator;
		}

		public Comparator<MwsProduct> getComparator()
		{
			return this.fComparator;
		}
	}

	private static class PrimarySalesRankComparator implements Comparator<MwsProduct>
	{
		@Override
		public int compare(MwsProduct pLeft, MwsProduct pRight)
		{
			int rc = 0;
			if (pLeft.getPrimarySalesRank() == null)
			{
				if (pRight.getPrimarySalesRank() == null)
					rc = 0;
				else
					rc = 1; //Null is greater
			}
			else if (pRight.getPrimarySalesRank() == null)
				rc = -1;
			else
				rc = CompareUtil.compareInteger(pLeft.getPrimarySalesRank(), pRight.getPrimarySalesRank());
			if (rc != 0)
				return rc;
			return defaultCompare(pLeft, pRight);
		}
	}

	private static int defaultCompare(MwsProduct pLeft, MwsProduct pRight)
	{
		return CompareUtil.compare(pLeft.getAsin(), pRight.getAsin());
	}

	public MwsProductComparator()
	{
		// default values
	}

	public MwsProductComparator(SortOrder pSortOrder, boolean pDescending)
	{
		this.fSortOrder = pSortOrder;
		this.fDescending = pDescending;
	}

	private SortOrder fSortOrder = SortOrder.PRIMARY_SALES_RANK;
	private boolean fDescending;

	@Override
	public int compare(MwsProduct pLeft, MwsProduct pRight)
	{
		if (!this.fDescending)
			return this.fSortOrder.getComparator().compare(pLeft, pRight);
		return this.fSortOrder.getComparator().compare(pRight, pLeft);
	}

	public SortOrder getSortOrder()
	{
		return this.fSortOrder;
	}

	public boolean getSortOrderIsPrimarySalesRank()
	{
		return this.fSortOrder.equals(SortOrder.PRIMARY_SALES_RANK);
	}

	public void setSortOrder(SortOrder pSortOrder)
	{
		this.fSortOrder = pSortOrder;
	}

	public boolean isDescending()
	{
		return this.fDescending;
	}

	public void setDescending(boolean pDescending)
	{
		this.fDescending = pDescending;
	}
}
