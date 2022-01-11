package com.example.springanalyser;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
@ContextConfiguration(classes = Configuration.class)
public class UserControllerTest {

    interface Result { }

    class ResultSuccess implements Result { }

    class ResultFailure implements Result {
        Exception exception;
        public ResultFailure(Exception exception) {
            this.exception = exception;
        }
    }

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
    void whenLoginThenReturnWelcomeMessage_parallelConnexions_ExecutorService() {

        final int ANY_NB_THREADS = 3;
        ExecutorService service = Executors.newFixedThreadPool(ANY_NB_THREADS);

        List<Future<Result>> futures = Stream.of("Bob", "Alice", "Bert", "Robert", "Jeff", "Raoul", "Gertrude")
                .map(userName -> service.submit(() -> {
                    try {
                        whenLoginThenReturnWelcomeMessage(userName);
                        return new ResultSuccess();
                    } catch (Exception e) {
                        return new ResultFailure(e);
                    }
                })).collect(Collectors.toList());

        futures.forEach(future -> new Thread(() -> {
            try {
                Result result = future.get();
                if (result instanceof ResultFailure) {
                    throw new RuntimeException(((ResultFailure) result).exception);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).run());
        
        /*final int ANY_NB_THREADS = 3;
        ExecutorService service = Executors.newFixedThreadPool(ANY_NB_THREADS);

        Stream.of("Bob", "Alice", "Bert", "Robert", "Jeff", "Raoul", "Gertrude")
                .map(userName -> service.submit(() -> {
                    try {
                        whenLoginThenReturnWelcomeMessage(userName);
                        return new ResultSuccess();
                    } catch (Exception e) {
                        return new ResultFailure(e);
                    }
                }))
                .forEach(future -> {
                    try {
                        Result result = future.get();
                        if (result instanceof ResultFailure) {
                            throw new RuntimeException(((ResultFailure) result).exception);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        service.shutdown();*/
    }

    private void whenLoginThenReturnWelcomeMessage(String userName) throws Exception {
        mockMvc.perform(get("/user/login/" + userName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Matchers.is("Bienvenue " + userName)));
    }


}

