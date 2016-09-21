package com.fedex.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Tracking.
 */
@Entity
@Table(name = "tracking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tracking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_start_date")
    private LocalDate order_start_date;

    @Column(name = "order_end_date")
    private LocalDate order_end_date;

    @Column(name = "actual_delivery_date")
    private LocalDate actual_delivery_date;

    @OneToOne
    @JoinColumn(unique = true)
    private Order tracking;

    @OneToMany(mappedBy = "travel")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Travel> travels = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOrder_start_date() {
        return order_start_date;
    }

    public void setOrder_start_date(LocalDate order_start_date) {
        this.order_start_date = order_start_date;
    }

    public LocalDate getOrder_end_date() {
        return order_end_date;
    }

    public void setOrder_end_date(LocalDate order_end_date) {
        this.order_end_date = order_end_date;
    }

    public LocalDate getActual_delivery_date() {
        return actual_delivery_date;
    }

    public void setActual_delivery_date(LocalDate actual_delivery_date) {
        this.actual_delivery_date = actual_delivery_date;
    }

    public Order getTracking() {
        return tracking;
    }

    public void setTracking(Order order) {
        this.tracking = order;
    }

    public Set<Travel> getTravels() {
        return travels;
    }

    public void setTravels(Set<Travel> travels) {
        this.travels = travels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tracking tracking = (Tracking) o;
        if(tracking.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tracking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tracking{" +
            "id=" + id +
            ", order_start_date='" + order_start_date + "'" +
            ", order_end_date='" + order_end_date + "'" +
            ", actual_delivery_date='" + actual_delivery_date + "'" +
            '}';
    }
}
