package de.hs.da.hskleinanzeigen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

@Entity
@DiscriminatorValue("Request")
public class Request extends Advertisement{
    public Request() {
    }
}
