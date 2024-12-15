package com.vanguard.test.repository;

import com.vanguard.test.model.jpa.VgRecordGameSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VgRecordGameSaleRepository extends JpaRepository<VgRecordGameSale, Long> {

}