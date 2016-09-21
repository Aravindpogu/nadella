package com.fedex.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Travel.
 */
@Entity
@Table(name = "travel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Travel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "from_place")
    private String from_place;

    @Column(name = "to_place")
    private String to_place;

    @Column(name = "from_date")
    private LocalDate from_date;

    @Column(name = "to_date")
    private LocalDate to_date;

    @ManyToOne
    private Tracking travel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom_place() {
        return from_place;
    }

    public void setFrom_place(String from_place) {
        this.from_place = from_place;
    }

    public String getTo_place() {
        return to_place;
    }

    public void setTo_place(String to_place) {
        this.to_place = to_place;
    }

    public LocalDate getFrom_date() {
        return from_date;
    }

    public void setFrom_date(LocalDate from_date) {
        this.from_date = from_date;
    }

    public LocalDate getTo_date() {
        return to_date;
    }

    public void setTo_date(LocalDate to_date) {
        this.to_date = to_date;
    }

    public Tracking getTravel() {
        return travel;
    }

    public void setTravel(Tracking tracking) {
        this.travel = tracking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Travel travel = (Travel) o;
        if(travel.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, travel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Travel{" +
            "id=" + id +
            ", from_place='" + from_place + "'" +
            ", to_place='" + to_place + "'" +
            ", from_date='" + from_date + "'" +
            ", to_date='" + to_date + "'" +
            '}';
    }
}
