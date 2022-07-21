package cmpt213.assignment3.packagedeliveries.view;

import cmpt213.assignment3.packagedeliveries.control.PackageManager;
import cmpt213.assignment3.packagedeliveries.model.Package;
import cmpt213.assignment3.packagedeliveries.model.PackageFactory;
import com.github.lgooddatepicker.components.DateTimePicker;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Handles GUI for Add Package Dialog Box
 */
public class AddPackageDialog extends JDialog {
    private JPanel contentPane;
    PackageManager manager = PackageManager.getInstance();
    private JComboBox<String> packageTypePA;
    private JTextField nameAP;
    private JTextField notesAP;
    private JSpinner weightAP;
    private JSpinner priceAP;
    private JButton createButtonAP;
    private JButton cancelButtonAP;
    private JLabel variableAP;
    private JPanel VarSlotAP;
    private DateTimePicker deliveryDateAP;
    private final JTextField authorAP;
    private final JSpinner environmentalFeeAP;
    private final DateTimePicker expiryAP;

    public AddPackageDialog() {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(createButtonAP);

        this.setTitle("Add Package");

        authorAP = new JTextField();
        environmentalFeeAP = new JSpinner();
        expiryAP = new DateTimePicker();

        packageTypePA.addItem("Book");
        packageTypePA.addItem("Electronic");
        packageTypePA.addItem("Perishable");
        packageTypePA.setSelectedIndex(0);

        setupVariableSlot();

        SpinnerNumberModel model = new SpinnerNumberModel(0.0, 0.0, 1000000.0, 1.0);
        weightAP.setModel(model);
        priceAP.setModel(model);
        environmentalFeeAP.setModel(model);

        createButtonAP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        cancelButtonAP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * sets up the dynamically changing input (Author for Books, ect.)
     */
    private void setupVariableSlot() {
        //default setup;
        variableAP.setText("Author:");
        VarSlotAP.setLayout(new GridLayout());
        VarSlotAP.add(authorAP);

        packageTypePA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(packageTypePA.getSelectedIndex() == 0){
                    variableAP.setText("Author:");
                    VarSlotAP.removeAll();
                    VarSlotAP.add(authorAP);
                }
                else if(packageTypePA.getSelectedIndex() == 1){
                    variableAP.setText("Environmental Fee:");
                    VarSlotAP.removeAll();
                    VarSlotAP.add(environmentalFeeAP);
                }
                else if(packageTypePA.getSelectedIndex() == 2){
                    variableAP.setText("Expiry date:");
                    VarSlotAP.removeAll();
                    VarSlotAP.add(expiryAP);
                }
            }
        });
    }

    private void onOK() {
        if(packageTypePA.getSelectedItem() == null || nameAP.getText().equals("") ||
                deliveryDateAP.getDateTimeStrict() == null || (packageTypePA.getSelectedItem().equals("Perishable") && expiryAP.getDateTimeStrict() == null) ||
                (packageTypePA.getSelectedItem().equals("Book") && authorAP.getText().equals(""))){
            JOptionPane.showMessageDialog(this, "Please fill out all fields in the form to continue", "Warning", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Package newPackage = PackageFactory.getInstance((String)packageTypePA.getSelectedItem());
        if(newPackage != null) {
            newPackage.setName(nameAP.getText());
            newPackage.setNotes(notesAP.getText());
            newPackage.setWeight((Double) weightAP.getValue());
            newPackage.setPriceInDollars((Double) priceAP.getValue());
            newPackage.setDeliveryDate(deliveryDateAP.getDateTimeStrict());
            if(packageTypePA.getSelectedItem().equals("Book")){
                newPackage.setSpecialAttributeFromString(authorAP.getText());
            }
            else if(packageTypePA.getSelectedItem().equals("Electronics")){
                newPackage.setSpecialAttributeFromString(environmentalFeeAP.getValue().toString());
            }
            else if(packageTypePA.getSelectedItem().equals("Perishable")){
                newPackage.setSpecialAttributeFromString(expiryAP.getDateTimeStrict().toString());
            }
            manager.addToPackages(newPackage);
            System.out.println(manager.numberOfPackages());
        }
        else{
            System.out.println("Error: Package type not recognized!");
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
