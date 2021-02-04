package edu.bit;

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
