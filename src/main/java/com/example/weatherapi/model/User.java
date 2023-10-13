package com.example.weatherapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Table(schema = "weather", name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column("id")
    private Long id;

    @Column("login")
    private String username;

    @Column("password")
    private String password;

    @Column("key")
    private String key;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @ToString.Include(name = "password")
    private String maskPassword(){
        return "********";
    }


}
