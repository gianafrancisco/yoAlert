package com.fransis.model;

import javax.persistence.*;

/**
 * Created by francisco on 23/09/2016.
 */
@Entity
public class FbGroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Id
    private Long id;
    private String groupId;
    private String groupName;

    public FbGroup() {
        this.id = 0L;
    }

    public FbGroup(String groupId, String groupName) {
        this();
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public Long getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }
}
