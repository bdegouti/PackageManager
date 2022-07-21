package cmpt213.assignment3.packagedeliveries.control;

import cmpt213.assignment3.packagedeliveries.model.BookPackage;
import cmpt213.assignment3.packagedeliveries.model.ElectronicPackage;
import cmpt213.assignment3.packagedeliveries.model.Package;
import cmpt213.assignment3.packagedeliveries.model.PerishablePackage;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Package manager handles the list of packages representing the total
 * sum of packages which have been sent through the system, and will
 * sort them as required whenever a new package is added. It will also
 * save these items, when prompted, to the json folder, or provide formatted
 * strings when called
 */
public class PackageManager {
    private static PackageManager instance = null;
    private static final String jSON_DATA_FOLDER = "../../json";
    private static final String jSON_PACKAGES_DATA_PATH = jSON_DATA_FOLDER +"/PackagesData.json";
    private final ArrayList<Package> packages;
    private final Gson packagesGson;
    public static PackageManager getInstance(){
        if(instance == null){
            instance = new PackageManager();
        }
        return instance;
    }
    private PackageManager(){
        packages = new ArrayList<>();
        packagesGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
            @Override
            public void write(JsonWriter jsonWriter,
                              LocalDateTime localDateTime) throws IOException {
                jsonWriter.value(localDateTime.toString());
            }

            @Override
            public LocalDateTime read(JsonReader jsonReader) throws IOException {
                return LocalDateTime.parse(jsonReader.nextString());
            }
        }).create();
    }

    public void addToPackages(Package p){
        if(p != null){
            packages.add(p);
            Collections.sort(packages);
        }
    }

    public void setDelivered(int i){
        packages.get(i).isDelivered();
    }

    public void removeFromPackages(int i){
        packages.remove(i);
    }

    public void removeFromPackages(Package p){
        packages.remove(p);
    }

    public int numberOfPackages(){
        return packages.size();
    }

    /**
     * lists all packages stored in the packages List.
     */
    public ArrayList<Package> getListOfPackages(){
        if(packages.size() > 0) {
            return packages;
        }
        else{
            return null;
        }
    }

    /**
     * lists all packages which have a delivery date prior to the current date, but are
     * not marked as delivered.
     */
    public ArrayList<Package> getListOfOverduePackages(){
        ArrayList<Package> odp = new ArrayList<>();
        if(packages.size() > 0) {
            for (Package aPackage : packages) {
                if (aPackage.getDeliveryDate().isBefore(LocalDateTime.now()) && !aPackage.checkIfDelivered()) {
                    odp.add(aPackage);
                }
            }
        }
        else{
            return null;
        }
        return odp;
    }

    /**
     * lists all packages that have a delivery date beyond the current date, and which
     * have not been marked as delivered.
     */
    public ArrayList<Package> getListOfUpcomingPackages(){
        ArrayList<Package> up = new ArrayList<>();
        if(packages.size() > 0) {
            for (Package aPackage : packages) {
                if (aPackage.getDeliveryDate().isAfter(LocalDateTime.now()) && !aPackage.checkIfDelivered()) {
                    up.add(aPackage);
                }
            }
        }
        else{
            return null;
        }
        return up;
    }

    static class SortByDate implements Comparator<Package> {

        @Override
        public int compare(Package p1, Package p2) {
            return p1.getDeliveryDate().compareTo(p2.getDeliveryDate());
        }
    }

    /**
     * Loads Package information from Json file at json/PackagesData.json
     */
    public void LoadFromJsonFile() {
        File packagesFile = new File(jSON_PACKAGES_DATA_PATH);
        if(packagesFile.exists() && !packagesFile.isDirectory()){
            try {
                JsonElement fileEl = JsonParser.parseReader(new FileReader(jSON_PACKAGES_DATA_PATH));
                JsonArray packagesDataArray = fileEl.getAsJsonArray();
                for(JsonElement packageElement : packagesDataArray){
                    JsonObject packageObject = packageElement.getAsJsonObject();
                    String author = null;
                    Double environmentalFee = null;
                    LocalDateTime expiryDate = null;
                    String name = packageObject.get("name").getAsString();
                    String notes = packageObject.get("notes").getAsString();
                    double priceInDollars = packageObject.get("priceInDollars").getAsDouble();
                    double weight = packageObject.get("weight").getAsDouble();
                    boolean delivered = packageObject.get("delivered").getAsBoolean();
                    LocalDateTime deliveryDate = LocalDateTime.parse(packageObject.get("deliveryDate").getAsString());
                    try {
                        author = packageObject.get("author").getAsString();
                    } catch(Exception ignored){}

                    try {
                        environmentalFee = packageObject.get("environmentalFee").getAsDouble();
                    } catch(Exception ignored){}
                    try {
                        expiryDate = LocalDateTime.parse(packageObject.get("expiryDate").getAsString());
                    } catch(Exception ignored){}
                    if(author != null){
                        packages.add(new BookPackage(name, notes, priceInDollars, weight, delivered, deliveryDate, author));
                    }
                    else if(environmentalFee != null){
                        packages.add(new ElectronicPackage(name, notes, priceInDollars, weight, delivered, deliveryDate, environmentalFee));
                    }
                    else if(expiryDate != null){
                        packages.add(new PerishablePackage(name, notes, priceInDollars, weight, delivered, deliveryDate, expiryDate));
                    }
                    else {
                        packages.add(new Package(name, notes, priceInDollars, weight, delivered, deliveryDate));
                    }
                }
            } catch(IOException io){
                io.printStackTrace();
            }
        }
    }

    /**
     * saves Package information to json file at json/PackagesData.json
     */
    public void saveToJsonFile() {
        try{
            PrintWriter out = new PrintWriter(new FileWriter(jSON_PACKAGES_DATA_PATH));
            out.write(packagesGson.toJson(packages, packages.getClass()));
            out.close();

        } catch(IOException io){
            File jsonDir = new File(jSON_DATA_FOLDER);
            if(jsonDir.mkdir()){
                File packagesData = new File(jSON_PACKAGES_DATA_PATH);
                try {
                    if(packagesData.createNewFile()) {
                        PrintWriter out = new PrintWriter(new FileWriter(jSON_PACKAGES_DATA_PATH));
                        out.write(packagesGson.toJson(packages, packages.getClass()));
                        out.close();
                    }
                }catch(IOException io2){
                    System.out.println("ERROR: file could not be found, creation failed");
                    io.printStackTrace();
                    io2.printStackTrace();
                }
            }
        }
    }
}
