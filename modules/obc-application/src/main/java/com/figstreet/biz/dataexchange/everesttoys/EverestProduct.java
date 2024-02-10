package com.figstreet.biz.dataexchange.everesttoys;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.figstreet.core.CompareUtil;
import com.figstreet.core.DateUtil;
import com.figstreet.core.FormatUtil;
import com.figstreet.data.product.Product;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendorproduct.VendorProduct;

public class EverestProduct
{
	private static final Pattern PER_ORDER_PATTERN = Pattern.compile(".+\\(([0-9]+)\\).*");
	private static final Pattern PER_ORDER_PATTERN_2 = Pattern.compile(".+\\(.*\\s+([0-9]+)\\).*");

	private String fCode;
	private String fDescription;
	private String fStatus1;
	private String fStatus2;
	private String fUpc;
	private double fPrice;
	private int fPerOrder = 1;
	private int fPerCase;
	private Integer fQuantityBreak;
	private Double fQuantityBreakPrice;

	private Product fProduct;
	private Product fLastProduct;

	private VendorProduct fVendorProduct;
	private VendorProduct fLastVendorProduct;

	public EverestProduct()
	{
		// Empty ctor
	}

	public EverestProduct(Row pSpreadsheetRow, boolean pIncludePerCase)
	{
		if (pSpreadsheetRow == null)
			throw new IllegalArgumentException("Row is required");

		Iterator<Cell> cellIterator = pSpreadsheetRow.cellIterator();
		if (cellIterator.hasNext())
		{
			Cell codeCell = cellIterator.next();
			this.fCode = codeCell.getStringCellValue();
		}
		if (cellIterator.hasNext())
		{
			Cell descCell = cellIterator.next();
			this.fDescription = descCell.getStringCellValue();
			Matcher perOrderMatcher = PER_ORDER_PATTERN.matcher(this.fDescription);
			if (!perOrderMatcher.matches())
				perOrderMatcher = PER_ORDER_PATTERN_2.matcher(this.fDescription);

			if (perOrderMatcher.matches())
			{
				String perOrder = perOrderMatcher.group(1);
				try
				{
					this.fPerOrder = Integer.parseInt(perOrder);
				}
				catch (Exception e)
				{
					//do nothing
				}
			}

		}
		if (cellIterator.hasNext())
		{
			Cell status1Cell = cellIterator.next();
			String status1 = status1Cell.getStringCellValue();
			if (!CompareUtil.isEmpty(status1))
				this.fStatus1 = status1;
		}
		if (cellIterator.hasNext())
		{
			Cell status2Cell = cellIterator.next();
			String status2 = status2Cell.getStringCellValue();
			if (!CompareUtil.isEmpty(status2))
				this.fStatus2 = status2;
		}
		if (cellIterator.hasNext())
		{
			Cell upcCell = cellIterator.next();
			this.fUpc = upcCell.getStringCellValue();
		}
		if (pIncludePerCase && cellIterator.hasNext())
		{
			Cell perCaseCell = cellIterator.next();
			this.fPerCase = new Double(perCaseCell.getNumericCellValue()).intValue();
		}
		if (cellIterator.hasNext())
		{
			Cell priceCell = cellIterator.next();
			this.fPrice = priceCell.getNumericCellValue();
		}
		if (cellIterator.hasNext())
		{
			Cell quantityBreakCell = cellIterator.next();
			double quantityBreak = quantityBreakCell.getNumericCellValue();
			if (quantityBreak > 0)
				this.fQuantityBreak = new Integer(new Double(quantityBreak).intValue());
		}
		if (cellIterator.hasNext())
		{
			Cell quantityBreakPriceCell = cellIterator.next();
			double quantityBreakPrice = quantityBreakPriceCell.getNumericCellValue();
			if (quantityBreakPrice > 0)
				this.fQuantityBreakPrice = new Double(quantityBreakPrice);
		}
	}

	public String getCode()
	{
		return this.fCode;
	}

	public void setCode(String pCode)
	{
		this.fCode = pCode;
	}

	public String getDescription()
	{
		return this.fDescription;
	}

	public void setDescription(String pDescription)
	{
		this.fDescription = pDescription;
	}

	public String getStatus1()
	{
		return this.fStatus1;
	}

	public void setStatus1(String pStatus1)
	{
		this.fStatus1 = pStatus1;
	}

	public String getStatus2()
	{
		return this.fStatus2;
	}

	public void setStatus2(String pStatus2)
	{
		this.fStatus2 = pStatus2;
	}

	public String getUpc()
	{
		return this.fUpc;
	}

