package com.tenx.ms.retail.product.rest;

import com.tenx.ms.commons.rest.RestConstants;
import com.tenx.ms.commons.rest.SystemError;
import com.tenx.ms.commons.rest.dto.Paginated;
import com.tenx.ms.commons.rest.dto.ResourceCreated;
import com.tenx.ms.retail.product.rest.dto.CreateProduct;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.product.service.ProductService;
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


@Api(value = "products", description = "Product API")
@RestController("productControllerV1")
@RequestMapping(RestConstants.VERSION_ONE + "/products")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Creates a new Product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful creation of product"),
            @ApiResponse(code = 400, message = "Product already exists"),
            @ApiResponse(code = 412, message = "Validation failure"),
            @ApiResponse(code = 500, message = "Error creating product")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(@Validated @RequestBody CreateProduct addProduct, HttpServletRequest request) {
        LOGGER.debug("Creating a new Product and adding to the store: {}", addProduct.getName());
        HttpHeaders headers = new HttpHeaders();
        try {
            Product p = productService.addProduct(addProduct);
            Long productId = p.getProductId();
            headers.add(HttpHeaders.LOCATION, request.getRequestURL().append(productId).toString());
            return new ResponseEntity<>(new ResourceCreated<>(productId), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new SystemError(e.getMessage(), HttpStatus.BAD_REQUEST.value(), e), HttpStatus.BAD_REQUEST);

        }

    }

    @ApiOperation(value = "Get list of products for store by store id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful getting list of products"),
            @ApiResponse(code = 404, message = "Store id does not exist"),
            @ApiResponse(code = 500, message = "Error getting list of products")
    })
    @RequestMapping(value = {"/store/{storeId:\\d+}"}, method = RequestMethod.GET)
    public List<Product> getStoreProducts(@ApiParam(name = "storeId", value = "The store id") @PathVariable long storeId) {

        LOGGER.debug("Pulling all products for store id: {}", storeId);
        return productService.getStoreProducts(storeId);
    }


    @ApiOperation(value = "Get product information by product name for specified store")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful, getting product detail"),
            @ApiResponse(code = 404, message = "Product with that given name does not exist"),
            @ApiResponse(code = 500, message = "Error getting product details")
    })
    @RequestMapping(value = {"/store/{storeId:\\d+}/product/{productName:.*[a-zA-Z].*}"}, method = RequestMethod.GET)
    public Product getStoreProductByName(@ApiParam(name = "storeId", value = "The store id") @PathVariable long storeId,
                                         @ApiParam(name = "productName", value = "The product name") @PathVariable String productName) {

        LOGGER.debug("Pulling product by name {} for store {}", productName, storeId);

        return productService.getStoreProductByName(storeId, productName).get();
    }


    @ApiOperation(value = "Get product information by product id for specified store")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful, getting  product information"),
            @ApiResponse(code = 404, message = "Product with that given id does not exist"),
            @ApiResponse(code = 500, message = "Error in getting product information")
    })
    @RequestMapping(value = {"/store/{storeId:\\d+}/product/{productId:\\d+}"}, method = RequestMethod.GET)
    public Product getStoreProductById(@ApiParam(name = "storeId", value = "The store id") @PathVariable long storeId,
                                       @ApiParam(name = "productId", value = "The product id") @PathVariable long productId) {

        LOGGER.debug("Fetching product by id {} for store {}", productId, storeId);

        return productService.getStoreProductById(storeId, productId).get();
    }


}
