package com.example.weatherapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@Table(schema = "weather", name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@RedisHash("User")
public class User implements Serializable {

    @Id
    @Column("id")
    private Long id;

    @Column("login")
    private String login;

    @Column("password")
    private String password;

    @Column("key")
    private String key;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @ToString.Include(name = "password")
    private String maskPassword(){
        return "********";
    }


}
