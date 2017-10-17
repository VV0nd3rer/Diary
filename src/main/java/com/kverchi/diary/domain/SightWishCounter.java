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
    private int wish_id;

    @Basic(optional = false)
    @Column(name="wish_datetime", insertable = false, updatable = false)
    private ZonedDateTime wish_datetime;

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
    public int getWish_id() {
        return wish_id;
    }

    public void setWish_id(int wish_id) {
        this.wish_id = wish_id;
    }

    public ZonedDateTime getWish_datetime() {
        return wish_datetime;
    }

    public void setWish_datetime(ZonedDateTime wish_datetime) {
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        String text = wish_datetime.format(formatter);
        wish_datetime = ZonedDateTime.parse(text, formatter);
        this.wish_datetime = wish_datetime;
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

