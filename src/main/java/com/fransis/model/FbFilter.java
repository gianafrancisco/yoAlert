package com.fransis.model;

import javax.persistence.*;

/**
 * Created by francisco on 9/19/16.
 */
@Entity
public class FbFilter {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Id
    private Long id;
    private String value;

    public FbFilter() {
    }

    public FbFilter(String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
