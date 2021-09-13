package com.codecool.shop.config;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.jdbc.ProductCategoryDaoJdbc;
import com.codecool.shop.dao.jdbc.ProductDaoJdbc;
import com.codecool.shop.dao.jdbc.SupplierDaoJdbc;
import com.codecool.shop.dao.memory.ProductCategoryDaoMem;
import com.codecool.shop.dao.memory.ProductDaoMem;
import com.codecool.shop.dao.memory.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

@WebListener
public class Initializer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

    ProductCategory food = new ProductCategory("Food", "Pet Supplies", "Food you give your pet");
    ProductCategory apparel = new ProductCategory("Apparel", "Pet Supplies", "Clothes you make your pet wear");
    ProductCategory toy = new ProductCategory("Toy", "Pet Supplies", "Toy for your pets to play with");

    Supplier catCostumeCompany = new Supplier("Beautiful Cat Costumes", "Costumes for cats");
    Supplier dogCostumeCompany = new Supplier("Best Dog Costumes", "Costumes for dogs");
    Supplier toyCompany = new Supplier("Toys for all", "Toys for pets");
    Supplier petFoodCompany = new Supplier("Best Pet Food", "Food for pets");

    Product catWings = new Product("Cat wings", 13.9f, "USD",
            "Fantastic price. Looks great on black cats.", apparel, catCostumeCompany);
    Product catCostume = new Product("Cat costume", 10.9f, "USD",
            "How adorable is that? Soft and cute.", apparel, catCostumeCompany);
    Product dogCostume = new Product("Dog costume", 10.9f, "USD",
            "Durable material, best for play dates.", apparel, dogCostumeCompany);
    Product dogDress = new Product("Dog dress", 13.9f, "USD",
            "Colorful dress, perfect for smaller dogs.", apparel, dogCostumeCompany);
    Product catFood = new Product("Premium quality cat food 7kg", 20.9f, "USD",
            "Best dry cat food you can get.", food, petFoodCompany);
    Product dogFood = new Product("Dry dog food 7kg", 32.9f, "USD",
            "Vet recommended dry food for your dog.", food, petFoodCompany);
    Product dogFood2 = new Product("Dry dog food 14kg", 29.9f, "USD",
            "Get more, pay less.", food, petFoodCompany);
    Product catnipToy = new Product("Catnip toys", 13.9f, "USD",
            "Your cat will go crazy for these toys.", toy, toyCompany);
    Product lobsterToy = new Product("Lobster chew toy", 16.9f, "USD",
            "Colorful chew toy for your dog.", toy, toyCompany);
    Product monkeyToy = new Product("Monkey chew toy", 17.9f, "USD",
            "Soft and funny.", toy, toyCompany);
    Product parrotToy = new Product("Parrot chew toy", 15.9f, "USD",
            "This parrot plush could be your dog's best friend.", toy, toyCompany);


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            if (ConnectionManager.getDaoType().equals("jdbc")) {
                dataBaseInitializer();
            } else if(ConnectionManager.getDaoType().equals("memory")){
                memoryInitializer();
            }
        } catch (IOException | SQLException e) {
            logger.error("Database connection error", e);
        }
    }

    private void dataBaseInitializer() throws IOException, SQLException {
        DataSource dataSource = ConnectionManager.connect();
        ProductCategoryDao productCategoryDataStore = new ProductCategoryDaoJdbc(dataSource);
        SupplierDao supplierDataStore = new SupplierDaoJdbc(dataSource);
        ProductDao productDataStore = new ProductDaoJdbc(dataSource, productCategoryDataStore, supplierDataStore);

        initializeStoreProducts(productCategoryDataStore, supplierDataStore, productDataStore);
    }

    private void memoryInitializer() {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();

        initializeStoreProducts(productCategoryDataStore, supplierDataStore, productDataStore);
    }

    private void initializeStoreProducts(ProductCategoryDao productCategoryDataStore, SupplierDao supplierDataStore, ProductDao productDataStore) {
        for (Supplier supplier : Arrays.asList(dogCostumeCompany, catCostumeCompany, toyCompany, petFoodCompany)) {
            supplierDataStore.add(supplier);
        }

        for (ProductCategory productCategory : Arrays.asList(food, apparel, toy)) {
            productCategoryDataStore.add(productCategory);
        }

        for (Product product : Arrays.asList(catWings, catCostume, dogCostume, dogDress, catFood, dogFood, dogFood2,
                catnipToy, lobsterToy, monkeyToy, parrotToy)) {
            productDataStore.add(product);
        }
    }
}
