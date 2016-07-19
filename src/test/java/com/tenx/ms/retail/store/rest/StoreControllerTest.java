package com.tenx.ms.retail.store.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenx.ms.commons.config.Profiles;
import com.tenx.ms.commons.rest.dto.Paginated;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.commons.tests.AbstractIntegrationTest;
import com.tenx.ms.retail.RetailServiceApp;
import com.tenx.ms.retail.TestConstants;
import com.tenx.ms.retail.store.rest.dto.Store;
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
public class StoreControllerTest extends AbstractIntegrationTest {

    private static final String REQUEST_URI = "%s/%s/stores/";
    private final RestTemplate template = new TestRestTemplate();

    @Autowired
    private ObjectMapper mapper;

    @Value("classpath:json/store/createStoreRequest.json")
    private File createStoreRequest;


    @Test
    public void testCreateStore() {

        try {
            ResponseEntity<String> response = getJSONResponse(
                    template,
                    String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1),
                    FileUtils.readFileToString(createStoreRequest),
                    HttpMethod.POST
            );

            assertEquals("HTTP Status code failed", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();

            ResourceCreated rc = mapper.readValue(received, ResourceCreated.class);
            Integer storeId = (Integer) rc.getId();
            assertThat("Incorrect store id", storeId > 0, is(true));
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testGetAllStores() {

        try {
            ResponseEntity<String> response = getJSONResponse(
                    template,
                    String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1),
                    null,
                    HttpMethod.GET
            );

            assertEquals("HTTP Status code failed", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();

            List<Store> storeList = mapper.readValue(received, new TypeReference<List<Store>>() {
            });
            List<Store> stores = storeList.stream().collect(Collectors.toList());

            assertThat("Incorrect store name", stores.get(0).getName(), is("test_Store1"));
            assertThat("Incorrect store name", stores.get(1).getName(), is("test_Store2"));

        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetStoreById() {

        try {
            ResponseEntity<String> response = getJSONResponse(
                    template,
                    String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1) + "1",
                    null,
                    HttpMethod.GET
            );

            assertEquals("HTTP Status code failed", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();

            Store store = mapper.readValue(received, Store.class);
            assertThat("Incorrect store name", store.getName(), is("test_Store1"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetStoreByName() {

        try {
            ResponseEntity<String> response = getJSONResponse(
                    template,
                    String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1) + "/name/test_Store1",
                    null,
                    HttpMethod.GET
            );

            assertEquals("HTTP Status code failed", HttpStatus.OK, response.getStatusCode());
            String received = response.getBody();

            Store store = mapper.readValue(received, Store.class);
            assertThat("Null store", store, is(notNullValue()));
            assertThat("Incorrect store name", store.getName(), is("test_Store1"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void testGetInvalidStoreId() {
        ResponseEntity<String> response = getJSONResponse(
                template,
                String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1) + "/" + "10001/",
                null,
                HttpMethod.GET
        );

        assertEquals("HTTP Status code failed", HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetInvalidStoreName() {
        ResponseEntity<String> response = getJSONResponse(
                template,
                String.format(REQUEST_URI, basePath(), TestConstants.API_VERSION_1) + "/name" + "testinginvalid",
                null,
                HttpMethod.GET
        );

        assertEquals("HTTP Status code failed", HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
