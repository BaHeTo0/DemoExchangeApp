package me.BaHeTo0.demoExchange.services;

import me.BaHeTo0.demoExchange.models.entities.User;
import me.BaHeTo0.demoExchange.models.request.LoginUserRequest;
import me.BaHeTo0.demoExchange.models.request.RegisterUserRequest;

public interface UserService {

    User registerUser(RegisterUserRequest request);

    User loginUser(LoginUserRequest request);
}
