package com.tenx.ms.retail.product.service;

import com.tenx.ms.commons.rest.dto.Paginated;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.product.rest.dto.CreateProduct;
import com.tenx.ms.retail.product.rest.dto.Product;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    /**
     * Add/Create product to the store
     *
     * @param addProduct CreateProduct DTO
     * @return
     * @throws Exception
     */
    public Product addProduct(CreateProduct addProduct) throws Exception {
        if (getStoreProductByName(addProduct.getStoreId(), addProduct.getName()).isPresent()) {
            throw new Exception("The product already added in the store");
        }

        ProductEntity product = convertToEntity(addProduct);
        productRepository.save(product);

        return convertToDTO(product);
    }

    /**
     * Pulls all products information for specified store
     *
     * @param storeId the store id
     * @return List of Products
     */
    public List<Product> getStoreProducts(Long storeId) {
        List<ProductEntity> productEntityList = productRepository.findByStoresStoreId(storeId);

        List<Product> listProductDTO = productEntityList.stream().map(product -> convertToDTO(product)).collect(Collectors.toList());

        return listProductDTO;
    }

    /**
     * Get Store Product Info by store ID + Product ID
     *
     * @param storeId   The Store ID
     * @param productId The product ID
     * @return Product DTO
     */
    public Optional<Product> getStoreProductById(Long storeId, Long productId) {
        return productRepository.findByStoresStoreIdAndProductId(storeId, productId).map(product -> convertToDTO(product));
    }

    /**
     * Get Store Product info by Store ID + Product Name
     *
     * @param storeId Store ID
     * @param name    Product Name
     * @return Product DTO
     */
    public Optional<Product> getStoreProductByName(Long storeId, String name) {
        return productRepository.findByStoresStoreIdAndName(storeId, name).map(product -> convertToDTO(product));
    }

    /**
     * Convert the CreateProduct DTO to Entity
     *
     * @param product Create Product
     * @return product Entity
     */
    private ProductEntity convertToEntity(CreateProduct product) {

        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(product.getName());

        Set<StoreEntity> stores = new HashSet<>();
        stores.add(storeRepository.findOneByStoreId(product.getStoreId()).get());
        productEntity.setStores(stores);
        productEntity.setDescription(product.getDescription());
        productEntity.setPrice(product.getPrice());
        productEntity.setSku(product.getSku());


        return productEntity;
    }

    /**
     * Convert the product Entity to DTO
     *
     * @param product Product Entity
     * @return product DTO
     */
    private Product convertToDTO(ProductEntity product) {

        Product productDto = new Product();
        productDto.setDescription(product.getDescription());
        productDto.setSku(product.getSku());
        productDto.setPrice(product.getPrice());
        productDto.setName(product.getName());
        productDto.setProductId(product.getProductId());

        return productDto;
    }
}
