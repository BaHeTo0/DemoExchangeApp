package me.BaHeTo0.demoExchange.models.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class BalanceId implements Serializable {
    private UUID userId;
    private String currency;

    public BalanceId() {}

    public BalanceId(UUID userId, String currency) {
        this.userId = userId;
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BalanceId)) return false;
        BalanceId that = (BalanceId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, currency);
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
