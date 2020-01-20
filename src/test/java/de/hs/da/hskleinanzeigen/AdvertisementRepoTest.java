package de.hs.da.hskleinanzeigen;

import de.hs.da.hskleinanzeigen.entity.Advertisement;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.repo.AdvertisementRepo;
import de.hs.da.hskleinanzeigen.repo.CategoryRepo;
import de.hs.da.hskleinanzeigen.repo.UserRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdvertisementRepoTest {
    @Autowired
    AdvertisementRepo adRepo;
    @Autowired
    CategoryRepo catRepo;
    @Autowired
    UserRepo userRepo;

    @Before
    public void setup(){
        RandomEntities.generateData();
    }

    @BeforeEach
    public void setupEach(){
    }

    @Test
    public void findById(){
        adRepo.deleteAll();
        userRepo.deleteAll();
        catRepo.deleteAll();

        Advertisement adv = RandomEntities.getAds().get(0);
        Category cat = adv.getCategory();
        User user = adv.getUser();

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        System.out.println(adv);
        System.out.println(cat);
        System.out.println(user);

        cat.setListSubCategories(new ArrayList<>());
        cat.setListAdvertisement(new ArrayList<>());
        cat = catRepo.save(cat);

        user.setAds(new ArrayList<>());
        user.setNotes(new ArrayList<>());
        user = userRepo.save(user);

        adv.setNotes(new ArrayList<>());
        adv.setUser(user);
        adv.setCategory(cat);
        adv = adRepo.save(adv);

        System.out.println(userRepo.count());
        System.out.println(catRepo.count());
        System.out.println(adRepo.count());


        Optional<Advertisement> foundAdv = adRepo.findById(adv.getId());

        Assert.assertNotNull(foundAdv);
        Assert.assertTrue(foundAdv.isPresent());
        Assert.assertEquals(adv, foundAdv.get());
    }


    @Test
    public void findAllByType(){
        adRepo.deleteAll();
        userRepo.deleteAll();
        catRepo.deleteAll();

        Advertisement adv = RandomEntities.getAds().get(0);
        Advertisement adv2 = RandomEntities.getAds().get(1);

        Assert.assertEquals(adv.getType(), adv2.getType());

        Category cat = adv.getCategory();
        User user = adv.getUser();

        Category cat2 = adv2.getCategory();
        User user2 = adv2.getUser();

        cat.setListSubCategories(new ArrayList<>());
        cat.setListAdvertisement(new ArrayList<>());
        cat = catRepo.save(cat);

        cat2.setListSubCategories(new ArrayList<>());
        cat2.setListAdvertisement(new ArrayList<>());
        cat2 = catRepo.save(cat2);

        user.setAds(new ArrayList<>());
        user.setNotes(new ArrayList<>());
        user = userRepo.save(user);

        user2.setAds(new ArrayList<>());
        user2.setNotes(new ArrayList<>());
        user2 = userRepo.save(user2);

        adv.setNotes(new ArrayList<>());
        adv.setUser(user);
        adv.setCategory(cat);
        adv = adRepo.save(adv);

        adv2.setNotes(new ArrayList<>());
        adv2.setUser(user2);
        adv2.setCategory(cat2);
        adv2 = adRepo.save(adv2);

        Optional<Collection<Advertisement>> foundAdv = adRepo.findAllByType(adv.getType());

        List<Advertisement> expected = List.of(adv, adv2);

        Assert.assertNotNull(foundAdv);
        Assert.assertTrue(foundAdv.isPresent());
        Assert.assertEquals(expected, foundAdv.get());
    }


    @Test
    public void findAllByCategory(){
        adRepo.deleteAll();
        userRepo.deleteAll();
        catRepo.deleteAll();

        Advertisement adv = RandomEntities.getAds().get(0);
        Advertisement adv2 =  RandomEntities.getAds().get(1);

        Category cat = adv.getCategory();
        User user = adv.getUser();
        User user2 = adv.getUser();

        cat.setListSubCategories(new ArrayList<>());
        cat.setListAdvertisement(new ArrayList<>());
        cat = catRepo.save(cat);

        user.setAds(new ArrayList<>());
        user.setNotes(new ArrayList<>());
        user = userRepo.save(user);

        user2.setAds(new ArrayList<>());
        user2.setNotes(new ArrayList<>());
        user2 = userRepo.save(user2);

        adv.setNotes(new ArrayList<>());
        adv.setUser(user);
        adv.setCategory(cat);
        adv = adRepo.save(adv);

        adv2.setNotes(new ArrayList<>());
        adv2.setUser(user2);
        adv2.setCategory(cat);
        adv2 = adRepo.save(adv2);

        Optional<Collection<Advertisement>> foundAdv = adRepo.findAllByCategory(adv.getCategory());

        List<Advertisement> expected = List.of(adv, adv2);

        Assert.assertNotNull(foundAdv);
        Assert.assertTrue(foundAdv.isPresent());
        Assert.assertEquals(expected, foundAdv.get());
    }


    @Test
    public void findAllByPriceBetween(){
        adRepo.deleteAll();
        userRepo.deleteAll();
        catRepo.deleteAll();

        Advertisement adv = RandomEntities.getAds().get(0);
        Advertisement adv2 =  RandomEntities.getAds().get(1);

        Integer priceLo = adv.getPrice();
        Integer priceHi = adv2.getPrice();

        if(priceLo > priceHi){
            Integer tmp = priceLo;
            priceLo = priceHi;
            priceHi = priceLo;
        }

        Category cat = adv.getCategory();
        User user = adv.getUser();
        User user2 = adv.getUser();

        cat.setListSubCategories(new ArrayList<>());
        cat.setListAdvertisement(new ArrayList<>());
        cat = catRepo.save(cat);

        user.setAds(new ArrayList<>());
        user.setNotes(new ArrayList<>());
        user = userRepo.save(user);

        user2.setAds(new ArrayList<>());
        user2.setNotes(new ArrayList<>());
        user2 = userRepo.save(user2);

        adv.setNotes(new ArrayList<>());
        adv.setUser(user);
        adv.setCategory(cat);
        adv = adRepo.save(adv);

        adv2.setNotes(new ArrayList<>());
        adv2.setUser(user2);
        adv2.setCategory(cat);
        adv2 = adRepo.save(adv2);

        Optional<Collection<Advertisement>> foundAdv = adRepo.findAllByPriceGreaterThanEqualAndPriceLessThanEqual(priceLo - 1, priceHi + 1);

        List<Advertisement> expected = List.of(adv, adv2);

        Assert.assertNotNull(foundAdv);
        Assert.assertTrue(foundAdv.isPresent());
        Assert.assertEquals(expected, foundAdv.get());
    }
}
