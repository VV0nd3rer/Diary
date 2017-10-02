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
    private int auth_id;
    @Column(name="auth_type")
    private int auth_type;
    @Column(name="auth_name")
    private String auth_name;

    public int getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(int auth_id) {
        this.auth_id = auth_id;
    }

    public int getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(int auth_type) {
        this.auth_type = auth_type;
    }

    public String getAuth_name() {
        return auth_name;
    }

    public void setAuth_name(String auth_name) {
        this.auth_name = auth_name;
    }
}
