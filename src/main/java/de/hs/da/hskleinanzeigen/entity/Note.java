package de.hs.da.hskleinanzeigen.entity;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "NOTEPAD")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    @JsonProperty(value="id", required = true)
    private Integer id;

    @JsonProperty("note")
    @Column(name="NOTE")
    private String note;

    @NotNull
    @JsonProperty(value="created", required = true)
    @Column(name="CREATED", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created;

    @NotNull
    @JsonProperty(value="user")
    @ManyToOne(optional = false)
    @JoinColumn(name="USER_ID")
    private User user;

    @NotNull
    @JsonProperty(value = "advertisement", required = true)
    @ManyToOne(optional = false)
    @JoinColumn(name="AD_ID")
    private Advertisement ad;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnoreProperties({"user"})
    public Advertisement getAd() {
        return ad;
    }

    public void setAd(Advertisement ad) {
        this.ad = ad;
    }

    public Note() {
    }

    public Note(int id) {
        this.id = id;
    }

    public Note(int id, String note, @NotNull Timestamp created, @NotNull User user, @NotNull Advertisement ad) {
        this.id = id;
        this.note = note;
        this.created = created;
        this.user = user;
        this.ad = ad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note1 = (Note) o;
        return id == note1.id &&
                Objects.equals(note, note1.note) &&
                created.equals(note1.created) &&
                user.equals(note1.user) &&
                ad.equals(note1.ad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, note, user, ad);
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", created=" + created +
                '}';
    }
}
