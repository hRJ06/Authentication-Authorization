package com.Hindol.SpringSecurity.RepositoryTest;

import com.Hindol.SpringSecurity.Model.Role;
import com.Hindol.SpringSecurity.Model.User;
import com.Hindol.SpringSecurity.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void UserRepository_Save_ReturnSavedUser() {
        User user = new User();
        user.setEmail("hindol.roy@gmail.com");
        user.setFirstName("Hindol");
        user.setLastName("Roy");
        user.setRole(Role.USER);
        user.setPassword("hR06");
        User savedUser = this.userRepository.save(user);
        assertNotNull(savedUser);
        assertTrue(user.getId() > 0);
    }

}
