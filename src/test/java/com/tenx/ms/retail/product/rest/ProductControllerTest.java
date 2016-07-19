package com.tenx.ms.retail.product.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.TestConstants;
import com.tenx.ms.retail.product.rest.dto.Product;
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RetailServiceApp.class)
@ActiveProfiles(Profiles.TEST_NOAUTH)
public class ProductControllerTest extends AbstractIntegrationTest {

    private static final String REQUEST_URI = "%s/%s/products/";
    private final RestTemplate template = new TestRestTemplate();

    @Autowired
    private ObjectMapper mapper;

    @Value("classpath:json/product/createProductRequest.json")
    private File createProductRequest;


    @Test
    public void testAddProduct() {
        try {
            ResponseEntity<String> response = getJSONResponse(
                    template,
                    String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1),
                    FileUtils.readFileToString(createProductRequest),
                    HttpMethod.POST
            );

            assertEquals("HTTP Status code failed", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();

            ResourceCreated rc = mapper.readValue(received, ResourceCreated.class);
            Integer productId = (Integer) rc.getId();
            assertThat("Incorrect product id", productId > 0, is(true));
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testGetStoreProducts() {
        try {
            ResponseEntity<String> response = getJSONResponse(
                    template,
                    String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1) + "/store/1",
                    null,
                    HttpMethod.GET
            );

            assertEquals("HTTP Status code failed", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();

            List<Product> productList = mapper.readValue(received, new TypeReference<List<Product>>() {
            });
            List<Product> products = productList.stream().collect(Collectors.toList());

            assertThat("Incorrect product name", products.get(0).getName(), is("Desk"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetStoreProductByName() {
        try {
            ResponseEntity<String> response = getJSONResponse(
                    template,
                    String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1) + "/store/1/product/Desk",
                    null,
                    HttpMethod.GET
            );

            assertEquals("HTTP Status code failed", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();


            Product product = mapper.readValue(received, Product.class);
            assertThat("Incorrect product desc", product.getDescription(), is("Desk"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetStoreProductById() {
        try {
            ResponseEntity<String> response = getJSONResponse(
                    template,
                    String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1) + "/store/1/product/1",
                    null,
                    HttpMethod.GET
            );

            assertEquals("HTTP Status code failed", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();


            Product product = mapper.readValue(received, Product.class);
            assertThat("Incorrect product desc", product.getName(), is("Desk"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
