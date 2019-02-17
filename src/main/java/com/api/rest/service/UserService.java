package com.api.rest.service;

import com.api.rest.entities.User;
import com.api.rest.exceptions.UserMissingInformationException;
import com.api.rest.exceptions.UserNotFoundException;
import com.api.rest.repository.UserRepository;
import com.api.rest.service.base.RestService;
import com.api.rest.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements RestService<User> {

    private final UserRepository userRepository;

    private final CounterService counterService;

    @Autowired
    public UserService(UserRepository userRepository, CounterService counterService) {
        this.userRepository = userRepository;
        this.counterService = counterService;
    }

    @Override
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> get(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) return new ResponseEntity<>(user.get(), HttpStatus.OK);
        else throw new UserNotFoundException();


    }

    @Override
    public ResponseEntity<User> add(User entity, HttpServletRequest request) {

        checkUser(entity);
        entity.setId(counterService.getNextSequence(User.COLLECTION_NAME));
        User newUser = userRepository.save(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location", generateUserUrl(newUser, request));
        return new ResponseEntity<>(newUser, responseHeaders, HttpStatus.CREATED);

    }


    @Override
    public ResponseEntity<User> patchUpdate(Integer id, User entityUpdates) {

        User existingUser = findUserIfExists(id);
        ApiUtils.mergeEntity(existingUser, entityUpdates);
        existingUser.setId(id);
        checkUser(existingUser);
        User updatedContact = userRepository.save(existingUser);
        return new ResponseEntity<User>(updatedContact, HttpStatus.OK);


    }

    @Override
    public ResponseEntity<User> putUpdate(Integer id, User entityUpdates) {
        User existingUser = findUserIfExists(id);
        checkUser(entityUpdates);
        entityUpdates.setId(id);
        userRepository.save(entityUpdates);
        return new ResponseEntity<>(entityUpdates, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<User> delete(Integer id) {

        User existingUser = findUserIfExists(id);
        userRepository.deleteById(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);

    }


    private String generateUserUrl(User newUser, HttpServletRequest request) {

        StringBuilder resourcePath = new StringBuilder();

        resourcePath.append(request.getRequestURL())
                .append("/")
                .append(newUser.getId());

        return resourcePath.toString();


    }


    private User findUserIfExists(Integer id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) return user.get();
        else throw new UserNotFoundException();
    }


    private void checkUser(User user) {

        if (!isUserNameValid(user.getName())) throw new UserMissingInformationException();
        if (!isUserBirthdayValid(user.getBirthdate())) throw new UserMissingInformationException();

    }

    private boolean isUserNameValid(String name) {

        return !StringUtils.isEmpty(name);
    }


    private boolean isUserBirthdayValid(Date birthdate) {

        return birthdate != null;
    }


}
