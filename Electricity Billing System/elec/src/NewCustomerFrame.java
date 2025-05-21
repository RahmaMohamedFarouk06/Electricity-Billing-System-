import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class NewCustomerFrame extends JFrame {
    private JTextField txtName, txtNid, txtAddress, txtEmail, txtRegion, txtPhoneNumber, txtContractPath;
    private JButton btnRegister, btnClear, btnBack, btnBrowseContract;

    private List<OldCustomer> customersList; // Reference to the main list of customers
    private String customersFilePath;

    public NewCustomerFrame(List<OldCustomer> customers, String customersFile) {
        this.customersList = customers;
        this.customersFilePath = customersFile;

        setTitle("New Customer Registration");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close to not exit the main app
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; txtName = new JTextField(20); formPanel.add(txtName, gbc);

        // NID
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; formPanel.add(new JLabel("National ID (14 digits):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; txtNid = new JTextField(20); formPanel.add(txtNid, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; txtAddress = new JTextField(20); formPanel.add(txtAddress, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Email (e.g., user@example.com):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; txtEmail = new JTextField(20); formPanel.add(txtEmail, gbc);

        // Region
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Region:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2; txtRegion = new JTextField(20); formPanel.add(txtRegion, gbc);

        // Phone Number
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Phone Number (11 digits):"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2; txtPhoneNumber = new JTextField(20); formPanel.add(txtPhoneNumber, gbc);

        // Contract Path
        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(new JLabel("Contract Copy Path:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 1; txtContractPath = new JTextField(15); txtContractPath.setEditable(false); formPanel.add(txtContractPath, gbc);
        gbc.gridx = 2; gbc.gridy = 6; gbc.gridwidth = 1; btnBrowseContract = new JButton("Browse"); formPanel.add(btnBrowseContract, gbc);

        formPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnRegister = new JButton("Register Customer");
        btnClear = new JButton("Clear Fields");
        btnBack = new JButton("Back to Main Menu");

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnBrowseContract.addActionListener(e -> browseContractFile());
        btnRegister.addActionListener(e -> registerNewCustomer());
        btnClear.addActionListener(e -> clearFormFields());
        btnBack.addActionListener(e -> dispose()); // Close this window
    }

    private void browseContractFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtContractPath.setText(selectedFile.getAbsolutePath());
        }
    }

    private void clearFormFields() {
        txtName.setText("");
        txtNid.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        txtRegion.setText("");
        txtPhoneNumber.setText("");
        txtContractPath.setText("");
    }

    private void registerNewCustomer() {
        String name = txtName.getText().trim();
        String nid = txtNid.getText().trim();
        String address = txtAddress.getText().trim();
        String email = txtEmail.getText().trim();
        String region = txtRegion.getText().trim();
        String phoneNumberStr = txtPhoneNumber.getText().trim();
        String contractPath = txtContractPath.getText().trim();

        // Basic Validation
        if (name.isEmpty() || nid.isEmpty() || address.isEmpty() || email.isEmpty() || region.isEmpty() || phoneNumberStr.isEmpty() || contractPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!nid.matches("\\d{14}")) {
            JOptionPane.showMessageDialog(this, "National ID must be exactly 14 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) { // Basic email regex
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phoneNumberStr.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be exactly 11 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long phoneNumber;
        try {
            phoneNumber = Long.parseLong(phoneNumberStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid phone number format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check for duplicate NID
        for (OldCustomer existingCust : customersList) {
            if (existingCust.getNid().equals(nid)) {
                JOptionPane.showMessageDialog(this, "Error: Customer with NID " + nid + " already exists.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        NewCustomer newCust = new NewCustomer();
        newCust.setName(name);
        newCust.setNid(nid); // Assumes setNid in Customer takes String and validates or just sets
        newCust.setAddress(address);
        newCust.setEmail(email); // Assumes setEmail in Customer takes String and validates or just sets
        newCust.setRegion(region);
        newCust.setNumber(phoneNumber); // Assumes setNumber in Customer takes long
        newCust.setContract(contractPath);
        newCust.generateMeterCode(); // Generate meter code

        // Check for duplicate Meter Code (though less likely if NID is unique and meter code derived from NID)
         for (OldCustomer existingCust : customersList) {
            if (existingCust.getMeterCode().equals(newCust.getMeterCode())) {
                JOptionPane.showMessageDialog(this, "Error: Customer with generated Meter Code " + newCust.getMeterCode() + " already exists (duplicate NID suffix).", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        OldCustomer registeredCustomer = newCust.createNewCustomer(customersList); // This adds to list and calls FileSystem.saveCustomerData

        if (registeredCustomer != null) {
            // FileSystem.saveCustomerData is called within createNewCustomer
            // We might want to make createNewCustomer return a status or have FileSystem calls return status to GUI
            // For now, assume success if not null
            JOptionPane.showMessageDialog(this, 
                "Customer registered successfully!\nName: " + registeredCustomer.getName() + 
                "\nMeter Code: " + registeredCustomer.getMeterCode() + 
                "\nContract: " + newCust.getContract() + // Display contract path
                "\nData saved to file.", 
                "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
            clearFormFields();
            // Optionally close the window: dispose();
        } else {
            // This case might be hit if newCust.duplicated was true, which is an odd internal flag.
            // The NID/Meter Code check above should be the primary guard.
            JOptionPane.showMessageDialog(this, "Failed to register customer. Possible duplication or other error.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Main method for testing this frame independently (optional)
    /*
    public static void main(String[] args) {
        List<OldCustomer> testCustomers = new ArrayList<>();
        // Simulate loading for testing
        FileSystem.loadCustomersFromFile(testCustomers, "Customers.txt"); 
        SwingUtilities.invokeLater(() -> {
            NewCustomerFrame frame = new NewCustomerFrame(testCustomers, "Customers.txt");
            frame.setVisible(true);
        });
    }
    */
}

