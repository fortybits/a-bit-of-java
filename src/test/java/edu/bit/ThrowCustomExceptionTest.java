package edu.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ThrowCustomExceptionTest {

    @Test()
    void testExceptionBeingThrown() {
        ExceptionHandling throwCustomException = new ExceptionHandling();
        ExceptionHandling.CustomException customException = Assertions.assertThrows(
                ExceptionHandling.CustomException.class, () -> throwCustomException.testException(null));
        customException.calledMethod(customException);
    }
}