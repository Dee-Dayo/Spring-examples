package org.suzyBarbie.repositories.db;

import java.sql.*;

public class DatabaseConnectionManager {

    private Connection connection;
    public DatabaseConnectionManager() {}

    private static final class SINGLETON{
        private static final DatabaseConnectionManager databaseConnectionManager = new DatabaseConnectionManager();
    }

    public static DatabaseConnectionManager getInstance() {
        return SINGLETON.databaseConnectionManager;
    }
    public Connection getConnection() {
        if (connection != null) return connection;
        // mysql -> localhost:3306, postgresql -> localhost:5432
        String url = "jdbc:postgresql://localhost:5432/suzies_store";
//        String url = "jdbc:mysql://localhost:3306/suzies_store?createDatabaseIfNotExist=true";
        String username = "postgres";  //root for mysql
        String password = " ";

        try {
            return DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long generateId(String tableName) {
        try(Connection connection = DatabaseConnectionManager.getInstance().getConnection()){
            String sql = "select max(id) from " + tableName;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long lastIdGenerated = resultSet.getLong(1);
            return lastIdGenerated + 1;
        } catch (SQLException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }
}
