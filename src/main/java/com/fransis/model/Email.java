package com.fransis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String description;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="WATCHER_ID")
    private Watcher watcher;

    public Email() {
    }

    public Email(String email, String description) {
        this.email = email;
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public Watcher getWatcher() {
        return watcher;
    }

    public void setWatcher(Watcher watcher) {
        this.watcher = watcher;
    }
}
