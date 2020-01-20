package de.hs.da.hskleinanzeigen.entity;

import com.fasterxml.jackson.annotation.*;
import de.hs.da.hskleinanzeigen.entity.json.EntityIdResolver;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(scope=Category.class, generator = ObjectIdGenerators.PropertyGenerator.class, resolver = EntityIdResolver.class, property = "id")
@Entity
@Table(name = "CATEGORY")
public class Category {
    @Id
    @JsonProperty(value = "id", required = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Integer id;

    @OneToMany(mappedBy = "category")
    private List<Advertisement> listAdvertisement = new ArrayList<>();

    @JsonProperty("name")
    @Column(name="NAME")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "parentCategory")
    private List<Category> listSubCategories = new ArrayList<>();

    @JsonProperty(value = "parentId")
    @ManyToOne
    @JoinColumn(name="PARENT_ID")
    private Category parentCategory;

    public Category() {}
    public Category(Integer id) {
        this.id = id;
    }

    public Category(Integer id, String name, Category parentCategory) {
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
    }

    public Category(Integer id, List<Advertisement> listAdvertisement, String name, List<Category> listSubCategories, Category parentCategory) {
        this.id = id;
        this.listAdvertisement = listAdvertisement;
        this.name = name;
        this.listSubCategories = listSubCategories;
        this.parentCategory = parentCategory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public List<Advertisement> getListAdvertisement() {
        return listAdvertisement;
    }

    public void setListAdvertisement(List<Advertisement> listAdvertisement) {
        this.listAdvertisement = listAdvertisement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public List<Category> getListSubCategories() {
        return listSubCategories;
    }

    public void setListSubCategories(List<Category> listSubCategories) {
        this.listSubCategories = listSubCategories;
    }

    @JsonIgnore
    public Category getParentCategory() {
        return parentCategory;
    }

    @JsonProperty
    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override
    public String toString() {
        if(parentCategory != null)
            return "Category{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", parentCategory=" + parentCategory +
                    '}';
        else
            return "Category{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id) &&
                Objects.equals(name, category.name) &&
                Objects.equals(parentCategory, category.parentCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentCategory);
    }
}
