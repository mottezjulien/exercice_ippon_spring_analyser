package com.example.springanalyser;

import com.example.springanalyser.multithread.MultiThreadThrowableExceptionsHandler;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
@ContextConfiguration(classes = Configuration.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void whenLoginThenReturnWelcomeMessage_oneConnexion() throws Exception {
        whenLoginThenReturnWelcomeMessage("Bob");
    }

    @Test
    void whenLoginThenReturnWelcomeMessage_twoSyncConnexions() throws Exception {
        whenLoginThenReturnWelcomeMessage("Bob");
        whenLoginThenReturnWelcomeMessage("Alice");
    }

    @Test
    void whenLoginThenReturnWelcomeMessage_fiveParallelConnexions() throws Exception {

        MultiThreadThrowableExceptionsHandler exceptionsHandler = new MultiThreadThrowableExceptionsHandler();

        List.of("Bob", "Alice", "Bert", "Robert", "Jeff")
                .stream()
                .forEach(userName -> exceptionsHandler.add(() -> {
                    try {
                        whenLoginThenReturnWelcomeMessage(userName);
                    } catch (Exception | Error e) {
                        throw new RuntimeException(e);
                    }
                }));
        exceptionsHandler.execUntilEnd();
    }


    private void whenLoginThenReturnWelcomeMessage(String userName) throws Exception {
        mockMvc.perform(get("/user/login/" + userName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Matchers.is("Bienvenue " + userName)));
    }


}

