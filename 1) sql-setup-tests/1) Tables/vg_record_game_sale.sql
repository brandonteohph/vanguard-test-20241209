-- vg_test.vg_record_game_sale definition

CREATE TABLE `vg_record_game_sale` (
  `recorded_id` bigint NOT NULL AUTO_INCREMENT,
  `id` bigint NOT NULL,
  `game_no` int NOT NULL,
  `game_name` varchar(20) NOT NULL,
  `game_code` varchar(5) NOT NULL,
  `game_type` int NOT NULL,
  `cost_price` decimal(10,2) NOT NULL,
  `tax` int NOT NULL,
  `sale_price` decimal(10,2) NOT NULL,
  `date_of_sale` timestamp NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`recorded_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;