package edu.bit;

import edu.bit.ImprovedNullHandling;
import org.junit.jupiter.api.Test;

class ImprovedNullHandlingTest {

    @Test
    void testNullPointerImprovedMessage() {
        ImprovedNullHandling.NPE npe = new ImprovedNullHandling.NPE(2, null);
        System.out.println(npe.reason().detail().equalsIgnoreCase("fix"));
    }
}