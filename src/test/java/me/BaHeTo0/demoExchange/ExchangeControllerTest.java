package me.BaHeTo0.demoExchange;

import jakarta.transaction.Transactional;
import me.BaHeTo0.demoExchange.models.entities.Balance;
import me.BaHeTo0.demoExchange.models.entities.ExchangeTransactionEntity;
import me.BaHeTo0.demoExchange.models.entities.User;
import me.BaHeTo0.demoExchange.repositories.UserRepository;
import me.BaHeTo0.demoExchange.services.ExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExchangeControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockitoBean
    private ExchangeService exchangeService;
    @Autowired
    private UserRepository userRepository;

    private static final String TEST_USERNAME = "test_user_123";
    private static final String TEST_STARTING_BALANCE_CURRENCY = "EUR";
    private static final BigDecimal TEST_STARTING_BALANCE_AMOUNT = BigDecimal.valueOf(1000000);


    @BeforeEach
    void setUp() {
        userRepository.deleteByUsername(TEST_USERNAME);

        User testUser = new User();
        testUser.setUsername(TEST_USERNAME);

        User savedTestUser = userRepository.save(testUser);
        Map<String, Balance> startingBalance = savedTestUser.getBalances();
        startingBalance.put(TEST_STARTING_BALANCE_CURRENCY, new Balance(savedTestUser.getId(), TEST_STARTING_BALANCE_CURRENCY, TEST_STARTING_BALANCE_AMOUNT));
    }

    @Test
    void testGetCurrencies() throws Exception {
        List<String> mockCurrencies = List.of("USD", "EUR", "GBP");
        Mockito.when(exchangeService.getCurrencies()).thenReturn(mockCurrencies);
        UUID userId = userRepository.findByUsername(TEST_USERNAME).get().getId();

        mockMvc.perform(get("/exchange-api/currencies").header("X-User-Id", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"USD\", \"EUR\", \"GBP\"]"));
    }

    @Test
    void testGetRates() throws Exception {
        BigDecimal mockRate = BigDecimal.valueOf(0.91);
        Mockito.when(exchangeService.getRate("EUR","USD")).thenReturn(mockRate);
        UUID userId = userRepository.findByUsername(TEST_USERNAME).get().getId();

        mockMvc.perform(get("/exchange-api/rate")
                        .header("X-User-Id", userId.toString())
                        .param("from", "EUR")
                        .param("to", "USD"))
                .andExpect(status().isOk())
                .andExpect(content().string(mockRate.toPlainString()));
    }

    @Test
    void testConvert() throws Exception {
        UUID userId = userRepository.findByUsername(TEST_USERNAME).get().getId();

        String jsonBody = """
        {
          "amount": "10.5",
          "fromCurrency": "EUR",
          "toCurrency": "USD"
        }
        """;

        // Mock the service call
        ExchangeTransactionEntity mockTransaction = new ExchangeTransactionEntity();
        mockTransaction.setAmount(new BigDecimal("10.5"));
        mockTransaction.setFromCurrency("EUR");
        mockTransaction.setToCurrency("USD");
        mockTransaction.setUserId(userId);
        mockTransaction.setTransactionId(UUID.randomUUID());
        mockTransaction.setDate(new Date());

        Mockito.when(exchangeService.convert(
                Mockito.any(), // user
                Mockito.eq(new BigDecimal("10.5")),
                Mockito.eq("EUR"),
                Mockito.eq("USD")
        )).thenReturn(mockTransaction);

        mockMvc.perform(post("/exchange-api/convert")
                        .header("X-User-Id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(10.5))
                .andExpect(jsonPath("$.fromCurrency").value("EUR"))
                .andExpect(jsonPath("$.toCurrency").value("USD"));
    }

    @Test
    void testTransactionHistory() throws Exception {
        UUID transactionId = UUID.randomUUID();
        UUID userId = userRepository.findByUsername(TEST_USERNAME).get().getId();
        LocalDate date = LocalDate.now();
        String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

        ExchangeTransactionEntity mockTransaction = new ExchangeTransactionEntity();
        mockTransaction.setTransactionId(transactionId);
        mockTransaction.setUserId(userId);
        mockTransaction.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        mockTransaction.setAmount(new BigDecimal("10.0"));
        mockTransaction.setFromCurrency("EUR");
        mockTransaction.setToCurrency("USD");

        List<ExchangeTransactionEntity> mockHistory = List.of(mockTransaction);

        Mockito.when(exchangeService.transactionHistory(0, 10, date, transactionId, userId))
                .thenReturn(mockHistory);

        mockMvc.perform(get("/exchange-api/history")
                        .header("X-User-Id", userId.toString())
                        .param("id", transactionId.toString())
                        .param("date", formattedDate)
                        .param("page", "0")
                        .param("pageSize", "10")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value(transactionId.toString()))
                .andExpect(jsonPath("$[0].userId").value(userId.toString()))
                .andExpect(jsonPath("$[0].fromCurrency").value("EUR"))
                .andExpect(jsonPath("$[0].toCurrency").value("USD"))
                .andExpect(jsonPath("$[0].amount").value(10.0));
    }



}
