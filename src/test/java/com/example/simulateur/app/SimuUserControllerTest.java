package com.example.simulateur.app;

import com.example.simulateur.black.box.model.SimuHttpRequest;
import com.example.simulateur.black.box.model.SimuServer;
import com.example.simulateur.black.box.model.SimuSpringConfig;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SimuUserControllerTest {

    interface Result { }

    class ResultSuccess implements Result { }

    class ResultFailure implements Result {
        Exception exception;
        public ResultFailure(Exception exception) {
            this.exception = exception;
        }
    }

    private SimuSpringConfig config = new SimuSpringConfig("com.example.simulateur.app");
    private SimuServer server = new SimuServer(config);

    @Test
    void test_oneConnexion() {
        expect("Alice", "Bienvenue Alice");
    }

    @Test
    void test_twoSyncConnexions() throws Exception {
        expect("Bob", "Bienvenue Bob");
        expect("Alice", "Bienvenue Alice");
    }


    @Test
    void test_parallelConnexions_ExecutorService() {
        final int ANY_NB_THREADS = 3;
        ExecutorService service = Executors.newFixedThreadPool(ANY_NB_THREADS);


        List<Future<Result>> futures = Stream.of("Bob", "Alice", "Bert", "Robert", "Jeff", "Raoul", "Gertrude")
                .map(userName -> service.submit(() -> {
                    try {
                        expect(userName, "Bienvenue " + userName);
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
    }

    private void expect(String request, String response) {
        assertThat(server.call(new SimuHttpRequest("/login", request)).data()).isEqualTo(response);
    }





}