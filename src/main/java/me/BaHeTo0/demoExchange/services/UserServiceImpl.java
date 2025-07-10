package me.BaHeTo0.demoExchange.services;

import me.BaHeTo0.demoExchange.models.entities.Balance;
import me.BaHeTo0.demoExchange.models.entities.User;
import me.BaHeTo0.demoExchange.models.request.LoginUserRequest;
import me.BaHeTo0.demoExchange.models.request.RegisterUserRequest;
import me.BaHeTo0.demoExchange.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final String STARTING_BALANCE_CURRENCY = "EUR";
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(RegisterUserRequest request) {
        log.info("Registering username: {} with starting balance of {} EUR", request.getUsername(), request.getStartingBalance());

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        User savedUser = userRepository.save(newUser);

        Map<String, Balance> startingBalance = savedUser.getBalances();
        startingBalance.put(STARTING_BALANCE_CURRENCY, new Balance(savedUser.getId(), STARTING_BALANCE_CURRENCY, request.getStartingBalance()));
        return userRepository.save(savedUser);
    }

    @Override
    public User loginUser(LoginUserRequest request) {
        log.info("Logging in username: {}", request.getUsername());
        return userRepository.findByUsername(request.getUsername()).get();
    }
}
