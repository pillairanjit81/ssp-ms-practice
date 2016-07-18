package com.tenx.ms.retail.store.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * This is DTO for store request payload.
 */
public class CreateStore {

    @Length(max = 50)
    @NotEmpty(message = "Name is empty")
    @ApiModelProperty("The store's name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
