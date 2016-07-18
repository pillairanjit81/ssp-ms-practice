package com.tenx.ms.retail.store.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.SystemError;
import com.tenx.ms.commons.rest.dto.Paginated;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.store.rest.dto.CreateStore;
import com.tenx.ms.retail.store.rest.dto.Store;
import com.tenx.ms.retail.store.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "stores", description = "Store API")
@RestController("storeControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/stores")
public class StoreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "Creates a new Store")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful creation of store"),
            @ApiResponse(code = 400, message = "Store already exists"),
            @ApiResponse(code = 412, message = "Validation failure"),
            @ApiResponse(code = 500, message = "Internal Error creating store")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createStore(@Validated @RequestBody CreateStore store, HttpServletRequest request) {
        LOGGER.debug("Create a new Store: {}", store.getName());
        try {
            HttpHeaders headers = new HttpHeaders();
            Store s = storeService.createStore(store);
            Long storeId = s.getStoreId();
            headers.add(HttpHeaders.LOCATION, request.getRequestURL().append(storeId).toString());
            return new ResponseEntity<>(new ResourceCreated<>(storeId), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new SystemError(e.getMessage(), HttpStatus.BAD_REQUEST.value(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Get list of all store information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful getting list of stores"),
            @ApiResponse(code = 500, message = "Error getting list of stores")
    })
    @RequestMapping(method = RequestMethod.GET)
    public List<Store> getAllStores(Pageable pageable) {

        LOGGER.debug("Getting all stores: {}", pageable);
        return storeService.getAllStores();
    }

    @ApiOperation(value = "Get store information by store id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of store detail"),
            @ApiResponse(code = 404, message = "Store with that given id does not exist"),
            @ApiResponse(code = 500, message = "Error retrieving store details")
    })
    @RequestMapping(value = {"/{storeId:\\d+}"}, method = RequestMethod.GET)
    public Store getStoreById(@ApiParam(name = "storeId", value = "The store id") @PathVariable long storeId) {

        LOGGER.debug("Pulling store information by id {}", storeId);

        return storeService.getStoreById(storeId).get();
    }

    @ApiOperation(value = "Get store details by store name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of store detail"),
            @ApiResponse(code = 404, message = "Store with that given name does not exist"),
            @ApiResponse(code = 500, message = "Error retrieving store details")
    })
    @RequestMapping(value = {"/name/{storeName}"}, method = RequestMethod.GET)
    public Store getStoreByName(@ApiParam(name = "storeName", value = "The store name") @PathVariable String storeName) {

        LOGGER.debug("Pulling store information by name {}", storeName);

        return storeService.getStoreByName(storeName).get();
    }
}
