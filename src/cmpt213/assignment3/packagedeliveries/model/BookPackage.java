package cmpt213.assignment3.packagedeliveries.model;

import java.time.LocalDateTime;

public class BookPackage extends Package {
    String author;
    public BookPackage(String name, String notes, double priceInDollars, double weight, boolean delivered, LocalDateTime deliveryDate, String Author) {
        super(name, notes, priceInDollars, weight, delivered, deliveryDate);
        this.author = Author;
    }
    public BookPackage(){
        super();
        this.author = "";
    }

    @Override
    public void setSpecialAttributeFromString(String sq) {
        this.author = sq;
    }

    @Override
    public String getSpecialAttributeAsString() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String getPackageType() {
        return "Book";
    }

    @Override
    public String toString() {
        return super.toString() + "\nAuthor: " + this.author;
    }
}
