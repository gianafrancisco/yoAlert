package com.fransis.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

    //    @ManyToOne
    @Transient
    private FbUsername username;

    //    @OneToMany(mappedBy="watcher", targetEntity = FbFilter.class)
    @Transient
    private List<FbFilter> filters;

    //    @OneToMany(mappedBy="watcher", targetEntity = FbGroup.class)
    @Transient
    private List<FbGroup> groups;

    //    @OneToMany(mappedBy="watcher", targetEntity = Email.class)
    @Transient
    private List<Email> emails;

    public Watcher(String description) {
        this();
        this.description = description;
    }

    public Watcher() {
        this.emails = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.filters = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public List<FbFilter> getFilters() {
        return filters;
    }

    public List<FbGroup> getGroups() {
        return groups;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public Long getId() {
        return id;
    }

    public FbUsername getUsername() {
        return username;
    }

    public void setUsername(FbUsername username) {
        this.username = username;
    }

    public void setFilters(List<FbFilter> filters) {
        this.filters = filters;
    }

    public void setGroups(List<FbGroup> groups) {
        this.groups = groups;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }
}
