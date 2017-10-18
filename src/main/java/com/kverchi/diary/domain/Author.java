package com.kverchi.diary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Liudmyla Melnychuk on 5.9.2017.
 */
@Entity
@Table(name="authors")
public class Author {
    @Id
    @Column(name="auth_id")
    private int authId;
    @Column(name="auth_type")
    private int type;
    @Column(name="auth_name")
    private String name;

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
