package com.tenx.ms.retail.product.repository;

import com.tenx.ms.retail.product.domain.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByStoresStoreId(final Long storeId);

    Optional<ProductEntity> findByStoresStoreIdAndProductId(final Long storeId, final Long productId);

    Optional<ProductEntity> findByStoresStoreIdAndName(final Long storeId, final String name);


}
