package edu.bit.basics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ThrowCustomExceptionTest {

    @Test()
    void testExceptionBeingThrown() {
        ThrowCustomException throwCustomException = new ThrowCustomException();
        ThrowCustomException.CustomException customException = Assertions.assertThrows(
                ThrowCustomException.CustomException.class, () -> throwCustomException.testException(null));
        customException.calledMethod(customException);
    }
}