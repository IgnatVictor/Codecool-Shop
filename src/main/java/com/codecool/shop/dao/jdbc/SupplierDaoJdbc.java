package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJdbc implements SupplierDao {
    private final DataSource dataSource;

    public SupplierDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Supplier supplier) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO supplier(name, description) VALUES(?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getDescription());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            supplier.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Supplier find(int id) {
        Supplier supplier = null;
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, description FROM supplier WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                supplier = new Supplier(resultSet.getString("name"), resultSet.getString("description"));
                supplier.setId(id);
            }
            return supplier;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Supplier find(String name) {
        Supplier supplier = null;
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, name, description FROM supplier WHERE name = ? LIMIT 1";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                supplier = new Supplier(resultSet.getString("name"), resultSet.getString("description"));
                supplier.setId(resultSet.getInt("id"));
            }
            return supplier;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(int id) {}

    @Override
    public List<Supplier> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            List<Supplier> suppliers = new ArrayList<>();
            String sql = "SELECT DISTINCT id, name, description FROM supplier";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            Supplier supplier;
            while (resultSet.next()) {
                supplier = new Supplier(resultSet.getString("name"), resultSet.getString("description"));
                suppliers.add(supplier);
                supplier.setId(resultSet.getInt("id"));
            }
            return suppliers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
