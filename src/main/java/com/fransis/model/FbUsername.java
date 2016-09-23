package com.fransis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by francisco on 9/4/16.
 */
@Entity
public class FbUsername {
    @Column(name = "id", unique = true, nullable = false)
    @Id
    private String username;
    private String accessToken;

    public FbUsername(String username, String accessToken) {
        this.username = username;
        this.accessToken = accessToken;
    }

    public FbUsername() {
    }

    public String getUsername() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
