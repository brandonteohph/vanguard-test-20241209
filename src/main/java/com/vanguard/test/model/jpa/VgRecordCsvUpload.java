package com.vanguard.test.model.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity(name = "vg_record_csv_upload")
public class VgRecordCsvUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name_of_file")
    private String nameOfFile;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "datetime_upload_start")
    private Timestamp datetimeUploadStart;
    @Column(name = "datetime_upload_end")
    private Timestamp datetimeUploadEnd;
    @Column(name = "error_description")
    private String errorDescription;
    @Column(name = "total_time_ms")
    private Long totalTimeMs;
    @Column(name = "success_description")
    private String successDescription;
    @Column(name = "number_of_rows")
    private Long numberOfRows;
}