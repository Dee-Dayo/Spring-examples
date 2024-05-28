package org.suzyBarbie.services;

import org.suzyBarbie.exception.UserNotFoundException;
import org.suzyBarbie.models.User;
import org.suzyBarbie.models.Wallet;
import org.suzyBarbie.repositories.UserRepository;
import org.suzyBarbie.repositories.db.DatabaseConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class UserService {
    private final UserRepository userRepository = new UserRepository();
    private final WalletService walletService = new WalletService();

    public String transferFunds(Long senderId, Long recepientId, BigDecimal amount) {
        try(Connection connection = DatabaseConnectionManager.getInstance().getConnection()) {
            connection.setAutoCommit(false);
            User sender = userRepository.findUserById(connection, senderId).orElseThrow();
            User receiver = userRepository.findUserById(connection, recepientId).orElseThrow();

            Wallet senderWallet = walletService.getWalletBy(connection, sender.getWalletId());
            Wallet receiverWallet = walletService.getWalletBy(connection, receiver.getWalletId());

            senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
            receiverWallet.setBalance(receiverWallet.getBalance().add(amount));

            walletService.update(connection, senderWallet);
            walletService.update(connection, receiverWallet);
            connection.commit();

            return "Transfer successful";
        } catch (SQLException e){
            return "Transfer failed";
        }
    }

    public User getUserById(Connection connection, Long id){
        return userRepository.findUserById(connection, id).orElseThrow(()->new UserNotFoundException("User not found"));
    }
}
