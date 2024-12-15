-- you can only run this as root to create the user. the program will use this user

CREATE USER 'vanguard_tester'@'localhost' IDENTIFIED BY 'password';

grant select, insert, update on vg_test.vg_record_game_sale TO 'vanguard_tester'@'localhost';
grant select, insert, update on vg_test.vg_record_csv_upload TO 'vanguard_tester'@'localhost';
grant select, update on vg_test.vg_record_game_sale_seq TO 'vanguard_tester'@'localhost';

GRANT EXECUTE ON PROCEDURE vg_test.vg_sp_list_game_sales_by_date_range_and_price_paging TO 'vanguard_tester'@'localhost';
GRANT EXECUTE ON PROCEDURE vg_test.vg_sp_game_sales_count_by_date_range_and_game_no TO 'vanguard_tester'@'localhost';
GRANT EXECUTE ON PROCEDURE vg_test.vg_sp_game_sales_rev_by_date_range_and_game_no TO 'vanguard_tester'@'localhost';
