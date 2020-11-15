package com.figstreet.biz.dataexchange.amazonmws;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.amazonservices.mws.products.model.ASINIdentifier;
import com.amazonservices.mws.products.model.AttributeSetList;
import com.amazonservices.mws.products.model.GetMatchingProductForIdResult;
import com.amazonservices.mws.products.model.Product;
import com.amazonservices.mws.products.model.RelationshipList;
import com.amazonservices.mws.products.model.SalesRankList;
import com.amazonservices.mws.products.model.SalesRankType;
import com.figstreet.core.CompareUtil;
import com.figstreet.core.Logging;
import com.figstreet.data.common.AmazonMarketplace;
import com.figstreet.data.common.AmazonSalesRankCategory;
import com.figstreet.data.common.MeasurementUnit;
import com.figstreet.data.common.PriceCurrency;

public class MwsProduct
{
	private static final String LOGGER_NAME = MwsProduct.class.getPackage().getName() + ".MwsProduct";

	public static final String ITEM_DIMENSION_UNIT_ATTRIBUTE = "units";
	public static final String NODE_VALUE_KEY = "#text";

	public static final String TITLE_NODE = "ns2:itemattributes" + MwsXmlNode.NAME_DELIMITER + "ns2:title";
	public static final String LENGTH_NODE = "ns2:itemattributes" + MwsXmlNode.NAME_DELIMITER + "ns2:itemdimensions"
			+ MwsXmlNode.NAME_DELIMITER + "ns2:length";
	public static final String WIDTH_NODE = "ns2:itemattributes" + MwsXmlNode.NAME_DELIMITER + "ns2:itemdimensions"
			+ MwsXmlNode.NAME_DELIMITER + "ns2:width";
	public static final String HEIGHT_NODE = "ns2:itemattributes" + MwsXmlNode.NAME_DELIMITER + "ns2:itemdimensions"
			+ MwsXmlNode.NAME_DELIMITER + "ns2:height";
	public static final String WEIGHT_NODE = "ns2:itemattributes" + MwsXmlNode.NAME_DELIMITER + "ns2:itemdimensions"
			+ MwsXmlNode.NAME_DELIMITER + "ns2:weight";

	public static final String LIST_PRICE_NODE = "ns2:itemattributes" + MwsXmlNode.NAME_DELIMITER + "ns2:listprice"
			+ MwsXmlNode.NAME_DELIMITER + "ns2:amount";
	public static final String LIST_PRICE_CURRENCY_NODE = "ns2:itemattributes" + MwsXmlNode.NAME_DELIMITER
			+ "ns2:listprice" + MwsXmlNode.NAME_DELIMITER + "ns2:currencycode";

	public static final String SMALL_IMAGE_URL_NODE = "ns2:itemattributes" + MwsXmlNode.NAME_DELIMITER
			+ "ns2:smallimage" + MwsXmlNode.NAME_DELIMITER + "ns2:url";

	private String fIdentifier;
	private MwsIdType fIdentifierType;

	private boolean fError;
	private String fErrorMessage;
	private String fResponseStatus;

	private String fAsin; // VendorIdentifier
	private AmazonMarketplace fMarketplace;
	private String fProductName;
	private Double fLength;
	private MeasurementUnit fLengthUnit;
	private Double fWidth;
	private MeasurementUnit fWidthUnit;
	private Double fHeight;
	private MeasurementUnit fHeightUnit;
	private Double fWeight;
	private MeasurementUnit fWeightUnit;

	private Double fListPrice;
	private PriceCurrency fListPriceCurrency;

	private String fSmallImageURL;

	private Integer fPrimarySalesRank;
	private AmazonSalesRankCategory fPrimarySalesRankCategory;
	private Integer fSecondarySalesRank;
	private AmazonSalesRankCategory fSecondarySalesRankCategory;

