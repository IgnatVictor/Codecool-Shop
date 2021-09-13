package com.codecool.shop.config;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    public static DataSource connect() throws SQLException, IOException {
        Properties connectionProps = getProperties("connection.properties");

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = connectionProps.getProperty("database");
        String user = connectionProps.getProperty("user");
        String password = connectionProps.getProperty("password");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        dataSource.getConnection().close();

        return dataSource;
    }

    public static Properties getProperties(String filename) throws IOException {
        Path currentRelativePath = Paths.get("");
        String rootPath = currentRelativePath.toAbsolutePath().toString();
        String connectionConfigPath = rootPath + "/src/main/resources/" + filename;

        Properties connectionProps = new Properties();
        connectionProps.load(new FileInputStream(connectionConfigPath));
        return connectionProps;
    }

    public static String getDaoType() throws IOException {
        Properties connectionProps = getProperties("connection.properties");
        return connectionProps.getProperty("dao");
    }

}
