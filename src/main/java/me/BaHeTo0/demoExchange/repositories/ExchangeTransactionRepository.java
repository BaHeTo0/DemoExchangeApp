package me.BaHeTo0.demoExchange.repositories;

import me.BaHeTo0.demoExchange.models.entities.ExchangeTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExchangeTransactionRepository extends JpaRepository<ExchangeTransactionEntity, UUID>, JpaSpecificationExecutor<ExchangeTransactionEntity> {

}