	public MwsProduct(GetMatchingProductForIdResult pMatchingProductForIdResult, Product pProduct)
	{
		if (pMatchingProductForIdResult.isSetError())
		{
			this.fError = true;
			this.fErrorMessage = pMatchingProductForIdResult.getError().getMessage();
		}
		this.fResponseStatus = pMatchingProductForIdResult.getStatus();

		this.fIdentifier = pMatchingProductForIdResult.getId();
		this.fIdentifierType = MwsIdType.newInstance(pMatchingProductForIdResult.getIdType());
		this.importProduct(pProduct);
	}

	// Error response
	public MwsProduct(GetMatchingProductForIdResult pMatchingProductForIdResult)
	{
		if (pMatchingProductForIdResult.isSetError())
		{
			this.fError = true;
			this.fErrorMessage = pMatchingProductForIdResult.getError().getMessage();
		}
		this.fResponseStatus = pMatchingProductForIdResult.getStatus();

		this.fIdentifier = pMatchingProductForIdResult.getId();
		this.fIdentifierType = MwsIdType.newInstance(pMatchingProductForIdResult.getIdType());
	}

	//Used by MwsListMatchingProductsResponse
	public MwsProduct(MwsIdType pIdType, String pIdentifier, Product pProduct)
	{
		this.fIdentifier = pIdentifier;
		this.fIdentifierType = pIdType;
		if (pProduct != null)
		{
			this.fError = false;
			this.importProduct(pProduct);
		}
		else
		{
			this.fError = true;
			this.fErrorMessage = "Product not found for " + MwsIdType.asString(pIdType) + " Identifier: " + pIdentifier;
		}
	}

	public String getIdentifier()
	{
		return this.fIdentifier;
	}

	public MwsIdType getIdentifierType()
	{
		return this.fIdentifierType;
	}

	public boolean isError()
	{
		return this.fError;
	}

	public String getErrorMessage()
	{
		if (this.isError())
			return this.fErrorMessage;
		return null;
	}

	public String getResponseStatus()
	{
		return this.fResponseStatus;
	}

	private void importProduct(Product pProduct)
	{
		if (pProduct.getIdentifiers() != null)
		{
			ASINIdentifier asin = pProduct.getIdentifiers().getMarketplaceASIN();
			if (asin != null)
			{
				this.fAsin = asin.getASIN();
				this.fMarketplace = AmazonMarketplace.newInstance(asin.getMarketplaceId());
			}
		}

		LinkedHashMap<String, List<MwsXmlNode>> nodeMap = new LinkedHashMap<>(500);
		AttributeSetList attributeSetList = pProduct.getAttributeSets();
		if (attributeSetList != null)
			nodeMap = buildMap(attributeSetList, nodeMap);

		RelationshipList relationshipList = pProduct.getRelationships();
		if (relationshipList != null && relationshipList.isSetAny())
			nodeMap = buildMap(relationshipList, nodeMap);

		// Retrieve nodes by string map key
		MwsXmlNode titleNode = this.findFirstNode(TITLE_NODE, nodeMap);
		if (titleNode != null)
			this.fProductName = titleNode.getValue();

		MwsXmlNode lengthNode = this.findFirstNode(LENGTH_NODE, nodeMap);
		if (lengthNode != null)
		{
			this.fLength = this.findDoubleValue(lengthNode);
			this.fLengthUnit = MeasurementUnit.newInstance(lengthNode.findAttribute(ITEM_DIMENSION_UNIT_ATTRIBUTE));
		}

		MwsXmlNode widthNode = this.findFirstNode(WIDTH_NODE, nodeMap);
		if (widthNode != null)
		{
			this.fWidth = this.findDoubleValue(widthNode);
			this.fWidthUnit = MeasurementUnit.newInstance(widthNode.findAttribute(ITEM_DIMENSION_UNIT_ATTRIBUTE));
		}

		MwsXmlNode heightNode = this.findFirstNode(HEIGHT_NODE, nodeMap);
		if (heightNode != null)
		{
			this.fHeight = this.findDoubleValue(heightNode);
			this.fHeightUnit = MeasurementUnit.newInstance(heightNode.findAttribute(ITEM_DIMENSION_UNIT_ATTRIBUTE));
		}

		MwsXmlNode weightNode = this.findFirstNode(WEIGHT_NODE, nodeMap);
		if (weightNode != null)
		{
			this.fWeight = this.findDoubleValue(weightNode);
			this.fWeightUnit = MeasurementUnit.newInstance(weightNode.findAttribute(ITEM_DIMENSION_UNIT_ATTRIBUTE));
		}

		MwsXmlNode listPriceNode = this.findFirstNode(LIST_PRICE_NODE, nodeMap);
		if (listPriceNode != null)
		{
			this.fListPrice = this.findDoubleValue(listPriceNode);
		}

		MwsXmlNode listPriceCurrencyNode = this.findFirstNode(LIST_PRICE_CURRENCY_NODE, nodeMap);
		if (listPriceCurrencyNode != null)
		{
			this.fListPriceCurrency = PriceCurrency.newInstance(listPriceCurrencyNode.getValue());
		}

		MwsXmlNode smallImageURLNode = this.findFirstNode(SMALL_IMAGE_URL_NODE, nodeMap);
		if (smallImageURLNode != null)
		{
			this.fSmallImageURL = smallImageURLNode.getValue();
		}

		SalesRankList salesRankList = pProduct.getSalesRankings();
		if (salesRankList != null)
		{
			List<SalesRankType> salesRankTypeList = salesRankList.getSalesRank();
			if (salesRankTypeList != null)
			{
				for (int i = 0; i < salesRankTypeList.size() && i < 2; i++)
				{
					SalesRankType salesRankType = salesRankTypeList.get(i);
					switch (i) {
					case 0:
						this.fPrimarySalesRank = salesRankType.getRank();
						this.fPrimarySalesRankCategory = AmazonSalesRankCategory
								.newInstance(salesRankType.getProductCategoryId());
						break;
					case 1:
						this.fSecondarySalesRank = salesRankType.getRank();
						this.fSecondarySalesRankCategory = AmazonSalesRankCategory
								.newInstance(salesRankType.getProductCategoryId());
						break;
					}
				}
			}
		}
	}

