package de.hs.da.hskleinanzeigen.repo;


import de.hs.da.hskleinanzeigen.entity.Advertisement;
import de.hs.da.hskleinanzeigen.entity.Note;
//import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface NoteRepo extends CrudRepository<Note, Integer> {
    //Optional<Collection<Note>> findByUser(User user);
    //Optional<Collection<Note>> findByAd(User user);
    Optional<Note> findNoteByAdAndUser(Advertisement ad, User user);

}
