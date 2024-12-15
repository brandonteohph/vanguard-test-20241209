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