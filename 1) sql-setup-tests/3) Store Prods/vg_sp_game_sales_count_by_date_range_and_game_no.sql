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