package com.fransis.model;

import javax.persistence.*;

/**
 * Created by francisco on 23/09/2016.
 */
@Entity
public class Email {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Id
    private Long id;
    private String email;
    private String desc;

    public Email() {
    }

    public Email(String email, String desc) {
        this.email = email;
        this.desc = desc;
    }

    public String getEmail() {
        return email;
    }

    public String getDesc() {
        return desc;
    }

    public Long getId() {
        return id;
    }
}
