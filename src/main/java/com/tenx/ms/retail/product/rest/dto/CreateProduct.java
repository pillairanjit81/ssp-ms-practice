package com.tenx.ms.retail.product.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * Represents the request payload used to add a new product
 */
public class CreateProduct {

    @NotNull(message = "Store id is empty3434")
    @ApiModelProperty("The store id")
    @JsonProperty(value = "storeId")
    private Long storeId;

    @NotEmpty
    @ApiModelProperty("The product name")
    @Length(max = 50)
    private String name;

    @ApiModelProperty("The product description")
    @Length(max = 100)
    private String description;

    @NotEmpty
    @Pattern(regexp = "[a-zA-Z0-9]{5,10}")
    @ApiModelProperty("An adhoc SKU allowing only alpha-numeric with a min length of 5 and max of 10")
    private String sku;

    @NotNull
    @ApiModelProperty("The product's price")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal price;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
