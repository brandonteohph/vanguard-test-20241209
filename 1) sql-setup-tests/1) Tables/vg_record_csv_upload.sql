-- vg_test.vg_record_csv_upload definition

CREATE TABLE `vg_record_csv_upload` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name_of_file` varchar(255) NOT NULL,
  `datetime_upload_start` timestamp NOT NULL,
  `datetime_upload_end` timestamp NULL DEFAULT NULL,
  `error_description` varchar(300) DEFAULT NULL,
  `total_time_ms` bigint DEFAULT NULL,
  `success_description` varchar(255) DEFAULT NULL,
  `number_of_rows` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;