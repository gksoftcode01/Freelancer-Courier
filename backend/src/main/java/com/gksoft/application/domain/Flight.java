package com.gksoft.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gksoft.application.domain.enumeration.FlightStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Flight.
 */
@Entity
@Table(name = "flight")
 @SuppressWarnings("common-java:DuplicatedBlocks")
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "flight_date")
    private Instant flightDate;

    @Column(name = "max_weight")
    private Long maxWeight;

    @Column(name = "notes")
    private String notes;

    @Column(name = "budget")
    private Long budget;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "to_door_available")
    private Boolean toDoorAvailable;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FlightStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "userRates", "country", "stateProvince", "city" }, allowSetters = true)
    private User createBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "Users" }, allowSetters = true)
    private Country fromCountry;

    @ManyToOne
    @JsonIgnoreProperties(value = { "Users" }, allowSetters = true)
    private Country toCountry;

    @ManyToOne
    @JsonIgnoreProperties(value = { "Users" }, allowSetters = true)
    private StateProvince fromState;

    @ManyToOne
    @JsonIgnoreProperties(value = { "Users" }, allowSetters = true)
    private StateProvince toState;

    @ManyToOne
    @JsonIgnoreProperties(value = { "Users" }, allowSetters = true)
    private City fromCity;

    @ManyToOne
    @JsonIgnoreProperties(value = { "Users" }, allowSetters = true)
    private City toCity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rel_flight__available_item_types",
        joinColumns = @JoinColumn(name = "flight_id"),
        inverseJoinColumns = @JoinColumn(name = "available_item_types_id")
    )
     @JsonIgnoreProperties(value = { "flights", "cargoRequests" }, allowSetters = true)
    private Set<ItemTypes> availableItemTypes = new HashSet<>();


    @OneToMany(mappedBy = "flight",fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "toUser", "cargoRequest" }, allowSetters = true)
    private Set<Ask> asks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Flight id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFlightDate() {
        return this.flightDate;
    }

    public Flight flightDate(Instant flightDate) {
        this.setFlightDate(flightDate);
        return this;
    }

    public void setFlightDate(Instant flightDate) {
        this.flightDate = flightDate;
    }

    public Long getMaxWeight() {
        return this.maxWeight;
    }

    public Flight maxWeight(Long maxWeight) {
        this.setMaxWeight(maxWeight);
        return this;
    }

    public void setMaxWeight(Long maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getNotes() {
        return this.notes;
    }

    public Flight notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getBudget() {
        return this.budget;
    }

    public Flight budget(Long budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Flight createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Boolean getToDoorAvailable() {
        return this.toDoorAvailable;
    }

    public Flight toDoorAvailable(Boolean toDoorAvailable) {
        this.setToDoorAvailable(toDoorAvailable);
        return this;
    }

    public void setToDoorAvailable(Boolean toDoorAvailable) {
        this.toDoorAvailable = toDoorAvailable;
    }

    public FlightStatus getStatus() {
        return this.status;
    }

    public Flight status(FlightStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public User getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(User User) {
        this.createBy = User;
    }

    public Flight createBy(User User) {
        this.setCreateBy(User);
        return this;
    }

    public Country getFromCountry() {
        return this.fromCountry;
    }

    public void setFromCountry(Country country) {
        this.fromCountry = country;
    }

    public Flight fromCountry(Country country) {
        this.setFromCountry(country);
        return this;
    }

    public Country getToCountry() {
        return this.toCountry;
    }

    public void setToCountry(Country country) {
        this.toCountry = country;
    }

    public Flight toCountry(Country country) {
        this.setToCountry(country);
        return this;
    }

    public StateProvince getFromState() {
        return this.fromState;
    }

    public void setFromState(StateProvince stateProvince) {
        this.fromState = stateProvince;
    }

    public Flight fromState(StateProvince stateProvince) {
        this.setFromState(stateProvince);
        return this;
    }

    public StateProvince getToState() {
        return this.toState;
    }

    public void setToState(StateProvince stateProvince) {
        this.toState = stateProvince;
    }

    public Flight toState(StateProvince stateProvince) {
        this.setToState(stateProvince);
        return this;
    }

    public City getFromCity() {
        return this.fromCity;
    }

    public void setFromCity(City city) {
        this.fromCity = city;
    }

    public Flight fromCity(City city) {
        this.setFromCity(city);
        return this;
    }

    public City getToCity() {
        return this.toCity;
    }

    public void setToCity(City city) {
        this.toCity = city;
    }

    public Flight toCity(City city) {
        this.setToCity(city);
        return this;
    }

    public Set<ItemTypes> getAvailableItemTypes() {
        return this.availableItemTypes;
    }

    public void setAvailableItemTypes(Set<ItemTypes> itemTypes) {
        this.availableItemTypes = itemTypes;
    }

    public Flight availableItemTypes(Set<ItemTypes> itemTypes) {
        this.setAvailableItemTypes(itemTypes);
        return this;
    }

    public Flight addAvailableItemTypes(ItemTypes itemTypes) {
        this.availableItemTypes.add(itemTypes);
        itemTypes.getFlights().add(this);
        return this;
    }

    public Flight removeAvailableItemTypes(ItemTypes itemTypes) {
        this.availableItemTypes.remove(itemTypes);
        itemTypes.getFlights().remove(this);
        return this;
    }
    public Set<Ask> getAsks() {
        return this.asks;
    }

    public void setAsks(Set<Ask> asks) {
        if (this.asks != null) {
            this.asks.forEach(i -> i.setFlight(null));
        }
        if (asks != null) {
            asks.forEach(i -> i.setFlight(this));
        }
        this.asks = asks;
    }

    public Flight asks(Set<Ask> asks) {
        this.setAsks(asks);
        return this;
    }

    public Flight addAsk(Ask ask) {
        this.asks.add(ask);
        ask.setFlight(this);
        return this;
    }

    public Flight removeAsk(Ask ask) {
        this.asks.remove(ask);
        ask.setFlight(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Flight)) {
            return false;
        }
        return id != null && id.equals(((Flight) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Flight{" +
            "id=" + getId() +
            ", flightDate='" + getFlightDate() + "'" +
            ", maxWeight=" + getMaxWeight() +
            ", notes='" + getNotes() + "'" +
            ", budget=" + getBudget() +
            ", createDate='" + getCreateDate() + "'" +
            ", toDoorAvailable='" + getToDoorAvailable() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
