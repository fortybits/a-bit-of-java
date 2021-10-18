package edu.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.constant.ConstantDescs;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

class MemoryConstantsTest {

    @Test
    void verifyIntegerConstable() {
        Integer integer = Integer.MAX_VALUE;
        Assertions.assertEquals(integer.describeConstable(), Optional.of(integer));
        Assertions.assertEquals(integer.resolveConstantDesc(MethodHandles.lookup()), integer);
    }

    @Test
    void verifyCustomMemoryConstable() throws ReflectiveOperationException {
        MemoryConstants memory = new MemoryConstants(1);
        Assertions.assertEquals(memory.describeConstable(), Optional.of(ConstantDescs.CD_long));
    }
}