package com.vanguard.test.controllers;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.vanguard.test.model.jpa.VgRecordCsvUpload;
import com.vanguard.test.model.jpa.VgRecordGameSale;
import com.vanguard.test.model.jpa.VgRecordGameSale_CSV;
import com.vanguard.test.repository.VgRecordCsvUploadRepository;
import com.vanguard.test.repository.VgRecordGameSaleRepository;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class ImportCSVGameSalesController {

    @Autowired
    VgRecordCsvUploadRepository vgRecordCsvUploadRepository;
    @Autowired
    VgRecordGameSaleRepository vgRecordGameSaleRepository;

    // task 1
    @PostMapping("/import")
    public ResponseEntity<String> uploadCsvFile(@RequestParam(value = "file", required = false) @Valid final MultipartFile file) throws IOException {
        // first validation - is the file empty?
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error - File is empty");
        }

        CsvMapper csvMapper = new CsvMapper();

        // start import
        // 1) save first record of of the timing
        VgRecordCsvUpload vgRecordCsvUpload = new VgRecordCsvUpload();
        vgRecordCsvUpload.setNameOfFile(file.getOriginalFilename());
        vgRecordCsvUpload.setTransactionId(UUID.randomUUID().toString());
        vgRecordCsvUpload.setDatetimeUploadStart(new Timestamp(System.currentTimeMillis())); // this follows the current system millis, should be ok for containerization
        vgRecordCsvUploadRepository.save(vgRecordCsvUpload);

        List<VgRecordGameSale> entityList = new ArrayList<>();
        Long numberOfRows = 0L;
        Long totalUploadTimeMs = 0L;
        Long startTimeOfFlush = 0L;

        // 2) begin import
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String readFirstLine = reader.readLine();
            // do validation of the first line!!
            log.info("Skipping first line: {}", readFirstLine);
            if (!StringUtils.containsIgnoreCase(readFirstLine, "id,game_no,game_name,game_code,type,cost_price,tax,sale_price,date_of_sale")) {
                throw new ValidationException("First line header is invalid (TransactionId: " + vgRecordCsvUpload.getTransactionId() + ") - must be "
                        + "id,game_no,game_name,game_code,type,cost_price,tax,sale_price,date_of_sale");
            }

            startTimeOfFlush = System.currentTimeMillis();
            log.info("StartTime: {}", new Timestamp(startTimeOfFlush));
            String readLine = "";
            while ((readLine = reader.readLine()) != null) {
                try {
                    VgRecordGameSale_CSV gameSaleCsv = csvMapper
                            .readerWithTypedSchemaFor(VgRecordGameSale_CSV.class)
                            .readValue(readLine);

                    // map to new object
                    VgRecordGameSale gameSale = new VgRecordGameSale();
                    gameSale.setId(gameSaleCsv.getId());
                    gameSale.setGameNo(gameSaleCsv.getGame_no());
                    gameSale.setGameName(gameSaleCsv.getGame_name());
                    gameSale.setGameCode(gameSaleCsv.getGame_code());
                    gameSale.setGameType(gameSaleCsv.getType());
                    gameSale.setCostPrice(gameSaleCsv.getCost_price());
                    gameSale.setTax(gameSaleCsv.getTax());
                    gameSale.setSalePrice(gameSaleCsv.getSale_price());
                    gameSale.setDateOfSale(gameSaleCsv.getDate_of_sale());
                    gameSale.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                    entityList.add(gameSale);

                } catch (Exception e) {
                    log.error("Unable to insert row due to following error: {}", e.getMessage());
                }

            }
            numberOfRows = (long) entityList.size();
            log.info("flushing");
            vgRecordGameSaleRepository.saveAllAndFlush(entityList);

            log.info("Saved {} rows to VgRecordGameSale table, transactionId: {}", numberOfRows, vgRecordCsvUpload.getTransactionId());

            long endTimeOfFlush = System.currentTimeMillis();
            log.info("EndTime: {}", new Timestamp(endTimeOfFlush));
            totalUploadTimeMs = endTimeOfFlush - startTimeOfFlush;
            log.info("totalUploadTimeMs: {}", totalUploadTimeMs);

        }

        catch (Exception e) {
            log.error("Something went wrong with the CSV import (TransactionId: {}) - {}: {}:", vgRecordCsvUpload.getTransactionId(), e.getClass(), e.getMessage());

            long endTimeError = System.currentTimeMillis();
            log.error("EndTime upon {}: {}", e.getClass(), new Timestamp(endTimeError));
            totalUploadTimeMs = endTimeError - startTimeOfFlush;
            log.error("totalUploadTimeMs upon {}: {}", e.getClass(), totalUploadTimeMs);


            vgRecordCsvUpload.setDatetimeUploadEnd(new Timestamp(System.currentTimeMillis()));
            vgRecordCsvUpload.setErrorDescription(StringUtils.truncate(e.getMessage(), 255));
            vgRecordCsvUpload.setTotalTimeMs(totalUploadTimeMs);
            vgRecordCsvUploadRepository.saveAndFlush(vgRecordCsvUpload);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while reading the CSV file - exception thrown: " + e.getMessage());
        }

        vgRecordCsvUpload.setDatetimeUploadEnd(new Timestamp(System.currentTimeMillis()));
        vgRecordCsvUpload.setSuccessDescription("Number of rows successfully inserted: " + numberOfRows);
        vgRecordCsvUpload.setTotalTimeMs(totalUploadTimeMs);
        vgRecordCsvUpload.setNumberOfRows(numberOfRows);
        vgRecordCsvUploadRepository.save(vgRecordCsvUpload);
        vgRecordGameSaleRepository.flush();


        // return correct/wrong
        return ResponseEntity.ok("CSV file uploaded and processed successfully. Total Rows inserted: " + numberOfRows + ", TransactionId: " + vgRecordCsvUpload.getTransactionId());

    }
}