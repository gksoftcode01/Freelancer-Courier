package com.gksoft.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ItemTypes.
 */
@Entity
@Table(name = "item_types")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "availableItemTypes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "createBy", "fromCountry", "toCountry", "fromState", "toState", "fromCity", "toCity", "availableItemTypes" },
        allowSetters = true
    )
    private Set<Flight> flights = new HashSet<>();

    @ManyToMany(mappedBy = "reqItemTypes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<CargoRequest> cargoRequests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ItemTypes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ItemTypes name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Flight> getFlights() {
        return this.flights;
    }

    public void setFlights(Set<Flight> flights) {
        if (this.flights != null) {
            this.flights.forEach(i -> i.removeAvailableItemTypes(this));
        }
        if (flights != null) {
            flights.forEach(i -> i.addAvailableItemTypes(this));
        }
        this.flights = flights;
    }

    public ItemTypes flights(Set<Flight> flights) {
        this.setFlights(flights);
        return this;
    }

    public ItemTypes addFlight(Flight flight) {
        this.flights.add(flight);
        flight.getAvailableItemTypes().add(this);
        return this;
    }

    public ItemTypes removeFlight(Flight flight) {
        this.flights.remove(flight);
        flight.getAvailableItemTypes().remove(this);
        return this;
    }

    public Set<CargoRequest> getCargoRequests() {
        return this.cargoRequests;
    }

    public void setCargoRequests(Set<CargoRequest> cargoRequests) {
        if (this.cargoRequests != null) {
            this.cargoRequests.forEach(i -> i.removeReqItemTypes(this));
        }
        if (cargoRequests != null) {
            cargoRequests.forEach(i -> i.addReqItemTypes(this));
        }
        this.cargoRequests = cargoRequests;
    }

    public ItemTypes cargoRequests(Set<CargoRequest> cargoRequests) {
        this.setCargoRequests(cargoRequests);
        return this;
    }

    public ItemTypes addCargoRequest(CargoRequest cargoRequest) {
        this.cargoRequests.add(cargoRequest);
        cargoRequest.getReqItemTypes().add(this);
        return this;
    }

    public ItemTypes removeCargoRequest(CargoRequest cargoRequest) {
        this.cargoRequests.remove(cargoRequest);
        cargoRequest.getReqItemTypes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemTypes)) {
            return false;
        }
        return id != null && id.equals(((ItemTypes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemTypes{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
