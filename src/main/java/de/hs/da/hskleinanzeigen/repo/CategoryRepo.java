package de.hs.da.hskleinanzeigen.repo;

import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface CategoryRepo extends CrudRepository<Category, Integer> {
    //Optional<Collection<Category>> findByID(int categoryID );
    Optional<Category> findByName(String name );

}
