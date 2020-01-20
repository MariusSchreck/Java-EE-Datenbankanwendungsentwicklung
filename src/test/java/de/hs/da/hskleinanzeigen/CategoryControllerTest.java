package de.hs.da.hskleinanzeigen;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hs.da.hskleinanzeigen.entity.Advertisement;
import de.hs.da.hskleinanzeigen.entity.Category;
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

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AdvertisementController.class)
@WebAppConfiguration
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {
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
    Category category = new Category();

    @BeforeEach
    void setUp() {
        category.setId(1);
        category.setParentCategory(category);
        category.setName("TestCategory");

        //given(Catrepo.findById(category.getId())).willReturn(Optional.of(category);
    }

    /**
     * Test zum erstellen einer neuen Kategorie
     * Kategorie wird erstellt
     * Response Verarbeitung erfolgreich
     * HTTP Status: 201
     */
    @Test   // New Category
    void insertCategoryTest() throws Exception {
        given(Catrepo.findById(category.getParentCategory().getId())).willReturn(Optional.of(category));
        mvc.perform(MockMvcRequestBuilders.post("/api/categories/")
                .content(mapper.writeValueAsString(category))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    /**
     * Test zum erstellen einer neuen Kategorie
     * Kategorie wird nicht erstellt
     * Response Fehler: Payload unvollständigoder Über-kategorie nicht vorhanden
     * HTTP Status: 400
     */
    @Test   // new Category
    void insertCategoryWithInvalidPayload() throws Exception {
        category.setName(null);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/categories/")
                .content(mapper.writeValueAsString(category))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(400);
    }

    @Test   // find Category
    void findCategoryTest() throws Exception {
        given(Catrepo.findById(category.getId())).willReturn(Optional.of(category));
            mvc.perform(MockMvcRequestBuilders
                    .get("/api/categories/{id}", category.getId())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

    @Test   // find Category
    void findCategoryWithUnknownIdTest() throws Exception {
        given(Catrepo.findById(category.getId())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mvc.perform(MockMvcRequestBuilders
                .get("/api/categories/{id}", category.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test   // find Category
    void findCategoryWithPayloadTest() throws Exception {
        given(Catrepo.findById(category.getId())).willReturn(Optional.of(category));
        mvc.perform(get("/api/categories/" + category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").isNotEmpty()
                );
    }
}