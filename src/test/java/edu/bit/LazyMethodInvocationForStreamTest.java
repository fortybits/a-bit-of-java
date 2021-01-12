package edu.bit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

class LazyMethodInvocationForStreamTest {

    @Mock
    static LazyInvocationUnderStreamsAsConsumer lazyMethodInvocationForStream;

    @BeforeAll
    static void setup() {
        lazyMethodInvocationForStream = Mockito.mock(LazyInvocationUnderStreamsAsConsumer.class);
    }

    @Test
    void testLazyLoadingForTerminalOperations() {
        Mockito.doCallRealMethod().when(lazyMethodInvocationForStream).terminateTheStreamCreatedBySupplier();
        lazyMethodInvocationForStream.terminateTheStreamCreatedBySupplier();
        Mockito.verify(lazyMethodInvocationForStream, Mockito.times(2)).getAList();
    }

    @Test
    void testLazyLoadingForIntermediateOperations() {
        Mockito.doCallRealMethod().when(lazyMethodInvocationForStream).intermediateStreamCreatedBySupplier();
        lazyMethodInvocationForStream.intermediateStreamCreatedBySupplier();
        Mockito.verify(lazyMethodInvocationForStream, Mockito.never()).getAList();
    }

    @Test
    void testLazyLoadingForIntermediateOperationsViaIncorrectMethod() {
        Mockito.doCallRealMethod().when(lazyMethodInvocationForStream).incorrectIntermediateStreamCreatedBySupplier();
        lazyMethodInvocationForStream.incorrectIntermediateStreamCreatedBySupplier();
        Mockito.verify(lazyMethodInvocationForStream).getAList();
    }
}