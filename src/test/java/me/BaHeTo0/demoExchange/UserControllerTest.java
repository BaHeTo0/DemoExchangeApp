package me.BaHeTo0.demoExchange;

import me.BaHeTo0.demoExchange.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public UserRepository userRepository;

    private static final String TEST_USERNAME = "test_user_123";
    private static final String TEST_STARTING_BALANCE_CURRENCY = "EUR";
    private static final BigDecimal TEST_STARTING_BALANCE = BigDecimal.valueOf(1000000);

    @Test
    public void testRegisterAndLoginUser() throws Exception {
        String jsonBody = """
            {
                "username": "%s",
                "startingBalance": "%s"
            }
            """.formatted(TEST_USERNAME, TEST_STARTING_BALANCE.toPlainString());

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.balances."+TEST_STARTING_BALANCE_CURRENCY+".amount").value(TEST_STARTING_BALANCE));

        String loginBody = """
            {
                "username": "%s"
            }
            """.formatted(TEST_USERNAME);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(TEST_USERNAME));
    }

}
