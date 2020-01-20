package de.hs.da.hskleinanzeigen;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hs.da.hskleinanzeigen.entity.Advertisement;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.repo.AdvertisementRepo;
import de.hs.da.hskleinanzeigen.repo.CategoryRepo;
import de.hs.da.hskleinanzeigen.repo.NoteRepo;
import de.hs.da.hskleinanzeigen.repo.UserRepo;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static de.hs.da.hskleinanzeigen.TestUtil.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AdvertisementController.class)
@MockBeans({@MockBean(AdvertisementRepo.class),
        @MockBean(UserRepo.class),
        @MockBean(CategoryRepo.class),
        @MockBean(NoteRepo.class)
})
public class AdvertisementControllerTest2 {
    private static final String urlBase = "/api/advertisements";


    @MockBean
    private AdvertisementRepo adRepo;
    @MockBean
    private CategoryRepo catRepo;
    @MockBean
    private UserRepo userRepo;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    private void setupEach() {
        RandomEntities.generateData();
    }

    private void mockReposById() {
        mockFindById(adRepo, RandomEntities.getAds(), Advertisement::getId);
        mockFindById(catRepo, RandomEntities.getCategories(), Category::getId);
        mockFindById(userRepo, RandomEntities.getUsers(), User::getId);
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void findAdvertisement() throws Exception {
        mockReposById();

        Advertisement ad = RandomEntities.getAds().get(0);

        mvc.perform(
                get(urlBase + "/{adId}", ad.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ad.getId()))
                .andExpect(jsonPath("$.type").value(Matchers.containsString(ad.getType().name())))
                .andExpect(jsonPath("$.category.name").value(ad.getCategory().getName()))
                .andExpect(jsonPath("$.category.id").value(ad.getCategory().getId()))
                .andExpect(jsonPath("$.title").value(ad.getTitle()))
                .andExpect(jsonPath("$.description").value(ad.getDescription()))
                .andExpect(jsonPath("$.price").value(ad.getPrice()))
                .andExpect(jsonPath("$.location").value(ad.getLocation()))
                .andExpect(jsonPath("$.user").value(ad.getUser().getId()))
        ;

        verify(adRepo).findById(ad.getId());
    }


    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void findAdvertisement_NotFound() throws Exception {
        mockReposById();

        Advertisement ad = RandomEntities.getAds().get(0);

        given(adRepo.findById(ad.getId())).willReturn(Optional.empty());

        mvc.perform(
                get(urlBase + "/{adId}", ad.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
                .andExpect(status().isNotFound())
        ;

        verify(adRepo).findById(ad.getId());
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void newAdvertisement() throws Exception {
        mockReposById();

        Advertisement ad = RandomEntities.getAds().get(0);

        given(adRepo.findById(ad.getId())).willReturn(Optional.empty());

        ObjectMapper mapper = new ObjectMapper();

        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        class AdJsonDTO {
            String type = ad.getType().name();
            int category = ad.getCategory().getId();
            String title = ad.getTitle();
            String description = ad.getDescription();
            String location = ad.getLocation();
            int price = ad.getPrice();
            int user = ad.getUser().getId();
        }


        String json = mapper.writeValueAsString(new AdJsonDTO());
        System.out.println(json);
        System.out.println(mvc);
        mvc.perform(
                post(urlBase + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(json)
        )
                .andExpect(status().isCreated())
        ;

        verify(userRepo).findById(ad.getUser().getId());
        verify(catRepo).findById(ad.getCategory().getId());

        ad.setId(null);
        ad.setCreated(null);

        verify(adRepo).save(checkArgs(ad, Advertisement::getTitle, Advertisement::getType, Advertisement::getCategory,
                Advertisement::getUser, Advertisement::getDescription, Advertisement::getLocation, Advertisement::getPrice));
    }

    //findAll to findAllByCategoryAndTypeAndPriceBetween

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void findAdvertisementsAll() throws Exception {
        mockReposById();

        given(adRepo.findAll()).willReturn(RandomEntities.getAds());

        mvc.perform(
                get(urlBase + "/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
                .andExpect(status().isOk())
        ;

        verify(adRepo).findAll();
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void findAdvertisementsByCategory() throws Exception {
        mockReposById();

        Category cat = RandomEntities.getCategories().get(0);
        List<Advertisement> ads = cat.getListAdvertisement();

        given(adRepo.findAllByCategory(new Category(cat.getId()))).willReturn(Optional.of(ads));

        mvc.perform(
                get(urlBase + "/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .param("category", "" + cat.getId())
        )
                .andExpect(status().isOk())
        ;

        verify(adRepo).findAllByCategory(new Category(cat.getId()));
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void findAdvertisementsByType() throws Exception {
        mockReposById();

        // RandomEntities generates only Offer
        Advertisement.Type type = RandomEntities.getAds().get(0).getType();
        List<Advertisement> ads = RandomEntities.getAds();

        given(adRepo.findAllByType(type)).willReturn(Optional.of(ads));

        mvc.perform(
                get(urlBase + "/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .param("type", type.name())
        )
                .andExpect(status().isOk())
        ;

        verify(adRepo).findAllByType(type);
    }

    @Disabled
    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void findAdvertisementsByPriceBetween() throws Exception {
        // TODO
        mockReposById();

        Category cat = RandomEntities.getCategories().get(0);
        List<Advertisement> ads = cat.getListAdvertisement();

        given(adRepo.findAllByCategory(cat)).willReturn(Optional.of(ads));

        mvc.perform(
                get(urlBase + "/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .param("category", cat.getName())
        )
                .andExpect(status().isOk())
        ;

        verify(adRepo).findAllByCategory(cat);
    }

    @Disabled
    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void findAdvertisementsByPriceTo() throws Exception {
        // TODO
        mockReposById();

        Category cat = RandomEntities.getCategories().get(0);
        List<Advertisement> ads = cat.getListAdvertisement();

        given(adRepo.findAllByCategory(new Category(cat.getId()))).willReturn(Optional.of(ads));

        mvc.perform(
                get(urlBase + "/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .param("category", "" + cat.getId())
        )
                .andExpect(status().isOk())
        ;

        verify(adRepo).findAllByCategory(cat);
    }

    @Disabled
    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void findAdvertisementsByPriceFrom() throws Exception {
        // TODO
        mockReposById();

        Category cat = RandomEntities.getCategories().get(0);
        List<Advertisement> ads = cat.getListAdvertisement();

        given(adRepo.findAllByCategory(cat)).willReturn(Optional.of(ads));

        mvc.perform(
                get(urlBase + "/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .param("category", cat.getName())
        )
                .andExpect(status().isOk())
        ;

        verify(adRepo).findAllByCategory(cat);
    }

    @Disabled
    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void findAdvertisementsByCategoryAndTypeAndPriceBetween() throws Exception {
        // TODO
        mockReposById();

        Category cat = RandomEntities.getCategories().get(0);
        List<Advertisement> ads = cat.getListAdvertisement();

        given(adRepo.findAllByCategory(cat)).willReturn(Optional.of(ads));

        mvc.perform(
                get(urlBase + "/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .param("category", cat.getName())
        )
                .andExpect(status().isOk())
        ;

        verify(adRepo).findAllByCategory(cat);
    }
}