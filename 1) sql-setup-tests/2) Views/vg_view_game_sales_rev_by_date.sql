drop view vg_view_game_sales_rev_by_date;
CREATE VIEW vg_view_game_sales_rev_by_date AS
select
	CAST(date_of_sale AS DATE) as ag_date_of_sale,
	game_no as game_no,
	SUM(sale_price) as total_sale_price_on_date
FROM vg_test.vg_record_game_sale vrgs
	group by ag_date_of_sale, game_no
order by ag_date_of_sale asc;