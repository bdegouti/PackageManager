package cmpt213.assignment3.packagedeliveries.model;

public class PackageFactory {
    public static Package getInstance(String type){
        if(type.equalsIgnoreCase("Book")){
            return new BookPackage();
        }
        else if(type.equalsIgnoreCase("Electronic")){
            return new ElectronicPackage();
        }
        else if(type.equalsIgnoreCase("Perishable")){
            return new PerishablePackage();
        }
        return null;
    }
}
