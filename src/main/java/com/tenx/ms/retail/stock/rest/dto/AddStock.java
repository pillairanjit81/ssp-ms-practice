package com.tenx.ms.retail.stock.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Represents the request payload used to add stock
 */
public class AddStock {

    @NotNull
    @ApiModelProperty("The product id")
    @JsonProperty(value = "product_id")
    private Long productId;

    @NotNull
    @ApiModelProperty("The store id")
    @JsonProperty(value = "store_id")
    private Long storeId;

    @NotNull
    @ApiModelProperty("The stock count")
    @JsonProperty(value = "count")
    private Integer count;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
