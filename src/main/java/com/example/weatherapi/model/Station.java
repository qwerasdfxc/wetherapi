package com.example.weatherapi.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(schema = "weather", name = "station")
@Data
@Builder
public class Station {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String code;

    @Column("country")
    private String country;
}
