package com.gksoft.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StateProvince.
 */
@Entity
@Table(name = "state_province")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StateProvince implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "country_id")
    private Long countryId;


    @OneToMany(mappedBy = "stateProvince")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "userRates", "country", "stateProvince", "city" }, allowSetters = true)
    private Set<User> users = new HashSet<>();





    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StateProvince id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public StateProvince name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> appUsers) {
        if (this.users != null) {
            this.users.forEach(i -> i.setStateProvince(null));
        }
        if (appUsers != null) {
            appUsers.forEach(i -> i.setStateProvince(this));
        }
        this.users = appUsers;
    }

    public StateProvince  Users(Set<User> appUsers) {
        this.setUsers(appUsers);
        return this;
    }

    public StateProvince addUser(User appUser) {
        this.users.add(appUser);
        appUser.setStateProvince(this);
        return this;
    }

    public StateProvince removeUser(User appUser) {
        this.users.remove(appUser);
        appUser.setStateProvince(null);
        return this;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StateProvince)) {
            return false;
        }
        return id != null && id.equals(((StateProvince) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StateProvince{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
