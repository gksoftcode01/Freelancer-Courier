package com.gksoft.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CargoRequestDetails.
 */
@Entity
@Table(name = "cargo_request_details")
 @SuppressWarnings("common-java:DuplicatedBlocks")
public class CargoRequestDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "item_desc")
    private String itemDesc;

    @Column(name = "item_weight")
    private Long itemWeight;

    @Column(name = "item_height")
    private Long itemHeight;

    @Column(name = "item_width")
    private Long itemWidth;

    @Lob
    @Column(name = "item_photo")
    private byte[] itemPhoto;

    @Column(name = "item_photo_content_type")
    private String itemPhotoContentType;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CargoRequestDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemDesc() {
        return this.itemDesc;
    }

    public CargoRequestDetails itemDesc(String itemDesc) {
        this.setItemDesc(itemDesc);
        return this;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Long getItemWeight() {
        return this.itemWeight;
    }

    public CargoRequestDetails itemWeight(Long itemWeight) {
        this.setItemWeight(itemWeight);
        return this;
    }

    public void setItemWeight(Long itemWeight) {
        this.itemWeight = itemWeight;
    }

    public Long getItemHeight() {
        return this.itemHeight;
    }

    public CargoRequestDetails itemHeight(Long itemHeight) {
        this.setItemHeight(itemHeight);
        return this;
    }

    public void setItemHeight(Long itemHeight) {
        this.itemHeight = itemHeight;
    }

    public Long getItemWidth() {
        return this.itemWidth;
    }

    public CargoRequestDetails itemWidth(Long itemWidth) {
        this.setItemWidth(itemWidth);
        return this;
    }

    public void setItemWidth(Long itemWidth) {
        this.itemWidth = itemWidth;
    }

    public byte[] getItemPhoto() {
        return this.itemPhoto;
    }

    public CargoRequestDetails itemPhoto(byte[] itemPhoto) {
        this.setItemPhoto(itemPhoto);
        return this;
    }

    public void setItemPhoto(byte[] itemPhoto) {
        this.itemPhoto = itemPhoto;
    }

    public String getItemPhotoContentType() {
        return this.itemPhotoContentType;
    }

    public CargoRequestDetails itemPhotoContentType(String itemPhotoContentType) {
        this.itemPhotoContentType = itemPhotoContentType;
        return this;
    }

    public void setItemPhotoContentType(String itemPhotoContentType) {
        this.itemPhotoContentType = itemPhotoContentType;
    }

    public CargoRequest getCargoRequest() {
        return this.cargoRequest;
    }

    public void setCargoRequest(CargoRequest cargoRequest) {
        this.cargoRequest = cargoRequest;
    }

    public CargoRequestDetails cargoRequest(CargoRequest cargoRequest) {
        this.setCargoRequest(cargoRequest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CargoRequestDetails)) {
            return false;
        }
        return id != null && id.equals(((CargoRequestDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CargoRequestDetails{" +
            "id=" + getId() +
            ", itemDesc='" + getItemDesc() + "'" +
            ", itemWeight=" + getItemWeight() +
            ", itemHeight=" + getItemHeight() +
            ", itemWidth=" + getItemWidth() +
            ", itemPhoto='" + getItemPhoto() + "'" +
            ", itemPhotoContentType='" + getItemPhotoContentType() + "'" +
            "}";
    }
}
