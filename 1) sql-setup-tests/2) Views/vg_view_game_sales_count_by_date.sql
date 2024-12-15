drop view if exists vg_view_game_sales_count_by_date;
CREATE VIEW vg_view_game_sales_count_by_date AS
	select
		CAST(date_of_sale AS DATE) as ag_date_of_sale,
		game_no as game_no,
		COUNT(id) as number_of_sales
FROM vg_test.vg_record_game_sale vrgs
	group by ag_date_of_sale, game_no
order by ag_date_of_sale asc;