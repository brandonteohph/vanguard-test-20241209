-- this file is a combination of every file in the above folders except for prefix _test_
CREATE TABLE `vg_record_csv_upload` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name_of_file` varchar(255) NOT NULL,
  `datetime_upload_start` timestamp NOT NULL,
  `datetime_upload_end` timestamp NULL DEFAULT NULL,
  `error_description` varchar(300) DEFAULT NULL,
  `total_time_ms` bigint DEFAULT NULL,
  `success_description` varchar(255) DEFAULT NULL,
  `number_of_rows` bigint DEFAULT NULL,
  `transaction_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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

CREATE TABLE `vg_record_game_sale_seq` (
  `next_val` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into vg_test.vg_record_game_sale_seq (next_val) VALUES (1);

drop view if exists vg_view_game_sales_count_by_date;
CREATE VIEW vg_view_game_sales_count_by_date AS
	select
		CAST(date_of_sale AS DATE) as ag_date_of_sale,
		game_no as game_no,
		COUNT(id) as number_of_sales
FROM vg_test.vg_record_game_sale vrgs
	group by ag_date_of_sale, game_no
order by ag_date_of_sale asc;

drop view if exists vg_view_game_sales_rev_by_date;
CREATE VIEW vg_view_game_sales_rev_by_date AS
select
	CAST(date_of_sale AS DATE) as ag_date_of_sale,
	game_no as game_no,
	SUM(sale_price) as total_sale_price_on_date
FROM vg_test.vg_record_game_sale vrgs
	group by ag_date_of_sale, game_no
order by ag_date_of_sale asc;

DROP PROCEDURE IF EXISTS vg_test.vg_sp_game_sales_count_by_date_range_and_game_no;


DELIMITER $$
$$

CREATE PROCEDURE vg_test.vg_sp_game_sales_count_by_date_range_and_game_no(
	in from_date TIMESTAMP,
	in to_date TIMESTAMP,
	in game_no_req INTEGER
)
begin
	select
		SUM(number_of_sales) as TotalCount
	from vg_view_game_sales_count_by_date vvgsrbd
	where
		IF(
			game_no_req is not null,
			game_no = game_no_req,
			1=1
		)
		and
		IF(
			from_date is not null and to_date is not null,
			cast(ag_date_of_sale as DATE) between cast(from_date as DATE) and cast(to_date as DATE),
			1=1
		)
		order by vvgsrbd.ag_date_of_sale asc;
END

$$
DELIMITER ;

DROP PROCEDURE IF EXISTS vg_test.vg_sp_game_sales_rev_by_date_range_and_game_no;


DELIMITER $$
$$

CREATE PROCEDURE vg_test.vg_sp_game_sales_rev_by_date_range_and_game_no(
	in from_date TIMESTAMP,
	in to_date TIMESTAMP,
	in game_no_req INTEGER
)
begin
	select
		sum(total_sale_price_on_date)
	from vg_view_game_sales_rev_by_date vvgsrbd
	where
		IF(
			game_no_req is not null,
			game_no = game_no_req,
			1=1
		)
		and
		IF(
			from_date is not null and to_date is not null,
			cast(ag_date_of_sale as DATE) between cast(from_date as DATE) and cast(to_date as DATE),
			1=1
		)
		order by vvgsrbd.ag_date_of_sale asc;
END

$$
DELIMITER ;

DROP PROCEDURE IF EXISTS vg_test.vg_sp_list_game_sales_by_date_range_and_price_paging;


DELIMITER $$
$$

CREATE PROCEDURE `vg_test`.`vg_sp_list_game_sales_by_date_range_and_price_paging`(
	in from_date TIMESTAMP,
	in to_date TIMESTAMP,
	in less_more BOOLEAN, -- lessthan = true, more than = false
	in price DECIMAL(10,2),
	in page INTEGER,
	in page_size INTEGER
)
begin
	declare row_skip BIGINT;
	SET row_skip=(page-1)*page_size;


	select
		vrgs.recorded_id,
		vrgs.id,
		vrgs.game_no,
		vrgs.game_name,
		vrgs.game_code,
		vrgs.game_type,
		vrgs.cost_price,
		vrgs.tax,
		vrgs.sale_price,
		vrgs.date_of_sale,
		vrgs.created_on,
		vrgs.last_updated_on
	from
	vg_test.vg_record_game_sale vrgs
		inner join
	-- late lookup method
	(
		select recorded_id from vg_test.vg_record_game_sale vrgs
		where

			if(
				from_date is not null and to_date is not null,
				cast(date_of_sale as DATE) between cast(from_date as DATE) and cast(to_date as DATE),
				1=1
			)

			and

			if(
				price is not null and less_more is not null,
				if(
					less_more = true,
					sale_price <= price, -- sale price less than or equal to price
					sale_price >= price  -- sale price more than or equal to price
				),
				1=1
			)
		limit page_size offset row_skip
	) as id_lookup
	on id_lookup.recorded_id = vrgs.recorded_id
	order by vrgs.recorded_id asc
		;

END

$$
DELIMITER ;

CREATE USER 'vanguard_tester'@'localhost' IDENTIFIED BY 'password';

grant select, insert, update on vg_test.vg_record_game_sale TO 'vanguard_tester'@'localhost';
grant select, insert, update on vg_test.vg_record_csv_upload TO 'vanguard_tester'@'localhost';
grant select, update on vg_test.vg_record_game_sale_seq TO 'vanguard_tester'@'localhost';

GRANT EXECUTE ON PROCEDURE vg_test.vg_sp_list_game_sales_by_date_range_and_price_paging TO 'vanguard_tester'@'localhost';
GRANT EXECUTE ON PROCEDURE vg_test.vg_sp_game_sales_count_by_date_range_and_game_no TO 'vanguard_tester'@'localhost';
GRANT EXECUTE ON PROCEDURE vg_test.vg_sp_game_sales_rev_by_date_range_and_game_no TO 'vanguard_tester'@'localhost';
