package com.figstreet.biz.common;

import com.figstreet.data.common.AmazonSalesRankCategory;
import com.google.gson.stream.JsonReader;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class AmazonSalesRankCategoryJsonAdapterTest {
    @Test
    public void readReturningNullTest() throws IOException {
        JsonReader jsonReader = Mockito.mock(JsonReader.class);
        when(jsonReader.hasNext()).thenReturn(false);
        AmazonSalesRankCategoryJsonAdapter adapter = new AmazonSalesRankCategoryJsonAdapter();
        AmazonSalesRankCategory result = adapter.read(jsonReader);
        assertNull(result);
    }


}
