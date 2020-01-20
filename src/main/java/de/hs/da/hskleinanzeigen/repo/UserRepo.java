package de.hs.da.hskleinanzeigen.repo;


import de.hs.da.hskleinanzeigen.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepo extends PagingAndSortingRepository  <User, Integer>{
    Optional<Collection<User>> findByEmail(String email);

}