	/*
	 * The Amazon MWS API returns product information as XML The next set of methods
	 * create a map of the complete "XML Node Name" to the list of XML nodes with
	 * that name
	 */

	public static LinkedHashMap<String, List<MwsXmlNode>> buildMap(AttributeSetList pAttributeSetList,
			LinkedHashMap<String, List<MwsXmlNode>> pNodeMap)
	{
		List<Object> any = pAttributeSetList.getAny();
		return buildMap(any, pNodeMap);
	}

	public static LinkedHashMap<String, List<MwsXmlNode>> buildMap(RelationshipList pRelationshipList,
			LinkedHashMap<String, List<MwsXmlNode>> pNodeMap)
	{
		List<Object> any = pRelationshipList.getAny();
		return buildMap(any, pNodeMap);
	}

	public static LinkedHashMap<String, List<MwsXmlNode>> buildMap(List<Object> pNodeList,
			LinkedHashMap<String, List<MwsXmlNode>> pNodeMap)
	{
		String fullName = "";
		for (Object obj : pNodeList)
		{
			Node xmlNode = (Node) obj;
			String nodeName = xmlNode.getNodeName().toLowerCase();
			fullName = nodeName;
			List<MwsXmlNode> mwsNodeList = pNodeMap.get(fullName);
			if (mwsNodeList == null)
			{
				mwsNodeList = new ArrayList<>();
				pNodeMap.put(fullName, mwsNodeList);
			}

			MwsXmlNode mwsNode = new MwsXmlNode(nodeName, fullName, xmlNode.getNodeValue());
			mwsNodeList.add(mwsNode);
			importAttributes(mwsNode, xmlNode.getAttributes());

			NodeList children = xmlNode.getChildNodes();
			if (children != null && children.getLength() > 0)
			{
				importChildren(mwsNode, children, pNodeMap);
			}

		}

		return pNodeMap;
	}

