package org.suzieBarbie.services;

import org.junit.jupiter.api.Test;
import org.suzyBarbie.models.User;
import org.suzyBarbie.services.UserService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserServiceTest {

    private final UserService userService = new UserService();


    @Test
    public void testTransferFunds(){
        Long senderId = 11L;
        Long recipientId = 3L;
        BigDecimal amount = new BigDecimal(100);
        String response = userService.transferFunds(senderId, recipientId, amount);
        assertNotNull(response);
    }
}
