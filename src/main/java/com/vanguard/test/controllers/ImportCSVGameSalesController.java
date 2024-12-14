package com.vanguard.test.controllers;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.vanguard.test.model.VgRecordCsvUpload;
import com.vanguard.test.model.VgRecordGameSale;
import com.vanguard.test.model.VgRecordGameSale_CSV;
import com.vanguard.test.repository.VgRecordCsvUploadRepository;
import com.vanguard.test.repository.VgRecordGameSaleRepository;
import jakarta.validation.Valid;
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        // TODO do rest of validations here


        CsvMapper csvMapper = new CsvMapper();

        // start import
        // 1) save first record of of the timing
        VgRecordCsvUpload vgRecordCsvUpload = new VgRecordCsvUpload();
        vgRecordCsvUpload.setNameOfFile(file.getName());
        vgRecordCsvUpload.setDatetimeUploadStart(new Timestamp(System.currentTimeMillis())); // this follows the current system millis, should be ok for containerization
        vgRecordCsvUploadRepository.save(vgRecordCsvUpload);

        List<VgRecordGameSale> entityList = new ArrayList<>();
        Long numberOfRows = 0L;
        Long totalUploadTimeMs = 0L;
        Long startTimeOfFlush = 0L;
        int batch_size = 1000;

        // 2) begin import
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String readFirstLine = reader.readLine();
            // do validation of the first line!!
            log.info("Skipping first line: {}", readFirstLine);
            // TODO

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

            log.info("Saved {} rows to VgRecordGameSale table", numberOfRows);

            long endTimeOfFlush = System.currentTimeMillis();
            log.info("EndTime: {}", new Timestamp(endTimeOfFlush));
            totalUploadTimeMs = endTimeOfFlush - startTimeOfFlush;
            log.info("totalUploadTimeMs: {}", totalUploadTimeMs);

        } catch (IOException e) {
            log.error("Something went wrong with the CSV import - IOException:", e);

            long endTimeError = System.currentTimeMillis();
            log.info("EndTime upon error: {}", new Timestamp(endTimeError));
            totalUploadTimeMs = endTimeError - startTimeOfFlush;
            log.info("totalUploadTimeMs upon error: {}", totalUploadTimeMs);


            vgRecordCsvUpload.setDatetimeUploadEnd(new Timestamp(System.currentTimeMillis()));
            vgRecordCsvUpload.setErrorDescription(StringUtils.truncate(e.getMessage(), 255));
            vgRecordCsvUpload.setTotalTimeMs(totalUploadTimeMs);
            vgRecordCsvUploadRepository.saveAndFlush(vgRecordCsvUpload);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while reading the CSV file.");
        }

        vgRecordCsvUpload.setDatetimeUploadEnd(new Timestamp(System.currentTimeMillis()));
        vgRecordCsvUpload.setSuccessDescription("Number of rows successfully inserted: " + numberOfRows);
        vgRecordCsvUpload.setTotalTimeMs(totalUploadTimeMs);
        vgRecordCsvUpload.setNumberOfRows(numberOfRows);
        vgRecordCsvUploadRepository.save(vgRecordCsvUpload);
        vgRecordGameSaleRepository.flush();


        // return correct/wrong
        return ResponseEntity.ok("CSV file uploaded and processed successfully.");
//
//        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
//            List<String[]> records = reader.readAll();
//            // Process CSV records here, for example, print to console or save to database
//            for (String[] record : records) {
//                System.out.println(record);
//                System.out.println(String.join("|", record));  // Just print for demonstration
//            }
//            return ResponseEntity.ok("CSV file uploaded and processed successfully.");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error while reading the CSV file.");
//        } catch (CsvException e) {
//            throw new RuntimeException(e);
//        }
    }
}