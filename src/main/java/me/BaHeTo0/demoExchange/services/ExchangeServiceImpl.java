package me.BaHeTo0.demoExchange.services;

import jakarta.transaction.Transactional;
import me.BaHeTo0.demoExchange.models.exceptions.InvalidInputException;
import me.BaHeTo0.demoExchange.models.utils.ExchangeTransactionSpecs;
import me.BaHeTo0.demoExchange.models.entities.Balance;
import me.BaHeTo0.demoExchange.models.entities.ExchangeTransactionEntity;
import me.BaHeTo0.demoExchange.models.entities.User;
import me.BaHeTo0.demoExchange.models.exceptions.InsufficientFundsException;
import me.BaHeTo0.demoExchange.models.responses.ExternalApiExchangeRateResponse;
import me.BaHeTo0.demoExchange.repositories.ExchangeTransactionRepository;
import me.BaHeTo0.demoExchange.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeServiceImpl.class);

    @Autowired
    private ExchangeTransactionRepository exchangeRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired ExchangeRateCacheService exchangeRateCache;

    @Override
    public List<String> getCurrencies() {
        log.info("Getting the currencies list!");

        Set<String> rateKeys = exchangeRateCache.getCachedRates().getRates().keySet();
        String base = exchangeRateCache.getCachedRates().getBase();

        List<String> currencies = new ArrayList<>(rateKeys);
        currencies.add(base);

        return currencies;
    }

    @Override
    public BigDecimal getRate(String fromCurr, String toCurr) {
        log.info("Getting conversion rate for {} to {}", fromCurr, toCurr);

        ExternalApiExchangeRateResponse rates = exchangeRateCache.getCachedRates();
        String baseCurr = rates.getBase();

        if (fromCurr.equals(baseCurr)) {
            return rates.getRates().get(toCurr);
        } else if (toCurr.equals(baseCurr)) {
            BigDecimal fromCurrRate = rates.getRates().get(fromCurr);
            return BigDecimal.ONE.divide(fromCurrRate,10);
        } else {
            BigDecimal toCurrRate = rates.getRates().get(toCurr);
            BigDecimal fromCurrRate = rates.getRates().get(fromCurr);

            return toCurrRate.divide(fromCurrRate, 10);
        }
    }

    @Override
    @Transactional
    public ExchangeTransactionEntity convert(User currentUser, BigDecimal amount, String from, String to) {
        final BigDecimal rate = getRate(from, to);
        final Balance userFromBalance = currentUser.getBalances().getOrDefault(from, new Balance(currentUser.getId(), from, BigDecimal.ZERO));
        final Balance userToBalance = currentUser.getBalances().getOrDefault(from, new Balance(currentUser.getId(), to, BigDecimal.ZERO));

        if(amount.compareTo(BigDecimal.ZERO)<=0) {
            throw new InvalidInputException("Invalid input!");
        }
        if( !hasSufficientAmount(userFromBalance, amount) ){
            throw new InsufficientFundsException("Insufficient funds to complete exchange!");
        } else {
            log.info("Starting convert transaction: {} {} to {}", amount.toPlainString(), from, to);
            userFromBalance.setAmount(userFromBalance.getAmount().subtract(amount));
            userToBalance.setAmount(userToBalance.getAmount().add(amount.multiply(rate)));

            currentUser.getBalances().put(from, userFromBalance);
            currentUser.getBalances().put(to, userToBalance);

            ExchangeTransactionEntity transactionEntity = new ExchangeTransactionEntity(null, new Date(), currentUser.getId(), rate, from, to, amount);
            userRepository.save(currentUser);
            exchangeRepository.save(transactionEntity);
            log.info("Transaction complete!");
            return transactionEntity;
        }
    }

    private boolean hasSufficientAmount(Balance bal, BigDecimal amount) {
        return bal.getAmount().compareTo(amount)>=0;
    }

    @Override
    public List<ExchangeTransactionEntity> transactionHistory(int page, int pageSize, LocalDate date, UUID transactionId, UUID userId) {
        final String transactionIdStr = transactionId != null ? transactionId.toString() : "null";
        final String userIdStr = userId != null ? userId.toString() : "null";
        final String dateStr = date != null ? date.toString() : "null";
        log.info("Getting transaction history with filter: (id: {}, userId: {}, date: {})(page: {}, pageSize{})", transactionIdStr,userIdStr, dateStr, page, pageSize);
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<ExchangeTransactionEntity> spec = ExchangeTransactionSpecs.filter(userId, transactionId, date);
        return exchangeRepository.findAll(spec, pageable).getContent();
    }
}
