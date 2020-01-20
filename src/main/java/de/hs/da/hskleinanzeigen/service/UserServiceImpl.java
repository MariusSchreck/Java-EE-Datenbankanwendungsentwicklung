package de.hs.da.hskleinanzeigen.service;

import java.util.ArrayList;
        import java.util.List;

import de.hs.da.hskleinanzeigen.entity.User;
import de.hs.da.hskleinanzeigen.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.domain.Sort;
        import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl {

    @Autowired
    UserRepo repository;

    public List<User> getAllUsers(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

        Page<User> pagedResult = repository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<User>();
        }
    }
/*
    public User getEmployeeById(Long id) throws RecordNotFoundException
    {
        Optional<User> employee = repository.findById(id);

        if(employee.isPresent()) {
            return employee.get();
        } else {
            throw new RecordNotFoundException("No employee record exist for given id");
        }
    }

    public EmployeeEntity createOrUpdateEmployee(EmployeeEntity entity) throws RecordNotFoundException
    {
        Optional<EmployeeEntity> employee = repository.findById(entity.getId());

        if(employee.isPresent())
        {
            EmployeeEntity newEntity = employee.get();
            newEntity.setEmail(entity.getEmail());
            newEntity.setFirstName(entity.getFirstName());
            newEntity.setLastName(entity.getLastName());

            newEntity = repository.save(newEntity);

            return newEntity;
        } else {
            entity = repository.save(entity);

            return entity;
        }
    }
*/
}