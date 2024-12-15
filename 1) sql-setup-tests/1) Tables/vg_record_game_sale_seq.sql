-- vg_test.vg_record_game_sale_seq definition

CREATE TABLE `vg_record_game_sale_seq` (
  `next_val` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into vg_test.vg_record_game_sale_seq (next_val) VALUES (1);