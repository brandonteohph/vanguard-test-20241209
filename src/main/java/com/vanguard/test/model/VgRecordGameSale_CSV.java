package com.vanguard.test.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.TimeZone;

@JsonPropertyOrder(
        {
                "id",
                "game_no",
                "game_name",
                "game_code",
                "type",
                "cost_price",
                "tax",
                "sale_price",
                "date_of_sale"
        }
    )
@Data
public class VgRecordGameSale_CSV {

    public Long id;
    public Integer game_no;
    public String game_name;
    public String game_code;
    public String type;
    public BigDecimal cost_price;
    public Integer tax;
    public BigDecimal sale_price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp date_of_sale;


}