package com.vanguard.test.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetTotalSalesController {

    // Task 4
    @GetMapping("/getTotalSales")
    public ResponseEntity<String> getTotalSales() {
        return ResponseEntity.ok("ready to check");
    }
}