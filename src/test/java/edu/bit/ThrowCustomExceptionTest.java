package edu.bit;

import edu.bit.CustomExceptionHandling;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ThrowCustomExceptionTest {

    @Test()
    void testExceptionBeingThrown() {
        CustomExceptionHandling throwCustomException = new CustomExceptionHandling();
        CustomExceptionHandling.CustomException customException = Assertions.assertThrows(
                CustomExceptionHandling.CustomException.class, () -> throwCustomException.testException(null));
        customException.calledMethod(customException);
    }
}