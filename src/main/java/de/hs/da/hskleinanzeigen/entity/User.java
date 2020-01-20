package de.hs.da.hskleinanzeigen.entity;

import com.fasterxml.jackson.annotation.*;
import de.hs.da.hskleinanzeigen.entity.json.EntityIdResolver;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(scope=User.class, generator = ObjectIdGenerators.PropertyGenerator.class, resolver = EntityIdResolver.class, property = "id")
@Entity
@Table(name = "USER")
public class User {
    @Id
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Integer id;

    @JsonProperty("created")
    @Column(name="CREATED",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created;

    @JsonProperty("email")
    @NotNull
    @Valid
    @Email(message = "Email should be valid")
    @Column(name="EMAIL")
    private String email;

    @Column(name="FIRST_NAME")
    @Size(max=255)
    @JsonProperty("firstName")
    private String firstName;

    @Column(name="LAST_NAME")
    @Size(max=255)
    @JsonProperty("lastName")
    private String lastName;

    @Column(name="LOCATION")
    @JsonProperty("location")
    private String location;

    @NotNull (message = "Passwort darf nicht leer sein!")
    @Size(min=6)
    @Column(name="PASSWORD")
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty("phone")
    @Column(name="PHONE")
    private String phone;

    @JsonProperty(value="advertisements", access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Advertisement> ads = new ArrayList<>();;

    /*
     alternatively an elementcollection & embeddable string,
     mapping ad to string note
     mapping on existing (!) table NOTEPAD
     */

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonProperty("notes")
    private List<Note> notes = new ArrayList<>();;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonIgnore
    public List<Advertisement> getAds() {
        return ads;
    }

    public void setAds(List<Advertisement> ads) {
        this.ads = ads;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public List<Note> getNotes() {
        return notes;
    }

    @JsonProperty
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(@NotNull Integer id, Timestamp created, String email, String firstName, String lastName, String location, @NotNull String password, String phone) {
        this.id = id;
        this.created = created;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.password = password;
        this.phone = phone;
    }


    public User(@NotNull Integer id, @NotNull Timestamp created, @NotNull String email, String firstName, String lastName, String location, @NotNull String password, String phone, List<Note> notes) {
        this.id = id;
        this.created = created;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.password = password;
        this.phone = phone;
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                created.equals(user.created) &&
                email.equals(user.email) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(location, user.location) &&
                password.equals(user.password) &&
                Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", created=" + created +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", location='" + location + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", notes=" + notes +
                '}';
    }
}