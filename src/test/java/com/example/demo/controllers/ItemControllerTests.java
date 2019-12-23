package com.example.demo.controllers;

import com.example.demo.TestData;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTests {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemById() {
        Item item = TestData.getTestItem();
        when(itemRepository.findById(item.getId())).thenReturn(java.util.Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(item.getId());
        assertNotNull(response);
        Item responseBody = response.getBody();
        assertEquals(item.getName(), responseBody.getName());

    }
}
