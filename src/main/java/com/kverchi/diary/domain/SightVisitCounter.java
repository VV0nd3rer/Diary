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
    private int visit_id;

    @Basic(optional = false)
    @Column(name="visit_datetime", insertable = false, updatable = false)
    private ZonedDateTime visit_datetime;

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

    public int getVisit_id() {
        return visit_id;
    }

    public void setVisit_id(int visit_id) {
        this.visit_id = visit_id;
    }

    public ZonedDateTime getVisit_datetime() {
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        String text = visit_datetime.format(formatter);
        visit_datetime = ZonedDateTime.parse(text, formatter);
        return visit_datetime;
    }

    public void setVisit_datetime(ZonedDateTime visit_datetime) {
        this.visit_datetime = visit_datetime;
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
