package me.BaHeTo0.demoExchange.config.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.BaHeTo0.demoExchange.models.entities.User;
import me.BaHeTo0.demoExchange.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserAuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    public static final String USER_ATTR = "authenticatedUser";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing X-User-Id header");
            return false;
        }

        try {
            UUID userId = UUID.fromString(userIdHeader);
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid user ID");
                return false;
            }
            request.setAttribute(USER_ATTR, user.get());
            return true;
        } catch (IllegalArgumentException e) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Malformed user ID");
            return false;
        }
    }
}
