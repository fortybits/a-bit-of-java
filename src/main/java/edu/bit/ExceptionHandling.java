package edu.bit;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ExceptionHandling {

    public void testException(String str) throws CustomException {
        if (str == null) {
            throw new CustomException();
        }
    }

    static class CustomException extends Exception {

        public CustomException() {
            super();
        }

        public void calledMethod(Exception testException) {
            if (testException instanceof TimeoutException) {
                System.out.println("Timeout here...");
            }
            if (testException instanceof NullPointerException) {
                System.out.println("NullPointer here...");
            }
            if (testException instanceof InterruptedException) {
                System.out.println("Interrupted here...");
            }
        }
    }

    //
    public void closeableInTryWithResource() {
        String soprano = null;

        CloseIt closeIt = new CloseIt();
        try (closeIt) {
            System.out.println(soprano.matches(null));
        } catch (Exception e) {
            System.out.println("Exception!");
        } catch (Throwable throwable) {
            System.out.println("Throwable!");
        }
    }

    static class CloseIt implements Closeable {
        @Override
        public void close() throws IOException {
            System.out.println("Close..");
        }
    }
}