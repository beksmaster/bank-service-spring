package com.example.bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    DataSource dataSource;

    @Test
    void printDb() throws Exception {
        System.out.println(
                dataSource.getConnection().getMetaData().getURL()
        );
    }

    @Test
    void shouldReturn401WithoutToken() throws Exception {

       mockMvc.perform(
              get("/api/v1/accounts/ACC-1")
               )
             .andExpect(status().isForbidden());
    }
}
