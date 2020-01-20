package de.hs.da.hskleinanzeigen;

import de.hs.da.hskleinanzeigen.entity.Advertisement;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.repo.AdvertisementRepo;
import de.hs.da.hskleinanzeigen.repo.CategoryRepo;
import de.hs.da.hskleinanzeigen.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

@RestController()
@RequestMapping(path = "/api/advertisements")
public class AdvertisementController {

    public class NotFoundException extends Exception{}
    public class PayloadException extends Exception{}
    public class NoSearchResultException extends Exception{}

    @Autowired
    private AdvertisementRepo adRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CategoryRepo catRepo;


    @GetMapping(path = "/{id}", produces = {"application/json"})
    Advertisement findAdvertisement(@PathVariable("id") Integer id) throws NotFoundException {
        return adRepo.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/", consumes = {"application/json"}, produces = {"application/json"})
    Advertisement newAdvertisement(@RequestBody() Advertisement newAdv) throws PayloadException {
        if(newAdv == null || newAdv.getDescription() == null || newAdv.getLocation() == null || /*newAdv.getPrice() == 0 || */newAdv.getTitle() == null || newAdv.getType() == null || newAdv.getCategory() == null){
            throw new PayloadException();
        }
        if (newAdv.getUser() == null || newAdv.getUser().getId() == null) {
            throw new PayloadException();
        }
        if (newAdv.getCategory() == null || newAdv.getCategory().getId() == null) {
            throw new PayloadException();
        }

        Optional<User> newAdvUser = userRepo.findById(newAdv.getUser().getId());
        if(newAdvUser.isEmpty()){
            throw new PayloadException();
        }

        Optional<Category> newAdvCategory = catRepo.findById(newAdv.getCategory().getId());
        if(newAdvCategory.isEmpty()){
            throw new PayloadException();
        }

        newAdv.setCategory(newAdvCategory.get());
        newAdv.setUser(newAdvUser.get());
        newAdv.setCreated(new Timestamp(System.currentTimeMillis()));
        return adRepo.save(newAdv);
    }

    @GetMapping(path = "/", produces = {"application/json"})
    Collection<Advertisement> findAdvertisements(@RequestParam(name = "type", required = false) Advertisement.Type type,
                                                 @RequestParam(name = "category", required = false) Integer category,
                                                 @RequestParam(name = "priceFrom", required = false) Integer priceFrom,
                                                 @RequestParam(name = "priceTo", required = false) Integer priceTo) throws NoSearchResultException {
        Collection<Advertisement> x = null;
        System.out.println(type + " " + category +  " " + priceFrom +  " " + priceTo);
        if (type != null && category != null && priceFrom != null && priceTo != null) {
            x = adRepo.findAllByCategoryAndTypeAndPriceBetween(new Category(category), type, priceFrom, priceTo).orElseThrow(() -> new NoSearchResultException());
        } else if(category != null && type != null && priceFrom != null){
            x = adRepo.findAllByCategoryAndTypeAndPriceGreaterThan(new Category(category), type, priceFrom).orElseThrow(() -> new NoSearchResultException());
        } else if(category != null && type != null && priceTo != null){
            x = adRepo.findAllByCategoryAndTypeAndPriceLessThan(new Category(category), type, priceTo).orElseThrow(() -> new NoSearchResultException());
        } // TODO type, price

        else if (priceFrom != null && priceTo != null){
            x = adRepo.findAllByPriceGreaterThanEqualAndPriceLessThanEqual(priceFrom, priceTo).orElseThrow(() -> new NoSearchResultException());
        } else if (category != null && priceTo != null){
            x = adRepo.findAllByCategoryAndPriceLessThan(new Category(category), priceTo).orElseThrow(() -> new NoSearchResultException());
        } else if (category != null && priceFrom != null){
            x = adRepo.findAllByCategoryAndPriceGreaterThan(new Category(category), priceFrom).orElseThrow(() -> new NoSearchResultException());
        } else if (category != null && type != null){
            x = adRepo.findAllByCategoryAndType(new Category(category), type).orElseThrow(() -> new NoSearchResultException());
        }

        else if (priceFrom != null){
            x = adRepo.findAllByPriceGreaterThan(priceFrom).orElseThrow(() -> new NoSearchResultException());
        } else if (priceTo != null){
            x = adRepo.findAllByPriceLessThan(priceTo).orElseThrow(() -> new NoSearchResultException());
        } else if( type != null ){
            x = adRepo.findAllByType(type).orElseThrow(() -> new NoSearchResultException());
        } else if (category != null) {
            x = adRepo.findAllByCategory(new Category(category)).orElseThrow(() -> new NoSearchResultException());
        }
       // System.out.println(x.size());
        if (x == null || x.size() == 0) {
            return (Collection<Advertisement>) adRepo.findAll();
            //throw new NoSearchResultException();
        }else {
            return x;
        }
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public void handleNotFound() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolation() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PayloadException.class)
    public void handleBadPayload() {
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoSearchResultException.class)
    public void handleNoSearchResult() {
    }
}
