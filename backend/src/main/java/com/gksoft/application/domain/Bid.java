package com.gksoft.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gksoft.application.domain.enumeration.BidAskStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bid.
 */
@Entity
@Table(name = "bid")
 @SuppressWarnings("common-java:DuplicatedBlocks")
public class Bid implements Serializable {

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
    @JsonIgnoreProperties(value = {   "country", "stateProvince", "country" }, allowSetters = true)
    private AppUser fromUser;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "cargoRequestDetails",
            "asks",
            "bids",
            "status",
            "createBy",
            "takenBy",
            "fromCountry",
            "toCountry",
            "fromState",
            "toState",
            "fromCity",
            "toCity",
            "reqItemTypes",
            "userRate",
        },
        allowSetters = true
    )
    private CargoRequest cargoRequest;

    @Column(name = "create_date")
    private Instant createDate;



    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Instant getCreateDate() {
        return this.createDate;
    }

    public Bid createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }
    public Long getId() {
        return this.id;
    }

    public Bid id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return this.notes;
    }

    public Bid notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getPrice() {
        return this.price;
    }

    public Bid price(Long price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public BidAskStatus getStatus() {
        return this.status;
    }

    public Bid status(BidAskStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(BidAskStatus status) {
        this.status = status;
    }

    public AppUser getFromUser() {
        return this.fromUser;
    }

    public void setFromUser(AppUser appUser) {
        this.fromUser = appUser;
    }

    public Bid fromUser(AppUser appUser) {
        this.setFromUser(appUser);
        return this;
    }

    public CargoRequest getCargoRequest() {
        return this.cargoRequest;
    }

    public void setCargoRequest(CargoRequest cargoRequest) {
        this.cargoRequest = cargoRequest;
    }

    public Bid cargoRequest(CargoRequest cargoRequest) {
        this.setCargoRequest(cargoRequest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bid)) {
            return false;
        }
        return id != null && id.equals(((Bid) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bid{" +
            "id=" + getId() +
            ", notes='" + getNotes() + "'" +
            ", price=" + getPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
