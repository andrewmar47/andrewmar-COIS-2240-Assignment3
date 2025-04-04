public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { AVAILABLE, RESERVED, RENTED, MAINTENANCE, OUTOFSERVICE }

    public Vehicle(String make, String model, int year) {
    	if (make == null || make.isEmpty())
    		this.make = null;
    	else
    		this.make = capitalize(make);
    	
    	if (model == null || model.isEmpty())
    		this.model = null;
    	else
    		this.model = capitalize(model);
    	
        this.year = year;
        this.status = VehicleStatus.AVAILABLE;
        this.licensePlate = null;
    }

    public Vehicle() {
        this(null, null, 0);
    }

    public void setLicensePlate(String plate) {
    	if (isValidPlate(plate) == false)
    		throw new IllegalArgumentException("ERROR: Invalid license plate format.");
        this.licensePlate = plate == null ? null : plate.toUpperCase();
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }
    
    private String capitalize(String input) {
    	return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    private boolean isValidPlate(String plate) {
    	// Check if plate is null, empty or not the correct length
    	if (plate == null || plate.length() != 6 || plate.isEmpty())
    		return false;
    	
    	// Check if first three characters are letters
    	for (int i = 0; i < 3; i++) {
    		if (Character.isLetter(plate.charAt(i)) == false)
    			return false;
    	}
    	
    	// Check if last three characters are numbers
    	for (int i = 3; i < 6; i++) {
    		if (Character.isDigit(plate.charAt(i)) == false)
    			return false;
    	}
    	
    	return true;
    }
}
