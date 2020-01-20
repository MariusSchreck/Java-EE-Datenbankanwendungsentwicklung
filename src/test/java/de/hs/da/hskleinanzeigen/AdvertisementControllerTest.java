package de.hs.da.hskleinanzeigen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.hs.da.hskleinanzeigen.entity.Advertisement;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.repo.AdvertisementRepo;
import de.hs.da.hskleinanzeigen.repo.CategoryRepo;
import de.hs.da.hskleinanzeigen.repo.NoteRepo;
import de.hs.da.hskleinanzeigen.repo.UserRepo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Timestamp;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(AdvertisementController.class)
@AutoConfigureMockMvc(addFilters = false)
@WebAppConfiguration
class AdvertisementControllerTest {
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

    Advertisement advertisement = new Advertisement();
    final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp(){
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Category testCategory = new Category(2);

        given(Catrepo.findById(testCategory.getId())).willReturn(Optional.of(testCategory));

        advertisement.setId(1);
        advertisement.setTitle("Title");
        advertisement.setCreated(timestamp);
        advertisement.setLocation("test-location");
        advertisement.setCategory(testCategory);
        advertisement.setDescription("test description");
        advertisement.setPrice(1);
        advertisement.setType(Advertisement.Type.Offer);

        User user = new User(50);
        advertisement.setUser(user);

        given(Userrepo.findById(user.getId())).willReturn(Optional.of(user));

    }

    /**
     * Test zum abfragen eines Inserates
     * Inserat wird gefunden
     * Response: Verarbeitung erfolgreich
     * HTTP Status: 200
     */
    @Test   // Advertisement findAdvertisement()
    void findAdvertisementTest() throws Exception {
        given(Adrepo.findById(advertisement.getId())).willReturn(java.util.Optional.of(advertisement));{
            mvc.perform(MockMvcRequestBuilders
                    .get("/api/advertisements/{id}", advertisement.getId())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    /**
     * Test zum abfragen eines Inserates
     * Inserat kann nicht gefunden werden
     * Response Fehler: Inserat nicht gefunden
     * HTTP Status: 404
     */
    @Test   // Advertisement findAdvertisement()
    void advertisementNotFoundTest() throws Exception {
        given(Adrepo.findById(advertisement.getId())).willReturn(java.util.Optional.empty());
        mvc.perform(MockMvcRequestBuilders
                .get("/api/advertisements/{id}", advertisement.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Test zum erstellen eines neuen Inserates
     * Daten sind Valide und Inserat sollte erstellt werden
     * Response: Verarbeitung erfolgreich
     * HTTP Status: 201
     */
    @Test   // Advertisement newAdvertisement()
    void insertAdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/advertisements/")
                .content(mapper.writeValueAsString(advertisement))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    /**
     * Test zum erstellen eines neuen Inserates
     * Daten sind Invalid und Inserat sollte nicht erstellt werden
     * Response: Fehler: Payload unvollständig
     * HTTP Status: 400
     */
    @Test  // Advertisement newAdvertisement()
    void insertAdWithInvalidPayload() throws Exception {
        advertisement.setTitle(null);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/advertisements/")
                .content(mapper.writeValueAsString(advertisement))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(400);
    }

    /**
     * Test zum abfragen mehrerer Inserate
     * Inserae werden gefunden
     * Response Verarbeitung erfolgreich
     * HTTP Status: 200
     */
    @Test   // Collection<Advertisement> findAdvertisements()
    void findAdvertisementWithPayloadTest() throws Exception {
        given(Adrepo.findById(advertisement.getId())).willReturn(Optional.of(advertisement));
        mvc.perform(get("/api/advertisements/" + advertisement.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("location").isNotEmpty())
                .andExpect(jsonPath("title").isNotEmpty())
                .andExpect(jsonPath("created").isNotEmpty())
                .andExpect(jsonPath("description").isNotEmpty())
                .andExpect(jsonPath("category").isNotEmpty())
                .andExpect(jsonPath("price").isNotEmpty())
                .andExpect(jsonPath("type").isNotEmpty())
                .andExpect(jsonPath("user").isNotEmpty()
                );
    }

    /**
     * Test zum abfragen mehrerer Inserate
     * Inserae werden nicht gefunden
     * Response Fehler: Inserat nicht gefunden
     * HTTP Status: 404
     */
    @Test   // Advertisement findAdvertisement()
    void findAdvertisementWithUnknownIdTest() throws Exception {
        given(Adrepo.findById(1)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mvc.perform(MockMvcRequestBuilders
                .get("/api/advertisements/{id}", advertisement.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}