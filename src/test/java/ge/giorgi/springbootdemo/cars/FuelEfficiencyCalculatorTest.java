package ge.giorgi.springbootdemo.cars;

import ge.giorgi.springbootdemo.car.FuelEfficiencyCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FuelEfficiencyCalculatorTest {

    private final FuelEfficiencyCalculator fuelEfficiencyCalculator=new FuelEfficiencyCalculator();

    @Test
    void shouldCalculateFuelEfficiencyCorrectly(){
        double horsePower = 400;
        double capacity = 4.4;
        double weightInKg = 1800;

        double expectedValue = 0.8;
        double actualResult = fuelEfficiencyCalculator.calculateFuelEfficiency(horsePower, capacity, weightInKg);

        Assertions.assertEquals(expectedValue, actualResult);
    }
}
