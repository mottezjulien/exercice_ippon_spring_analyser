package com.example.springanalyser.multithread;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class MultiThreadThrowableExceptionsHandlerTest {

    private MultiThreadThrowableExceptionsHandler exceptionsHandler = new MultiThreadThrowableExceptionsHandler();

    private interface Action {
        void any();
    }

    @Test
    public void expectRunAllRunnableUntilEnd() throws Exception {
        Action action1 = mock(Action.class);
        Action action2 = mock(Action.class);

        exceptionsHandler
                .reset()
                .add(() -> action1.any())
                .add(waitingRunnable(action2, 500))
                .execUntilEnd();
        verify(action1, only()).any();
        verify(action2, only()).any();
    }


    @Test
    public void expectRunParallel() throws Exception {
        Action action1 = mock(Action.class);
        Action action2 = mock(Action.class);
        Action action3 = mock(Action.class);
        Action action4 = mock(Action.class);

        exceptionsHandler
                .reset()
                .add(waitingRunnable(action1, 1000))
                .add(waitingRunnable(action2, 200))
                .add(() -> action3.any())
                .add(waitingRunnable(action4, 600))
                .execUntilEnd();

        InOrder inOrder = inOrder(action1, action2,action3,action4);
        inOrder.verify(action3).any();
        inOrder.verify(action2).any();
        inOrder.verify(action4).any();
        inOrder.verify(action1).any();
    }

    @Test
    public void expectThrowExceptionIfTheRunnableThrowRuntimeException() {
        Runnable exceptionRunnable = mock(Runnable.class);
        doThrow(new RuntimeException("Mon message d'exception")).when(exceptionRunnable).run();

        assertThatThrownBy(() -> exceptionsHandler
                .reset()
                .add(exceptionRunnable)
                .execUntilEnd()).isInstanceOf(MultiThreadThrowableHandlerException.class)
                .hasMessageContaining("MultiThreadThrowableHandler Exception cause by: Mon message d'exception");
    }


    @Test
    public void expectThrowAllExceptions() {
        Runnable exceptionRunnable1 = mock(Runnable.class);
        doThrow(new RuntimeException("Mon message d'exception")).when(exceptionRunnable1).run();

        Runnable successRunnable2 = mock(Runnable.class);

        Runnable exceptionRunnable3 = mock(Runnable.class);
        doThrow(new RuntimeException("Mon autre message d'exception")).when(exceptionRunnable3).run();

        assertThatThrownBy(() -> exceptionsHandler
                .reset()
                .add(exceptionRunnable1)
                .add(successRunnable2)
                .add(exceptionRunnable3)
                .execUntilEnd()).isInstanceOf(MultiThreadThrowableHandlerException.class)
                .hasMessageContaining("MultiThreadThrowableHandler Exception cause by: Mon message d'exception; Mon autre message d'exception");

        verify(successRunnable2, only()).run();
    }


    private Runnable waitingRunnable(Action act, int millis) {
        return () -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            act.any();
        };
    }

}
