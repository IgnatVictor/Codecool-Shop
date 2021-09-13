package com.codecool.shop.dao;


import com.codecool.shop.dao.memory.ProductDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ProductDaoTest {
    private static ProductDao products;
    private static Product product1 = mock(Product.class);
    private static Product product2 = mock(Product.class);
    private static ProductCategory category = new ProductCategory("Dog Food", "Dog Food", "Dog Food");
    private static Supplier supplier = new Supplier("Happy Dog", "Best dog food");

    @BeforeEach
    void beforeEach() {
        products.remove(1);
        products.remove(2);
    }

    @BeforeAll
    static void beforeAll(){
        products = ProductDaoMem.getInstance();
        when(product1.getId()).thenReturn(1);
        when(product1.getName()).thenReturn("Jack Russel Food");
        when(product1.getSupplier()).thenReturn(supplier);
        when(product1.getProductCategory()).thenReturn(category);
        when(product2.getId()).thenReturn(2);
        when(product2.getSupplier()).thenReturn(new Supplier("Purina", "Good dog food"));
        when(product2.getProductCategory()).thenReturn(new ProductCategory("Cat Food", "Cat Food", "Cat food"));
    }

    @Test
    void add_aProduct_sizeGrowsByOne() {
        products.add(product1);
        int expected = 1;
        assertEquals(expected, products.getAll().size());
    }

    @Test
    void add_twoProducts_SizeGrowsByTwo() {
        products.add(product1);
        products.add(product2);
        int expected = 2;
        assertEquals(expected, products.getAll().size());
    }

    @Test
    void find_addTwoProducts_findOneOfTheseProductsById() {
        products.add(product1);
        products.add(product2);
        Product expected = product1;
        assertEquals(expected, products.find(product1.getId()));
    }

    @Test
    void find_addTwoProducts_findOneOfTheseProductsByName() {
        products.add(product1);
        products.add(product2);
        Product expected = product1;
        assertEquals(expected, products.find(product1.getName()));
    }

    @Test
    void remove_removeOneOfOneAdded_sizeIsZero() {
        products.add(product1);
        products.remove(product1.getId());
        int expected = 0;
        assertEquals(expected, products.getAll().size());
    }


    @Test
    void remove_removeOneOfTwoAdded_sizeIsOne()  {
        products.add(product1);
        products.add(product2);
        products.remove(product1.getId());
        int expected = 1;
        assertEquals(expected, products.getAll().size());
    }

    @Test
    void getAll_addNothing_sizeIsZero() {
        int expected = 0;
        assertEquals(expected, products.getAll().size());
    }

    @Test
    void getAll_addTwoProducts_sizeIsTwo() {
        products.add(product1);
        products.add(product2);
        int expected = 2;
        assertEquals(expected, products.getAll().size());
    }

    @Test
    void getBySupplier_oneAdded_returnsTheSupplier() {
        products.add(product1);
        int size = products.getBy(product1.getSupplier()).size();
        int expected = 1;
        assertEquals(expected, size);
    }


    @Test
    void getBySupplier_addTwo_returnsOneBySupplier() {
        products.add(product1);
        products.add(product2);
        int size = products.getBy(product2.getSupplier()).size();
        int expected = 1;
        assertEquals(expected, size);
    }

    @Test
    void getByCategory_addOne_returnsTheCategory() {
        products.add(product1);
        int size = products.getBy(product1.getProductCategory()).size();
        int expected = 1;
        assertEquals(expected, size);
    }

    @Test
    void getByCategory_addTwo_returnsOneByCategory() {
        products.add(product1);
        products.add(product2);
        int size = products.getBy(product1.getProductCategory()).size();
        int expected = 1;
        assertEquals(expected, size);
    }

}