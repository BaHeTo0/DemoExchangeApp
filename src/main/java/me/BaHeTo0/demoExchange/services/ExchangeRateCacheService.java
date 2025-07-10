package me.BaHeTo0.demoExchange.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import me.BaHeTo0.demoExchange.models.responses.ExternalApiExchangeRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Service
public class ExchangeRateCacheService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateCacheService.class);

    private static final String CACHE_FOLDER = "src/main/resources/exchange-rate-cache";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ExternalApiExchangeRateResponse cachedRates;

    public ExternalApiExchangeRateResponse getCachedRates() {
        return cachedRates;
    }

    @PostConstruct
    public void loadRatesOnStartup() throws IOException {
        log.info("Loading exchange rates on startup!");
        loadRates();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateRatesOnSchedule() throws IOException {
        log.info("Scheduled update on exchange rates");
        loadRates();
    }

    /*
    Since the exchange rates API is limited to 100 calls/month
    We cache them and update daily
    */
    public void loadRates() throws IOException {
        LocalDate today = LocalDate.now();
        Path filePath = Path.of(CACHE_FOLDER, "exchangerates-" + today + ".json");

        if(Files.exists(filePath)) {
            log.info("ExchangeRateCache hit for date {} ,loading rates from file!", today);

            cachedRates = objectMapper.readValue(filePath.toFile(), ExternalApiExchangeRateResponse.class);
        } else {
            log.info("ExchangeRateCache miss for date {}, pulling rates from API", today);

            String url = "http://data.fixer.io/api/latest?access_key=41cc7bb36703873b15032693e14fe6b3&base=EUR";
            ExternalApiExchangeRateResponse response = new RestTemplate().getForObject(url, ExternalApiExchangeRateResponse.class);
            cachedRates = response;

            if(response != null && response.isSuccess()) {
                log.info("API call for latest rates was success, caching the results");

                Files.createDirectories(filePath.getParent());
                objectMapper.writeValue(filePath.toFile(), cachedRates);
            } else {
                log.error("Exchange rate API error: code={} type={}", response.getError().getCode(), response.getError().getType());

                throw new IllegalStateException("Failed to fetch exchange rates: " + response.getError().getType());
            }

        }

    }

}
