package com.vanguard.test.repository;

import com.vanguard.test.model.VgRecordCsvUpload;
import com.vanguard.test.model.VgRecordGameSale;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Repository
public interface VgRecordGameSaleRepository extends JpaRepository<VgRecordGameSale, Long> {

}