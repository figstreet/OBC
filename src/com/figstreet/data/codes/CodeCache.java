package com.figstreet.data.codes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.figstreet.core.RefreshCache;
import com.figstreet.data.common.AmazonMarketplace;
import com.figstreet.data.common.AmazonSalesRankCategory;
import com.figstreet.data.common.MeasurementUnit;

public class CodeCache extends RefreshCache
{
	private static final String NAME = "CODECACHE";
	public static final String LOGGER_NAME = CodeCache.class.getPackage().getName() + ".CodeCache";

	// Initialization-on-demand holder
	private static class Holder
	{
		static volatile CodeCache fCache = new CodeCache();
	}

	public static CodeCache getThe()
	{
		return Holder.fCache;
	}

	@SuppressWarnings("rawtypes")
	private Map<CodesType, CodeConstant.CodeConstantFactory> fCodeConstantFactories = Collections
			.synchronizedMap(new HashMap<CodesType, CodeConstant.CodeConstantFactory>());

	private CodeCache()
	{
		super(NAME);
	}

	@Override
	public String getLoggerName()
	{
		return LOGGER_NAME;
	}

	@SuppressWarnings("rawtypes")
	public void addCodeConstantFactory(CodeConstant.CodeConstantFactory pFactory)
	{
		this.fCodeConstantFactories.put(pFactory.getCodesType(), pFactory);
	}

	public void initialize()
	{
		//TODO - use reflection find all classes that extend CodeConstant and make sure they are referenced
		AmazonSalesRankCategory category = AmazonSalesRankCategory.GROCERY_AND_GOURMET_FOOD;
		AmazonMarketplace marketplace = AmazonMarketplace.UNITED_STATES;
		MeasurementUnit unit = MeasurementUnit.INCHES;
		this.refresh();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void refresh()
	{
		for (CodeConstant.CodeConstantFactory codeConstantFactory : this.fCodeConstantFactories.values())
			codeConstantFactory.populateAllValues();
	}

}
