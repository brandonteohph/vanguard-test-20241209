package com.vanguard.test.repository;

import com.vanguard.test.model.VgRecordCsvUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VgRecordCsvUploadRepository extends JpaRepository<VgRecordCsvUpload, Long> {

}