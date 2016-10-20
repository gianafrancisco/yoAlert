package com.fransis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by francisco on 9/4/16.
 */
@Entity
public class FbUsername {
    @Column(name = "id", unique = true, nullable = false)
    @Id
    private String username;
    private String accessToken;
    private String usuarioId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="WATCHER_ID")
    private Watcher watcher;

    public FbUsername(String username, String accessToken, String usuarioId) {
        this.username = username;
        this.accessToken = accessToken;
        this.usuarioId = usuarioId;
    }

    public FbUsername() {
    }

    public String getUsername() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @JsonIgnore
    public Watcher getWatcher() {
        return watcher;
    }

    public void setWatcher(Watcher watcher) {
        this.watcher = watcher;
    }

    public String getUsuarioId() {
        return usuarioId;
    }
}
