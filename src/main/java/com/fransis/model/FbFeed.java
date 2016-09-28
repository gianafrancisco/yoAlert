package com.fransis.model;

import javax.persistence.*;

/**
 * Created by francisco on 9/4/16.
 */
@Entity
public class FbFeed {

    @Column(name = "id", unique = true, nullable = false)
    @Id
    private String id;
    @Transient
    private String message;

    public FbFeed() {

    }

    public FbFeed(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
