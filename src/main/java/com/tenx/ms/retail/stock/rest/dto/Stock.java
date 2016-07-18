package com.tenx.ms.retail.stock.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class Stock {

    @ApiModelProperty("The product ID")
    @JsonProperty(value = "product_id")
    private Long productId;

    @ApiModelProperty("The store ID")
    @JsonProperty(value = "store_id")
    private Long storeId;

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
