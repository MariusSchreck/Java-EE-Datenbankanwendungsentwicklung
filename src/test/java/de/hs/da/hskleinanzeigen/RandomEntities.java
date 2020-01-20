package de.hs.da.hskleinanzeigen;

import de.hs.da.hskleinanzeigen.entity.Advertisement;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.Note;
import de.hs.da.hskleinanzeigen.entity.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomEntities {
    private static int baseId = 123123;

    public static int nUsers = 4;
    public static int nCategories = 6;
    public static int nAdvertisements = nUsers *10;
    public static int nNotes = nUsers *2;

    public static final List<User> users =  new ArrayList<>();
    public static final List<Category> categories =  new ArrayList<>();
    public static final List<Advertisement> ads =  new ArrayList<>();
    public static final List<Note> notes =  new ArrayList<>();

    private static final List<String> phonePrefixes = List.of("0177", "0178", "0151");
    private static final List<String> predefinedUserFirstNames = List.of("Alex", "Marius", "Jonas", "Dennis");
    private static final List<String> predefinedUserLastNames = List.of("Heinz", "Müller", "Becker", "Johann", "Markus");
    private static final List<String> predefinedCategoryNames = List.of("Marken", "Fruechte", "Kleidung",
            "Technik", "Haushalt", "Moebel");
    private static final List<String> predefinedLocations = List.of("Darmstadt", "Offenbach", "Egelsbach", "Schaffenhausen",
            "Riedstadt", "Roßdorf", "Dieburg", "Frankfurt", "Mainz");

    static void generateData() {
        users.clear();
        categories.clear();
        ads.clear();
        notes.clear();

        for(int i = 0; i < nUsers; i++) {
            users.add(newUser());
        }

        for(int i = 0; i < nCategories; i++){
            categories.add(newCategory());
        }

        final float catPerAd = nCategories  / nAdvertisements;
        final float userPerAd = nUsers / nAdvertisements;
        for(int i = 0, iCat = 0, iUser = 0; i < nAdvertisements; i++, iCat = (int) Math.floor(i * catPerAd), iUser = (int) Math.floor(i * userPerAd)){
            Category c = categories.get(iCat);
            User u = users.get(iUser);
            Advertisement a = newAdvertisement(c, u, Advertisement.Type.Offer);
            ads.add(a);
        }

        final float adPerNote = nAdvertisements / nNotes;
        final float userPerNote = nUsers / nNotes;
        for(int i = 0, iAd = 0, iUser = 0; i < nNotes; i++, iAd = (int) Math.floor(i * adPerNote), iUser = (int) Math.floor(i * userPerNote)){
            Advertisement a = ads.get(iAd);
            User u = users.get(iUser);
            Note n = newNote(a, u);
            notes.add(n);
        }
    }

    public static User newUser(){
        final int nextUserId = users.size();

        User u = new User();
        u.setId(baseId + nextUserId);
        u.setCreated(Timestamp.from(Instant.ofEpochSecond(
                System.currentTimeMillis() - ThreadLocalRandom.current().nextInt(60*60*1000, 14*24*60*60*1000)
        )));
        u.setEmail("testuser" + nextUserId + "@somedomain.com");
        u.setFirstName((nextUserId < predefinedUserFirstNames.size())
                ?predefinedUserFirstNames.get(nextUserId)
                :capitalize(generateRandomString(5, 'a', 'z'))
        );
        u.setLastName((nextUserId < predefinedUserLastNames.size())
                ?predefinedUserLastNames.get(nextUserId)
                :capitalize(generateRandomString(5, 'a', 'z'))
        );
        u.setLocation((nextUserId < predefinedLocations.size())
                ?predefinedLocations.get(nextUserId)
                :capitalize(generateRandomString(8, 'a', 'z'))
        );
        u.setPassword("supersecret");

        u.setPhone(
                phonePrefixes.get(ThreadLocalRandom.current().nextInt(phonePrefixes.size()))
                + Integer.toString(ThreadLocalRandom.current().nextInt(182310, Integer.MAX_VALUE))
        );

        return u;
    }

    public static Category newCategory(){
        final int nextCatId = categories.size();

        Category c = new Category();
        c.setId(baseId + nextCatId);

        c.setName( (nextCatId < predefinedCategoryNames.size())
                ? predefinedCategoryNames.get(nextCatId)
                :capitalize(generateRandomString(8 ,'a', 'z')) );

        if(nextCatId > 0){
            c.setParentCategory(categories.get(0));
            categories.get(0).getListSubCategories().add(c);
        }

        return c;
    }
    public static Advertisement newAdvertisement(Category c, User u, Advertisement.Type type){
        final int nextAdId = ads.size();

        Advertisement a = new Advertisement();
        a.setId(baseId + nextAdId);
        a.setType(type);
        a.setCategory(c);
        a.setUser(u);

        a.setLocation(u.getLocation());

        a.setPrice(ThreadLocalRandom.current().nextInt(11, 333));
        a.setDescription(generateRandomString(30, 'a', 'z'));
        a.setTitle(capitalize(generateRandomString(30, 'a', 'z')));

        do {
            a.setCreated(Timestamp.from(Instant.ofEpochSecond(
                    System.currentTimeMillis() - ThreadLocalRandom.current().nextInt(60 * 60 * 1000, 14 * 24 * 60 * 60 * 1000)
            )));
        } while (a.getCreated().before(u.getCreated()));


        u.getAds().add(a);
        c.getListAdvertisement().add(a);

        return a;
    }
    public static Note newNote(Advertisement a, User u){
        final int nextNoteId = notes.size();

        Note n = new Note();
        n.setId(baseId + nextNoteId);

        n.setNote(a.getTitle() + " noted by " + u.getFirstName());

        a.getNotes().add(n);
        u.getNotes().add(n);

        return n;
    }

    /*
    Source: https://www.baeldung.com/java-random-string
     */
    private static String generateRandomString(int length, char leftLimit, char rightLimit) {
        final int leftBound = (int) leftLimit;
        final int rightBound = (int) rightLimit;

        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            buffer.append((char) ThreadLocalRandom.current().nextInt(leftBound, rightBound));
        }
        return buffer.toString();
    }
    private static String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static List<User> getUsers(){
        return users;
    }

    public static List<Category> getCategories() {
        return categories;
    }

    public static List<Advertisement> getAds() {
        return ads;
    }

    public static List<Note> getNotes() {
        return notes;
    }
}
