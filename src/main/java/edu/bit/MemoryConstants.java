package edu.bit;

import java.lang.constant.ClassDesc;
import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.lang.constant.ConstantDescs;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

/**
 * Q. when does it make sense to implement Constable and not ConstantDesc?
 * Q. what's a practical use of these interfaces?
 * Design Q. why are these two separate interfaces (related to first closely)?
 * <p>
 * The API is completely documented under https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8202031
 */
public record MemoryConstants(int size) implements Constable {
    @Override
    public Optional<? extends ConstantDesc> describeConstable() {
        Optional.of(ConstantDescs.CD_Class); // ClassDesc.of("java.lang.Class")
        return Optional.of(ConstantDescs.CD_long); //  ClassDesc.ofDescriptor("J")
    }

    @Override
    public String toString() {
        return "Memory{" + "size=" + size + '}';
    }
}
