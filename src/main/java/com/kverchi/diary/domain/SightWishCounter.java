package com.kverchi.diary.domain;

/**
 * Created by Liudmyla Melnychuk on 13.9.2017.
 */

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="sight_wishes_counter")
public class SightWishCounter {
    @Id
    @Column(name="wish_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int wishId;

    @Basic(optional = false)
    @Column(name="wish_datetime", insertable = false, updatable = false)
    private ZonedDateTime wishDatetime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sight_id")
    private CountriesSight countriesSight;

    public SightWishCounter() {};
    public SightWishCounter(int sight_id, int user_id) {
        this.countriesSight = new CountriesSight(sight_id);
        this.user = new User(user_id);
    }
    public int getWishId() {
        return wishId;
    }

    public void setWishId(int wishId) {
        this.wishId = wishId;
    }

    public ZonedDateTime getWishDatetime() {
        return wishDatetime;
    }

    public void setWishDatetime(ZonedDateTime wishDatetime) {
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        String text = wishDatetime.format(formatter);
        wishDatetime = ZonedDateTime.parse(text, formatter);
        this.wishDatetime = wishDatetime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CountriesSight getCountriesSight() {
        return countriesSight;
    }

    public void setCountriesSight(CountriesSight countriesSight) {
        this.countriesSight = countriesSight;
    }
}

