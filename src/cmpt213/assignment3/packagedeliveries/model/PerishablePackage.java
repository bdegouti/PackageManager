package cmpt213.assignment3.packagedeliveries.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PerishablePackage extends Package {
    private LocalDateTime expiryDate;
    private final String inputDateTimeFormat = "yyyy-MM-dd['T']HH:mm";

    public PerishablePackage(String name, String notes, double priceInDollars, double weight, boolean delivered, LocalDateTime deliveryDate, LocalDateTime expiryDate) {
        super(name, notes, priceInDollars, weight, delivered, deliveryDate);
        this.expiryDate = expiryDate;
    }
    public PerishablePackage(){
        super();
        expiryDate = null;
    }
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(LocalDateTime expiryDate){
        this.expiryDate = expiryDate;
    }
    @Override
    public void setSpecialAttributeFromString(String sq) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(inputDateTimeFormat);
        this.expiryDate = LocalDateTime.parse(sq, dateFormat);
    }

    @Override
    public String getSpecialAttributeAsString() {
        return getExpiryDateAsFormattedString();
    }

    private String getExpiryDateAsFormattedString(){
        if(expiryDate == null){
            return "";
        }
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(Package.getDateTimeFormat());
        return expiryDate.format(dateFormat);
    }

    @Override
    public String toString() {
        return super.toString() + "\nExpiry Date: " + getExpiryDateAsFormattedString();
    }

    @Override
    public String getPackageType() {
        return "Perishable";
    }
}
