package me.BaHeTo0.demoExchange.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
public class ExchangeTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transactionId;
    private Date date;
    private UUID userId;
    private BigDecimal rate;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;

    public ExchangeTransactionEntity() {
    }

    public ExchangeTransactionEntity(UUID transactionId, Date date, UUID userId,BigDecimal rate, String fromCurrency, String toCurrency, BigDecimal amount) {
        this.transactionId = transactionId;
        this.date = date;
        this.userId = userId;
        this.rate = rate;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
