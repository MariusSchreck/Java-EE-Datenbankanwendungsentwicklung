package de.hs.da.hskleinanzeigen.repo;

import de.hs.da.hskleinanzeigen.entity.Advertisement;
import de.hs.da.hskleinanzeigen.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface AdvertisementRepo extends CrudRepository<Advertisement, Integer> {
    Optional<Collection<Advertisement>> findAllByType(Advertisement.Type type);
    Optional<Collection<Advertisement>> findAllByPriceBetween(int low, int up);
    Optional<Collection<Advertisement>> findAllByPriceGreaterThanEqualAndPriceLessThanEqual(int low, int up);
    Optional<Collection<Advertisement>> findAllByCategory(Category category);
    Optional<Collection<Advertisement>> findAllByCategoryAndType(Category category, Advertisement.Type type);
    Optional<Collection<Advertisement>> findAllByCategoryAndTypeAndPriceGreaterThan(Category category, Advertisement.Type type, Integer price);
    Optional<Collection<Advertisement>> findAllByCategoryAndTypeAndPriceLessThan(Category category, Advertisement.Type type, Integer price);
    Optional<Collection<Advertisement>> findAllByCategoryAndTypeAndPriceBetween(Category category, Advertisement.Type type, int low, int up);
    Optional<Collection<Advertisement>> findAllByCategoryAndTypeAndPriceGreaterThanEqualAndPriceLessThanEqual(Category category, Advertisement.Type type, int low, int up);
    Optional<Collection<Advertisement>> findAllByCategoryAndPriceGreaterThan(Category category, Integer price);
    Optional<Collection<Advertisement>> findAllByCategoryAndPriceLessThan(Category category, Integer price);
    Optional<Collection<Advertisement>> findAllByCategoryAndPriceBetween(Category category, int low, int up);
    Optional<Collection<Advertisement>> findAllByCategoryAndPriceGreaterThanEqualAndPriceLessThanEqual(Category category, int low, int up);
    Optional<Collection<Advertisement>> findAllByTypeAndPriceGreaterThan(Advertisement.Type type, Integer price);
    Optional<Collection<Advertisement>> findAllByTypeAndPriceLessThan(Advertisement.Type type, Integer price);
    Optional<Collection<Advertisement>> findAllByTypeAndPriceBetween(Advertisement.Type type, Integer low, Integer up);
    Optional<Collection<Advertisement>> findAllByPriceGreaterThan(Integer price);
    Optional<Collection<Advertisement>> findAllByPriceLessThan(Integer price);
}
