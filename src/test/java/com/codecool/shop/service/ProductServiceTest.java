package com.codecool.shop.service;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    private static ProductDao productDao;
    private static ProductCategoryDao productCategoryDao;
    private static ProductService productService;
    static List<Product> testProducts = new ArrayList<>();
    static List<Product> empty = new ArrayList<>();

    @BeforeAll
    static void setup(){
        productDao = mock(ProductDao.class);
        productCategoryDao = mock(ProductCategoryDao.class);
        ProductCategory testCategory = new ProductCategory("Test", "test", "test");
        ProductCategory emptyCategory = new ProductCategory("Empty","empty", "empty");
        Product testProduct = mock(Product.class);
        Product testProduct2 = mock(Product.class);

        testProducts.add(testProduct);
        testProducts.add(testProduct2);

        when(productCategoryDao.find(0)).thenReturn(null);

        when(productCategoryDao.find(1)).thenReturn(testCategory);
        when(productDao.getBy(testCategory)).thenReturn(testProducts);

        when(productCategoryDao.find(2)).thenReturn(emptyCategory);
        when(productDao.getBy(emptyCategory)).thenReturn(empty);

        productService = new ProductService(productDao, productCategoryDao);
    }

    @Test
    void getNo_Products_For_MissingCategory() {
        assertEquals(empty, productService.getProductsForCategory(0));
    }

    @Test
    void getAll_Products_For_Category() {
        assertEquals(testProducts, productService.getProductsForCategory(1));
    }

    @Test
    void getNo_Products_For_EmptyCategory() {
        assertEquals(empty, productService.getProductsForCategory(2));
    }

}