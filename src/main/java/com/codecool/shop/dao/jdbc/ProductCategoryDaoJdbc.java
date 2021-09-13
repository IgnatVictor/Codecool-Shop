package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJdbc implements ProductCategoryDao {
    private final DataSource dataSource;

    public ProductCategoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(ProductCategory productCategory) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO product_category(name, description, department) VALUES(?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, productCategory.getName());
            statement.setString(2, productCategory.getDescription());
            statement.setString(3, productCategory.getDepartment());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            productCategory.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ProductCategory find(int id) {
        ProductCategory productCategory = null;
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, description, department FROM product_category WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                productCategory = new ProductCategory(resultSet.getString("name"), resultSet.getString("department"), resultSet.getString("description"));
                productCategory.setId(id);
            }
            return productCategory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductCategory find(String name) {
        ProductCategory productCategory = null;
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, name, description, department FROM product_category WHERE name = ? LIMIT 1";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                productCategory = new ProductCategory(resultSet.getString("name"), resultSet.getString("department"), resultSet.getString("description"));
                productCategory.setId(resultSet.getInt("id"));
            }
            return productCategory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(int id) {}

    @Override
    public List<ProductCategory> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            List<ProductCategory> productCategories = new ArrayList<>();
            String sql = "SELECT DISTINCT id, name, department, description FROM product_category";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            ProductCategory productCategory;
            while (resultSet.next()) {
                productCategory = new ProductCategory(resultSet.getString("name"), resultSet.getString("department"), resultSet.getString("description"));
                productCategories.add(productCategory);
                productCategory.setId(resultSet.getInt("id"));
            }
            return productCategories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
