package de.felixalbert.expensetracker.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import de.felixalbert.expensetracker.user.exception.UserAlreadyInUseException;
import de.felixalbert.expensetracker.user.exception.UserNotFoundException;
import de.felixalbert.expensetracker.user.model.User;
import de.felixalbert.expensetracker.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getById(Long id){
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User createUser(String email, String rawPassword) {

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyInUseException();
        }

        String passwordHash = passwordEncoder.encode(rawPassword);

        User user = new User(email, passwordHash);
        return userRepository.save(user);
    }
}
