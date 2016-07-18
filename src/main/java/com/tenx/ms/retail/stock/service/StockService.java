package com.tenx.ms.retail.stock.service;

import com.tenx.ms.commons.rest.dto.Paginated;
import com.tenx.ms.retail.product.domain.ProductEntity;
import com.tenx.ms.retail.product.repository.ProductRepository;
import com.tenx.ms.retail.product.service.ProductService;
import com.tenx.ms.retail.stock.domain.StockEntity;
import com.tenx.ms.retail.stock.repository.StockRespository;
import com.tenx.ms.retail.stock.rest.dto.AddStock;
import com.tenx.ms.retail.stock.rest.dto.Stock;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    private StockRespository stockRespository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductService productService;

    /**
     * Add Product Stock to the Store
     *
     * @param stock The Stock DTO
     * @return
     */
    public Long addStockToStore(AddStock stock) {
        StockEntity isnewStock;

        productService.getStoreProductById(stock.getStoreId(), stock.getProductId());

        Optional<StockEntity> isExistingStock = stockRespository.findOneByStoreStoreIdAndProductProductId(stock.getStoreId(), stock.getProductId());

        if (isExistingStock.isPresent()) { //if its existing stock available add the new stock to it
            isnewStock = isExistingStock.get();
            isnewStock.setCount(isnewStock.getCount() + stock.getCount());
        } else {
            isnewStock = convertToEntity(stock);
        }

        return stockRespository.save(isnewStock).getStockId();
    }

    /**
     * Gets all stocks for a store
     *
     * @param storeId the store id
     * @return Stock list
     */
    public List<Stock> getStocksByStoreId(Long storeId) {
        List<StockEntity> stockEntityList = stockRespository.findByStoreStoreId(storeId);

        List<Stock> stockList = stockEntityList.stream().map(stock -> convertToDTO(stock)).collect(Collectors.toList());

        return stockList;
    }


    public Optional<Stock> getStockByStoreIdAndProductId(Long storeId, Long productId) {
        return stockRespository.findOneByStoreStoreIdAndProductProductId(storeId, productId).map(product -> convertToDTO(product));
    }


    /**
     * Convert the Stock DTO to Stock Entity
     *
     * @param stock DTO
     * @return stock Entity
     */
    private StockEntity convertToEntity(AddStock stock) {
        ProductEntity product = productRepository.findByStoresStoreIdAndProductId(stock.getStoreId(), stock.getProductId()).get();

        StoreEntity store = storeRepository.findOneByStoreId(stock.getStoreId()).get();

        StockEntity stockEntity = new StockEntity();
        stockEntity.setProduct(product);
        stockEntity.setStore(store);
        stockEntity.setCount(stock.getCount());

        return stockEntity;
    }

    /**
     * Convert Entity to DTO
     *
     * @param stock Entity
     * @return stock DTO
     */
    private Stock convertToDTO(StockEntity stock) {

        Stock stockDTO = new Stock();
        stockDTO.setProductId(stock.getProduct().getProductId());
        stockDTO.setStoreId(stock.getStore().getStoreId());
        stockDTO.setCount(stock.getCount());

        return stockDTO;
    }
}
