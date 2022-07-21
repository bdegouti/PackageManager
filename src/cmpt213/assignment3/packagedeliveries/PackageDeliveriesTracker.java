package cmpt213.assignment3.packagedeliveries;

import cmpt213.assignment3.packagedeliveries.view.PackageManagerGUI;

import javax.swing.*;

public class PackageDeliveriesTracker {
    public static void main (String[]args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PackageManagerGUI();
            }
        });
        System.out.println("Finished!");
    }
}
