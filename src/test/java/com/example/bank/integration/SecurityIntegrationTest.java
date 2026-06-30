package com.example.bank.integration;

import com.example.bank.dto.LoginResponse;
import com.example.bank.enums.Role;
import com.example.bank.enums.TransactionStatus;
import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.model.User;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
 class SecurityIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        transactionRepository.deleteAll();

        User user = new User();
        user.setUsername("samat");
        user.setPassword(passwordEncoder.encode("samat"));
        user.setRole(Role.USER);

        User user1 = new User();
        user1.setUsername("bektur");
        user1.setPassword(passwordEncoder.encode("bektur"));
        user1.setRole(Role.USER);

        userRepository.save(user);
        userRepository.save(user1);

        Account account = new Account("ACC-11111111", new BigDecimal("1000"), user);

        Account account1 = new Account("ACC-22222222", new BigDecimal("1000"), user1);

        accountRepository.save(account);
        accountRepository.save(account1);
    }

    @Test
    void shouldCreateUser() throws Exception {
        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username":"aibek",
                                "password":"aibek"
                                }
                                """
                        )
        )
                .andExpect(status().isOk());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username":"samat",
                                "password":"samat"
                                }
                                """)
        )
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn401LoginUnsuccesfully() throws Exception {
        mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username":"samat",
                                "password":"bektur"
                                }
                                """
                        )
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn401UserNotFound() throws Exception {
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                "username":"userNotExists",
                                "password":"randomPassword"
                                }
                                """
                                )
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn409UserAlreadyExists() throws Exception {
        mockMvc.perform(
                post(
                        "/api/v1/auth/register"
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username":"samat",
                                "password":"samat"
                                }
                                """)
        )
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn400UserOrPasswordNotValid() throws Exception {
        mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username":"",
                                "password":" "
                                }
                                """)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn401WithoutToken() throws Exception {

       mockMvc.perform(
              get("/api/v1/accounts/ACC-1")
               )
             .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectInvalidJWT() throws Exception {
        mockMvc.perform(
                get("/api/v1/accounts/ACC-11111111")
                        .header("Authorization", "Bearer invalid-token")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldGenerateJwt() throws Exception {

        String token = loginAndGetToken("samat", "samat");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldAccessProtectedEndpointWithJwt() throws Exception {

        String token = loginAndGetToken("samat", "samat");

        mockMvc.perform(
                get("/api/v1/accounts/ACC-123")
                        .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAccessOwnAccount() throws Exception {

        String token = loginAndGetToken("samat", "samat");

        mockMvc.perform(
                get("/api/v1/accounts/{number}", "ACC-11111111")
                        .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateAccount() throws Exception {
        String token = loginAndGetToken("samat", "samat");

        mockMvc.perform(
                post("/api/v1/accounts/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "balance":100
                                }
                                """)
        )
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotAccessForeignAccount() throws Exception {

        String token = loginAndGetToken("samat", "samat");

        mockMvc.perform(
                get("/api/v1/accounts/ACC-22222222")
                        .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotTransferMoneyFromForeignAccount() throws Exception {

        String token = loginAndGetToken("samat", "samat");

        mockMvc.perform(
                post("/api/v1/transfers/transfer")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fromAccount":"ACC-22222222",
                                 "toAccount":"ACC-11111111",
                                 "amount":100
                                }
                                """)
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldGetOwnAccount() throws Exception {

        String token = loginAndGetToken("samat", "samat");

        mockMvc.perform(
                get("/api/v1/accounts/ACC-11111111")
                        .header("Authorization", "Bearer " + token)

        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber")
                        .value("ACC-11111111"))
                .andExpect(jsonPath("$.balance")
                        .value("1000"));
    }

    @Test
    void shouldTransferMoney() throws Exception {

        String token = loginAndGetToken("samat", "samat");

        mockMvc.perform(
                post("/api/v1/transfers/transfer")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "fromAccount":"ACC-11111111",
                                "toAccount":"ACC-22222222",
                                "amount":100
                                }
                                """)
        )
                .andExpect(status().isOk());
        Account from = accountRepository.findById("ACC-11111111").orElseThrow();

        Account to = accountRepository.findById("ACC-22222222").orElseThrow();

        assertEquals(new BigDecimal("900"), from.getBalance());
        assertEquals(new BigDecimal("1100"), to.getBalance());

        assertEquals(1, transactionRepository.count());

        Transaction transaction = transactionRepository.findAll().get(0);

        assertEquals("ACC-11111111", transaction.getFromAccount().getAccountNumber());
        assertEquals("ACC-22222222", transaction.getToAccount().getAccountNumber());

        assertEquals(new BigDecimal("100"), transaction.getAmount());
        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
    }

    @Test
    void shouldRejectSelfTransfer() throws Exception {

        String token = loginAndGetToken("samat", "samat");

        mockMvc.perform(
                        post("/api/v1/transfers/transfer")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                "fromAccount":"ACC-11111111",
                                "toAccount":"ACC-11111111",
                                "amount":100
                                }
                                """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Self transfer not allowed"));
    }

    @Test
    void shouldRejectTransferWhenInsufficientFunds() throws Exception {
        String token = loginAndGetToken("samat", "samat");

        mockMvc.perform(
                post("/api/v1/transfers/transfer")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "fromAccount":"ACC-11111111",
                                "toAccount":"ACC-22222222",
                                "amount":1100
                                }
                                """)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404TransactionNotFound() throws Exception {
        String token = loginAndGetToken("samat", "samat");
        mockMvc.perform(
                get("/api/v1/transactions/11")
                        .header("Authorization", "Bearer " + token)
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenSenderAccountNotFound() throws Exception {

        String token = loginAndGetToken("samat", "samat");

        mockMvc.perform(
                post("/api/v1/transfers/transfer")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "fromAccount":"ACC-NOT-EXISTS",
                                "toAccount":"ACC-22222222",
                                "amount":100
                                }
                                """)
        ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Account not found"));
    }

    @Test
    void shouldReturn404WhenReceiverAccountNotFound() throws Exception {

        String token = loginAndGetToken("samat", "samat");

        mockMvc.perform(
                        post("/api/v1/transfers/transfer")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                "fromAccount":"ACC-11111111",
                                "toAccount":"ACC-NOT-EXISTS",
                                "amount":100
                                }
                                """)
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Account not found"));

    }

    private String loginAndGetToken(String username, String password) throws Exception {

        MvcResult result = mockMvc.perform(
                post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username":"samat",
                                "password":"samat"
                                }
                                """.formatted(username, password))
        )
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();

        LoginResponse response =
                mapper.readValue(json, LoginResponse.class);

        return response.token();
    }

}
