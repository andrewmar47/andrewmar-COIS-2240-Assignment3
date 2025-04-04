import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleRentalTest {
	private Vehicle vehicle;
	
	@BeforeEach
	public void setUp() {
		vehicle = new Car("Toyota", "Corolla", 2024, 5);
	}
	
	@Test
	public void testLicensePlateValidation() {
		// Test valid plates
		vehicle.setLicensePlate("AAA100");
		vehicle.setLicensePlate("ABC657");
		vehicle.setLicensePlate("ZZZ999");
		
		// Test invalid plates
		assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate(""));
		assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate(null));
		assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("AAA1000"));
		assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("ZZZ99"));
	}

}
