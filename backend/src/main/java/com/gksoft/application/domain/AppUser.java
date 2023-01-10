package com.gksoft.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gksoft.application.domain.enumeration.GenderType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "birth_date")
    private Instant birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;

    @Column(name = "register_date")
    private Instant registerDate;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cargoRequest", "appUser" }, allowSetters = true)
    private Set<UserRate> userRates = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUsers" }, allowSetters = true)
    private Country country;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUsers" }, allowSetters = true)
    private StateProvince stateProvince;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUsers" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getBirthDate() {
        return this.birthDate;
    }

    public AppUser birthDate(Instant birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public GenderType getGender() {
        return this.gender;
    }

    public AppUser gender(GenderType gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public Instant getRegisterDate() {
        return this.registerDate;
    }

    public AppUser registerDate(Instant registerDate) {
        this.setRegisterDate(registerDate);
        return this;
    }

    public void setRegisterDate(Instant registerDate) {
        this.registerDate = registerDate;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public AppUser phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public AppUser mobileNumber(String mobileNumber) {
        this.setMobileNumber(mobileNumber);
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AppUser user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<UserRate> getUserRates() {
        return this.userRates;
    }

    public void setUserRates(Set<UserRate> userRates) {
        if (this.userRates != null) {
            this.userRates.forEach(i -> i.setAppUser(null));
        }
        if (userRates != null) {
            userRates.forEach(i -> i.setAppUser(this));
        }
        this.userRates = userRates;
    }

    public AppUser userRates(Set<UserRate> userRates) {
        this.setUserRates(userRates);
        return this;
    }

    public AppUser addUserRate(UserRate userRate) {
        this.userRates.add(userRate);
        userRate.setAppUser(this);
        return this;
    }

    public AppUser removeUserRate(UserRate userRate) {
        this.userRates.remove(userRate);
        userRate.setAppUser(null);
        return this;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public AppUser country(Country country) {
        this.setCountry(country);
        return this;
    }

    public StateProvince getStateProvince() {
        return this.stateProvince;
    }

    public void setStateProvince(StateProvince stateProvince) {
        this.stateProvince = stateProvince;
    }

    public AppUser stateProvince(StateProvince stateProvince) {
        this.setStateProvince(stateProvince);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public AppUser city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return id != null && id.equals(((AppUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", birthDate='" + getBirthDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", registerDate='" + getRegisterDate() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", mobileNumber='" + getMobileNumber() + "'" +
            "}";
    }
}
