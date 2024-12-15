package com.vanguard.test.model.jpa;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity(name = "vg_record_game_sale")
public class VgRecordGameSale {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "vg_record_game_sale_seq", initialValue = 1)
    @Column(name = "recorded_id")
    private Long recordedId;

    @Column(name = "id")
    private Long id;
    @Column(name = "game_no")
    private Integer gameNo;
    @Column(name = "game_name")
    private String gameName;
    @Column(name = "game_code")
    private String gameCode;
    @Column(name = "game_type")
    private String gameType;
    @Column(name = "cost_price")
    private BigDecimal costPrice;
    @Column(name = "tax")
    private Integer tax;
    @Column(name = "sale_price")
    private BigDecimal salePrice;
    @Column(name = "date_of_sale")
    private Timestamp dateOfSale;
    @Column(name = "created_on")
    private Timestamp createdOn;
    @Column(name = "last_updated_on")
    private Timestamp lastUpdatedOn;
}