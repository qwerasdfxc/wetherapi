package com.example.weatherapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(schema = "weather", name = "user")
@Builder

public class User {

    @Id
    @Column("id")
    private Long id;

    @Column("login")
    private String login;

    @Column("password")
    private String password;

    @Column("key")
    private String key;
}
