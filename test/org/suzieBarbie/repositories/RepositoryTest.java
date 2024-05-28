package org.suzieBarbie.repositories;

import org.junit.jupiter.api.Test;
import org.suzyBarbie.models.User;
import org.suzyBarbie.repositories.UserRepository;
import org.suzyBarbie.repositories.db.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {

    private final UserRepository userRepository = new UserRepository();
    private final DatabaseConnectionManager databaseConnectionManager = new DatabaseConnectionManager();

    @Test
    public void testDatabaseConnection(){
        try(Connection connection = databaseConnectionManager.getConnection()){
            assertNotNull(connection);
        } catch (SQLException e) {
            assertNull(e);
        }
    }

    @Test
    public void saveUserTest(){
        User user = new User();
        User savedUser = userRepository.saveUser(user);
        assertNotNull(savedUser);
    }

    @Test
    public void testUpdateUser(){
        Long userId = 1L;
        Long walletId = 500L;
        Connection connection = DatabaseConnectionManager.getInstance().getConnection();
        User user = userRepository.updateUser(connection, userId, walletId);
        assertNotNull(user);
        assertEquals(500L, user.getWalletId());
    }

    @Test
    public void testDeleteUser(){
        Long userId = 7L;
        userRepository.deleteUser(userId);
        Connection connection = DatabaseConnectionManager.getInstance().getConnection();
        Optional<User> user = userRepository.findUserById(connection, 7L);
        assertTrue(user.isEmpty());
    }

    @Test
    public void testFindAllUsers(){
        List<User> users = userRepository.findAll();
        assertEquals(11, users.size());
    }

}
