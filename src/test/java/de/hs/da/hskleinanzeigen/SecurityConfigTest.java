package de.hs.da.hskleinanzeigen;

import de.hs.da.hskleinanzeigen.repo.AdvertisementRepo;
import de.hs.da.hskleinanzeigen.repo.CategoryRepo;
import de.hs.da.hskleinanzeigen.repo.NoteRepo;
import de.hs.da.hskleinanzeigen.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AdvertisementController.class)
@WebAppConfiguration
//@AutoConfigureMockMvc(addFilters = false)
//@ContextConfiguration(classes = {SecurityConfig.class})

class SecurityConfigTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    UserRepo Userrepo;
    @MockBean
    CategoryRepo Catrepo;
    @MockBean
    NoteRepo Noterepo;
    @MockBean
    AdvertisementRepo Adrepo;

    @WithMockUser(username = "user", password = "user", roles = "USER")
    @Test
    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/advertisements/").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", password = "user", roles = "USER")
    @Test
    public void givenAuthRequestOnPrivateService_shouldFail() throws Exception {
        mvc.perform(get("/actuator/metrics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}