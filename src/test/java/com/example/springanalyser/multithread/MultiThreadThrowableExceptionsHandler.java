package com.example.springanalyser.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MultiThreadThrowableExceptionsHandler {

    private class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        private List<Throwable> throwables = new ArrayList<>();

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            insert(e);
        }

        public boolean hasUncaughtException() {
            return !throwables.isEmpty();
        }

        public void insert(Throwable throwable) {
            this.throwables.add(throwable);
        }

        public String getErrorMessages() {
            String errorMessages = throwables.stream().map(Throwable::getMessage).collect(Collectors.joining("; "));
            return "MultiThreadThrowableHandler Exception cause by: " + errorMessages;
        }
    }

    List<Runnable> runnables = new ArrayList<>();

    public MultiThreadThrowableExceptionsHandler reset() {
        this.runnables.clear();
        return this;
    }

    public MultiThreadThrowableExceptionsHandler add(Runnable runnable) {
        this.runnables.add(runnable);
        return this;
    }

    public void execUntilEnd() throws MultiThreadThrowableHandlerException {

        UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionHandler();
        List<Thread> threads = runnables
                .stream()
                .map(runnable -> {
                    Thread th = new Thread(runnable);
                    th.setUncaughtExceptionHandler(exceptionHandler);
                    return th;
                }).collect(Collectors.toList());
        threads.forEach(thread -> thread.start());
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException exception) {
                exceptionHandler.insert(exception);
            }
        });
        if(exceptionHandler.hasUncaughtException()) {
            throw new MultiThreadThrowableHandlerException(exceptionHandler.getErrorMessages());
        }

    }


}
