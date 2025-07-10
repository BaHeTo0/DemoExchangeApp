package me.BaHeTo0.demoExchange.models.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Balance {

    @EmbeddedId
    private BalanceId id;
    private BigDecimal amount;

    public Balance() {
    }

    public Balance(UUID userId, String currency, BigDecimal amount) {
        this.id = new BalanceId(userId,currency);
        this.amount = amount;
    }

    public UUID getUserId() {
        return id.getUserId();
    }

    public void setUserId(UUID userId) {
        this.id.setUserId(userId);
    }

    public String getCurrency() {
        return id.getCurrency();
    }

    public void setCurrency(String currency) {
        this.setCurrency(currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
