package org.suzyBarbie.repositories;

import org.suzyBarbie.exception.UserUpdateFailedException;
import org.suzyBarbie.models.Wallet;
import org.suzyBarbie.repositories.db.DatabaseConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class WalletRepository {

    public Wallet save(Wallet wallet) {
        try(Connection connection = DatabaseConnectionManager.getInstance().getConnection()){
            var databaseManager = DatabaseConnectionManager.getInstance();
            String sql = "INSERT into wallet(id, balance) VALUES(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            Long id = databaseManager.generateId("wallet");
            wallet.setId(id);
            preparedStatement.setLong(1, wallet.getId());
            preparedStatement.setBigDecimal(2, wallet.getBalance());
            preparedStatement.executeUpdate();
            return wallet;
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<Wallet> findById(Connection connection, Long id){
        try{
            String sql = "SELECT id, balance from wallet where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long walletId = resultSet.getLong("id");
            BigDecimal balance = resultSet.getBigDecimal("balance");
            Wallet wallet = new Wallet();
            wallet.setId(walletId);
            wallet.setBalance(balance);
            return Optional.of(wallet);
        }catch (SQLException e){
            System.err.println("Error: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Wallet updateWallet(Connection connection, Long walletId, BigDecimal balance){
        try{
            String sql = "UPDATE wallet SET balance = ? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBigDecimal(1, balance);
            statement.setLong(2, walletId);
            statement.executeUpdate();
            return findById(connection, walletId).orElseThrow();
        } catch (SQLException e){
            System.err.println(e.getMessage());
            throw new UserUpdateFailedException("Failed to find user");
        }
    }
}
