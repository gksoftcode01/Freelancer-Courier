package com.gksoft.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CargoRequest.
 */
@Entity
@Table(name = "cargo_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CargoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "budget")
    private Long budget;

    @Column(name = "is_to_door")
    private Boolean isToDoor;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "agreed_price")
    private Long agreedPrice;

    @OneToMany(mappedBy = "cargoRequest")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cargoRequest" }, allowSetters = true)
    private Set<CargoRequestDetails> cargoRequestDetails = new HashSet<>();

    @OneToMany(mappedBy = "cargoRequest")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "toUser", "cargoRequest" }, allowSetters = true)
    private Set<Ask> asks = new HashSet<>();

    @OneToMany(mappedBy = "cargoRequest")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fromUser", "cargoRequest" }, allowSetters = true)
    private Set<Bid> bids = new HashSet<>();

    @ManyToOne
    private CargoRequestStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "userRates", "country", "stateProvince", "country" }, allowSetters = true)
    private AppUser createBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "userRates", "country", "stateProvince", "country" }, allowSetters = true)
    private AppUser takenBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUsers" }, allowSetters = true)
    private Country fromCountry;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUsers" }, allowSetters = true)
    private Country toCountry;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUsers" }, allowSetters = true)
    private StateProvince fromState;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUsers" }, allowSetters = true)
    private StateProvince toState;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUsers" }, allowSetters = true)
    private City fromCity;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUsers" }, allowSetters = true)
    private City toCity;

    @ManyToMany
    @JoinTable(
        name = "rel_cargo_request__req_item_types",
        joinColumns = @JoinColumn(name = "cargo_request_id"),
        inverseJoinColumns = @JoinColumn(name = "req_item_types_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "flights", "cargoRequests" }, allowSetters = true)
    private Set<ItemTypes> reqItemTypes = new HashSet<>();

    @JsonIgnoreProperties(value = { "cargoRequest", "appUser" }, allowSetters = true)
    @OneToOne(mappedBy = "cargoRequest")
    private UserRate userRate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CargoRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBudget() {
        return this.budget;
    }

    public CargoRequest budget(Long budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public Boolean getIsToDoor() {
        return this.isToDoor;
    }

    public CargoRequest isToDoor(Boolean isToDoor) {
        this.setIsToDoor(isToDoor);
        return this;
    }

    public void setIsToDoor(Boolean isToDoor) {
        this.isToDoor = isToDoor;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public CargoRequest createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Long getAgreedPrice() {
        return this.agreedPrice;
    }

    public CargoRequest agreedPrice(Long agreedPrice) {
        this.setAgreedPrice(agreedPrice);
        return this;
    }

    public void setAgreedPrice(Long agreedPrice) {
        this.agreedPrice = agreedPrice;
    }

    public Set<CargoRequestDetails> getCargoRequestDetails() {
        return this.cargoRequestDetails;
    }

    public void setCargoRequestDetails(Set<CargoRequestDetails> cargoRequestDetails) {
        if (this.cargoRequestDetails != null) {
            this.cargoRequestDetails.forEach(i -> i.setCargoRequest(null));
        }
        if (cargoRequestDetails != null) {
            cargoRequestDetails.forEach(i -> i.setCargoRequest(this));
        }
        this.cargoRequestDetails = cargoRequestDetails;
    }

    public CargoRequest cargoRequestDetails(Set<CargoRequestDetails> cargoRequestDetails) {
        this.setCargoRequestDetails(cargoRequestDetails);
        return this;
    }

    public CargoRequest addCargoRequestDetails(CargoRequestDetails cargoRequestDetails) {
        this.cargoRequestDetails.add(cargoRequestDetails);
        cargoRequestDetails.setCargoRequest(this);
        return this;
    }

    public CargoRequest removeCargoRequestDetails(CargoRequestDetails cargoRequestDetails) {
        this.cargoRequestDetails.remove(cargoRequestDetails);
        cargoRequestDetails.setCargoRequest(null);
        return this;
    }

    public Set<Ask> getAsks() {
        return this.asks;
    }

    public void setAsks(Set<Ask> asks) {
        if (this.asks != null) {
            this.asks.forEach(i -> i.setCargoRequest(null));
        }
        if (asks != null) {
            asks.forEach(i -> i.setCargoRequest(this));
        }
        this.asks = asks;
    }

    public CargoRequest asks(Set<Ask> asks) {
        this.setAsks(asks);
        return this;
    }

    public CargoRequest addAsk(Ask ask) {
        this.asks.add(ask);
        ask.setCargoRequest(this);
        return this;
    }

    public CargoRequest removeAsk(Ask ask) {
        this.asks.remove(ask);
        ask.setCargoRequest(null);
        return this;
    }

    public Set<Bid> getBids() {
        return this.bids;
    }

    public void setBids(Set<Bid> bids) {
        if (this.bids != null) {
            this.bids.forEach(i -> i.setCargoRequest(null));
        }
        if (bids != null) {
            bids.forEach(i -> i.setCargoRequest(this));
        }
        this.bids = bids;
    }

    public CargoRequest bids(Set<Bid> bids) {
        this.setBids(bids);
        return this;
    }

    public CargoRequest addBid(Bid bid) {
        this.bids.add(bid);
        bid.setCargoRequest(this);
        return this;
    }

    public CargoRequest removeBid(Bid bid) {
        this.bids.remove(bid);
        bid.setCargoRequest(null);
        return this;
    }

    public CargoRequestStatus getStatus() {
        return this.status;
    }

    public void setStatus(CargoRequestStatus cargoRequestStatus) {
        this.status = cargoRequestStatus;
    }

    public CargoRequest status(CargoRequestStatus cargoRequestStatus) {
        this.setStatus(cargoRequestStatus);
        return this;
    }

    public AppUser getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(AppUser appUser) {
        this.createBy = appUser;
    }

    public CargoRequest createBy(AppUser appUser) {
        this.setCreateBy(appUser);
        return this;
    }

    public AppUser getTakenBy() {
        return this.takenBy;
    }

    public void setTakenBy(AppUser appUser) {
        this.takenBy = appUser;
    }

    public CargoRequest takenBy(AppUser appUser) {
        this.setTakenBy(appUser);
        return this;
    }

    public Country getFromCountry() {
        return this.fromCountry;
    }

    public void setFromCountry(Country country) {
        this.fromCountry = country;
    }

    public CargoRequest fromCountry(Country country) {
        this.setFromCountry(country);
        return this;
    }

    public Country getToCountry() {
        return this.toCountry;
    }

    public void setToCountry(Country country) {
        this.toCountry = country;
    }

    public CargoRequest toCountry(Country country) {
        this.setToCountry(country);
        return this;
    }

    public StateProvince getFromState() {
        return this.fromState;
    }

    public void setFromState(StateProvince stateProvince) {
        this.fromState = stateProvince;
    }

    public CargoRequest fromState(StateProvince stateProvince) {
        this.setFromState(stateProvince);
        return this;
    }

    public StateProvince getToState() {
        return this.toState;
    }

    public void setToState(StateProvince stateProvince) {
        this.toState = stateProvince;
    }

    public CargoRequest toState(StateProvince stateProvince) {
        this.setToState(stateProvince);
        return this;
    }

    public City getFromCity() {
        return this.fromCity;
    }

    public void setFromCity(City city) {
        this.fromCity = city;
    }

    public CargoRequest fromCity(City city) {
        this.setFromCity(city);
        return this;
    }

    public City getToCity() {
        return this.toCity;
    }

    public void setToCity(City city) {
        this.toCity = city;
    }

    public CargoRequest toCity(City city) {
        this.setToCity(city);
        return this;
    }

    public Set<ItemTypes> getReqItemTypes() {
        return this.reqItemTypes;
    }

    public void setReqItemTypes(Set<ItemTypes> itemTypes) {
        this.reqItemTypes = itemTypes;
    }

    public CargoRequest reqItemTypes(Set<ItemTypes> itemTypes) {
        this.setReqItemTypes(itemTypes);
        return this;
    }

    public CargoRequest addReqItemTypes(ItemTypes itemTypes) {
        this.reqItemTypes.add(itemTypes);
        itemTypes.getCargoRequests().add(this);
        return this;
    }

    public CargoRequest removeReqItemTypes(ItemTypes itemTypes) {
        this.reqItemTypes.remove(itemTypes);
        itemTypes.getCargoRequests().remove(this);
        return this;
    }

    public UserRate getUserRate() {
        return this.userRate;
    }

    public void setUserRate(UserRate userRate) {
        if (this.userRate != null) {
            this.userRate.setCargoRequest(null);
        }
        if (userRate != null) {
            userRate.setCargoRequest(this);
        }
        this.userRate = userRate;
    }

    public CargoRequest userRate(UserRate userRate) {
        this.setUserRate(userRate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CargoRequest)) {
            return false;
        }
        return id != null && id.equals(((CargoRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoRequest{" +
            "id=" + getId() +
            ", budget=" + getBudget() +
            ", isToDoor='" + getIsToDoor() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", agreedPrice=" + getAgreedPrice() +
            "}";
    }
}
