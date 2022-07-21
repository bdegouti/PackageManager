package cmpt213.assignment3.packagedeliveries.model;

import java.time.LocalDateTime;

public class ElectronicPackage extends Package {
    double environmentalFee;
    public ElectronicPackage(String name, String notes, double priceInDollars, double weight, boolean delivered, LocalDateTime deliveryDate, double envFee) {
        super(name, notes, priceInDollars, weight, delivered, deliveryDate);
        this.environmentalFee = envFee;
    }

    public ElectronicPackage(){
        super();
        environmentalFee = 0;
    }

    @Override
    public void setSpecialAttributeFromString(String sq) {
        environmentalFee = Double.parseDouble(sq);
    }

    @Override
    public String getSpecialAttributeAsString() {
        return String.valueOf(environmentalFee);
    }

    public double getEnvironmentalFee() {
        return environmentalFee;
    }

    public void setEnvironmentalFee(double environmentalFee) {
        this.environmentalFee = environmentalFee;
    }

    @Override
    public String getPackageType() {
        return "Electronic";
    }

    @Override
    public String toString() {
        return super.toString() + "\nEnvironmental Handling Fee: " + this.environmentalFee;
    }
}
