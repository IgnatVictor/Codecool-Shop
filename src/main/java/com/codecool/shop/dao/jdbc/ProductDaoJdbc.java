package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.memory.ProductCategoryDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJdbc implements ProductDao {
    private DataSource dataSource;
    private ProductCategoryDao productCategoryDao;
    private SupplierDao supplierDao;

    public ProductDaoJdbc(DataSource dataSource, ProductCategoryDao productCategoryDao, SupplierDao supplierDao) {
        this.dataSource = dataSource;
        this.productCategoryDao = productCategoryDao;
        this.supplierDao = supplierDao;
    }

    @Override
    public void add(Product product) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO product (name, description, default_price, default_currency, product_category_id, supplier_id)" +
                    " VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setFloat(3, product.getDefaultPrice());
            statement.setString(4, product.getDefaultCurrency().getCurrencyCode());
            statement.setInt(5, product.getProductCategory().getId());
            statement.setInt(6, product.getSupplier().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            product.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT name, description, default_price, default_currency, product_category_id, supplier_id" +
                    " FROM product WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(resultSet.getString("name"), resultSet.getFloat("default_price"),
                        resultSet.getString("default_currency"), resultSet.getString("description"),
                        productCategoryDao.find(resultSet.getInt("product_category_id")),
                        supplierDao.find(resultSet.getInt("supplier_id")));
                product.setId(id);
                return product;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Product find(String name) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Product> getAll() {
        return null;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, name, description, default_price, default_currency, product_category_id" +
                    " FROM product WHERE supplier_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, supplier.getId());
            ResultSet resultSet = statement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product(resultSet.getString("name"), resultSet.getFloat("default_price"),
                        resultSet.getString("default_currency"), resultSet.getString("description"),
                        productCategoryDao.find(resultSet.getInt("product_category_id")),
                        supplierDao.find(supplier.getId()));
                product.setId(resultSet.getInt("id"));
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, name, description, default_price, default_currency, supplier_id" +
                    " FROM product WHERE product_category_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, productCategory.getId());
            ResultSet resultSet = statement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product(resultSet.getString("name"), resultSet.getFloat("default_price"),
                        resultSet.getString("default_currency"), resultSet.getString("description"),
                        productCategoryDao.find(productCategory.getId()),
                        supplierDao.find(resultSet.getInt("supplier_id")));
                product.setId(resultSet.getInt("id"));
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
