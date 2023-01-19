package com.gksoft.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gksoft.application.domain.enumeration.BidAskStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ask.
 */
@Entity
@Table(name = "ask")
 @SuppressWarnings("common-java:DuplicatedBlocks")
public class Ask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "notes")
    private String notes;

    @Column(name = "price")
    private Long price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BidAskStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = {  "userRates", "country", "stateProvince", "city" }, allowSetters = true)
    private User fromUser;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "asks",
            "status",
            "createBy",
            "fromCountry",
            "toCountry",
            "fromState",
            "toState",
            "fromCity",
            "toCity",
            "availableItemTypes",
         },
        allowSetters = true
    )
    private Flight flight;

    @Column(name = "cargo_request_id")
    private Long cargoRequestId;
    @Column(name = "create_date")
    private Instant createDate;



    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Instant getCreateDate() {
        return this.createDate;
    }

    public  Ask createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }



    public Long getId() {
        return this.id;
    }

    public Ask id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return this.notes;
    }

    public Ask notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getPrice() {
        return this.price;
    }

    public Ask price(Long price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public BidAskStatus getStatus() {
        return this.status;
    }

    public Ask status(BidAskStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(BidAskStatus status) {
        this.status = status;
    }


    public Ask FromUser( User  user) {
        this.setFromUser( user);
        return this;
    }

    public Long getCargoRequestId() {
        return cargoRequestId;
    }

    public void setCargoRequestId(Long cargoRequestId) {
        this.cargoRequestId = cargoRequestId;
    }

    public Flight getFlight() {
        return this.flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Ask flight(Flight flight) {
        this.setFlight(flight);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ask)) {
            return false;
        }
        return id != null && id.equals(((Ask) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ask{" +
            "id=" + getId() +
            ", notes='" + getNotes() + "'" +
            ", price=" + getPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
