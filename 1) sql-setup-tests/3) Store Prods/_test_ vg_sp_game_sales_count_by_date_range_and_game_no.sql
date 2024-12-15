-- task 4.1 - select between two ranges (SQL SUM)
call vg_sp_game_sales_count_by_date_range_and_game_no('2024-04-04', '2024-04-04', null);
-- same as
select SUM(number_of_sales) from vg_view_game_sales_count_by_date vvgsrbd
	where
		ag_date_of_sale = '2024-04-04';

-- vg_sp_game_sales_count_by_date_range_and_game_no
call vg_sp_game_sales_count_by_date_range_and_game_no('2024-04-04', '2024-04-19', null);
-- same as
select SUM(number_of_sales) from vg_view_game_sales_count_by_date vvgsrbd
	where
		ag_date_of_sale between '2024-04-04' and '2024-04-19';

-- task 4.3 - select between two ranges (SQL SUM) and game no
-- storeprods

-- vg_sp_game_sales_count_by_game_no_and_date
call vg_sp_game_sales_count_by_date_range_and_game_no('2024-04-04', '2024-04-04', 2);
-- same as
select
	SUM(number_of_sales)
from vg_view_game_sales_count_by_date vvgsrbd
	where
		game_no = 2
		and
		ag_date_of_sale = '2024-04-04';

-- vg_sp_game_sales_count_by_game_no_and_date_range
call vg_sp_game_sales_count_by_date_range_and_game_no('2024-04-04','2024-04-30', 2);
-- same as
select
	SUM(number_of_sales)
from vg_view_game_sales_count_by_date vvgsrbd
	where
		game_no = 2
		and
		ag_date_of_sale between '2024-04-04' and '2024-04-30';