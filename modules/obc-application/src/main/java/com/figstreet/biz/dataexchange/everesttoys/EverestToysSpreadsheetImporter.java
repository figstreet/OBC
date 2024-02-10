package com.figstreet.biz.dataexchange.everesttoys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.figstreet.biz.product.ProductHistory;
import com.figstreet.biz.vendorproduct.VendorProductHistory;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.HibernateTransaction;
import com.figstreet.core.Logging;
import com.figstreet.core.SystemInitializer;
import com.figstreet.data.history.History;
import com.figstreet.data.product.Product;
import com.figstreet.data.product.ProductID;
import com.figstreet.data.product.ProductList;
import com.figstreet.data.users.UsersID;
import com.figstreet.data.vendor.VendorID;
import com.figstreet.data.vendorproduct.VendorProduct;
import com.figstreet.data.vendorproduct.VendorProductID;
import com.figstreet.data.vendorproduct.VendorProductList;

public class EverestToysSpreadsheetImporter
{
	private static final String LOGGER_NAME = EverestToysSpreadsheetImporter.class.getPackage().getName()
			+ ".EverestToysSpreadsheetImporter";
	public static final String XLSX_FILE_PATH = "./apps/OBC/USA October 15 2020 - In Stock List.xlsx";

	public static void main(String[] args)
	{
		String spreadsheetPath = XLSX_FILE_PATH;
		if (args.length > 0)
			spreadsheetPath = args[0];

		boolean includePerCase = true;
		if (args.length > 1)
			includePerCase = Boolean.valueOf(args[1]);

		SystemInitializer.initialize("/apps/OBC/testLog.conf", "/apps/OBC/hibernate.cfg.xml");
		try
		{
			importSpreadsheet(new File(spreadsheetPath), 4, VendorID.EVEREST_TOYS, includePerCase, UsersID.ADMIN);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		SystemInitializer.shutdown();
	}

	public static void importSpreadsheet(File pSpreadsheetFile, int pRowsToAdvance, VendorID pVendorID,
			boolean pIncluePerCase, UsersID pBy) throws InvalidFormatException, IOException
	{
		try (OPCPackage pkg = OPCPackage.open(XLSX_FILE_PATH, PackageAccess.READ);)
		{
			XSSFWorkbook workbook = new XSSFWorkbook(pkg);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			int rowIndex = 1;
			for (; pRowsToAdvance > 0; pRowsToAdvance--)
			{
				if (rowIterator.hasNext())
					rowIterator.next();
				rowIndex++;
			}

			int listSize = 100;
			int rowStart = rowIndex;
			int rowEnd = 0;
			List<EverestProduct> productList = new ArrayList<>(listSize);
			while (rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				EverestProduct product = new EverestProduct(row, pIncluePerCase);

				productList.add(product);
				if (productList.size() >= listSize)
				{
					rowEnd = rowIndex;
					processList(pVendorID, productList, rowStart, rowEnd, pBy);



					productList.clear();
					rowStart = rowEnd++;
				}
				rowIndex++;
			}

			if (!productList.isEmpty())
			{
				rowEnd = rowIndex;
				processList(pVendorID, productList, rowStart, rowEnd, pBy);
			}
		}
	}

	public static void processList(VendorID pVendorID, List<EverestProduct> pProductList, int pStartRow, int pEndRow,
			UsersID pBy)
	{
		// The UPC isn't always correct, so using the Vendor Code/Identifier instead
		ArrayList<String> vendorIdList = new ArrayList<>(pProductList.size());
		LinkedHashMap<String, EverestProduct> vendorIdentifierMap = new LinkedHashMap<>(pProductList.size());
		ArrayList<EverestProduct> noVendorIdentifierList = new ArrayList<>(); // TODO Log these records as an error at
																				// the end

		for (EverestProduct evProduct : pProductList)
		{
			if (!CompareUtil.isEmpty(evProduct.getCode()))
			{
				vendorIdList.add(evProduct.getCode());
				vendorIdentifierMap.put(evProduct.getCode(), evProduct);
			}
			else
			{
				noVendorIdentifierList.add(evProduct);
			}
		}

		try
		{
			VendorProductList byVendorIdentifierList = VendorProductList.loadByVendorIdentifierList(pVendorID,
					vendorIdList);
			// Used for syncing Product data
			LinkedHashMap<ProductID, EverestProduct> productIDMap = new LinkedHashMap<>(byVendorIdentifierList.size());
			// Used to load ProductRecords
			LinkedHashSet<ProductID> productIDSet = new LinkedHashSet<>(byVendorIdentifierList.size());

			// Update the VendorProduct data with information from the spreadsheet
			// using syncVendorProduct
			for (VendorProduct byVendorIdentifier : byVendorIdentifierList)
			{
				productIDSet.add(byVendorIdentifier.getProductID());
				EverestProduct evProduct = vendorIdentifierMap.get(byVendorIdentifier.getVendorIdentifier());
				if (evProduct != null)
				{
					productIDMap.put(byVendorIdentifier.getProductID(), evProduct);
					evProduct.syncVendorProduct(byVendorIdentifier);
				}
				else
				{
					Logging.errorf(LOGGER_NAME, "processList",
							"Retrieved VendorProductID %s by VendorIdentifier %s, but did not find EverestProduct in vendorIdentifierMap, new vendor_product may be created",
							VendorProductID.asString(byVendorIdentifier.getRecordID()),
							byVendorIdentifier.getVendorIdentifier());
				}
			}

			// Now load Products by ProductID
			ProductList productList = ProductList.loadByProductIDs(productIDSet);
			// Update the Product data with information from the spreadsheet using
			// syncProduct
			for (Product product : productList)
			{
				EverestProduct evProduct = productIDMap.get(product.getRecordID());
				if (evProduct != null)
				{
					evProduct.syncProduct(product);
				}
				else
				{
					Logging.errorf(LOGGER_NAME, "processList",
							"Retrieved Product by ProductID %s, but did not find EverestProduct in productIDMap, new product may be created",
							ProductID.asString(product.getRecordID()));
				}
			}

			ArrayList<VendorProductID> activeVpidList = new ArrayList<>(pProductList.size());

			// Now update the Product and VendorProdcut data stored in each EverestProduct
			for (EverestProduct evProduct : pProductList)
			{
				VendorProduct vendorProduct = null;
				Product product = evProduct.getProduct(pBy);
				History productHistory = ProductHistory.buildProductHistory(product, evProduct.getLastProduct(), pBy);
				HibernateTransaction trans = HibernateTransaction.open();
				try
				{
					product.saveOrUpdate(pBy);
					if (productHistory != null)
						productHistory.saveOrUpdate(pBy);
					vendorProduct = evProduct.getVendorProduct(pVendorID, product.getRecordID(), pBy);
					vendorProduct.saveOrUpdate(pBy);
					History vendorProductHistory = VendorProductHistory.buildVendorProductHistory(vendorProduct,
							evProduct.getLastVendorProduct(), pBy);
					if (vendorProductHistory != null)
						vendorProductHistory.saveOrUpdate(pBy);
					trans.commit();
				}
				finally
				{
					trans.close();
				}
				activeVpidList.add(vendorProduct.getRecordID());
			}

			//TODO Deactivate all VendorProduct for Everest that weren't on the spreadsheet
//			for ()
//			{
//
//			}
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "processList",
					"Error processing list of records from row " + pStartRow + " to " + pEndRow, e);
		}

	}
}
