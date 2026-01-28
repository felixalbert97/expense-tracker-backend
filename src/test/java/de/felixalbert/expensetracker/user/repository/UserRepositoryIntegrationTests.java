package de.felixalbert.expensetracker.user.repository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import de.felixalbert.expensetracker.user.model.User;
import de.felixalbert.expensetracker.user.model.UserTestDataBuilder;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_returnsUser() {
        // Arrange
        User user = userRepository.save(
            UserTestDataBuilder.aUser().build()
        );

        // Act
        Optional<User> found = userRepository.findByEmail(user.getEmail());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }
}