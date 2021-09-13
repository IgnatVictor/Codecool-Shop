package com.codecool.shop.dao;


import com.codecool.shop.dao.memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.memory.ProductDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.service.ProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ProductServiceTest {
    public static final int NUMBER_OF_ITEMS = 3;
    private static ProductDao products;
    private static ProductCategoryDao categories;
    private static Product product1 = mock(Product.class);
    private static Product product2 = mock(Product.class);
    private static Product product3 = mock(Product.class);
    private static ProductCategory category1 = mock(ProductCategory.class);
    private static ProductCategory category2 = mock(ProductCategory.class);
    private static ProductCategory category3 = mock(ProductCategory.class);
    private static ProductService productService;

    @BeforeEach
    void beforeEach() {
        for (int i = 1; i <= NUMBER_OF_ITEMS; i++) {
            products.remove(i);
            categories.remove(i);
        }
    }

    @BeforeAll
    static void beforeAll() {
        categories = ProductCategoryDaoMem.getInstance();
        products = ProductDaoMem.getInstance();
        when(category1.getId()).thenReturn(1);
        when(category1.getProducts()).thenReturn(Arrays.asList(new Product[1]));
        when(category2.getId()).thenReturn(2);
        when(category2.getProducts()).thenReturn(Arrays.asList(new Product[1]));
        when(product1.getId()).thenReturn(1);
        when(product1.getProductCategory()).thenReturn(category1);
        when(product2.getId()).thenReturn(2);
        when(product2.getProductCategory()).thenReturn(category2);
        when(product3.getId()).thenReturn(3);
        when(product3.getProductCategory()).thenReturn(category1);
        productService = new ProductService(products, categories);
    }

    @Test
    void getProductCategory_AddOneCategory_getAProductCategoryById() {
        products.add(product1);
        categories.add(category1);
        ProductCategory expected = category1;
        assertEquals(expected, productService.getProductCategory(1));
    }

    @Test
    void getProductCategory_AddTwoCategories_getAProductCategoryById() {
        products.add(product1);
        categories.add(category1);
        categories.add(category2);
        ProductCategory expected = category1;
        assertEquals(expected, productService.getProductCategory(1));
    }


    @Test
    void getProductCategory_AddTwoProducts_getAProductCategoryById() {
        products.add(product1);
        products.add(product2);
        categories.add(category1);
        categories.add(category2);
        ProductCategory expected = category1;
        ProductCategory expected2 = category2;
        assertEquals(expected, productService.getProductCategory(1));
        assertEquals(expected2, productService.getProductCategory(2));
    }

    @Test
    void getProductCategory_addNoProducts_getNull() {
        assertNull(productService.getProductCategory(1));
    }

    @Test
    void getProductsForCategory_addNoProductOrCategory_getAListOfASizeOfZero() {
        int expected = 0;
        assertEquals(expected, productService.getProductsForCategory(1).size());
    }

    @Test
    void getProductsForCategory_AddOneProduct_getAListOfASizeOfOne() {
        products.add(product1);
        categories.add(category1);
        int expected = 1;
        assertEquals(expected, productService.getProductsForCategory(1).size());
    }

    @Test
    void getProductsForCategory_AddTwoProductsButOnlyOneFromExactCategory_getAListOfASizeOfOne() {
        products.add(product1);
        products.add(product2);
        categories.add(category1);
        int expected = 1;
        assertEquals(expected, productService.getProductsForCategory(1).size());
    }

    @Test
    void getProductsForCategory_AddTwoProductsBothFromExactCategory_getAListOfASizeOfTwo() {
        products.add(product1);
        products.add(product3);
        categories.add(category1);
        int expected = 2;
        assertEquals(expected, productService.getProductsForCategory(1).size());
    }

    @Test
    void getProductsForCategory_AddTwoProductsBothFromExactCategory_getAListOfThem() {
        products.add(product1);
        products.add(product3);
        categories.add(category1);
        List<Product> expected = List.of(product1, product3);
        assertEquals(expected, productService.getProductsForCategory(1));
    }

    @Test
    void getProductsForCategory_AddTwoProductsFromExactCategoryThenRemoveOne_getAListOfTheRemainingOne() {
        products.add(product1);
        products.add(product3);
        categories.add(category1);
        List<Product> expected = List.of(product1, product3);
        assertEquals(expected, productService.getProductsForCategory(1));
        products.remove(3);
        List<Product> expected2 = List.of(product1);
        assertEquals(expected2, productService.getProductsForCategory(1));
    }

    @Test
    void getProductsForCategory_AddTwoProductsFromExactCategoryThenRemoveOne_getAListSizeOfOne() {
        products.add(product1);
        products.add(product3);
        categories.add(category1);
        int expected = 2;
        assertEquals(expected, productService.getProductsForCategory(1).size());
        products.remove(3);
        int expected2 = 1;
        assertEquals(expected2, productService.getProductsForCategory(1).size());
    }

}