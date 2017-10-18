package com.kverchi.diary.domain;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Liudmyla Melnychuk on 14.9.2017.
 */
@Entity
@Table(name="sight_visits_counter")
public class SightVisitCounter {
    @Id
    @Column(name="visit_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int visitId;

    @Basic(optional = false)
    @Column(name="visit_datetime", insertable = false, updatable = false)
    private ZonedDateTime visitDatetime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sight_id")
    private CountriesSight countriesSight;

    public SightVisitCounter() {};
    public SightVisitCounter(int sight_id, int user_id) {
        this.countriesSight = new CountriesSight(sight_id);
        this.user = new User(user_id);
    }

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public ZonedDateTime getVisitDatetime() {
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        String text = visitDatetime.format(formatter);
        visitDatetime = ZonedDateTime.parse(text, formatter);
        return visitDatetime;
    }

    public void setVisitDatetime(ZonedDateTime visitDatetime) {
        this.visitDatetime = visitDatetime;
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
