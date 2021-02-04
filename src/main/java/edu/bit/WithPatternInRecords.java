package edu.bit;

/**
 * Problem statement: Make use of 'with' in records.
 * Details: https://stackoverflow.com/questions/66048957/do-java-records-support-with-syntax
 */
public class WithPatternInRecords {
    public record Vehicle(String brand, String licensePlate) {
        Vehicle withLicense(String licensePlate) {
            return new Vehicle(this.brand(), licensePlate);
        }

        Vehicle withBrand(String licensePlate) {
            return new Vehicle(this.brand(), licensePlate);
        }
    }
}
