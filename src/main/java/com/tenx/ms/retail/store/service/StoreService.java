package com.tenx.ms.retail.store.service;

import com.tenx.ms.commons.rest.dto.Paginated;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.store.rest.dto.CreateStore;
import com.tenx.ms.retail.store.rest.dto.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public Store createStore(CreateStore store) throws Exception {

        if (getStoreByName(store.getName()).isPresent()) {
            throw new Exception("Store  " + store.getName() + " already exists");
        }
        return convertToStoreDTO(storeRepository.save(convertToStoreEntity(store)));

    }


    /**
     * Get all the store information
     *
     * @return List of Store DTO
     */
    public List<Store> getAllStores() {
        List<StoreEntity> storeEntityList = storeRepository.findAll();
        //TODO need to implement with stream API as intelliJ recommending
        // List <Store> storeList = new ArrayList<Store>();
        /*
        for (StoreEntity storeEntity : storeEntityList){
            storeList.add(convertToStoreDTO(storeEntity));
        }*/
        List<Store> storeList = storeEntityList.stream().map(store -> convertToStoreDTO(store)).collect(Collectors.toList());
        return storeList;
    }

    /**
     * Gets the Store information by ID
     *
     * @param storeId
     * @return store dto
     */
    public Optional<Store> getStoreById(Long storeId) {
        return storeRepository.findOneByStoreId(storeId).map(store -> convertToStoreDTO(store));
    }

    /**
     * Gets the Store information by Name
     *
     * @param storeName The Store Name
     * @return store dto
     */
    public Optional<Store> getStoreByName(String storeName) {
        return storeRepository.findOneByName(storeName).map(store -> convertToStoreDTO(store));
    }

    /**
     * Convert the Store Entity to DTO
     *
     * @param store Entity
     * @return store dto
     */
    private Store convertToStoreDTO(StoreEntity store) {
        Store storeDto = new Store();
        storeDto.setStoreId(store.getStoreId());
        storeDto.setName(store.getName());
        return storeDto;
    }

    /**
     * Convert the store DTO to Entity
     *
     * @param store DTO
     * @return Store Entity
     */
    private StoreEntity convertToStoreEntity(CreateStore store) {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setName(store.getName());
        return storeEntity;
    }


}