	public void setUpc(String pUpc)
	{
		this.fUpc = pUpc;
	}

	public double getPrice()
	{
		return this.fPrice;
	}

	public void setPrice(double pPrice)
	{
		this.fPrice = pPrice;
	}

	public int getPerOrder()
	{
		return this.fPerOrder;
	}

	public void setPerOrder(int pPerOrder)
	{
		this.fPerOrder = pPerOrder;
	}

	public int getPerCase()
	{
		return this.fPerCase;
	}

	public void setPerCase(int pPerCase)
	{
		this.fPerCase = pPerCase;
	}

	public Integer getQuantityBreak()
	{
		return this.fQuantityBreak;
	}

	public void setQuantityBreak(Integer pQuantityBreak)
	{
		this.fQuantityBreak = pQuantityBreak;
	}

	public Double getQuantityBreakPrice()
	{
		return this.fQuantityBreakPrice;
	}

	public void setQuantityBreakPrice(Double pQuantityBreakPrice)
	{
		this.fQuantityBreakPrice = pQuantityBreakPrice;
	}

	public Product getProduct(UsersID pBy)
	{
		if (this.fProduct != null)
			return this.fProduct;

		synchronized (this)
		{
			if (this.fProduct == null)
			{
				// Using the first 100 characters of the Description for the name, only when
				// adding a new Product
				Product product = new Product(FormatUtil.truncate(this.getDescription(), 100), this.getUpc(), null, pBy);
				product.setShortDescription(this.getDescription());
				this.fProduct = product;
			}
		}

		return this.fProduct;
	}

	public void syncProduct(Product pProduct) throws CloneNotSupportedException
	{
		this.fLastProduct = (Product) pProduct.clone();
		this.fProduct = pProduct;
		// Not updating the name
		this.fProduct.setActive(true);
		if (CompareUtil.isEmpty(this.fProduct.getUpc()))
			this.fProduct.setUpc(this.getUpc());
		if (CompareUtil.isEmpty(this.fProduct.getShortDescription()))
			this.fProduct.setShortDescription(this.getDescription());
	}

	public Product getLastProduct()
	{
		return this.fLastProduct;
	}

	public VendorProduct getVendorProduct(VendorID pVendorID, ProductID pProductID, UsersID pBy)
	{
		if (this.fVendorProduct != null)
			return this.fVendorProduct;

		synchronized (this)
		{
			if (this.fVendorProduct == null)
			{
				VendorProduct vendorProduct = new VendorProduct(pVendorID, pProductID, pBy);
				this.syncFields(vendorProduct);
				this.fVendorProduct = vendorProduct;
			}
		}

		return this.fVendorProduct;
	}

	public void syncVendorProduct(VendorProduct pVendorProduct) throws CloneNotSupportedException
	{
		this.fLastVendorProduct = (VendorProduct) pVendorProduct.clone();
		this.fVendorProduct = pVendorProduct;
		this.fVendorProduct.setActive(true);

		this.syncFields(this.fVendorProduct);
	}

	private void syncFields(VendorProduct pVendorProduct)
	{
		pVendorProduct.setAvailableOnline(true);
		BigDecimal bdPrice = BigDecimal.valueOf(this.getPrice());
		bdPrice = bdPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		if (this.getPerOrder() > 1)
		{
			BigDecimal bdPerOrder = BigDecimal.valueOf(this.getPerOrder());
			bdPrice = bdPrice.divide(bdPerOrder, 2, BigDecimal.ROUND_HALF_EVEN);
		}
		pVendorProduct.setPrice(bdPrice.doubleValue());
		pVendorProduct.setAlternativePrice(this.getQuantityBreakPrice());
		pVendorProduct.setMinimumOrderQuantity(this.getQuantityBreak());
		if (this.getCode() != null && pVendorProduct.getVendorIdentifier() == null)
			pVendorProduct.setVendorIdentifier(this.getCode());
		pVendorProduct.setDownloaded(DateUtil.now());
	}

	public VendorProduct getLastVendorProduct()
	{
		return this.fLastVendorProduct;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		if (!CompareUtil.isEmpty(this.fCode))
		{
			builder.append("Code: ");
			builder.append(this.getCode());
		}
		if (!CompareUtil.isEmpty(this.fUpc))
		{
			if (builder.length() > 0)
				builder.append("; ");
			builder.append("UPC: ");
			builder.append(this.getUpc());
		}
		if (!CompareUtil.isEmpty(this.fDescription))
		{
			if (builder.length() > 0)
				builder.append("; ");
			builder.append("Desc: ");
			builder.append(this.getDescription());
		}
		return builder.toString();
	}
}
