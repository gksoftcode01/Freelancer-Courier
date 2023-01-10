package com.gksoft.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserRate.
 */
@Entity
@Table(name = "user_rate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "rate")
    private Long rate;

    @Column(name = "note")
    private String note;

    @Column(name = "rate_date")
    private Instant rateDate;

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
    @OneToOne
    @JoinColumn(unique = true)
    private CargoRequest cargoRequest;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "userRates", "country", "stateProvince", "country" }, allowSetters = true)
    private AppUser appUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserRate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRate() {
        return this.rate;
    }

    public UserRate rate(Long rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public String getNote() {
        return this.note;
    }

    public UserRate note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Instant getRateDate() {
        return this.rateDate;
    }

    public UserRate rateDate(Instant rateDate) {
        this.setRateDate(rateDate);
        return this;
    }

    public void setRateDate(Instant rateDate) {
        this.rateDate = rateDate;
    }

    public CargoRequest getCargoRequest() {
        return this.cargoRequest;
    }

    public void setCargoRequest(CargoRequest cargoRequest) {
        this.cargoRequest = cargoRequest;
    }

    public UserRate cargoRequest(CargoRequest cargoRequest) {
        this.setCargoRequest(cargoRequest);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public UserRate appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRate)) {
            return false;
        }
        return id != null && id.equals(((UserRate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRate{" +
            "id=" + getId() +
            ", rate=" + getRate() +
            ", note='" + getNote() + "'" +
            ", rateDate='" + getRateDate() + "'" +
            "}";
    }
}
