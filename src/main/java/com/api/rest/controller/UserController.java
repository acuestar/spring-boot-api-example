package com.api.rest.controller;

import com.api.rest.entities.User;
import com.api.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = "users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAll();
    }


    @RequestMapping(value = "users/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable Integer id) throws Throwable {
        return userService.get(id);
    }


    @RequestMapping(value = "users", method = RequestMethod.POST)
    public ResponseEntity<User> addUser(@Valid @RequestBody User user, HttpServletRequest request) {
        return userService.add(user, request);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<User> patchUpdateUser(@PathVariable Integer id, @Valid @RequestBody User user) {
        return userService.patchUpdate(id, user);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> putUpdateUser(@PathVariable Integer id, @Valid @RequestBody User user) {
        return userService.putUpdate(id, user);
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable Integer id) {
        return userService.delete(id);
    }


}
