package com.example.demo.controllers;

import com.example.demo.TestData;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTests {


    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void createUser() {
        User user = TestData.getTestUser();
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("hashed");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());
        request.setConfirmPassword(user.getConfirmPassword());
        final ResponseEntity<?> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        User responseBodyUser = (User) response.getBody();
        assertNotNull(responseBodyUser);
        assertEquals(user.getUsername(), responseBodyUser.getUsername());
        assertEquals("hashed", responseBodyUser.getPassword());
    }
}
