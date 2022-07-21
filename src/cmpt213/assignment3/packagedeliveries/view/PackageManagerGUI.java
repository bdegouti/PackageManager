package cmpt213.assignment3.packagedeliveries.view;

import cmpt213.assignment3.packagedeliveries.control.PackageManager;
import cmpt213.assignment3.packagedeliveries.model.Package;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static java.lang.System.exit;

/**
 * Handles GUI for My Package Deliveries Tracker Window
 */
public class PackageManagerGUI extends JFrame{
    public enum packageListType {
        All, Overdue, Upcoming
    }

    packageListType currentType = packageListType.All;
    cmpt213.assignment3.packagedeliveries.control.PackageManager manager;
    private JButton allPackagesButton;
    private JButton upcomingPackagesButton;
    private JButton overduePackages;
    private JButton addPackageButton;
    private JScrollPane packagesField;
    private JPanel basePanel;

    private final JPanel scrollView;

    public PackageManagerGUI() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.manager = PackageManager.getInstance();
        loadAndSetUpSave();

        packagesField.setLayout(new ScrollPaneLayout());
        allPackagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                overduePackages.setBackground(Color.white);
                upcomingPackagesButton.setBackground(Color.white);
                allPackagesButton.setBackground(Color.LIGHT_GRAY);
                currentType = packageListType.All;
                refreshPackageField();
            }
        });
        overduePackages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                overduePackages.setBackground(Color.LIGHT_GRAY);
                upcomingPackagesButton.setBackground(Color.white);
                allPackagesButton.setBackground(Color.white);
                currentType = packageListType.Overdue;
                refreshPackageField();
            }
        });
        upcomingPackagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                overduePackages.setBackground(Color.white);
                upcomingPackagesButton.setBackground(Color.LIGHT_GRAY);
                allPackagesButton.setBackground(Color.white);
                currentType = packageListType.Upcoming;
                refreshPackageField();
            }
        });

        addPackageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new AddPackageDialog();
                dialog.pack();
                dialog.setVisible(true);
                refreshPackageField();
            }
        });
        scrollView = new JPanel();
        BoxLayout scrollLayout = new BoxLayout(scrollView, BoxLayout.Y_AXIS);
        scrollView.setLayout(scrollLayout);
        packagesField.getViewport().add(scrollView);

        packagesField.getVerticalScrollBar().setUnitIncrement(15);

        this.add(basePanel);
        this.setTitle("My Package Deliveries Tracker");
        this.pack();
        this.setVisible(true);

        allPackagesButton.doClick();
    }

    /**
     * Fill ScrollPanel with packages from the packageManager
     */
    private void populatedPackageField() {
        ArrayList<Package> p;
        if(currentType == packageListType.All){
            p = manager.getListOfPackages();
            if(p == null){
                resultIfNoPackagesEmpty();
                return;
            }
        }
        else if(currentType == packageListType.Overdue){
            p = manager.getListOfOverduePackages();
            if(p == null){
                resultIfNoPackagesEmpty();
                return;
            }
        }
        else{
            p = manager.getListOfUpcomingPackages();
            if(p == null){
                resultIfNoPackagesEmpty();
                return;
            }
        }
        for(int i = 0; i < p.size(); i++){
            scrollView.add(createPanel(p.get(i), i));
        }
    }

    private void resultIfNoPackagesEmpty(){
        JLabel noPackagesText = new JLabel("No packages to show");
        noPackagesText.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollView.add(noPackagesText);
    }

    /**
     * refresh the list of packages displayed in the ScrollPanel
     */
    private void refreshPackageField(){
        scrollView.removeAll();
        populatedPackageField();
        scrollView.revalidate();
        scrollView.repaint();
    }

    /**
     * Load data to project and setup save for closing
     */
    private void loadAndSetUpSave(){
        //loads from JSON
        manager.LoadFromJsonFile();

        //sets up listener to save to JSON at Exit
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manager.saveToJsonFile();
                super.windowClosing(e);
            }
        });
    }

    /**
     * creates subpanel displaying package information
     * @param p package data
     * @param i index number
     * @return The formatted panel
     */
    private JPanel createPanel(Package p, int i) {
        JPanel outerPanel = new JPanel();

        JPanel packagePanel = new JPanel(new BorderLayout());
        packagePanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 150));
        packagePanel.setMinimumSize(new Dimension(0, 150));
        //packagePanel.add(new Box.Filler(new Dimension(0, 0), new Dimension(Short.MAX_VALUE, 0), new Dimension(Short.MAX_VALUE, 0)));
        packagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        packagePanel.add(new JLabel("Package #" + (i+1) + " (" + p.getPackageType()+ ")"), BorderLayout.NORTH);

        JPanel innerPanel = new JPanel(new BorderLayout());

        JPanel centerPanel = new JPanel();
        BoxLayout centerLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
        centerPanel.setLayout(centerLayout);

        centerPanel.add(new JLabel("Name: " + p.getName()));
        centerPanel.add(new JLabel("Notes: " + p.getNotes()));
        centerPanel.add(new JLabel("Price: $" + p.getPriceInDollars()));
        centerPanel.add(new JLabel("Weight: " + p.getWeight() + "lbs"));
        centerPanel.add(new JLabel("Expected delivery date: " + p.getDeliveryDateAsFormattedString()));
        if(p.getPackageType().equalsIgnoreCase("Book")){
            centerPanel.add(new JLabel("Author: " + p.getSpecialAttributeAsString()));
        }
        else if(p.getPackageType().equalsIgnoreCase("Electronic")){
            centerPanel.add(new JLabel("Environmental fee: " + p.getSpecialAttributeAsString()), BorderLayout.LINE_END);
        }
        else if(p.getPackageType().equalsIgnoreCase("Perishable")){
            centerPanel.add(new JLabel("Expiry date: " + p.getSpecialAttributeAsString()), BorderLayout.LINE_END);
        }
        centerPanel.setBackground(Color.LIGHT_GRAY);

        innerPanel.add(centerPanel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new BorderLayout());
        JCheckBox isDelivered = new JCheckBox("delivered");
        isDelivered.setBackground(Color.LIGHT_GRAY);

        if(p.checkIfDelivered()){
            isDelivered.setSelected(true);
        }

        isDelivered.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isDelivered.isSelected()){
                    p.isDelivered();
                }
                else{
                    p.isNotDelivered();
                }
            }
        });
        bottomPanel.add(isDelivered, BorderLayout.WEST);

        Button remove = new Button("Remove");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.removeFromPackages(p);
                refreshPackageField();
            }
        });
        bottomPanel.add(remove, BorderLayout.EAST);
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        innerPanel.add(bottomPanel, BorderLayout.SOUTH);

        innerPanel.setBorder(new LineBorder(Color.BLACK));
        packagePanel.add(innerPanel);
        return packagePanel;
    }
}
