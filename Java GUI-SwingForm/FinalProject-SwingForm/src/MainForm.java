import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainForm extends JFrame{
    private JLabel employeeIdLabel;
    private JPanel MainPanel;
    private JComboBox IDcomboBox;
    private JTextField firstNameTextField;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel ageLabel;
    private JLabel phoneLabel;
    private JTextField ageTextField;
    private JTextField lastNameTextField;
    private JTextField phoneTextField;
    private JButton addButton;
    private JButton updateButton;
    private JTextField testTextField;
    private JLabel employeeNameLabel;
    private JTextField employeeNameTextField;
    private JButton searchEmployeeButton;
    private JScrollPane ScrollPane;
    private JTable employeeInfoTable;
    private JLabel message1Label;

    public MainForm(){

        initiateForm();

        fillComboBox();

        fillTextBoxesData();

        createTable();

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = JOptionPane.showInputDialog("Enter the new First Name");
                String lastName = JOptionPane.showInputDialog("Enter the new Last Name");
                String age = JOptionPane.showInputDialog("Enter the new Age");
                String phone = JOptionPane.showInputDialog("Enter the new Phone");

                if(firstName == null || lastName == null || age == null || phone == null){
                    JOptionPane.showMessageDialog(MainForm.this, "You must enter all new data to update");
                    return;
                }

                int parsedAge;
                try
                {
                    parsedAge = Integer.parseInt(age);
                }
                catch (Exception exception)
                {
                    JOptionPane.showMessageDialog(MainForm.this, exception.getMessage(), "Age exception", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try
                {
                    Database.getINSTANCE().updateContact((int) IDcomboBox.getSelectedItem(), firstName, lastName, parsedAge, phone);
                }
                catch (SQLException exception)
                {
                    JOptionPane.showMessageDialog(MainForm.this, exception.getMessage(), "SQL exception", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                fillTextBoxesData();

            }
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nextId;
                try
                {
                    nextId = Database.getINSTANCE().getNextAvailableId();
                }
                catch (SQLException exception)
                {
                    JOptionPane.showMessageDialog(MainForm.this, exception.getMessage(), "SQL exception", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String firstName = JOptionPane.showInputDialog("Enter the new First Name");
                String lastName = JOptionPane.showInputDialog("Enter the new Last Name");
                String age = JOptionPane.showInputDialog("Enter the new Age");
                String phone = JOptionPane.showInputDialog("Enter the new Phone");

                if(firstName == null || lastName == null || age == null || phone == null){
                    JOptionPane.showMessageDialog(MainForm.this, "You must enter all new data to insert");
                    return;
                }

                int parsedAge;
                try
                {
                    parsedAge = Integer.parseInt(age);
                }
                catch (Exception exception)
                {
                    JOptionPane.showMessageDialog(MainForm.this, exception.getMessage(), "Age exception", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try
                {
                    Database.getINSTANCE().addNewContact(nextId, firstName, lastName, parsedAge, phone);
                }
                catch (Exception exception)
                {
                    JOptionPane.showMessageDialog(MainForm.this, exception.getMessage(), "SQL exception", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                fillComboBox();

                fillTextBoxesData();
            }
        });

        IDcomboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillTextBoxesData();
            }
        });

        searchEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, String> currentContactInfo;

                try
                {
                    currentContactInfo = Database.getINSTANCE().getContactInfoByName(employeeNameTextField.getText());
                }
                catch (SQLException exception)
                {
                    JOptionPane.showMessageDialog(MainForm.this, "SQL error, employee name not found", "SQL exception", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Object[][] values = {
                        {
                                currentContactInfo.get("Id"),
                                currentContactInfo.get("firstName"),
                                currentContactInfo.get("lastName"),
                                currentContactInfo.get("age"),
                                currentContactInfo.get("phone")
                        }};

                employeeInfoTable.setModel(new DefaultTableModel(
                        values,
                        new String[] {"Id", "First Name", "Last Name", "Age", "Phone"}
                ));
            }
        });
    }

    private void initiateForm(){
        setContentPane(MainPanel);
        setTitle("Swing and Database");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(750, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fillComboBox(){
        //puts the value of every contact id in the comboBox
        //Method can be usefull if I want to update the comboBox, Like after inserting

        ArrayList<Integer> contactIds;

        try
        {
            contactIds = Database.getINSTANCE().getAllContactId();
        }
        catch (SQLException exception)
        {
            JOptionPane.showMessageDialog(MainForm.this, exception.getMessage(), "SQL exception", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (IDcomboBox.getSelectedItem() == null)
        {
            for (int i : contactIds) {
                IDcomboBox.addItem(i);
            }
        }
        else
        {
            IDcomboBox.addItem(contactIds.get(contactIds.size()-1));
        }



    }

    private void createTable(){
        employeeInfoTable.setModel(new DefaultTableModel(
                null,
                new String[] {"Id", "First Name", "Last Name", "Age", "Phone"}
        ));
    }

    private void fillTextBoxesData() {
        HashMap<String, String> currentIdInfo;

        try
        {
            currentIdInfo = Database.getINSTANCE().getContactInfoById((int) IDcomboBox.getSelectedItem());
        }
        catch (SQLException exception)
        {
            JOptionPane.showMessageDialog(MainForm.this, exception.getMessage(), "SQL exception", JOptionPane.ERROR_MESSAGE);
            return;
        }

        firstNameTextField.setText(currentIdInfo.get("firstName"));
        lastNameTextField.setText(currentIdInfo.get("lastName"));
        ageTextField.setText(currentIdInfo.get("age"));
        phoneTextField.setText(currentIdInfo.get("phone"));
    }
}
