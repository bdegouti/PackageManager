package cmpt213.assignment3.packagedeliveries.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Package class holds information relevant to packages, including name, notes, the price (in Dollars),
 * the weight (in kgs), the delivery status, and the date of delivery, as well as the relevant getters
 * and setters. It also holds a toString class which allows for the information to be retrieved as a formatted string.
 */
public class Package implements SpecialAttributeSetGetAsString, Comparable<Package> {
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm";
    private String name;
    private String notes;
    private double priceInDollars;
    private double weight;
    private boolean delivered;
    private LocalDateTime deliveryDate;


    public Package(String name, String notes, double priceInDollars, double weight, boolean delivered, LocalDateTime deliveryDate) {
        if(name.length() == 0){
            System.out.println("ERROR: Name cannot be empty!");
            return;
        }
        this.name = name;
        this.notes = notes;
        this.priceInDollars = priceInDollars;
        this.weight = weight;
        this.delivered = delivered;
        this.deliveryDate = deliveryDate;
    }

    public Package(){
        this.name = "";
        this.notes = "";
        priceInDollars = 0;
        weight = 0;
        this.delivered = false;
        deliveryDate = null;
    }

    /**
     * returns Package information as a formatted string.
     * @return The Formatted string form of the object
     */
    @Override
    public String toString() {
        return "Package: " + this.name +"\nNotes: " + this.notes + "\nPrice: $" + this.priceInDollars
                + "\nWeight: " + this.weight +"kg\nExpected Delivery Date: " + getDeliveryDateAsFormattedString() + "\nDelivered? " + DeliveredYorN();
    }

    /**
     * returns date as a formatted string (yyyy-MM-dd HH:mm)
     * @return The formatted string of the date
     */
    public String getDeliveryDateAsFormattedString(){
        if(this.deliveryDate == null){
            return "";
        }
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(dateTimeFormat);
        return deliveryDate.format(dateFormat);
    }

    private String DeliveredYorN(){
        if(this.delivered){
            return "Yes";
        }
        else{
            return "No";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name; }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPriceInDollars(double priceInDollars) {
        this.priceInDollars = priceInDollars;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean checkIfDelivered() {
        return delivered;
    }

    public void isDelivered() {
        this.delivered = true;
    }

    public void isNotDelivered() { this.delivered = false; }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setSpecialAttributeFromString(String sq){ }

    public String getSpecialAttributeAsString(){
        return null;
    }
    public static String getDateTimeFormat(){
        return dateTimeFormat;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getNotes() {
        return notes;
    }

    public double getPriceInDollars() {
        return priceInDollars;
    }

    public double getWeight() {
        return weight;
    }

    public String getPackageType(){
        return "";
    }

    @Override
    public int compareTo(Package p) {
        return deliveryDate.compareTo(p.getDeliveryDate());
    }
}
