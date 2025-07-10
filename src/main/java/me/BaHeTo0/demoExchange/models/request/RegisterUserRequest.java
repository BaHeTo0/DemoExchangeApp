package me.BaHeTo0.demoExchange.models.request;

import java.math.BigDecimal;

public class RegisterUserRequest {
    private String username;
    private BigDecimal startingBalance;

    public RegisterUserRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(BigDecimal startingBalance) {
        this.startingBalance = startingBalance;
    }
}
