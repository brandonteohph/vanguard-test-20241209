package com.vanguard.test.controllers;

import com.vanguard.test.model.enums.CountOrSalesToggle;
import com.vanguard.test.model.enums.PriceModeToggle;
import com.vanguard.test.model.jpa.VgRecordGameSale;
import com.vanguard.test.repository.VgRecordGameSaleProcedureRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class GetTotalSalesController {


    @Autowired
    VgRecordGameSaleProcedureRepository vgRecordGameSaleProcedureRepository;

    // Task 4
    @GetMapping("/getTotalSales")
    @Transactional
    public ResponseEntity<String> getTotalSales(
            @RequestParam(name = "type") CountOrSalesToggle type,
            @RequestParam(name = "from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(name = "gameNo", required = false) Integer gameNo
    ) {
        if (null != fromDate && null == toDate) {
            toDate = fromDate;
        }
        String repsonse = "Unable to be found";

        switch (type) {
            case COUNT:
                Long count = callGameCount(fromDate, toDate, gameNo);
                repsonse = "" + count;
                break;

            case SALES:
                BigDecimal revenue = callGameRev(fromDate, toDate, gameNo);
                repsonse = "" + revenue;
                break;

            default:
                // error\
                log.error("Enum not found, value: {}", type);
                break;
        }

        return ResponseEntity.ok(repsonse);

    }

    @Transactional
    private Long callGameCount(
            Date fromDate,
            Date toDate,
            Integer gameNo
    ) {
        return vgRecordGameSaleProcedureRepository.getGameSalesCount(
                null != fromDate ? new java.sql.Date(fromDate.getTime()) : null,
                null != toDate ? new java.sql.Date(toDate.getTime()) : null,
                gameNo
        ).get(0);

    }

    @Transactional
    private BigDecimal callGameRev(
            Date fromDate,
            Date toDate,
            Integer gameNo
    ) {
        return vgRecordGameSaleProcedureRepository.getGameSalesRev(
                null != fromDate ? new java.sql.Date(fromDate.getTime()) : null,
                null != toDate ? new java.sql.Date(toDate.getTime()) : null,
                gameNo
        ).get(0);

    }
}