	private static void importChildren(MwsXmlNode pParent, NodeList pChildNodes,
			LinkedHashMap<String, List<MwsXmlNode>> pNodeMap)
	{
		for (int i = 0; i < pChildNodes.getLength(); i++)
		{
			Node xmlNode = pChildNodes.item(i);
			String nodeName = xmlNode.getNodeName().toLowerCase();
			if (MwsProduct.NODE_VALUE_KEY.equalsIgnoreCase(nodeName))
			{
				if (CompareUtil.isEmpty(pParent.getValue()))
					pParent.setValue(xmlNode.getNodeValue());
				else
					Logging.warnf(LOGGER_NAME, "importChildren", "Node %s already had a value %s, not using %s",
							pParent.getFullName(), pParent.getValue(), xmlNode.getNodeValue());
			}
			else
			{
				String fullName = pParent.getFullName() + MwsXmlNode.NAME_DELIMITER + nodeName;
				List<MwsXmlNode> mwsNodeList = pNodeMap.get(fullName);
				if (mwsNodeList == null)
				{
					mwsNodeList = new ArrayList<>();
					pNodeMap.put(fullName, mwsNodeList);
				}

				MwsXmlNode mwsNode = new MwsXmlNode(nodeName, fullName, xmlNode.getNodeValue());
				mwsNodeList.add(mwsNode);

				importAttributes(mwsNode, xmlNode.getAttributes());

				NodeList children = xmlNode.getChildNodes();
				if (children != null && children.getLength() > 0)
				{
					importChildren(mwsNode, children, pNodeMap);
				}
			}
		}

	}

	private static void importAttributes(MwsXmlNode pXmlNode, NamedNodeMap pAttributes)
	{
		if (pAttributes != null && pAttributes.getLength() > 0)
		{
			for (int j = 0; j < pAttributes.getLength(); j++)
			{
				Node attribute = pAttributes.item(j);
				pXmlNode.addAttribute(attribute.getNodeName().toLowerCase(), attribute.getNodeValue());
			}
		}
	}

	private MwsXmlNode findFirstNode(String pNodeName, Map<String, List<MwsXmlNode>> pNodeMap)
	{
		List<MwsXmlNode> nodes = pNodeMap.get(pNodeName);
		if (nodes != null && nodes.size() > 0)
			return nodes.get(0);
		return null;
	}

	private Double findDoubleValue(MwsXmlNode pNode)
	{
		Double doubleValue = null;
		try
		{
			String value = pNode.getValue();
			if (value != null)
				doubleValue = Double.valueOf(value);
		}
		catch (Exception e)
		{
			Logging.warnf(LOGGER_NAME, "findDoubleValue", "Node %s: error parsing '%s' as double, returning null",
					pNode.getFullName(), pNode.getValue());
		}
		return doubleValue;
	}

	public String getAsin()
	{
		return this.fAsin;
	}

	public AmazonMarketplace getMarketplace()
	{
		return this.fMarketplace;
	}

	public String getProductName()
	{
		return this.fProductName;
	}

	public Double getLength()
	{
		return this.fLength;
	}

	public MeasurementUnit getLengthUnit()
	{
		return this.fLengthUnit;
	}

	public Double getWidth()
	{
		return this.fWidth;
	}

	public MeasurementUnit getWidthUnit()
	{
		return this.fWidthUnit;
	}

	public Double getHeight()
	{
		return this.fHeight;
	}

	public MeasurementUnit getHeightUnit()
	{
		return this.fHeightUnit;
	}

	public Double getWeight()
	{
		return this.fWeight;
	}

	public MeasurementUnit getWeightUnit()
	{
		return this.fWeightUnit;
	}

	public Double getListPrice()
	{
		return this.fListPrice;
	}

	public PriceCurrency getListPriceCurrency()
	{
		return this.fListPriceCurrency;
	}

	public String getSmallImageURL()
	{
		return this.fSmallImageURL;
	}

	public Integer getPrimarySalesRank()
	{
		return this.fPrimarySalesRank;
	}

	public AmazonSalesRankCategory getPrimarySalesRankCategory()
	{
		return this.fPrimarySalesRankCategory;
	}

	public Integer getSecondarySalesRank()
	{
		return this.fSecondarySalesRank;
	}

	public AmazonSalesRankCategory getSecondarySalesRankCategory()
	{
		return this.fSecondarySalesRankCategory;
	}

}
