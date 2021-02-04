package edu.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WithPatternInRecordsTest {

    @Test
    void testWithPatternInRecords() {
        WithPatternInRecords.Vehicle vehicle = new WithPatternInRecords.Vehicle("Subaru", "ABC-DEFG");
        WithPatternInRecords.Vehicle ultraVehicle = vehicle.withLicense("LMN-OPQR");
        WithPatternInRecords.Vehicle premierVehicle = vehicle.withBrand("Umanga");

        Assertions.assertEquals(vehicle.brand(), ultraVehicle.brand());
        Assertions.assertNotEquals(vehicle.brand(), premierVehicle.brand());
        Assertions.assertEquals(vehicle.brand(), ultraVehicle.brand());
        Assertions.assertNotEquals(vehicle.licensePlate(), premierVehicle.licensePlate());
    }
}