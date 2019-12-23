package com.example.demo.controllers;

import com.example.demo.TestData;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTests {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart() {
        User user = TestData.getTestUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        Item item = TestData.getTestItem();
        when(itemRepository.findById(item.getId())).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setQuantity(3);
        modifyCartRequest.setUsername(user.getUsername());

        ResponseEntity<Cart> cartResponseEntity = cartController.addToCart(modifyCartRequest);
        assertNotNull(cartResponseEntity);

        Cart cartResponseEntityBody = cartResponseEntity.getBody();
        assertEquals(3, cartResponseEntityBody.getItems().size());
        assertEquals(item.getId(), cartResponseEntityBody.getItems().get(0).getId());
        assertEquals(item.getName(), cartResponseEntityBody.getItems().get(0).getName());
    }

    @Test
    public void removeFromCart() {
        Item item = TestData.getTestItem();
        when(itemRepository.findById(item.getId())).thenReturn(java.util.Optional.of(item));

        Cart cart = new Cart();
        cart.addItem(item);
        cart.addItem(item);
        cart.addItem(item);
        cart.addItem(item);
        cart.addItem(item);

        User user = TestData.getTestUser(cart);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(item.getId());
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername(user.getUsername());

        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromCart(modifyCartRequest);
        assertNotNull(cartResponseEntity);

        Cart cartResponseEntityBody = cartResponseEntity.getBody();
        assertEquals(3, cartResponseEntityBody.getItems().size());
    }

}
