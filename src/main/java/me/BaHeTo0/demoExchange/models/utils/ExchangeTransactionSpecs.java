package me.BaHeTo0.demoExchange.models.utils;

import jakarta.persistence.criteria.Predicate;
import me.BaHeTo0.demoExchange.models.entities.ExchangeTransactionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExchangeTransactionSpecs {

    public static Specification<ExchangeTransactionEntity> filter(UUID userId, UUID transactionId, LocalDate date) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(cb.equal(root.get("userId"), userId));
            }
            if (transactionId != null) {
                predicates.add(cb.equal(root.get("id"), transactionId));
            }
            if (date != null) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
                predicates.add(cb.between(root.get("date"), startOfDay, endOfDay));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
