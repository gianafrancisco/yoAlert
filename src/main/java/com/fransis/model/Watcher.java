package com.fransis.model;


import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by francisco on 9/25/16.
 */
@Entity
public class Watcher {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Id
    private Long id;
    private String description;

    @OneToMany(fetch=FetchType.EAGER)
    private Collection<FbFilter> filters;

    @OneToMany(fetch=FetchType.EAGER)
    private Collection<FbGroup> groups;

    @OneToMany(fetch=FetchType.EAGER)
    private Collection<Email> emails;

    public Watcher(String description) {
        this();
        this.description = description;
    }

    public Watcher() {
        this.emails = new HashSet<>();
        this.groups = new HashSet<>();
        this.filters = new HashSet<>();
    }


    public String getDescription() {
        return description;
    }

    public Collection<FbFilter> getFilters() {
        return filters;
    }

    public Collection<FbGroup> getGroups() {
        return groups;
    }

    public Collection<Email> getEmails() {
        return emails;
    }

    public Long getId() {
        return id;
    }
}
