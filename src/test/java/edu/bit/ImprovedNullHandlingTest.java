package edu.bit;

import org.junit.jupiter.api.Test;

class ImprovedNullHandlingTest {

    @Test
    void testNullPointerImprovedMessage() {
        ImprovedNullHandling.NPE npe = new ImprovedNullHandling.NPE(2, null);
        System.out.println(npe.reason());
        // java.lang.NullPointerException: Cannot invoke "edu.bit.ImprovedNullHandling$Reason.detail()"
        // because the return value of "edu.bit.ImprovedNullHandling$NPE.reason()" is null
        // System.out.println(npe.reason().detail().equalsIgnoreCase("fix"));
    }
}