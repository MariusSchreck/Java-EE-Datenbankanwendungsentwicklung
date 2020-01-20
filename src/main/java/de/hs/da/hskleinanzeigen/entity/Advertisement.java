package de.hs.da.hskleinanzeigen.entity;

import com.fasterxml.jackson.annotation.*;
import de.hs.da.hskleinanzeigen.entity.json.EntityIdResolver;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@JsonIdentityInfo(scope=Advertisement.class, generator = ObjectIdGenerators.PropertyGenerator.class, resolver = EntityIdResolver.class, property = "id")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@Table(name = "AD")
public class Advertisement {
    public enum Type {
        Offer,
        Request
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    @JsonProperty("id")
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonProperty("type")
    @Column(name="TYPE")
    private Type type;

    @NotNull
    @JsonProperty(value = "category", access = JsonProperty.Access.READ_WRITE)
    @JsonIdentityReference(alwaysAsId = false)
    @ManyToOne(optional = false)
    @JoinColumn(name="CATEGORY_ID")
    private Category category;

    @NotNull
    @JsonProperty("title")
    @Column(name="TITLE")
    private String title;

    @NotNull
    @JsonProperty("description")
    @Column(name="DESCRIPTION")
    private String description;

    @JsonProperty("price")
    @Column(name="PRICE")
    private Integer price;

    @JsonProperty("location")
    @Column(name="LOCATION")
    private String location;

    @NotNull
    @JsonProperty("created")
    @Column(name="CREATED", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created;

    @NotNull
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("user")
    @ManyToOne(optional = false)
    private User user;

    @JsonProperty(value = "notes", access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "ad", fetch = FetchType.LAZY)
    private List<Note> notes = new ArrayList<>();



    public Advertisement() {
    }

    public Advertisement(Integer id, @NotNull Type type, @NotNull Category category, @NotNull String title, @NotNull String description, int price, String location, @NotNull Timestamp created, @NotNull User user) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.created = created;
        this.user = user;
    }

    public Advertisement(Integer id) {
        this.id = id;
    }

    public Advertisement(Integer id, @NotNull Type type, @NotNull Category category, @NotNull String title, @NotNull String description, int price, String location, @NotNull Timestamp created, @NotNull User user, List<Note> notes) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.created = created;
        this.user = user;
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

     public String getLocation() {
        return location;
    }

    public Timestamp getCreated() {
        return created;
    }

    public User getUser() {
        return user;
    }

    @JsonIgnore
    public List<Note> getNotes() {
        return notes;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getPrice() {
        return price;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public void setLocation(String location) {
        this.location = location;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }


    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advertisement that = (Advertisement) o;
        System.out.println(((Advertisement) o).id);
        System.out.println(id);
        return id == that.id &&
                price == that.price &&
                type == that.type &&
                category.equals(that.category) &&
                title.equals(that.title) &&
                description.equals(that.description) &&
                Objects.equals(location, that.location) &&
                Objects.equals(created, that.created)&&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, category, title, description, price, location, created, user);
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "adID=" + id +
                ", adType=" + type +
                ", category=" + category +
                ", adTitle='" + title + '\'' +
                ", adDescription='" + description + '\'' +
                ", adPrice=" + price +
                ", adLocation='" + location + '\'' +
                ", adCreated=" + created +
                ", user=" + user +
                '}';
    }
}

