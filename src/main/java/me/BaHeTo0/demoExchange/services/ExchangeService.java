package me.BaHeTo0.demoExchange.services;

import me.BaHeTo0.demoExchange.models.entities.ExchangeTransactionEntity;
import me.BaHeTo0.demoExchange.models.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExchangeService {

    List<String> getCurrencies();
    ExchangeTransactionEntity convert(User user, BigDecimal amount, String from, String to);
    List<ExchangeTransactionEntity> transactionHistory(int page, int pageSize, LocalDate date, UUID transactionId, UUID userId);

    BigDecimal getRate(String from, String to);

}
