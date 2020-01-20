package de.hs.da.hskleinanzeigen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Offer")
public class Offer extends Advertisement {

    public Offer() {
    }
    @JsonProperty("price")
    @Column(name="PRICE")
    private Integer price;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
