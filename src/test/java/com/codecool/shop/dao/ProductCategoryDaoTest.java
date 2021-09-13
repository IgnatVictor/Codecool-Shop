package com.codecool.shop.dao;


import com.codecool.shop.dao.memory.ProductCategoryDaoMem;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ProductCategoryDaoTest {
    private static ProductCategoryDao productCategories;
    private static ProductCategory productCategory = mock(ProductCategory.class);
    private static ProductCategory productCategory1 = mock(ProductCategory.class);

    @BeforeEach
    void beforeEach() {
        productCategories.remove(1);
        productCategories.remove(2);
    }

    @BeforeAll
    static void beforeAll(){
        productCategories = ProductCategoryDaoMem.getInstance();
        when(productCategory.getId()).thenReturn(1);
        when(productCategory.getName()).thenReturn("Dog Food");
        when(productCategory1.getId()).thenReturn(2);
    }

    @Test
    void add_aProduct_sizeGrowsByOne() {
        productCategories.add(productCategory);
        int expected = 1;
        assertEquals(expected, productCategories.getAll().size());
    }

    @Test
    void add_twoProducts_SizeGrowsByTwo() {
        productCategories.add(productCategory);
        productCategories.add(productCategory1);
        int expected = 2;
        assertEquals(expected, productCategories.getAll().size());
    }

    @Test
    void find_addTwoProducts_findOneOfTheseProductsById() {
        productCategories.add(productCategory);
        productCategories.add(productCategory1);
        ProductCategory expected = productCategory;
        assertEquals(expected, productCategories.find(productCategory.getId()));
    }

    @Test
    void find_addTwoProducts_findOneOfTheseProductsByName() {
        productCategories.add(productCategory);
        productCategories.add(productCategory1);
        ProductCategory expected = productCategory;
        assertEquals(expected, productCategories.find(productCategory.getName()));
    }

    @Test
    void remove_removeOneOfOneAdded_sizeIsZero() {
        productCategories.add(productCategory);
        productCategories.remove(productCategory.getId());
        int expected = 0;
        assertEquals(expected, productCategories.getAll().size());
    }


    @Test
    void remove_removeOneOfTwoAdded_sizeIsOne()  {
        productCategories.add(productCategory);
        productCategories.add(productCategory1);
        productCategories.remove(productCategory.getId());
        int expected = 1;
        assertEquals(expected, productCategories.getAll().size());
    }

    @Test
    void getAll_addNothing_sizeIsZero() {
        int expected = 0;
        assertEquals(expected, productCategories.getAll().size());
    }

    @Test
    void getAll_addTwoProducts_sizeIsTwo() {
        productCategories.add(productCategory);
        productCategories.add(productCategory1);
        int expected = 2;
        assertEquals(expected, productCategories.getAll().size());
    }

}