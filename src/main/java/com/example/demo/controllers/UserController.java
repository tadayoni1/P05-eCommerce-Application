package com.example.demo.controllers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        log.debug("/api/user/id/(id): User with id {} was looked up.", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            log.debug("/api/user/(username): User {} was not found.", username);
        } else {
            log.debug("/api/user/(username): User {} was found.", username);
        }
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {

        if(createUserRequest.getUsername() == null || createUserRequest.getUsername().isEmpty()) {
            log.error("/api/user/create: Username is missing. Cannot create user");
            return new ResponseEntity<String>("Username is missing. Cannot create user.", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);

        if (createUserRequest.getPassword() == null ||
                createUserRequest.getPassword().length() < 7) {
            log.error("/api/user/create: Error with user password. Cannot create User {}", createUserRequest.getUsername());
            return new ResponseEntity<String>("Error with user password. Cannot create User.", HttpStatus.BAD_REQUEST);
        }
        if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            log.warn("/api/user/create: Passwords do not match.");
            return new ResponseEntity<String>("Passwords do not match. Cannot create User.", HttpStatus.BAD_REQUEST);
        }
        String encryptedPassword = bCryptPasswordEncoder.encode(createUserRequest.getPassword());
        user.setPassword(encryptedPassword);
        user.setConfirmPassword(encryptedPassword);

        userRepository.save(user);
        log.error("/api/user/create: User {} has been created", user.getUsername());
        return ResponseEntity.ok(user);
    }

}
