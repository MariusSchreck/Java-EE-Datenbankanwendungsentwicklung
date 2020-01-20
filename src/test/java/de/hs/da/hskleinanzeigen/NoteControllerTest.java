package de.hs.da.hskleinanzeigen;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import de.hs.da.hskleinanzeigen.entity.Advertisement;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.Note;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.repo.AdvertisementRepo;
import de.hs.da.hskleinanzeigen.repo.CategoryRepo;
import de.hs.da.hskleinanzeigen.repo.NoteRepo;
import de.hs.da.hskleinanzeigen.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AdvertisementController.class)
@WebAppConfiguration
@AutoConfigureMockMvc(addFilters = false)
class NoteControllerTest {

    @MockBean
    UserRepo Userrepo;
    @MockBean
    CategoryRepo Catrepo;
    @MockBean
    NoteRepo Noterepo;
    @MockBean
    AdvertisementRepo Adrepo;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;


    Note note = new Note();
    User user = new User(50);
    Advertisement ad = new Advertisement(50);
    final ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        note.setId(1);
        note.setNote("TestNote");
        note.setCreated(timestamp);


        note.setUser(user);
        //given(Userrepo.findById(user.getId())).willReturn(Optional.of(user));

        note.setAd(ad);
        //given(Adrepo.findById(ad.getId())).willReturn(java.util.Optional.of(ad));

        List<Note> notes = new ArrayList<>();
        notes.add(note);
        user.setNotes(notes);
    }

    /**
     * Test zum abfragen eines Merkzettels eines Benutzers
     * Merkzettel wird gefunden
     * Response Verarbeitung erfolgreich
     * HTTP Status: 200
     */
    @Test   // find Note
    void findNoteTest() throws Exception {

        given(Userrepo.findById(note.getUser().getId())).willReturn(Optional.of(user));
        mvc.perform(MockMvcRequestBuilders
                .get("/api/users/{userId}/notepad/", user.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Test zum abfragen eines Merkzettels eines Benutzers
     * Merkzettel wird nicht gefunden
     * Response Keine Einträge gefunden
     * HTTP Status: 204
     */
    @Test   // find Note
    void noteNotFoundTest() throws Exception {
        note.setAd(null);
        given(Userrepo.findById(note.getUser().getId())).willThrow(new ResponseStatusException(HttpStatus.NO_CONTENT));
        mvc.perform(MockMvcRequestBuilders
                .get("/api/users/{userId}/notepad/", user.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    /**
     * Test zum abfragen eines Merkzettels eines Benutzers
     * Benutzer wird nicht gefunden
     * Response Fehler: Benutzer nicht vorhanden
     * HTTP Status: 404
     */
    @Test   // find Note
    void findNoteWithUnknownIdTest() throws Exception {

        given(Userrepo.findById(note.getUser().getId())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mvc.perform(MockMvcRequestBuilders
                .get("/api/users/{userId}/notepad/", user.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    /**
     * Test zum hinzufügen eines Inserates zum Merkzettel
     * Inserat wird dem Merkzettel hinzugefügt
     * Response Verarbeitung erfolgreich
     * HTTP Status: 201
     */
    /*
    @Test
    void insertAdvertisementToNote() throws Exception{
        String jsonString = "{\"advertisementId\":50, \"note\":\"Zimmer direkt bei der HS\"}";

        given(Adrepo.findById(note.getAd().getId())).willReturn(Optional.of(ad));
        given(Userrepo.findById(note.getUser().getId())).willReturn(Optional.of(user));
        given(Noterepo.findById(note.getId())).willReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders
                .post("/api/users/{userId}/notepad/", note.getUser().getId())
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    */
    
    @Test
    void findNoteWithPayloadTest() throws Exception {

        given(Userrepo.findById(note.getUser().getId())).willReturn(Optional.of(user));


        mvc.perform(get("/api/users/{userId}/notepad/", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].note").isNotEmpty())
                .andExpect(jsonPath("$[0].created").isNotEmpty())
                .andExpect(jsonPath("$[0].user").isNotEmpty())
                .andExpect(jsonPath("$[0].advertisement").isNotEmpty()
                );
    }

    @Test
    void deleteNote() throws Exception {

        given(Userrepo.findById(note.getUser().getId())).willReturn(Optional.of(user));
        given(Adrepo.findById(note.getAd().getId())).willReturn(Optional.of(ad));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/users/{userId}/notepad/{adId}", user.getId(), ad.getId())).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(204, status);
    }
}
