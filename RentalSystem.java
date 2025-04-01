import java.util.List;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalSystem {
	
	// Private Static Instance
	private static RentalSystem instance;
	
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    
    // Private Constructor
    private RentalSystem() { loadData(); }

    public boolean addVehicle(Vehicle vehicle) {
    	if (findVehicleByPlate(vehicle.getLicensePlate()).equals(null)) {
    		vehicles.add(vehicle);
    		return true;
    	}
    	return false;
    }
    
    public void saveVehicle(Vehicle vehicle) {
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt", true));
			writer.append(vehicle.getClass().getSimpleName() + "," + vehicle.getLicensePlate() + "," + vehicle.getMake()+ "," + vehicle.getModel() + "," + vehicle.getYear() + "," + vehicle.getStatus() + ",");
			if (vehicle instanceof Car)
				writer.append(((Car) vehicle).getNumSeats() + "\n");
			if (vehicle instanceof Motorcycle)
				writer.append(((Motorcycle) vehicle).hasSidecar() + "\n");
			if (vehicle instanceof Truck)
				writer.append(((Truck) vehicle).getCargoCapacity() + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public boolean addCustomer(Customer customer) {
    	if(findCustomerById(customer.getCustomerId()).equals(null)) {
    		customers.add(customer);
        	saveCustomer(customer);
        	return true;
    	}
    	return false;
    }
    
    public void saveCustomer(Customer customer) {
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true));
			writer.append(customer.getCustomerName() + "," + customer.getCustomerId() + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(record);
            System.out.println("Vehicle rented to " + customer.getCustomerName());
            saveRecord(record);
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(record);
            System.out.println("Vehicle returned by " + customer.getCustomerName());
            saveRecord(record);
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }
    
    public void saveRecord(RentalRecord record) {
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("rental_records.txt", true));
			writer.append(record.getVehicle().getLicensePlate() + "," + record.getCustomer().getCustomerName() + "," + record.getRecordDate() + "," + record.getTotalAmount() + "," + record.getRecordType() + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void displayAvailableVehicles() {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllVehicles() {
        for (Vehicle v : vehicles) {
            System.out.println("  " + v.getInfo());
        }
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }

    public Customer findCustomerByName(String name) {
        for (Customer c : customers)
            if (c.getCustomerName().equalsIgnoreCase(name))
                return c;
        return null;
    }
    
    public static RentalSystem getInstance() {
    	if (instance == null) {
    		instance = new RentalSystem();
    	}
    	return instance;
    }
    
    private void loadData() {
    	try {
			BufferedReader reader = new BufferedReader(new FileReader("vehicles.txt"));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] vehicleInfo = line.split(",");
				String type = vehicleInfo[0];
				String licensePlate = vehicleInfo[1];
				String make = vehicleInfo[2];
				String model = vehicleInfo[3];
				int year = Integer.parseInt(vehicleInfo[4]);
				String status = vehicleInfo[5];
				Vehicle vehicle;
				if (type.equals("Car")) {
					int numSeats = Integer.parseInt(vehicleInfo[6]);
					vehicle = new Car(make, model, year, numSeats);
				}
				else if(type.equals("Motorcycle")) {
					boolean hasSidecar = false;
					if (vehicleInfo[6].equals("true"))
						hasSidecar = true;
					vehicle = new Motorcycle(make, model, year, hasSidecar);
				}
				else {
					double cargoCapacity = Double.parseDouble(vehicleInfo[6]);
					vehicle = new Truck(make, model, year, cargoCapacity);
				}
				if (status.equals("AVAILABLE"))
					vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
				else
					vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
				vehicle.setLicensePlate(licensePlate);
				vehicles.add(vehicle);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
			BufferedReader reader = new BufferedReader(new FileReader("customers.txt"));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] customerInfo = line.split(",");
				String customerName = customerInfo[0];
				int customerID = Integer.parseInt(customerInfo[1]);
				Customer customer = new Customer(customerID, customerName);
				customers.add(customer);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader("rental_records.txt"));
    		String line;
    		while ((line = reader.readLine()) != null) {
    			String[] recordInfo = line.split(",");
    			String licensePlate = recordInfo[0];
    			String customerName = recordInfo[1];
    			LocalDate date = LocalDate.parse(recordInfo[2]);
    			double totalAmount = Double.parseDouble(recordInfo[3]);
    			String recordType = recordInfo[4];
    			Vehicle vehicle = findVehicleByPlate(licensePlate);
    			Customer customer = findCustomerByName(customerName);
    			if (recordType.equals("RENT"))
    				vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
    			else if (recordType.equals("RETURN"))
    				vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
    			RentalRecord record = new RentalRecord(vehicle, customer, date, totalAmount, recordType);
    			rentalHistory.addRecord(record);
    		}
    		reader.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
}