package de.hs.da.hskleinanzeigen;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import de.hs.da.hskleinanzeigen.entity.Advertisement;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.Note;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.repo.AdvertisementRepo;
import de.hs.da.hskleinanzeigen.repo.CategoryRepo;
import de.hs.da.hskleinanzeigen.repo.NoteRepo;
import de.hs.da.hskleinanzeigen.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

@RestController()
@RequestMapping(path = "/api/users/{userId}/notepad/")
public class NoteController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class UserNotFoundException extends Exception{}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public class NotesNotFoundException extends Exception{}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public class PayloadException extends Exception{}

    @ResponseStatus(HttpStatus.CONFLICT)
    public class DuplicateNoteException extends Exception{}


    @Autowired
    private NoteRepo noteRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AdvertisementRepo adRepo;

    @GetMapping(path = "/", produces = {"application/json"})
    Collection<Note> findNotes(@PathVariable("userId") Integer userId) throws UserNotFoundException, NotesNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty())
            throw new UserNotFoundException();

        Collection<Note> notes = user.get().getNotes();
        if(notes.isEmpty())
            throw new NotesNotFoundException();

        return notes;
    }

    static class NoteCreation{
        @JsonProperty(value = "advertisementId", required = true)
        public Integer adId;
        @JsonProperty(value = "note", required = true)
        public String note;
    }

    static class NoteCreationResult extends  NoteCreation{
        @JsonProperty(value = "id", required = true)
        public Integer id;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/", consumes = {"application/json"}, produces = {"application/json"})
    NoteCreationResult newNote(@PathVariable("userId") Integer userId, @RequestBody() NoteCreation noteToCreate) throws PayloadException, DuplicateNoteException {
        // Payload unvollständig
        if(noteToCreate == null || noteToCreate.adId == null || noteToCreate.note == null){
            // 400
            throw new PayloadException();
        }
        Optional<User> user = userRepo.findById(userId);
        Optional<Advertisement> ad = adRepo.findById(noteToCreate.adId);
        if(user.isEmpty() || ad.isEmpty())
            throw new PayloadException();
        Note note = new Note();
        note.setNote(noteToCreate.note);
        note.setUser(user.get());
        note.setAd(ad.get());
        Optional<Note> tmpNote = noteRepo.findNoteByAdAndUser(note.getAd(), note.getUser());
        if((noteRepo.findNoteByAdAndUser(note.getAd(), note.getUser())).isPresent()){
            // 409
            throw new DuplicateNoteException();
        }

        note.setCreated(new Timestamp(System.currentTimeMillis()));
        note = noteRepo.save(note);

        NoteCreationResult result = new NoteCreationResult();
        result.adId = noteToCreate.adId;
        result.note = noteToCreate.note;
        result.id = note.getId();
        return result;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}", produces = {"application/json"})
    public void deleteById(@PathVariable("id") Integer id) {
        noteRepo.deleteById(id);
    }

}
