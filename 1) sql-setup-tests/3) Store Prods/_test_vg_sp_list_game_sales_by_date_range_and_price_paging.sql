-- vg_sp_list_game_sales_by_date_range_and_price_paging testing
-- uses the test-sample-10rows.csv

-- basic "call all", page 1, size 10 (id 1 to 10)
call vg_sp_list_game_sales_by_date_range_and_price_paging(null, null, null, null, 1, 10);
-- paged "call all", page 1, size 2 (expect ids 1 and 2)
call vg_sp_list_game_sales_by_date_range_and_price_paging(null, null, null, null, 1, 2);
-- paged "call all", page 2, size 4 (expect ids 5-8)
call vg_sp_list_game_sales_by_date_range_and_price_paging(null, null, null, null, 2, 4);

-- from date is equal "target date", page 1, size 10 (id 6, 8)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-04', '2024-04-04', null, null, 1, 10);
-- from date is equal "target date", page 1, size 1 (expect id 6)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-04', '2024-04-04', null, null, 1, 1);
-- from date is equal "target date", page 2, size 1 (expect id 8)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-04', '2024-04-04', null, null, 2, 1);

-- from date and to date are different "date range", page 1, size 10 (id 3, 5, 9)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-19', '2024-04-30', null, null, 1, 10);
-- from date and to date are different "date range", page 1, size 2 (expect id 3, 5)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-19', '2024-04-30', null, null, 1, 2);
-- from date and to date are different "date range", page 2, size 2 (expect id 9)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-19', '2024-04-30', null, null, 2, 2);

-- price range less than 20, page 1, size 10 (id 3, 7)
call vg_sp_list_game_sales_by_date_range_and_price_paging(null, null, true, 20, 1, 10);
-- price range less than 20, page 1, size 1 (expect id 3)
call vg_sp_list_game_sales_by_date_range_and_price_paging(null, null, true, 20, 1, 1);
-- price range less than 20, page 2, size 1 (expect id 7)
call vg_sp_list_game_sales_by_date_range_and_price_paging(null, null, true, 20, 2, 1);

-- price range more than 15, page 1, size 10 (id 1, 2, 4, 5, 6, 8, 9, 10)
call vg_sp_list_game_sales_by_date_range_and_price_paging(null, null, false, 15, 1, 10);
-- price range more than 15, page 1, size 5 (expect id 1, 2, 4, 5, 6)
call vg_sp_list_game_sales_by_date_range_and_price_paging(null, null, false, 15, 1, 5);
-- price range more than 15, page 1, size 5 (expect id 8, 9, 10)
call vg_sp_list_game_sales_by_date_range_and_price_paging(null, null, false, 15, 2, 5);

-- from date and to date are different "date range", price less than 25, page 1, size 10 (id 1, 5, 7)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-04', '2024-04-21', true, 25, 1, 10);
-- from date and to date are different "date range", price less than 25, page 1, size 2 (expect id 1, 5)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-04', '2024-04-21', true, 25, 1, 2);
-- from date and to date are different "date range", price less than 25, page 2, size 2 (expect id 7)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-04', '2024-04-21', true, 25, 2, 2);

-- from date and to date are different "date range", price more than 25, page 1, size 10 (id 2, 4, 6, 8, 10)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-04', '2024-04-21', false, 25, 1, 10);
-- from date and to date are different "date range", price more than 25, page 1, size 2 (expect id 2, 4, 6)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-04', '2024-04-21', false, 25, 1, 3);
-- from date and to date are different "date range", price more than 25, page 2, size 2 (expect id 8, 10)
call vg_sp_list_game_sales_by_date_range_and_price_paging('2024-04-04', '2024-04-21', false, 25, 2, 3);
