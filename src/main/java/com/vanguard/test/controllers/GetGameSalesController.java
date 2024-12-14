package com.vanguard.test.controllers;

import com.vanguard.test.model.VgRecordGameSale;
import com.vanguard.test.model.enums.PriceModeToggle;
import com.vanguard.test.repository.VgRecordGameSaleProcedureRepository;
import com.vanguard.test.repository.VgRecordGameSaleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Validated
@RestController
public class GetGameSalesController {

    @Autowired
    VgRecordGameSaleProcedureRepository vgRecordGameSaleProcedureRepository;

    // Task 3
    @GetMapping("/getGameSales")
    @Transactional
    public ResponseEntity<List<VgRecordGameSale>> getGameSales(
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(name = "priceMode", required = false) PriceModeToggle priceModeToggle,
            @RequestParam(name = "price", required = false) BigDecimal salePrice,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {


        if (null != fromDate && null == toDate) {
            toDate = fromDate;
        }
        if(
                (null != salePrice && null == priceModeToggle)
                ||
                (null == salePrice && null != priceModeToggle)
        ) {
            log.error("Both sale price and priceModeToggle must be non-null to check price. Defaulting to no price check");
            salePrice = null;
            priceModeToggle = null;
        }

        List<VgRecordGameSale> gameSalesPaged =  callRepository(fromDate, toDate, priceModeToggle, salePrice, page, size);


        return ResponseEntity.ok(gameSalesPaged);

    }

    @Transactional
    private List<VgRecordGameSale> callRepository(
            Date fromDate,
            Date toDate,
            PriceModeToggle priceModeToggle,
            BigDecimal salePrice,
            Integer page,
            Integer size
    ){
        return vgRecordGameSaleProcedureRepository.getGameSalesPaged(
                null != fromDate ? new java.sql.Date(fromDate.getTime()) : null,
                null != toDate ? new java.sql.Date(toDate.getTime()) : null,
                (null != priceModeToggle) ? PriceModeToggle.LESS_THAN.equals(priceModeToggle) : null,
                salePrice,
                page,
                size);

    }
}