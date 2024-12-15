-- Task 4.2
-- storeprods
call vg_sp_game_sales_rev_by_date_range_and_game_no('2024-04-04', '2024-04-04', 2);
-- same as
select
	sum(total_sale_price_on_date)
from vg_view_game_sales_rev_by_date vvgsrbd
	where
		game_no = 2
		AND
		ag_date_of_sale = '2024-04-04';

call vg_sp_game_sales_rev_by_date_range_and_game_no('2024-04-04', '2024-04-04', 2);
-- same as
select
	sum(total_sale_price_on_date)
from vg_view_game_sales_rev_by_date vvgsrbd
	where
		game_no = 2
		AND
		ag_date_of_sale between '2024-04-04' and '2024-04-04';


-- Task 4.3
-- storeprods

call vg_sp_game_sales_rev_by_date_range_and_game_no('2024-04-04', '2024-04-04', null);
-- same as
select
	sum(total_sale_price_on_date)
from vg_view_game_sales_rev_by_date vvgsrbd
	where
		ag_date_of_sale = '2024-04-04';

call vg_sp_game_sales_rev_by_date_range_and_game_no('2024-04-04', '2024-04-06', null);
-- same as
select
	sum(total_sale_price_on_date)
from vg_view_game_sales_rev_by_date vvgsrbd
	where
		ag_date_of_sale between '2024-04-04' and '2024-04-06';