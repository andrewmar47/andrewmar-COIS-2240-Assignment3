import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VehicleRentalTest {
	private Vehicle vehicle;
	private Customer customer;
	private RentalSystem rentalSystem;
	private Constructor<RentalSystem> constructor;
	
	@BeforeEach
	public void setUp() {
		vehicle = new Car("Toyota", "Corolla", 2024, 5);
		customer = new Customer(001, "George");
		rentalSystem = RentalSystem.getInstance();
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
	
	@Test
	public void testRentAndReturnVehicle() {
		// Check if vehicle starts at AVAILABLE
		assertEquals(Vehicle.VehicleStatus.AVAILABLE, vehicle.getStatus());
		
		// Check if vehicle is rented successfully
		assertTrue(rentalSystem.rentVehicle(vehicle, customer, LocalDate.now(), 0));
		// Check if vehicle status is RENTED
		assertEquals(Vehicle.VehicleStatus.RENTED, vehicle.getStatus());
		
		// Check if renting an already rented vehicle fails
		assertFalse(rentalSystem.rentVehicle(vehicle, customer, LocalDate.now(), 0));
		
		//Check if vehicle is returned successfully
		assertTrue(rentalSystem.returnVehicle(vehicle, customer, LocalDate.now(), 0));
		//Check if vehicle status is AVAILABLE
		assertEquals(Vehicle.VehicleStatus.AVAILABLE, vehicle.getStatus());
		
		// Check if returning an available vehicle fails
		assertFalse(rentalSystem.returnVehicle(vehicle, customer, LocalDate.now(), 0));
	}
	
	@Test
	public void testSingletonRentalSystem() {
		
		try {
			constructor = RentalSystem.class.getDeclaredConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		assertEquals(Modifier.PRIVATE, constructor.getModifiers());
		
		assertNotNull(RentalSystem.getInstance());
	}
}
