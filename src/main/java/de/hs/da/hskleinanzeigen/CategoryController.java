package de.hs.da.hskleinanzeigen;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import de.hs.da.hskleinanzeigen.entity.Category;
import de.hs.da.hskleinanzeigen.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@RestController()
@RequestMapping(path = "/api/categories")
public class CategoryController {

    public class NotFoundException extends Exception{}
    public class PayloadException extends Exception{}
    public class NoSearchResultException extends Exception{}
    public class DuplicateCategoryNameException extends Exception{}

    @Autowired
    private CategoryRepo repo;

    @GetMapping(path = "/{id}", produces = {"application/json"})
    Category findCategory(@PathVariable("id") Integer id) throws NotFoundException {
        return repo.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/", consumes = {"application/json"}, produces = {"application/json"})
    Category newCategory(@RequestBody() Category newCategory) throws PayloadException, DuplicateCategoryNameException {
        if(newCategory == null || newCategory.getName() == null || newCategory.getParentCategory() == null) {
            throw new PayloadException();
        }

        Optional<Category> x = repo.findById(newCategory.getParentCategory().getId());
        if (x.isEmpty()) {
            throw new PayloadException();
        } else {
            newCategory.setParentCategory(x.get());
        }

        Optional<Category> y = repo.findByName(newCategory.getName());
        if (y.isPresent()) {
            throw new DuplicateCategoryNameException();
        }

        return repo.save(newCategory);
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


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateCategoryNameException.class)
    public void handleDuplicateCategoryNameException() {
    }
}
