package de.hs.da.hskleinanzeigen;

import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.repo.UserRepo;
import de.hs.da.hskleinanzeigen.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping(path = "/api/users")

public class UserController {
    public class NotFoundException extends Exception {
    }

    public class PayloadException extends Exception {
    }

    public class DuplicateMailException extends Exception {
    }

    public class NoSearchResultException extends Exception {
    }

    @Autowired
    private UserRepo repo;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/", consumes = {"application/json"}, produces = {"application/json"})
    User newUser(@Valid @RequestBody() User newUser) throws PayloadException, DuplicateMailException {
        if (newUser == null ||  newUser.getPassword() == null || newUser.getEmail() == null  || newUser.getFirstName() == null || newUser.getLastName() == null || newUser.getPhone() == null || newUser.getLocation() == null) {
            throw new PayloadException();
        }
        Optional<Collection<User>> x = repo.findByEmail(newUser.getEmail());

        if (x.isPresent()) {
            throw new DuplicateMailException();
        } else {
            newUser.setCreated(new Timestamp(System.currentTimeMillis()));
            return repo.save(newUser);
        }

    }
    @GetMapping(path = "/{id}", produces = {"application/json"})
    User findUserbyId(@PathVariable("id") Integer id) throws UserController.NotFoundException {
        return repo.findById(id).orElseThrow(NotFoundException::new);
    }
    @Autowired
    UserServiceImpl service;

    @GetMapping(path={""}, produces = {"application/json"}, params = { "pageStart", "pageSize" })
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(value = "pageStart") Integer pageNo,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(defaultValue = "created") String sortBy) throws PayloadException, NoSearchResultException {
        if (pageNo < 0|| pageSize <= 0){
            throw new PayloadException();
        }
        List<User> list = service.getAllUsers(pageNo, pageSize, sortBy);
        if(list.isEmpty()){
            throw new NoSearchResultException();
        }

        return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(de.hs.da.hskleinanzeigen.UserController.NotFoundException.class)
    public void handleNotFound() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolation() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(de.hs.da.hskleinanzeigen.UserController.PayloadException.class)
    public void handleBadPayload() {
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(de.hs.da.hskleinanzeigen.UserController.NoSearchResultException.class)
    public void handleNoSearchResult() {
    }
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(de.hs.da.hskleinanzeigen.UserController.DuplicateMailException.class)
    public void handleConflictPayload() {
    }
}


