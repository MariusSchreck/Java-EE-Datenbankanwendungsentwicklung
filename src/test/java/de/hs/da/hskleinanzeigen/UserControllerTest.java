package de.hs.da.hskleinanzeigen;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@WebAppConfiguration
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
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

    class UserDTO {
        @Id
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID")
        private Integer id;

        @JsonProperty("created")
        @Column(name = "CREATED", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        private Timestamp created;

        @JsonProperty("email")
        @NotNull
        @Valid
        @Email(message = "Email should be valid")
        @Column(name = "EMAIL")
        private String email;

        @Column(name = "FIRST_NAME")
        @Size(max = 255)
        @JsonProperty("firstName")
        private String firstName;

        @Column(name = "LAST_NAME")
        @Size(max = 255)
        @JsonProperty("lastName")
        private String lastName;

        @Column(name = "LOCATION")
        @JsonProperty("location")
        private String location;

        @NotNull(message = "Passwort darf nicht leer sein!")
        @Size(min = 6)
        @Column(name = "PASSWORD")
        private String password;

        @JsonProperty("phone")
        @Column(name = "PHONE")
        private String phone;

        @JsonProperty(value = "advertisements", access = JsonProperty.Access.WRITE_ONLY)
        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
        private List<Advertisement> ads;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Timestamp getCreated() {
            return created;
        }

        public void setCreated(Timestamp created) {
            this.created = created;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<Advertisement> getAds() {
            return ads;
        }

        public void setAds(List<Advertisement> ads) {
            this.ads = ads;
        }
    }

        UserDTO userDTO = new UserDTO();
        final ObjectMapper mapper = new ObjectMapper();
        User user = new User();


    @BeforeEach
        void setUp(){

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        userDTO.setId(1);
        userDTO.setEmail("studi213@hs-da.de");
        userDTO.setCreated(timestamp);
        userDTO.setFirstName("Thomas");
        userDTO.setLastName("Mueller");
        userDTO.setPhone("069-123456");
        userDTO.setLocation("Darmstadt");
        userDTO.setPassword("secret343");

        user.setId(1);
        user.setEmail("studi213@hs-da.de");
        user.setCreated(timestamp);
        user.setFirstName("Thomas");
        user.setLastName("Mueller");
        user.setPhone("069-123456");
        user.setLocation("Darmstadt");
        user.setPassword("secret343");
    }

    @Test
    void findUserbyId() throws Exception {


        User user = new User();
        given(Userrepo.findById(99999)).willReturn(java.util.Optional.of(user));

        {
            mvc.perform(MockMvcRequestBuilders
                    .get("/api/users/{id}", 99999)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void findUserWithUnknownIdTest() throws Exception {

        User user = new User();
        given(Userrepo.findById(1)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        {
            mvc.perform(MockMvcRequestBuilders
                    .get("/api/users/{id}", 1)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void findUserWithPayloadTest() throws Exception {
        given(Userrepo.findById(user.getId())).willReturn(Optional.of(user));

        mvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").isNotEmpty())
                .andExpect(jsonPath("firstName").isNotEmpty())
                .andExpect(jsonPath("lastName").isNotEmpty())
                .andExpect(jsonPath("phone").isNotEmpty())
                .andExpect(jsonPath("location").isNotEmpty()
                );
    }

    @Test
    void insertUserTest() throws Exception {

        System.out.println(mapper.writeValueAsString(userDTO));
        mvc.perform(MockMvcRequestBuilders.post("/api/users/")
                .content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void insertUserWithInvalidPayload() throws Exception {

        userDTO.setEmail(null);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/advertisements/")
                .content(mapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(400);

    }

}