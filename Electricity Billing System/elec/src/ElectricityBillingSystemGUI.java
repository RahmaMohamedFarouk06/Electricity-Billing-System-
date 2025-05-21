import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ElectricityBillingSystemGUI extends JFrame {
    private static List<OldCustomer> customers = new ArrayList<>();
    private static List<Operator> operators = new ArrayList<>();
    private static List<Admin> admins = new ArrayList<>();

    private static final String CUSTOMERS_FILE = "Customers.txt";
    private static final String OPERATORS_FILE = "Operators.txt";

    public ElectricityBillingSystemGUI() {
        setTitle("⚡ Electricity Billing System ⚡");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        FileSystem.loadCustomersFromFile(customers, CUSTOMERS_FILE);
        FileSystem.loadOperatorsFromFile(operators, OPERATORS_FILE);
        if (admins.isEmpty()) {
            admins.add(new Admin("admin1")); // Default admin
        }

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("⚡ Electricity Billing System ⚡", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JButton btnNewCustomer = new JButton("New Customer Module");
        JButton btnOldCustomer = new JButton("Old Customer Module");
        JButton btnOperator = new JButton("Operator Module");
        JButton btnAdmin = new JButton("Admin Module");
        JButton btnExit = new JButton("Exit System");

        styleButton(btnNewCustomer);
        styleButton(btnOldCustomer);
        styleButton(btnOperator);
        styleButton(btnAdmin);
        styleButton(btnExit);

        mainPanel.add(btnNewCustomer);
        mainPanel.add(btnOldCustomer);
        mainPanel.add(btnOperator);
        mainPanel.add(btnAdmin);
        mainPanel.add(btnExit);

        add(mainPanel, BorderLayout.CENTER);

        btnNewCustomer.addActionListener(e -> openNewCustomerDialog());
        btnOldCustomer.addActionListener(e -> openOldCustomerDialog());
        btnOperator.addActionListener(e -> openOperatorDialog());
        btnAdmin.addActionListener(e -> openAdminDialog());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
    }

    private void openNewCustomerDialog() {
        NewCustomerDialog dialog = new NewCustomerDialog(this, customers, CUSTOMERS_FILE);
        dialog.setVisible(true);
    }

    private void openOldCustomerDialog() {
        String meterCode = JOptionPane.showInputDialog(this, "Enter your meter code to login:", "Old Customer Login", JOptionPane.PLAIN_MESSAGE);
        if (meterCode != null && !meterCode.trim().isEmpty()) {
            OldCustomer customer = OldCustomer.findCustomerByMeterCode(customers, meterCode.trim());
            if (customer != null) {
                OldCustomerDialog dialog = new OldCustomerDialog(this, customer, customers, CUSTOMERS_FILE);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Customer with meter code '" + meterCode.trim() + "' not found.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (meterCode != null) {
             JOptionPane.showMessageDialog(this, "Meter code cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openOperatorDialog() {
        String operatorName = JOptionPane.showInputDialog(this, "Enter your operator name to login:", "Operator Login", JOptionPane.PLAIN_MESSAGE);
        if (operatorName != null && !operatorName.trim().isEmpty()) {
            Operator operator = Operator.findOperatorByName(operators, operatorName.trim());
            if (operator != null) {
                OperatorDialog dialog = new OperatorDialog(this, operator, customers, operators, CUSTOMERS_FILE, OPERATORS_FILE);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Operator '" + operatorName.trim() + "' not found.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (operatorName != null) {
            JOptionPane.showMessageDialog(this, "Operator name cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAdminDialog() {
        String adminName = JOptionPane.showInputDialog(this, "Enter your admin name to login:", "Admin Login", JOptionPane.PLAIN_MESSAGE);
        if (adminName != null && !adminName.trim().isEmpty()) {
            Admin admin = Admin.findAdminByName(admins, adminName.trim());
            if (admin != null) {
                AdminDialog dialog = new AdminDialog(this, admin, customers, operators, CUSTOMERS_FILE, OPERATORS_FILE);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Admin '" + adminName.trim() + "' not found.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (adminName != null) {
            JOptionPane.showMessageDialog(this, "Admin name cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Inner class for New Customer Registration Dialog ---
    class NewCustomerDialog extends JDialog { 
        private JTextField txtName, txtNid, txtAddress, txtEmail, txtRegion, txtPhoneNumber, txtContractPath;
        private JButton btnBrowseContract;
        private List<OldCustomer> customersListRef;

        public NewCustomerDialog(Frame owner, List<OldCustomer> customersList, String customersFile) {
            super(owner, "New Customer Registration", true);
            this.customersListRef = customersList;
            setSize(500, 450); setLocationRelativeTo(owner); setLayout(new BorderLayout(10, 10));
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
            int y = 0;
            formPanel.add(new JLabel("Name:"), gbc(0, y, 1)); txtName = new JTextField(20); formPanel.add(txtName, gbc(1, y++, 2));
            formPanel.add(new JLabel("National ID (14 digits):"), gbc(0, y, 1)); txtNid = new JTextField(20); formPanel.add(txtNid, gbc(1, y++, 2));
            formPanel.add(new JLabel("Address:"), gbc(0, y, 1)); txtAddress = new JTextField(20); formPanel.add(txtAddress, gbc(1, y++, 2));
            formPanel.add(new JLabel("Email (user@example.com):"), gbc(0, y, 1)); txtEmail = new JTextField(20); formPanel.add(txtEmail, gbc(1, y++, 2));
            formPanel.add(new JLabel("Region:"), gbc(0, y, 1)); txtRegion = new JTextField(20); formPanel.add(txtRegion, gbc(1, y++, 2));
            formPanel.add(new JLabel("Phone Number (11 digits):"), gbc(0, y, 1)); txtPhoneNumber = new JTextField(20); formPanel.add(txtPhoneNumber, gbc(1, y++, 2));
            formPanel.add(new JLabel("Contract Copy Path:"), gbc(0, y, 1));
            txtContractPath = new JTextField(15); txtContractPath.setEditable(false); formPanel.add(txtContractPath, gbc(1, y, 1));
            btnBrowseContract = new JButton("Browse"); formPanel.add(btnBrowseContract, gbc(2, y++, 1));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); add(formPanel, BorderLayout.CENTER);
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton btnRegister = new JButton("Register Customer"); JButton btnClear = new JButton("Clear Fields"); JButton btnCancel = new JButton("Cancel");
            buttonPanel.add(btnRegister); buttonPanel.add(btnClear); buttonPanel.add(btnCancel); add(buttonPanel, BorderLayout.SOUTH);
            btnBrowseContract.addActionListener(e -> browseContractFile());
            btnRegister.addActionListener(e -> registerNewCustomer());
            btnClear.addActionListener(e -> clearFormFields());
            btnCancel.addActionListener(e -> dispose());
        }
        private GridBagConstraints gbc(int x, int y, int width) {
            GridBagConstraints g = new GridBagConstraints(); g.gridx = x; g.gridy = y; g.gridwidth = width;
            g.insets = new Insets(5,5,5,5); g.fill = GridBagConstraints.HORIZONTAL; return g;
        }
        private void browseContractFile() { JFileChooser fc = new JFileChooser(); if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) txtContractPath.setText(fc.getSelectedFile().getAbsolutePath()); }
        private void clearFormFields() { txtName.setText(""); txtNid.setText(""); txtAddress.setText(""); txtEmail.setText(""); txtRegion.setText(""); txtPhoneNumber.setText(""); txtContractPath.setText(""); }
        private void registerNewCustomer() {
            String name = txtName.getText().trim(), nid = txtNid.getText().trim(), address = txtAddress.getText().trim(), email = txtEmail.getText().trim(), region = txtRegion.getText().trim(), phoneStr = txtPhoneNumber.getText().trim(), contractPath = txtContractPath.getText().trim();
            if (name.isEmpty() || nid.isEmpty() || address.isEmpty() || email.isEmpty() || region.isEmpty() || phoneStr.isEmpty() || contractPath.isEmpty()) { JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!nid.matches("\\d{14}")) { JOptionPane.showMessageDialog(this, "NID must be 14 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) { JOptionPane.showMessageDialog(this, "Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!phoneStr.matches("\\d{11}")) { JOptionPane.showMessageDialog(this, "Phone must be 11 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
            long phoneNumber; try { phoneNumber = Long.parseLong(phoneStr); } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid phone number.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
            for (OldCustomer existing : customersListRef) if (existing.getNid().equals(nid)) { JOptionPane.showMessageDialog(this, "NID " + nid + " already exists.", "Registration Error", JOptionPane.ERROR_MESSAGE); return; }
            NewCustomer newCustObj = new NewCustomer(); newCustObj.setName(name); newCustObj.setNid(nid); newCustObj.setAddress(address); newCustObj.setEmail(email); newCustObj.setRegion(region); newCustObj.setNumber(phoneNumber); newCustObj.setContract(contractPath); newCustObj.generateMeterCode();
            for (OldCustomer existing : customersListRef) if (existing.getMeterCode().equals(newCustObj.getMeterCode())) { JOptionPane.showMessageDialog(this, "Generated Meter Code " + newCustObj.getMeterCode() + " already exists (possible NID suffix collision).", "Registration Error", JOptionPane.ERROR_MESSAGE); return; }
            OldCustomer registered = newCustObj.createNewCustomer(customersListRef);
            if (registered != null) { JOptionPane.showMessageDialog(this, "Customer registered successfully!\nName: " + registered.getName() + "\nMeter Code: " + registered.getMeterCode(), "Success", JOptionPane.INFORMATION_MESSAGE); clearFormFields(); dispose(); }
            else { JOptionPane.showMessageDialog(this, "Failed to register customer (possibly marked as duplicate internally).", "Registration Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    // --- Inner class for Old Customer Module Dialog ---
    class OldCustomerDialog extends JDialog { 
        private OldCustomer currentCustomer;
        private List<OldCustomer> customersListRef;
        private JLabel lblBalance, lblReadings;
        public OldCustomerDialog(Frame owner, OldCustomer customer, List<OldCustomer> allCustomers, String customersFile) {
            super(owner, "Old Customer Module - " + customer.getName(), true);
            this.currentCustomer = customer; this.customersListRef = allCustomers;
            setSize(450, 350); setLocationRelativeTo(owner); setLayout(new BorderLayout(10, 10));
            JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5)); infoPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            infoPanel.add(new JLabel("Welcome, " + currentCustomer.getName() + " (Meter: " + currentCustomer.getMeterCode() + ")"));
            lblBalance = new JLabel("Current Balance Due: " + currentCustomer.getBalanceDue() + " EGP"); infoPanel.add(lblBalance);
            lblReadings = new JLabel("Last Reading: " + currentCustomer.getLastReading() + " | Current Reading: " + currentCustomer.getCurrentReading()); infoPanel.add(lblReadings);
            add(infoPanel, BorderLayout.NORTH);
            JTabbedPane tabbedPane = new JTabbedPane();
            JPanel payBillPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); JTextField txtPaymentAmount = new JTextField(10); JButton btnPay = new JButton("Pay Bill");
            payBillPanel.add(new JLabel("Amount to Pay:")); payBillPanel.add(txtPaymentAmount); payBillPanel.add(btnPay);
            btnPay.addActionListener(e -> payCustomerBill(txtPaymentAmount.getText())); tabbedPane.addTab("Pay Bill", payBillPanel);
            JPanel readingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); JTextField txtNewReading = new JTextField(10); JButton btnSubmitReading = new JButton("Submit Reading");
            readingPanel.add(new JLabel("New Meter Reading:")); readingPanel.add(txtNewReading); readingPanel.add(btnSubmitReading);
            btnSubmitReading.addActionListener(e -> enterNewReading(txtNewReading.getText())); tabbedPane.addTab("Enter Reading", readingPanel);
            JPanel complainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); JButton btnComplain = new JButton("Register a Complaint");
            if (currentCustomer.isComplain()) { btnComplain.setText("Complaint Already Registered"); btnComplain.setEnabled(false); }
            btnComplain.addActionListener(e -> registerComplaint(btnComplain)); complainPanel.add(btnComplain); tabbedPane.addTab("Complain", complainPanel);
            add(tabbedPane, BorderLayout.CENTER);
            JButton btnClose = new JButton("Close Module"); btnClose.addActionListener(e -> dispose());
            JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); southPanel.add(btnClose); add(southPanel, BorderLayout.SOUTH);
        }
        private void updateDisplay() { lblBalance.setText("Current Balance Due: " + currentCustomer.getBalanceDue() + " EGP"); lblReadings.setText("Last Reading: " + currentCustomer.getLastReading() + " | Current Reading: " + currentCustomer.getCurrentReading()); }
        private void payCustomerBill(String amountStr) {
            if (amountStr.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter a payment amount.", "Input Error", JOptionPane.ERROR_MESSAGE); return; }
            try { int amount = Integer.parseInt(amountStr); if (amount <= 0) { JOptionPane.showMessageDialog(this, "Payment amount must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE); return; }
                String result = currentCustomer.payBill(currentCustomer.getMeterCode(), amount);
                JOptionPane.showMessageDialog(this, result, "Payment Status", result.startsWith("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                if (!result.startsWith("Error")) { FileSystem.saveCustomerData(customersListRef, CUSTOMERS_FILE); updateDisplay(); }
            } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Input Error", JOptionPane.ERROR_MESSAGE); }
        }
        private void enterNewReading(String readingStr) {
            if (readingStr.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter the new reading.", "Input Error", JOptionPane.ERROR_MESSAGE); return; }
            try { int reading = Integer.parseInt(readingStr);
                String result = currentCustomer.enterMonthlyReading(reading);
                JOptionPane.showMessageDialog(this, result, "Reading Update Status", result.startsWith("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                if (!result.startsWith("Error")) { FileSystem.saveCustomerData(customersListRef, CUSTOMERS_FILE); updateDisplay(); }
            } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid reading entered.", "Input Error", JOptionPane.ERROR_MESSAGE); }
        }
        private void registerComplaint(JButton complainButton) { currentCustomer.setComplain(true); FileSystem.saveCustomerData(customersListRef, CUSTOMERS_FILE); JOptionPane.showMessageDialog(this, "Complaint registered successfully for meter " + currentCustomer.getMeterCode(), "Complaint Registered", JOptionPane.INFORMATION_MESSAGE); complainButton.setText("Complaint Already Registered"); complainButton.setEnabled(false); updateDisplay(); }
    }

    // --- Inner class for Operator Module Dialog (Reconciled with provided Operator.java) ---
    class OperatorDialog extends JDialog {
        private Operator currentOperator;
        private List<OldCustomer> allCustomersRef;
        private List<Operator> allOperatorsRef;
        private JLabel lblTotalCollected;

        public OperatorDialog(Frame owner, Operator operator, List<OldCustomer> customers, List<Operator> operatorsList, String custFile, String opFile) {
            super(owner, "Operator Module - " + operator.getOperatorName(), true);
            this.currentOperator = operator;
            this.allCustomersRef = customers;
            this.allOperatorsRef = operatorsList;

            setSize(650, 550);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout(10, 10));

            JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
            infoPanel.add(new JLabel("Operator: " + currentOperator.getOperatorName()));
            lblTotalCollected = new JLabel(" | Total Collected: " + currentOperator.getTotalCollected() + " EGP");
            infoPanel.add(lblTotalCollected);
            add(infoPanel, BorderLayout.NORTH);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Collect Payment", createCollectPaymentPanel());
            tabbedPane.addTab("Print Bill Details", createPrintBillDetailsPanel());
            tabbedPane.addTab("View Bills by Region", createViewBillsByRegionPanel());
            tabbedPane.addTab("Enter/Update Customer Reading", createEnterUpdateReadingPanel());
            tabbedPane.addTab("Define Bill (Apply Tariff)", createDefineBillPanel());
            tabbedPane.addTab("Stop Meter/Cancel", createStopMeterPanel());
            add(tabbedPane, BorderLayout.CENTER);

            JButton btnClose = new JButton("Close Module");
            btnClose.addActionListener(e -> dispose());
            JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            southPanel.add(btnClose);
            add(southPanel, BorderLayout.SOUTH);
        }

        private void updateTotalCollectedDisplay() {
            lblTotalCollected.setText(" | Total Collected: " + currentOperator.getTotalCollected() + " EGP");
        }

        private JPanel createCollectPaymentPanel() {
            JPanel panel = new JPanel(new GridBagLayout()); GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(5,5,5,5);
            JTextField txtMeterCode = new JTextField(15);
            JTextField txtAmount = new JTextField(10);
            JButton btnCollect = new JButton("Collect Payment");
            gbc.gridx=0; gbc.gridy=0; panel.add(new JLabel("Customer Meter Code:"), gbc);
            gbc.gridx=1; gbc.gridy=0; panel.add(txtMeterCode, gbc);
            gbc.gridx=0; gbc.gridy=1; panel.add(new JLabel("Amount Paid:"), gbc);
            gbc.gridx=1; gbc.gridy=1; panel.add(txtAmount, gbc);
            gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; panel.add(btnCollect, gbc);
            btnCollect.addActionListener(e -> {
                String meter = txtMeterCode.getText().trim();
                String amountStr = txtAmount.getText().trim();
                if(meter.isEmpty() || amountStr.isEmpty()) { JOptionPane.showMessageDialog(this, "Meter code and amount are required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                OldCustomer cust = OldCustomer.findCustomerByMeterCode(allCustomersRef, meter);
                if(cust == null) { JOptionPane.showMessageDialog(this, "Customer with meter code '" + meter + "' not found.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                try {
                    int amount = Integer.parseInt(amountStr);
                    String result = currentOperator.collectPayment(cust, amount);
                    JOptionPane.showMessageDialog(this, result, "Payment Collection", result.startsWith("Error") || result.startsWith("Payment was unsuccessful") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                    if(!(result.startsWith("Error") || result.startsWith("Payment was unsuccessful"))) { 
                        FileSystem.saveCustomerData(allCustomersRef, CUSTOMERS_FILE); 
                        FileSystem.saveOperatorData(allOperatorsRef, OPERATORS_FILE); 
                        updateTotalCollectedDisplay(); 
                        txtMeterCode.setText(""); txtAmount.setText("");
                    }
                } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE); }
            });
            return panel;
        }

        private JPanel createPrintBillDetailsPanel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JTextField txtMeterCode = new JTextField(15);
            JButton btnPrint = new JButton("Get Bill Details");
            panel.add(new JLabel("Customer Meter Code:")); panel.add(txtMeterCode); panel.add(btnPrint);
            btnPrint.addActionListener(e -> {
                String meter = txtMeterCode.getText().trim();
                if(meter.isEmpty()) { JOptionPane.showMessageDialog(this, "Meter code is required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                OldCustomer cust = OldCustomer.findCustomerByMeterCode(allCustomersRef, meter);
                if(cust == null) { JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                String billDetails = currentOperator.getBillDetails(cust);
                JTextArea billArea = new JTextArea(billDetails, 10, 40);
                billArea.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(billArea), "Customer Bill Details for " + cust.getName(), JOptionPane.INFORMATION_MESSAGE);
            });
            return panel;
        }

        private JPanel createViewBillsByRegionPanel() {
            JPanel panel = new JPanel(new BorderLayout(5,5));
            String[] regionsArray = allCustomersRef.stream().map(OldCustomer::getRegion).filter(r -> r != null && !r.isEmpty()).distinct().sorted().toArray(String[]::new);
            if (regionsArray.length == 0) regionsArray = new String[]{"N/A"};
            JComboBox<String> regionComboBox = new JComboBox<>(regionsArray);

            JTextArea billsArea = new JTextArea(15, 40); billsArea.setEditable(false);
            JButton btnView = new JButton("View Bills");
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(new JLabel("Select Region:")); topPanel.add(regionComboBox); topPanel.add(btnView);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(billsArea), BorderLayout.CENTER);
            btnView.addActionListener(e -> {
                String selectedRegion = (String) regionComboBox.getSelectedItem();
                if(selectedRegion == null || selectedRegion.equals("N/A")) { billsArea.setText("Please select a valid region or add customers."); return; }
                String result = currentOperator.getBillsByRegion(allCustomersRef, selectedRegion);
                billsArea.setText(result);
            });
            return panel;
        }

        private JPanel createEnterUpdateReadingPanel() {
            JPanel panel = new JPanel(new GridBagLayout()); GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(5,5,5,5);
            JTextField txtMeterCode = new JTextField(15);
            JTextField txtReading = new JTextField(10);
            JButton btnSubmitReading = new JButton("Submit New Reading");
            gbc.gridx=0; gbc.gridy=0; panel.add(new JLabel("Customer Meter Code:"), gbc);
            gbc.gridx=1; gbc.gridy=0; panel.add(txtMeterCode, gbc);
            gbc.gridx=0; gbc.gridy=1; panel.add(new JLabel("New Reading:"), gbc);
            gbc.gridx=1; gbc.gridy=1; panel.add(txtReading, gbc);
            gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; panel.add(btnSubmitReading, gbc);
            btnSubmitReading.addActionListener(e -> {
                String meter = txtMeterCode.getText().trim();
                String readingStr = txtReading.getText().trim();
                if(meter.isEmpty() || readingStr.isEmpty()) { JOptionPane.showMessageDialog(this, "Meter code and reading are required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                OldCustomer cust = OldCustomer.findCustomerByMeterCode(allCustomersRef, meter);
                if(cust == null) { JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                try {
                    int newReading = Integer.parseInt(readingStr);
                    String result = cust.enterMonthlyReading(newReading); 
                    JOptionPane.showMessageDialog(this, result, "Reading Submission Status", result.startsWith("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                    if(!result.startsWith("Error")) { 
                        FileSystem.saveCustomerData(allCustomersRef, CUSTOMERS_FILE); 
                        txtMeterCode.setText(""); txtReading.setText("");
                    }
                } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid reading format.", "Error", JOptionPane.ERROR_MESSAGE); }
            });
            return panel;
        }

        private JPanel createDefineBillPanel() {
            JPanel panel = new JPanel(new GridBagLayout()); GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(5,5,5,5);
            JTextField txtMeterCode = new JTextField(15);
            JTextField txtPricePerUnit = new JTextField(10);
            JButton btnDefineBill = new JButton("Define Bill for Customer");
            gbc.gridx=0; gbc.gridy=0; panel.add(new JLabel("Customer Meter Code:"), gbc);
            gbc.gridx=1; gbc.gridy=0; panel.add(txtMeterCode, gbc);
            gbc.gridx=0; gbc.gridy=1; panel.add(new JLabel("Price Per Unit (EGP):"), gbc);
            gbc.gridx=1; gbc.gridy=1; panel.add(txtPricePerUnit, gbc);
            gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; panel.add(btnDefineBill, gbc);

            btnDefineBill.addActionListener(e -> {
                String meter = txtMeterCode.getText().trim();
                String priceStr = txtPricePerUnit.getText().trim();
                if(meter.isEmpty() || priceStr.isEmpty()) { JOptionPane.showMessageDialog(this, "Meter code and price per unit are required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                OldCustomer cust = OldCustomer.findCustomerByMeterCode(allCustomersRef, meter);
                if(cust == null) { JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                try {
                    int price = Integer.parseInt(priceStr);
                    String result = currentOperator.defineTariff(cust, price);
                    JOptionPane.showMessageDialog(this, result, "Define Bill Status", result.startsWith("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                    if(result.startsWith("✅")) { 
                        FileSystem.saveCustomerData(allCustomersRef, CUSTOMERS_FILE);
                        txtMeterCode.setText(""); txtPricePerUnit.setText("");
                    }
                } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid price per unit.", "Error", JOptionPane.ERROR_MESSAGE); }
            });
            return panel;
        }

        private JPanel createStopMeterPanel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JTextField txtMeterCode = new JTextField(15);
            JButton btnStop = new JButton("Stop Meter & Cancel Subscription");
            panel.add(new JLabel("Customer Meter Code:")); panel.add(txtMeterCode); panel.add(btnStop);
            btnStop.addActionListener(e -> {
                String meter = txtMeterCode.getText().trim();
                if(meter.isEmpty()) { JOptionPane.showMessageDialog(this, "Meter code is required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                OldCustomer cust = OldCustomer.findCustomerByMeterCode(allCustomersRef, meter);
                if(cust == null) { JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to stop meter " + meter + " for " + cust.getName() + " and cancel their subscription?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String result = currentOperator.stopMeterAndCancelSubscription(cust);
                    JOptionPane.showMessageDialog(this, result, "Meter Stoppage", result.startsWith("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                    if(result.contains("has been stopped")) { 
                        FileSystem.saveCustomerData(allCustomersRef, CUSTOMERS_FILE); 
                        txtMeterCode.setText(""); 
                    }
                }
            });
            return panel;
        }
    }

    // --- Inner class for Admin Module Dialog (Reconciled with provided Admin.java) ---
    class AdminDialog extends JDialog {
        private Admin currentAdmin;
        private List<OldCustomer> allCustomersRef;
        private List<Operator> allOperatorsRef;

        public AdminDialog(Frame owner, Admin admin, List<OldCustomer> customersList, List<Operator> operatorsList, String custFile, String opFile) {
            super(owner, "Admin Module - " + admin.getAdminName(), true);
            this.currentAdmin = admin;
            this.allCustomersRef = customersList;
            this.allOperatorsRef = operatorsList;

            setSize(750, 600);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout(10, 10));

            JTabbedPane tabbedPane = new JTabbedPane();

            tabbedPane.addTab("View Bills/Region", createViewBillsByRegionPanel());
            tabbedPane.addTab("View Total Collections", createViewTotalCollectionsPanel());
            tabbedPane.addTab("Consumption Stats", createConsumptionStatsPanel());
            tabbedPane.addTab("Manage Customers", createManageCustomersPanel());
            tabbedPane.addTab("Manage Operators", createManageOperatorsPanel());

            add(tabbedPane, BorderLayout.CENTER);

            JButton btnClose = new JButton("Close Module");
            btnClose.addActionListener(e -> dispose());
            JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            southPanel.add(btnClose);
            add(southPanel, BorderLayout.SOUTH);
        }

        private JPanel createViewBillsByRegionPanel() {
            JPanel panel = new JPanel(new BorderLayout(5,5));
            String[] regionsArray = allCustomersRef.stream().map(OldCustomer::getRegion).filter(r -> r != null && !r.isEmpty()).distinct().sorted().toArray(String[]::new);
            if (regionsArray.length == 0) regionsArray = new String[]{"N/A"};
            JComboBox<String> regionComboBox = new JComboBox<>(regionsArray);
            JTextArea displayArea = new JTextArea(15, 50); displayArea.setEditable(false);
            JButton btnView = new JButton("View Bills for Region");
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(new JLabel("Select Region:")); topPanel.add(regionComboBox); topPanel.add(btnView);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
            btnView.addActionListener(e -> {
                String selectedRegion = (String) regionComboBox.getSelectedItem();
                if(selectedRegion == null || selectedRegion.equals("N/A")) { displayArea.setText("Please select a valid region or add customers."); return; }
                // Corrected method call to match Admin.java
                String result = currentAdmin.viewAllBillsByRegion(allCustomersRef, selectedRegion);
                displayArea.setText(result);
            });
            return panel;
        }

        private JPanel createViewTotalCollectionsPanel() {
            JPanel panel = new JPanel(new BorderLayout(5,5));
            JTextArea displayArea = new JTextArea(15, 50); displayArea.setEditable(false);
            JButton btnView = new JButton("View Total Collections by Operator");
            panel.add(btnView, BorderLayout.NORTH);
            panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
            btnView.addActionListener(e -> {
                // Corrected method call to match Admin.java
                String result = currentAdmin.viewTotalCollected(allOperatorsRef);
                displayArea.setText(result);
            });
            return panel;
        }

        private JPanel createConsumptionStatsPanel() {
            JPanel panel = new JPanel(new BorderLayout(5,5));
            String[] regionsArray = allCustomersRef.stream().map(OldCustomer::getRegion).filter(r -> r != null && !r.isEmpty()).distinct().sorted().toArray(String[]::new);
            if (regionsArray.length == 0) regionsArray = new String[]{"N/A"};
            JComboBox<String> regionComboBox = new JComboBox<>(regionsArray);
            JTextArea displayArea = new JTextArea(15, 50); displayArea.setEditable(false);
            JButton btnView = new JButton("View Consumption Statistics for Region");
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(new JLabel("Select Region:")); topPanel.add(regionComboBox); topPanel.add(btnView);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
            btnView.addActionListener(e -> {
                String selectedRegion = (String) regionComboBox.getSelectedItem();
                if(selectedRegion == null || selectedRegion.equals("N/A")) { displayArea.setText("Please select a valid region or add customers."); return; }
                // Corrected method call to match Admin.java
                String result = currentAdmin.makeConsumptionStatistics(allCustomersRef, selectedRegion);
                displayArea.setText(result);
            });
            return panel;
        }

        private JPanel createManageCustomersPanel() {
            JPanel panel = new JPanel(new BorderLayout(5,5));
            JTextArea displayArea = new JTextArea(15, 50); displayArea.setEditable(false);
            JButton btnViewAllCust = new JButton("View All Customers");
            JButton btnDeleteCustomer = new JButton("Delete Customer by Meter Code");
            // Add New Customer and Update Customer buttons could be added here for full CRUD
            // For now, Admin can only view and delete customers.
            // Adding new customers is done via the NewCustomerModule.
            // Updating existing customers is not implemented in Admin GUI yet.
            
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(btnViewAllCust);
            topPanel.add(btnDeleteCustomer);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

            btnViewAllCust.addActionListener(e -> {
                StringBuilder sb = new StringBuilder("All Customers:\n");
                if (allCustomersRef.isEmpty()) sb.append("No customers found.\n");
                else for(OldCustomer cust : allCustomersRef) {
                    sb.append(String.format("Name: %s, NID: %s, Meter: %s, Region: %s, Balance: %.2f, Cancelled: %s\n", 
                                          cust.getName(), cust.getNid(), cust.getMeterCode(), cust.getRegion(), cust.getBalanceDue(), cust.isStopAndCancel() ? "Yes" : "No"));
                }
                displayArea.setText(sb.toString());
            });

            btnDeleteCustomer.addActionListener(e -> {
                String meterCode = JOptionPane.showInputDialog(this, "Enter Meter Code of customer to delete:");
                if(meterCode != null && !meterCode.trim().isEmpty()) {
                    // Corrected method call to match Admin.java
                    String result = currentAdmin.deleteCustomerByMeterCode(allCustomersRef, meterCode.trim());
                    JOptionPane.showMessageDialog(this, result, "Delete Customer Status", result.startsWith("✅") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                    if(result.startsWith("✅")) {
                        FileSystem.saveCustomerData(allCustomersRef, CUSTOMERS_FILE);
                        btnViewAllCust.doClick(); 
                    }
                }
            });
            return panel;
        }

        private JPanel createManageOperatorsPanel() {
            JPanel panel = new JPanel(new BorderLayout(5,5));
            JTextArea displayArea = new JTextArea(10, 50); displayArea.setEditable(false);
            JButton btnViewAllOps = new JButton("View All Operators");
            JButton btnAddOperator = new JButton("Add New Operator");
            JButton btnDeleteOperator = new JButton("Delete Operator by Name");
            // Update Operator button could be added here for full CRUD

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(btnViewAllOps);
            topPanel.add(btnAddOperator);
            topPanel.add(btnDeleteOperator);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

            btnViewAllOps.addActionListener(e -> {
                StringBuilder sb = new StringBuilder("All Operators:\n");
                if (allOperatorsRef.isEmpty()) sb.append("No operators found.\n");
                else for(Operator op : allOperatorsRef) {
                    sb.append(String.format("Name: %s, Total Collected: %.2f\n", op.getOperatorName(), op.getTotalCollected()));
                }
                displayArea.setText(sb.toString());
            });

            btnAddOperator.addActionListener(e -> {
                String opName = JOptionPane.showInputDialog(this, "Enter new operator's name:");
                if(opName != null && !opName.trim().isEmpty()) {
                    // Corrected method call to match Admin.java
                    String result = currentAdmin.addNewOperator(allOperatorsRef, opName.trim());
                    JOptionPane.showMessageDialog(this, result, "Add Operator Status", result.startsWith("✅") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                    if(result.startsWith("✅")) {
                        FileSystem.saveOperatorData(allOperatorsRef, OPERATORS_FILE);
                        btnViewAllOps.doClick(); 
                    } 
                }
            });
            
            btnDeleteOperator.addActionListener(e -> {
                String opName = JOptionPane.showInputDialog(this, "Enter name of operator to delete:");
                if(opName != null && !opName.trim().isEmpty()) {
                    // Corrected method call to match Admin.java
                    String result = currentAdmin.deleteOperatorByName(allOperatorsRef, opName.trim());
                     JOptionPane.showMessageDialog(this, result, "Delete Operator Status", result.startsWith("✅") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                    if(result.startsWith("✅")) {
                        FileSystem.saveOperatorData(allOperatorsRef, OPERATORS_FILE);
                        btnViewAllOps.doClick(); 
                    }
                }
            });
            return panel;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ElectricityBillingSystemGUI gui = new ElectricityBillingSystemGUI();
            gui.setVisible(true);
        });
    }
}

