package com.tenx.ms.retail.stock.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.TestConstants;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class StockControllerTest extends AbstractIntegrationTest {

    private static final String REQUEST_URI = "%s/%s/stocks/";
    private final RestTemplate template = new TestRestTemplate();

    @Autowired
    private ObjectMapper mapper;

    @Value("classpath:json/stock/createStockRequest.json")
    private File createStockRequest;



    @Test
    public void testAddStockToStore() {
        try {
            ResponseEntity<String> response = getJSONResponse(
                    template,
                    String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1),
                    FileUtils.readFileToString(createStockRequest),
                    HttpMethod.POST
            );

            assertEquals("HTTP Status code failed", HttpStatus.OK, response.getStatusCode());
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }


    @Test
    public void testGetStockByStoreIdAndProductId() {
        try {
            ResponseEntity<String> response = getJSONResponse(
                    template,
                    String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1) + "/store/1/product/1",
                    null,
                    HttpMethod.GET
            );

            assertEquals("HTTP Status code failed", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();


            Stock stock = mapper.readValue(received, Stock.class);
            assertThat("Incorrect stock count", stock.getCount(), is(1));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetStockByStoreId() {
        try {
            ResponseEntity<String> response = getJSONResponse(
                    template,
                    String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1) + "/store/1",
                    null,
                    HttpMethod.GET
            );

            assertEquals("HTTP Status code failed", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();


            List<Stock> stockList = mapper.readValue(received, new TypeReference<List<Stock>>() {
            });
            List<Stock> stocks = stockList.stream().collect(Collectors.toList());
            assertThat("Incorrect stock name", stocks.get(0).getCount(), is(1));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
