package com.vanguard.test.repository;

import com.vanguard.test.model.jpa.VgRecordGameSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Repository
public interface VgRecordGameSaleProcedureRepository extends JpaRepository<VgRecordGameSale, Long> {

    @Procedure(procedureName = "vg_sp_list_game_sales_by_date_range_and_price_paging")
    List<VgRecordGameSale> getGameSalesPaged
            (
                    @Param("from_date") Date fromDate,
                    @Param("to_date") Date toDate,
                    @Param("less_more") Boolean lessMore,
                    @Param("price") BigDecimal price,
                    @Param("page") Integer page,
                    @Param("size") Integer size
            );

    @Procedure(procedureName = "vg_sp_game_sales_count_by_date_range_and_game_no")
    List<Long> getGameSalesCount
            (
                    @Param("from_date") Date fromDate,
                    @Param("to_date") Date toDate,
                    @Param("game_no_req") Integer gameNoReq
            );


    @Procedure(procedureName = "vg_sp_game_sales_rev_by_date_range_and_game_no")
    List<BigDecimal> getGameSalesRev
            (
                    @Param("from_date") Date fromDate,
                    @Param("to_date") Date toDate,
                    @Param("game_no_req") Integer gameNoReq
            );
}