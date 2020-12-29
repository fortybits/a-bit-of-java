package edu.bit.__dump;

import edu.bit.OptionalsUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class OptionalUtilityTest {

    @Test
    void testOptionalStringCustomImplementation() {
        Entity entity = new Entity("nullpointer");
        Service service = new Service("stackoverflow");

        String presentImpl = Optional.ofNullable(entity.name())
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .or(service::getMostCommonNameOptional)
                .filter(s -> !s.isEmpty()) // java-9
                .orElse("success");
        Assertions.assertEquals("nullpointer", presentImpl);

        String customImpl = OptionalsUtility.OptionalString.of(entity.name())
                .map(String::trim)
                .or(service::mostCommonName)
                .orElse("learning");
        Assertions.assertEquals("nullpointer", customImpl);
    }

    record Entity(String name) {
    }

    record Service(String mostCommonName) {
        Optional<String> getMostCommonNameOptional() {
            return Optional.of(mostCommonName);
        }
    }
}