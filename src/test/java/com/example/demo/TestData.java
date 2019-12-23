package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.math.BigDecimal;

public class TestData {

    public static User getTestUser() {
        return getTestUser(new Cart());
    }

    public static User getTestUser(Cart cart) {
        User user = new User();
        user.setUsername("john");
        user.setPassword("somepassword");
        user.setConfirmPassword("somepassword");
        user.setCart(cart);
        return user;
    }

    public static Item getTestItem() {
        Item item = new Item();
        item.setPrice(BigDecimal.valueOf(30));
        item.setId(1L);
        item.setName("someitem");
        return item;
    }
}
