create PROCEDURE `vg_test`.`vg_sp_list_game_sales_by_date_range_and_price_paging`(
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

	-- if i want to make this ultra efficient, i would do late lookup. This query to look for IDs, then join against the index.
	select recorded_id, id, game_no, game_name, game_code, game_type, cost_price, tax, sale_price, date_of_sale, created_on, last_updated_on
	from
	vg_test.vg_record_game_sale vrgs
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
		order by id asc
		limit page_size offset row_skip
		;

END