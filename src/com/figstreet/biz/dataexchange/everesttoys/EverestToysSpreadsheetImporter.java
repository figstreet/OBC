package com.figstreet.biz.dataexchange.everesttoys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.figstreet.biz.product.ProductHistory;
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
	public static final String XLSX_FILE_PATH = "./apps/OBC/USA May 14 2020 - In Stock List.xlsx";

	public static void main(String[] args)
	{
		String spreadsheetPath = XLSX_FILE_PATH;
		if (args.length > 0)
			spreadsheetPath = args[0];

		SystemInitializer.initialize("/apps/OBC/testLog.conf", "/apps/OBC/hibernate.cfg.xml");
		try
		{
			importSpreadsheet(new File(spreadsheetPath), 4, VendorID.EVEREST_TOYS, UsersID.ADMIN);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		SystemInitializer.shutdown();
	}

	public static void importSpreadsheet(File pSpreadsheetFile, int pRowsToAdvance, VendorID pVendorID, UsersID pBy)
			throws InvalidFormatException, IOException
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
				EverestProduct product = new EverestProduct(row);
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
		// Used for loading Product records
		ArrayList<String> upcList = new ArrayList<>(pProductList.size());
		// Used for syncing Product data by UPC
		LinkedHashMap<String, EverestProduct> upcMap = new LinkedHashMap<>(pProductList.size());
		ArrayList<EverestProduct> noUpcList = new ArrayList<>(); // TODO Log these records as an error at the end

		for (EverestProduct evProduct : pProductList)
		{
			if (!CompareUtil.isEmpty(evProduct.getUpc()))
			{
				upcList.add(evProduct.getUpc());
				upcMap.put(evProduct.getUpc(), evProduct);
			}
			else
			{
				noUpcList.add(evProduct);
			}
		}

		try
		{
			// Now load Products by UPC
			ProductList byUpcList = ProductList.loadByUpcList(upcList);
			// Used for syncing VendorProduct data
			LinkedHashMap<ProductID, EverestProduct> productIDMap = new LinkedHashMap<>(byUpcList.size());
			// Used to load VendorProduct records
			ArrayList<ProductID> productIDList = new ArrayList<>(byUpcList.size());

			// Update the Product data with information from the spreadsheet using
			// syncProduct
			for (Product byUpc : byUpcList)
			{
				productIDList.add(byUpc.getRecordID());
				EverestProduct evProduct = upcMap.get(byUpc.getUpc());
				if (evProduct != null)
				{
					evProduct.syncProduct(byUpc);
					productIDMap.put(byUpc.getRecordID(), evProduct); // Used when syncing VendorProduct
				}
				else
				{
					Logging.errorf(LOGGER_NAME, "processList",
							"Retrieved ProductID %s by UPC %s, but did not find EverestProduct in upcMap, new product may be created",
							ProductID.asString(byUpc.getRecordID()), byUpc.getUpc());
				}
			}

			if (!productIDList.isEmpty())
			{
				// Now load VendorProduct by VendorID and ProductID list
				VendorProductList byProductIDList = VendorProductList.loadByVendorAndProductIDList(pVendorID,
						productIDList);

				// Update the VendorProduct data with information from the spreadsheet using
				// syncVendorProduct
				for (VendorProduct byProductID : byProductIDList)
				{
					EverestProduct evProduct = productIDMap.get(byProductID.getProductID());
					if (evProduct != null)
					{
						evProduct.syncVendorProduct(byProductID);
					}
					else
					{
						Logging.errorf(LOGGER_NAME, "processList",
								"Retrieved VendorProductID %s by ProductID %s, but did not find EverestProduct in productIDMap, new vendor_product may be created",
								VendorProductID.asString(byProductID.getRecordID()),
								ProductID.asString(byProductID.getProductID()));
					}
				}
			}

			// Now update the Product and VendorProdcut data stored in each EverestProduct
			for (EverestProduct evProduct : pProductList)
			{
				Product product = evProduct.getProduct(pBy);
				History productHistory = ProductHistory.buildProductHistory(product, evProduct.getLastProduct(), pBy);
				HibernateTransaction trans = HibernateTransaction.open();
				try
				{
					product.saveOrUpdate(pBy);
					if (productHistory != null)
						productHistory.saveOrUpdate(pBy);
					VendorProduct vendorProduct = evProduct.getVendorProduct(pVendorID, product.getRecordID(), pBy);
					vendorProduct.saveOrUpdate(pBy);
					// TODO - add history for lastVendorProduct
					trans.commit();
				}
				finally
				{
					trans.close();
				}
			}
		}
		catch (Exception e)
		{
			Logging.error(LOGGER_NAME, "processList",
					"Error processing list of records from row " + pStartRow + " to " + pEndRow, e);
		}
	}
}
