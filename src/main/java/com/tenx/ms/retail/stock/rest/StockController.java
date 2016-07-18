package com.tenx.ms.retail.stock.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.SystemError;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.stock.rest.dto.AddStock;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.stock.service.StockService;
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

@Api(value = "stocks", description = "Stock API")
@RestController("stockControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/stocks")
public class StockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @ApiOperation(value = "Add Stock to the product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful addition of stock"),
            @ApiResponse(code = 500, message = "Error adding stock")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addStockToStore(@Validated @RequestBody AddStock stock, HttpServletRequest request) {

        LOGGER.debug("Adding stock: {}", stock);
        HttpHeaders headers = new HttpHeaders();

        try {
            stockService.addStockToStore(stock);

        } catch (Exception e) {
            LOGGER.error(e.getStackTrace().toString(), stock.getProductId(), stock.getStoreId());
            return new ResponseEntity<>(new SystemError(e.getMessage(), HttpStatus.PRECONDITION_FAILED.value(), e), headers, HttpStatus.PRECONDITION_FAILED);

        }
        headers.add(HttpHeaders.LOCATION, request.getRequestURL().toString());
        return new ResponseEntity<>(new ResourceCreated<>(), headers, HttpStatus.OK);
    }

    @ApiOperation(value = "Get stock details by product id for a store")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of stock detail"),
            @ApiResponse(code = 500, message = "Error retrieving stock details")
    })

    @RequestMapping(value = {"/store/{storeId:\\d+}/product/{productId:\\d+}"}, method = RequestMethod.GET)
    public Stock getStockByStoreIdAndProductId(@ApiParam(name = "storeId", value = "The store id") @PathVariable long storeId,
                                               @ApiParam(name = "productId", value = "The product id") @PathVariable long productId) {

        LOGGER.debug("Pulling stock by id {} for store {}", productId, storeId);

        return stockService.getStockByStoreIdAndProductId(storeId, productId).get();
    }

    @ApiOperation(value = "Get list of stocks for store by store id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of stocks"),
            @ApiResponse(code = 500, message = "Error retrieving list of stocks")
    })
    @RequestMapping(value = {"/store/{storeId:\\d+}"}, method = RequestMethod.GET)
    public List<Stock> getStockByStoreId(@ApiParam(name = "storeId", value = "The store id") @PathVariable long storeId, Pageable pageable) {

        LOGGER.debug("Pulling all products for store id : {}", storeId);

        return stockService.getStocksByStoreId(storeId);
    }


}
