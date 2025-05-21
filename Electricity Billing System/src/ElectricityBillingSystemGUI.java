import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    // Define a color scheme
    private static final Color PRIMARY_COLOR = new Color(60, 70, 100); // Dark blue-gray
    private static final Color SECONDARY_COLOR = new Color(230, 230, 250); // Lavender blush (light background)
    private static final Color ACCENT_COLOR = new Color(100, 149, 237); // Cornflower blue (for buttons, highlights)
    private static final Color TEXT_COLOR_DARK = Color.WHITE;
    private static final Color TEXT_COLOR_LIGHT = new Color(50, 50, 50);
    private static final Font GLOBAL_FONT_LABEL = new Font("Arial", Font.PLAIN, 16);
    private static final Font GLOBAL_FONT_INPUT = new Font("Arial", Font.PLAIN, 15);
    private static final Font GLOBAL_FONT_BUTTON = new Font("Arial", Font.BOLD, 16);
    private static final Font GLOBAL_FONT_TITLE = new Font("Serif", Font.BOLD, 30);
    private static final Font GLOBAL_FONT_DIALOG_TITLE = new Font("Arial", Font.BOLD, 20);


    public ElectricityBillingSystemGUI() {
        setTitle("⚡ Electricity Billing System ⚡");
        setSize(700, 550); // Increased main window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(SECONDARY_COLOR);
        setLayout(new BorderLayout(15, 15)); // Increased gaps

        FileSystem.loadCustomersFromFile(customers, CUSTOMERS_FILE);
        FileSystem.loadOperatorsFromFile(operators, OPERATORS_FILE);
        if (admins.isEmpty()) {
            admins.add(new Admin("admin1")); // Default admin
        }

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 15, 15)); // Increased gaps
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Increased padding
        mainPanel.setBackground(SECONDARY_COLOR);

        JLabel titleLabel = new JLabel("⚡ Electricity Billing System ⚡", SwingConstants.CENTER);
        titleLabel.setFont(GLOBAL_FONT_TITLE);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        add(titleLabel, BorderLayout.NORTH);

        JButton btnNewCustomer = new JButton("➕ New Customer");
        JButton btnOldCustomer = new JButton("👤 Existing Customer");
        JButton btnOperator = new JButton("⚙️ Operator Panel");
        JButton btnAdmin = new JButton("👑 Admin Panel");
        JButton btnExit = new JButton("🚪 Exit System");

        styleMainButton(btnNewCustomer);
        styleMainButton(btnOldCustomer);
        styleMainButton(btnOperator);
        styleMainButton(btnAdmin);
        styleMainButton(btnExit);

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

    private void styleMainButton(JButton button) {
        button.setFont(GLOBAL_FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(TEXT_COLOR_DARK);
        button.setPreferredSize(new Dimension(200, 50)); // Set preferred size for main buttons
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
    }
    
    private void styleDialogButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    private void openNewCustomerDialog() {
        NewCustomerDialog dialog = new NewCustomerDialog(this, customers, CUSTOMERS_FILE);
        dialog.setVisible(true);
    }

    private void openOldCustomerDialog() {
        // Use a custom dialog for login to style it better
        JTextField meterCodeField = new JTextField(15);
        meterCodeField.setFont(GLOBAL_FONT_INPUT);
        JPanel panel = new JPanel(new GridLayout(0,1,5,5));
        panel.add(new JLabel("Enter your meter code to login:"));
        panel.add(meterCodeField);

        int result = JOptionPane.showConfirmDialog(this, panel, "👤 Existing Customer Login", 
                                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String meterCode = meterCodeField.getText();
            if (meterCode != null && !meterCode.trim().isEmpty()) {
                OldCustomer customer = OldCustomer.findCustomerByMeterCode(customers, meterCode.trim());
                if (customer != null) {
                    OldCustomerDialog dialog = new OldCustomerDialog(this, customer, customers, CUSTOMERS_FILE);
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Customer with meter code \"" + meterCode.trim() + "\" not found.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Meter code cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openOperatorDialog() {
        JTextField operatorNameField = new JTextField(15);
        operatorNameField.setFont(GLOBAL_FONT_INPUT);
        JPanel panel = new JPanel(new GridLayout(0,1,5,5));
        panel.add(new JLabel("Enter your operator name to login:"));
        panel.add(operatorNameField);

        int result = JOptionPane.showConfirmDialog(this, panel, "⚙️ Operator Login", 
                                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String operatorName = operatorNameField.getText();
            if (operatorName != null && !operatorName.trim().isEmpty()) {
                Operator operator = Operator.findOperatorByName(operators, operatorName.trim());
                if (operator != null) {
                    OperatorDialog dialog = new OperatorDialog(this, operator, customers, operators, CUSTOMERS_FILE, OPERATORS_FILE);
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Operator \"" + operatorName.trim() + "\" not found.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Operator name cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openAdminDialog() {
        JTextField adminNameField = new JTextField(15);
        adminNameField.setFont(GLOBAL_FONT_INPUT);
        JPanel panel = new JPanel(new GridLayout(0,1,5,5));
        panel.add(new JLabel("Enter your admin name to login:"));
        panel.add(adminNameField);

        int result = JOptionPane.showConfirmDialog(this, panel, "👑 Admin Login", 
                                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String adminName = adminNameField.getText();
            if (adminName != null && !adminName.trim().isEmpty()) {
                Admin admin = Admin.findAdminByName(admins, adminName.trim());
                if (admin != null) {
                    AdminDialog dialog = new AdminDialog(this, admin, customers, operators, CUSTOMERS_FILE, OPERATORS_FILE);
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Admin \"" + adminName.trim() + "\" not found.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Admin name cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Helper to set font for JOptionPanes if needed, though it's tricky globally
    private void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    // --- Inner class for New Customer Registration Dialog ---
    class NewCustomerDialog extends JDialog { 
        private JTextField txtName, txtNid, txtAddress, txtEmail, txtRegion, txtPhoneNumber, txtContractPath;
        private JButton btnBrowseContract;
        private List<OldCustomer> customersListRef;

        public NewCustomerDialog(Frame owner, List<OldCustomer> customersList, String customersFile) {
            super(owner, "➕ New Customer Registration", true);
            this.customersListRef = customersList;
            setSize(650, 550); // Increased size
            setLocationRelativeTo(owner);
            getContentPane().setBackground(SECONDARY_COLOR);
            setLayout(new BorderLayout(10, 10));
            
            JLabel dialogTitleLabel = new JLabel("New Customer Registration Form", SwingConstants.CENTER);
            dialogTitleLabel.setFont(GLOBAL_FONT_DIALOG_TITLE);
            dialogTitleLabel.setForeground(PRIMARY_COLOR);
            dialogTitleLabel.setBorder(new EmptyBorder(10,0,10,0));
            add(dialogTitleLabel, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(SECONDARY_COLOR);
            formPanel.setBorder(new EmptyBorder(10,20,10,20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8); // Increased insets
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;
            // int y = 0; // Original y, not used with yPosition array approach

            // Declare yPosition array BEFORE the lambda that uses it
            final int[] yPosition = {0}; // Using array to be modifiable in lambda

            // Helper for creating styled labels and text fields
            BiConsumer<String, JTextField> addFormField = (labelText, textField) -> {
                JLabel label = new JLabel(labelText);
                label.setFont(GLOBAL_FONT_LABEL);
                label.setForeground(TEXT_COLOR_LIGHT);
                textField.setFont(GLOBAL_FONT_INPUT);
                textField.setColumns(25); // Increased columns
                gbc.gridx = 0; gbc.gridy = yPosition[0]; gbc.gridwidth = 1; formPanel.add(label, gbc);
                gbc.gridx = 1; gbc.gridy = yPosition[0]; gbc.gridwidth = 2; formPanel.add(textField, gbc);
                yPosition[0]++;
            };
            // final int[] yPosition = {0}; // MOVED: This was the original problematic location

            txtName = new JTextField(); addFormField.accept("Full Name:", txtName);
            txtNid = new JTextField(); addFormField.accept("National ID (14 digits):", txtNid);
            txtAddress = new JTextField(); addFormField.accept("Full Address:", txtAddress);
            txtEmail = new JTextField(); addFormField.accept("Email (user@example.com):", txtEmail);
            txtRegion = new JTextField(); addFormField.accept("Region/City:", txtRegion);
            txtPhoneNumber = new JTextField(); addFormField.accept("Phone Number (11 digits):", txtPhoneNumber);

            JLabel lblContract = new JLabel("Contract Copy Path:"); lblContract.setFont(GLOBAL_FONT_LABEL); lblContract.setForeground(TEXT_COLOR_LIGHT);
            gbc.gridx = 0; gbc.gridy = yPosition[0]; gbc.gridwidth = 1; formPanel.add(lblContract, gbc);
            txtContractPath = new JTextField(20); txtContractPath.setFont(GLOBAL_FONT_INPUT); txtContractPath.setEditable(false);
            gbc.gridx = 1; gbc.gridy = yPosition[0]; gbc.gridwidth = 1; formPanel.add(txtContractPath, gbc);
            btnBrowseContract = new JButton("📁 Browse"); styleDialogButton(btnBrowseContract);
            gbc.gridx = 2; gbc.gridy = yPosition[0]++; gbc.gridwidth = 1; formPanel.add(btnBrowseContract, gbc);
            
            add(formPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15)); // Increased gaps
            buttonPanel.setBackground(SECONDARY_COLOR);
            JButton btnRegister = new JButton("✔️ Register Customer"); styleDialogButton(btnRegister);
            JButton btnClear = new JButton("✨ Clear Fields"); styleDialogButton(btnClear);
            JButton btnCancel = new JButton("❌ Cancel"); styleDialogButton(btnCancel);
            buttonPanel.add(btnRegister); buttonPanel.add(btnClear); buttonPanel.add(btnCancel);
            add(buttonPanel, BorderLayout.SOUTH);

            btnBrowseContract.addActionListener(e -> browseContractFile());
            btnRegister.addActionListener(e -> registerNewCustomer());
            btnClear.addActionListener(e -> clearFormFields());
            btnCancel.addActionListener(e -> dispose());
        }

        private void browseContractFile() { 
            JFileChooser fc = new JFileChooser(); 
            fc.setDialogTitle("Select Contract File");
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
                txtContractPath.setText(fc.getSelectedFile().getAbsolutePath()); 
            }
        }
        private void clearFormFields() { txtName.setText(""); txtNid.setText(""); txtAddress.setText(""); txtEmail.setText(""); txtRegion.setText(""); txtPhoneNumber.setText(""); txtContractPath.setText(""); }
        
        private void registerNewCustomer() {
            String name = txtName.getText().trim(), nid = txtNid.getText().trim(), address = txtAddress.getText().trim(), email = txtEmail.getText().trim(), region = txtRegion.getText().trim(), phoneStr = txtPhoneNumber.getText().trim(), contractPath = txtContractPath.getText().trim();
            if (name.isEmpty() || nid.isEmpty() || address.isEmpty() || email.isEmpty() || region.isEmpty() || phoneStr.isEmpty() || contractPath.isEmpty()) { JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!nid.matches("\\d{14}")) { JOptionPane.showMessageDialog(this, "NID must be 14 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) { JOptionPane.showMessageDialog(this, "Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
            if (!phoneStr.matches("\\d{11}")) { JOptionPane.showMessageDialog(this, "Phone must be 11 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
            long phoneNumber; try { phoneNumber = Long.parseLong(phoneStr); } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid phone number format.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
            for (OldCustomer existing : customersListRef) if (existing.getNid().equals(nid)) { JOptionPane.showMessageDialog(this, "NID " + nid + " already exists.", "Registration Error", JOptionPane.ERROR_MESSAGE); return; }
            NewCustomer newCustObj = new NewCustomer(); newCustObj.setName(name); newCustObj.setNid(nid); newCustObj.setAddress(address); newCustObj.setEmail(email); newCustObj.setRegion(region); newCustObj.setNumber(phoneNumber); newCustObj.setContract(contractPath); newCustObj.generateMeterCode();
            for (OldCustomer existing : customersListRef) if (existing.getMeterCode().equals(newCustObj.getMeterCode())) { JOptionPane.showMessageDialog(this, "Generated Meter Code " + newCustObj.getMeterCode() + " already exists (possible NID suffix collision). Please try again or check NID.", "Registration Error", JOptionPane.ERROR_MESSAGE); return; }
            OldCustomer registered = newCustObj.createNewCustomer(customersListRef);
            if (registered != null) { JOptionPane.showMessageDialog(this, "Customer registered successfully!\nName: " + registered.getName() + "\nMeter Code: " + registered.getMeterCode(), "Success", JOptionPane.INFORMATION_MESSAGE); clearFormFields(); dispose(); }
            else { JOptionPane.showMessageDialog(this, "Failed to register customer (possibly marked as duplicate internally by logic).", "Registration Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    // --- Inner class for Old Customer Module Dialog ---
    class OldCustomerDialog extends JDialog { 
        private OldCustomer currentCustomer;
        private List<OldCustomer> customersListRef;
        private JLabel lblBalance, lblReadings, welcomeLabel;
        public OldCustomerDialog(Frame owner, OldCustomer customer, List<OldCustomer> allCustomers, String customersFile) {
            super(owner, "👤 Existing Customer Portal - " + customer.getName(), true);
            this.currentCustomer = customer; this.customersListRef = allCustomers;
            setSize(600, 450); // Increased size
            setLocationRelativeTo(owner);
            getContentPane().setBackground(SECONDARY_COLOR);
            setLayout(new BorderLayout(10, 10));

            JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 10)); // Increased vgap
            infoPanel.setBackground(SECONDARY_COLOR);
            infoPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
            welcomeLabel = new JLabel("Welcome, " + currentCustomer.getName() + " (Meter: " + currentCustomer.getMeterCode() + ")");
            welcomeLabel.setFont(GLOBAL_FONT_DIALOG_TITLE.deriveFont(Font.PLAIN, 18f)); welcomeLabel.setForeground(PRIMARY_COLOR);
            infoPanel.add(welcomeLabel);
            lblBalance = new JLabel("Current Balance Due: " + currentCustomer.getBalanceDue() + " EGP"); 
            lblBalance.setFont(GLOBAL_FONT_LABEL); lblBalance.setForeground(TEXT_COLOR_LIGHT);
            infoPanel.add(lblBalance);
            lblReadings = new JLabel("Last Reading: " + currentCustomer.getLastReading() + " | Current Reading: " + currentCustomer.getCurrentReading()); 
            lblReadings.setFont(GLOBAL_FONT_LABEL); lblReadings.setForeground(TEXT_COLOR_LIGHT);
            infoPanel.add(lblReadings);
            add(infoPanel, BorderLayout.NORTH);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(GLOBAL_FONT_LABEL.deriveFont(Font.BOLD));
            tabbedPane.setBackground(SECONDARY_COLOR);
            tabbedPane.setForeground(PRIMARY_COLOR);

            // Pay Bill Panel
            JPanel payBillPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); payBillPanel.setBackground(SECONDARY_COLOR);
            JLabel lblAmountPay = new JLabel("Amount to Pay (EGP):"); lblAmountPay.setFont(GLOBAL_FONT_LABEL); lblAmountPay.setForeground(TEXT_COLOR_LIGHT);
            JTextField txtPaymentAmount = new JTextField(12); txtPaymentAmount.setFont(GLOBAL_FONT_INPUT);
            JButton btnPay = new JButton("💳 Pay Bill"); styleDialogButton(btnPay);
            payBillPanel.add(lblAmountPay); payBillPanel.add(txtPaymentAmount); payBillPanel.add(btnPay);
            btnPay.addActionListener(e -> payCustomerBill(txtPaymentAmount.getText())); 
            tabbedPane.addTab("💵 Pay Bill", payBillPanel);

            // Enter Reading Panel
            JPanel readingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); readingPanel.setBackground(SECONDARY_COLOR);
            JLabel lblNewReading = new JLabel("New Meter Reading:"); lblNewReading.setFont(GLOBAL_FONT_LABEL); lblNewReading.setForeground(TEXT_COLOR_LIGHT);
            JTextField txtNewReading = new JTextField(12); txtNewReading.setFont(GLOBAL_FONT_INPUT);
            JButton btnSubmitReading = new JButton("Submit Reading"); styleDialogButton(btnSubmitReading);
            readingPanel.add(lblNewReading); readingPanel.add(txtNewReading); readingPanel.add(btnSubmitReading);
            btnSubmitReading.addActionListener(e -> enterNewReading(txtNewReading.getText())); 
            tabbedPane.addTab("📊 Enter Reading", readingPanel);

            // Complain Panel
            JPanel complainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); complainPanel.setBackground(SECONDARY_COLOR);
            JButton btnComplain = new JButton("🚩 Register a Complaint"); styleDialogButton(btnComplain);
            if (currentCustomer.isComplain()) { btnComplain.setText("✔️ Complaint Already Registered"); btnComplain.setEnabled(false); btnComplain.setBackground(Color.LIGHT_GRAY);}
            btnComplain.addActionListener(e -> registerComplaint(btnComplain)); 
            complainPanel.add(btnComplain); 
            tabbedPane.addTab("🚩 Complain", complainPanel);
            
            add(tabbedPane, BorderLayout.CENTER);

            JButton btnClose = new JButton("❌ Close Module"); styleDialogButton(btnClose);
            JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); southPanel.setBackground(SECONDARY_COLOR);
            southPanel.add(btnClose); add(southPanel, BorderLayout.SOUTH);
            btnClose.addActionListener(e -> dispose());
        }
        private void updateDisplay() { 
            lblBalance.setText("Current Balance Due: " + currentCustomer.getBalanceDue() + " EGP"); 
            lblReadings.setText("Last Reading: " + currentCustomer.getLastReading() + " | Current Reading: " + currentCustomer.getCurrentReading()); 
        }
        private void payCustomerBill(String amountStr) {
            if (amountStr.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter a payment amount.", "Input Error", JOptionPane.ERROR_MESSAGE); return; }
            try { int amount = Integer.parseInt(amountStr); if (amount <= 0) { JOptionPane.showMessageDialog(this, "Payment amount must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE); return; }
                String result = currentCustomer.payBill(currentCustomer.getMeterCode(), amount);
                JOptionPane.showMessageDialog(this, result, "Payment Status", result.startsWith("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                if (!result.startsWith("Error")) { FileSystem.saveCustomerData(customersListRef, CUSTOMERS_FILE); updateDisplay(); }
            } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid amount entered. Please use numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE); }
        }
        private void enterNewReading(String readingStr) {
            if (readingStr.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter the new reading.", "Input Error", JOptionPane.ERROR_MESSAGE); return; }
            try { int reading = Integer.parseInt(readingStr);
                String result = currentCustomer.enterMonthlyReading(reading);
                JOptionPane.showMessageDialog(this, result, "Reading Update Status", result.startsWith("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                if (!result.startsWith("Error")) { FileSystem.saveCustomerData(customersListRef, CUSTOMERS_FILE); updateDisplay(); }
            } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Invalid reading entered. Please use numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE); }
        }
        private void registerComplaint(JButton complainButton) { 
            currentCustomer.setComplain(true); 
            FileSystem.saveCustomerData(customersListRef, CUSTOMERS_FILE); 
            JOptionPane.showMessageDialog(this, "Complaint registered successfully for meter " + currentCustomer.getMeterCode(), "Complaint Registered", JOptionPane.INFORMATION_MESSAGE); 
            complainButton.setText("✔️ Complaint Already Registered"); 
            complainButton.setEnabled(false); 
            complainButton.setBackground(Color.LIGHT_GRAY);
            updateDisplay(); 
        }
    }

    // --- Inner class for Operator Module Dialog (Reconciled with provided Operator.java) ---
    class OperatorDialog extends JDialog {
        private Operator currentOperator;
        private List<OldCustomer> allCustomersRef;
        private List<Operator> allOperatorsRef;
        private JLabel lblTotalCollected, operatorWelcomeLabel;

        public OperatorDialog(Frame owner, Operator operator, List<OldCustomer> customers, List<Operator> operatorsList, String custFile, String opFile) {
            super(owner, "⚙️ Operator Panel - " + operator.getOperatorName(), true);
            this.currentOperator = operator;
            this.allCustomersRef = customers;
            this.allOperatorsRef = operatorsList;

            setSize(900, 750); // Increased size
            setLocationRelativeTo(owner);
            getContentPane().setBackground(SECONDARY_COLOR);
            setLayout(new BorderLayout(10, 10));

            JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            infoPanel.setBackground(SECONDARY_COLOR);
            infoPanel.setBorder(BorderFactory.createEmptyBorder(10,15,5,15));
            operatorWelcomeLabel = new JLabel("Operator: " + currentOperator.getOperatorName());
            operatorWelcomeLabel.setFont(GLOBAL_FONT_DIALOG_TITLE.deriveFont(Font.PLAIN, 18f)); operatorWelcomeLabel.setForeground(PRIMARY_COLOR);
            infoPanel.add(operatorWelcomeLabel);
            lblTotalCollected = new JLabel(" | Total Collected: " + currentOperator.getTotalCollected() + " EGP");
            lblTotalCollected.setFont(GLOBAL_FONT_LABEL); lblTotalCollected.setForeground(TEXT_COLOR_LIGHT);
            infoPanel.add(lblTotalCollected);
            add(infoPanel, BorderLayout.NORTH);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(GLOBAL_FONT_LABEL.deriveFont(Font.BOLD));
            tabbedPane.setBackground(SECONDARY_COLOR);
            tabbedPane.setForeground(PRIMARY_COLOR);

            tabbedPane.addTab("💳 Collect Payment", createCollectPaymentPanel());
            tabbedPane.addTab("📄 Print Bill Details", createPrintBillDetailsPanel());
            tabbedPane.addTab("🗺️ View Bills/Region", createViewBillsByRegionPanel());
            tabbedPane.addTab("📊 Enter/Update Reading", createEnterUpdateReadingPanel());
            tabbedPane.addTab("💲 Define Bill (Tariff)", createDefineBillPanel());
            tabbedPane.addTab("🚫 Stop Meter/Cancel", createStopMeterPanel());
            add(tabbedPane, BorderLayout.CENTER);

            JButton btnClose = new JButton("❌ Close Module"); styleDialogButton(btnClose);
            JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10,10)); southPanel.setBackground(SECONDARY_COLOR);
            southPanel.add(btnClose);
            add(southPanel, BorderLayout.SOUTH);
            btnClose.addActionListener(e -> dispose());
        }

        private void updateTotalCollectedDisplay() {
            lblTotalCollected.setText(" | Total Collected: " + currentOperator.getTotalCollected() + " EGP");
        }
        
        private JPanel createStyledPanel() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(SECONDARY_COLOR);
            panel.setBorder(new EmptyBorder(15,15,15,15));
            return panel;
        }

        private GridBagConstraints createGbc(int x, int y, int width, int anchor) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = x; gbc.gridy = y; gbc.gridwidth = width;
            gbc.insets = new Insets(8,8,8,8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = anchor;
            return gbc;
        }

        private JPanel createCollectPaymentPanel() {
            JPanel panel = createStyledPanel(); GridBagConstraints gbc;
            JTextField txtMeterCode = new JTextField(20); txtMeterCode.setFont(GLOBAL_FONT_INPUT);
            JTextField txtAmount = new JTextField(15); txtAmount.setFont(GLOBAL_FONT_INPUT);
            JButton btnCollect = new JButton("✔️ Collect Payment"); styleDialogButton(btnCollect);
            
            JLabel lblMeter = new JLabel("Customer Meter Code:"); lblMeter.setFont(GLOBAL_FONT_LABEL); lblMeter.setForeground(TEXT_COLOR_LIGHT);
            gbc = createGbc(0,0,1, GridBagConstraints.WEST); panel.add(lblMeter, gbc);
            gbc = createGbc(1,0,1, GridBagConstraints.WEST); panel.add(txtMeterCode, gbc);
            
            JLabel lblAmount = new JLabel("Amount Paid (EGP):"); lblAmount.setFont(GLOBAL_FONT_LABEL); lblAmount.setForeground(TEXT_COLOR_LIGHT);
            gbc = createGbc(0,1,1, GridBagConstraints.WEST); panel.add(lblAmount, gbc);
            gbc = createGbc(1,1,1, GridBagConstraints.WEST); panel.add(txtAmount, gbc);
            
            gbc = createGbc(0,2,2, GridBagConstraints.CENTER); panel.add(btnCollect, gbc);

            btnCollect.addActionListener(e -> {
                String meter = txtMeterCode.getText().trim();
                String amountStr = txtAmount.getText().trim();
                if(meter.isEmpty() || amountStr.isEmpty()) { JOptionPane.showMessageDialog(this, "Meter code and amount are required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                OldCustomer cust = OldCustomer.findCustomerByMeterCode(allCustomersRef, meter);
                if(cust == null) { JOptionPane.showMessageDialog(this, "Customer with meter code \"" + meter + "\" not found.", "Error", JOptionPane.ERROR_MESSAGE); return; }
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
                } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid amount. Please use numbers only.", "Error", JOptionPane.ERROR_MESSAGE); }
            });
            return panel;
        }

        private JPanel createPrintBillDetailsPanel() {
            JPanel panel = createStyledPanel(); GridBagConstraints gbc;
            JTextField txtMeterCode = new JTextField(20); txtMeterCode.setFont(GLOBAL_FONT_INPUT);
            JButton btnPrint = new JButton("📄 Get Bill Details"); styleDialogButton(btnPrint);
            JTextArea billArea = new JTextArea(10, 45); billArea.setFont(GLOBAL_FONT_INPUT); billArea.setEditable(false);
            billArea.setLineWrap(true); billArea.setWrapStyleWord(true);

            JLabel lblMeter = new JLabel("Customer Meter Code:"); lblMeter.setFont(GLOBAL_FONT_LABEL); lblMeter.setForeground(TEXT_COLOR_LIGHT);
            gbc = createGbc(0,0,1, GridBagConstraints.WEST); panel.add(lblMeter, gbc);
            gbc = createGbc(1,0,1, GridBagConstraints.WEST); panel.add(txtMeterCode, gbc);
            gbc = createGbc(2,0,1, GridBagConstraints.WEST); panel.add(btnPrint, gbc);
            gbc = createGbc(0,1,3, GridBagConstraints.CENTER); gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
            panel.add(new JScrollPane(billArea), gbc);

            btnPrint.addActionListener(e -> {
                String meter = txtMeterCode.getText().trim();
                if(meter.isEmpty()) { JOptionPane.showMessageDialog(this, "Meter code is required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                OldCustomer cust = OldCustomer.findCustomerByMeterCode(allCustomersRef, meter);
                if(cust == null) { JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE); billArea.setText(""); return; }
                String billDetails = currentOperator.getBillDetails(cust);
                billArea.setText(billDetails);
            });
            return panel;
        }

        private JPanel createViewBillsByRegionPanel() {
            JPanel panel = createStyledPanel(); GridBagConstraints gbc;
            String[] regionsArray = allCustomersRef.stream().map(OldCustomer::getRegion).filter(r -> r != null && !r.isEmpty()).distinct().sorted().toArray(String[]::new);
            if (regionsArray.length == 0) regionsArray = new String[]{"N/A"};
            JComboBox<String> regionComboBox = new JComboBox<>(regionsArray); regionComboBox.setFont(GLOBAL_FONT_INPUT);
            JTextArea billsArea = new JTextArea(12, 45); billsArea.setFont(GLOBAL_FONT_INPUT); billsArea.setEditable(false);
            billsArea.setLineWrap(true); billsArea.setWrapStyleWord(true);
            JButton btnView = new JButton("🗺️ View Bills"); styleDialogButton(btnView);

            JLabel lblRegion = new JLabel("Select Region:"); lblRegion.setFont(GLOBAL_FONT_LABEL); lblRegion.setForeground(TEXT_COLOR_LIGHT);
            gbc = createGbc(0,0,1, GridBagConstraints.WEST); panel.add(lblRegion, gbc);
            gbc = createGbc(1,0,1, GridBagConstraints.WEST); panel.add(regionComboBox, gbc);
            gbc = createGbc(2,0,1, GridBagConstraints.WEST); panel.add(btnView, gbc);
            gbc = createGbc(0,1,3, GridBagConstraints.CENTER); gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
            panel.add(new JScrollPane(billsArea), gbc);

            btnView.addActionListener(e -> {
                String selectedRegion = (String) regionComboBox.getSelectedItem();
                if(selectedRegion == null || selectedRegion.equals("N/A")) { billsArea.setText("Please select a valid region or add customers with regions."); return; }
                String result = currentOperator.getBillsByRegion(allCustomersRef, selectedRegion);
                billsArea.setText(result);
            });
            return panel;
        }

        private JPanel createEnterUpdateReadingPanel() {
            JPanel panel = createStyledPanel(); GridBagConstraints gbc;
            JTextField txtMeterCode = new JTextField(20); txtMeterCode.setFont(GLOBAL_FONT_INPUT);
            JTextField txtReading = new JTextField(15); txtReading.setFont(GLOBAL_FONT_INPUT);
            JButton btnSubmitReading = new JButton("✔️ Submit New Reading"); styleDialogButton(btnSubmitReading);

            JLabel lblMeter = new JLabel("Customer Meter Code:"); lblMeter.setFont(GLOBAL_FONT_LABEL); lblMeter.setForeground(TEXT_COLOR_LIGHT);
            gbc = createGbc(0,0,1, GridBagConstraints.WEST); panel.add(lblMeter, gbc);
            gbc = createGbc(1,0,1, GridBagConstraints.WEST); panel.add(txtMeterCode, gbc);
            
            JLabel lblReading = new JLabel("New Reading:"); lblReading.setFont(GLOBAL_FONT_LABEL); lblReading.setForeground(TEXT_COLOR_LIGHT);
            gbc = createGbc(0,1,1, GridBagConstraints.WEST); panel.add(lblReading, gbc);
            gbc = createGbc(1,1,1, GridBagConstraints.WEST); panel.add(txtReading, gbc);
            
            gbc = createGbc(0,2,2, GridBagConstraints.CENTER); panel.add(btnSubmitReading, gbc);

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
                } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid reading format. Please use numbers only.", "Error", JOptionPane.ERROR_MESSAGE); }
            });
            return panel;
        }

        private JPanel createDefineBillPanel() {
            JPanel panel = createStyledPanel(); GridBagConstraints gbc;
            JTextField txtMeterCode = new JTextField(20); txtMeterCode.setFont(GLOBAL_FONT_INPUT);
            JTextField txtPricePerUnit = new JTextField(15); txtPricePerUnit.setFont(GLOBAL_FONT_INPUT);
            JButton btnDefineBill = new JButton("💲 Define Bill for Customer"); styleDialogButton(btnDefineBill);

            JLabel lblMeter = new JLabel("Customer Meter Code:"); lblMeter.setFont(GLOBAL_FONT_LABEL); lblMeter.setForeground(TEXT_COLOR_LIGHT);
            gbc = createGbc(0,0,1, GridBagConstraints.WEST); panel.add(lblMeter, gbc);
            gbc = createGbc(1,0,1, GridBagConstraints.WEST); panel.add(txtMeterCode, gbc);
            
            JLabel lblPrice = new JLabel("Price Per Unit (EGP):"); lblPrice.setFont(GLOBAL_FONT_LABEL); lblPrice.setForeground(TEXT_COLOR_LIGHT);
            gbc = createGbc(0,1,1, GridBagConstraints.WEST); panel.add(lblPrice, gbc);
            gbc = createGbc(1,1,1, GridBagConstraints.WEST); panel.add(txtPricePerUnit, gbc);
            
            gbc = createGbc(0,2,2, GridBagConstraints.CENTER); panel.add(btnDefineBill, gbc);

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
                } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid price per unit. Please use numbers only.", "Error", JOptionPane.ERROR_MESSAGE); }
            });
            return panel;
        }

        private JPanel createStopMeterPanel() {
            JPanel panel = createStyledPanel(); GridBagConstraints gbc;
            JTextField txtMeterCode = new JTextField(20); txtMeterCode.setFont(GLOBAL_FONT_INPUT);
            JButton btnStop = new JButton("🚫 Stop Meter & Cancel Subscription"); styleDialogButton(btnStop);

            JLabel lblMeter = new JLabel("Customer Meter Code:"); lblMeter.setFont(GLOBAL_FONT_LABEL); lblMeter.setForeground(TEXT_COLOR_LIGHT);
            gbc = createGbc(0,0,1, GridBagConstraints.WEST); panel.add(lblMeter, gbc);
            gbc = createGbc(1,0,1, GridBagConstraints.WEST); panel.add(txtMeterCode, gbc);
            gbc = createGbc(0,1,2, GridBagConstraints.CENTER); panel.add(btnStop, gbc);

            btnStop.addActionListener(e -> {
                String meter = txtMeterCode.getText().trim();
                if(meter.isEmpty()) { JOptionPane.showMessageDialog(this, "Meter code is required.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                OldCustomer cust = OldCustomer.findCustomerByMeterCode(allCustomersRef, meter);
                if(cust == null) { JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE); return; }
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to stop meter " + meter + " for " + cust.getName() + " and cancel their subscription?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
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
        private JLabel adminWelcomeLabel;
        private JButton btnViewAllOps;
        private JButton btnViewAllCust;

        public AdminDialog(Frame owner, Admin admin, List<OldCustomer> customersList, List<Operator> operatorsList, String custFile, String opFile) {
            super(owner, "👑 Admin Panel - " + admin.getAdminName(), true);
            this.currentAdmin = admin;
            this.allCustomersRef = customersList;
            this.allOperatorsRef = operatorsList;

            setSize(900, 700); // Increased size
            setLocationRelativeTo(owner);
            getContentPane().setBackground(SECONDARY_COLOR);
            setLayout(new BorderLayout(10, 10));

            adminWelcomeLabel = new JLabel("Administrator Panel: " + currentAdmin.getAdminName(), SwingConstants.CENTER);
            adminWelcomeLabel.setFont(GLOBAL_FONT_DIALOG_TITLE);
            adminWelcomeLabel.setForeground(PRIMARY_COLOR);
            adminWelcomeLabel.setBorder(new EmptyBorder(10,0,10,0));
            add(adminWelcomeLabel, BorderLayout.NORTH);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(GLOBAL_FONT_LABEL.deriveFont(Font.BOLD));
            tabbedPane.setBackground(SECONDARY_COLOR);
            tabbedPane.setForeground(PRIMARY_COLOR);

            tabbedPane.addTab("🗺️ Bills/Region", createViewBillsByRegionPanel());
            tabbedPane.addTab("💰 Total Collections", createViewTotalCollectionsPanel());
            tabbedPane.addTab("📈 Consumption Stats", createConsumptionStatsPanel());
            tabbedPane.addTab("👥 Manage Customers", createManageCustomersPanel());
            tabbedPane.addTab("🛠️ Manage Operators", createManageOperatorsPanel());

            add(tabbedPane, BorderLayout.CENTER);

            JButton btnClose = new JButton("❌ Close Module"); styleDialogButton(btnClose);
            JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10)); southPanel.setBackground(SECONDARY_COLOR);
            southPanel.add(btnClose);
            add(southPanel, BorderLayout.SOUTH);
            btnClose.addActionListener(e -> dispose());
        }
        
        private JPanel createStyledAdminPanel() {
            JPanel panel = new JPanel(new BorderLayout(10,10));
            panel.setBackground(SECONDARY_COLOR);
            panel.setBorder(new EmptyBorder(15,15,15,15));
            return panel;
        }

        private JPanel createViewBillsByRegionPanel() {
            JPanel panel = createStyledAdminPanel();
            String[] regionsArray = allCustomersRef.stream().map(OldCustomer::getRegion).filter(r -> r != null && !r.isEmpty()).distinct().sorted().toArray(String[]::new);
            if (regionsArray.length == 0) regionsArray = new String[]{"N/A"};
            JComboBox<String> regionComboBox = new JComboBox<>(regionsArray); regionComboBox.setFont(GLOBAL_FONT_INPUT);
            JTextArea displayArea = new JTextArea(18, 60); displayArea.setFont(GLOBAL_FONT_INPUT); displayArea.setEditable(false);
            displayArea.setLineWrap(true); displayArea.setWrapStyleWord(true);
            JButton btnView = new JButton("🗺️ View Bills for Region"); styleDialogButton(btnView);
            
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5)); topPanel.setBackground(SECONDARY_COLOR);
            JLabel lblRegion = new JLabel("Select Region:"); lblRegion.setFont(GLOBAL_FONT_LABEL); lblRegion.setForeground(TEXT_COLOR_LIGHT);
            topPanel.add(lblRegion); topPanel.add(regionComboBox); topPanel.add(btnView);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

            btnView.addActionListener(e -> {
                String selectedRegion = (String) regionComboBox.getSelectedItem();
                if(selectedRegion == null || selectedRegion.equals("N/A")) { displayArea.setText("Please select a valid region or add customers with regions."); return; }
                String result = currentAdmin.viewAllBillsByRegion(allCustomersRef, selectedRegion);
                displayArea.setText(result);
            });
            return panel;
        }

        private JPanel createViewTotalCollectionsPanel() {
            JPanel panel = createStyledAdminPanel();
            JTextArea displayArea = new JTextArea(18, 60); displayArea.setFont(GLOBAL_FONT_INPUT); displayArea.setEditable(false);
            displayArea.setLineWrap(true); displayArea.setWrapStyleWord(true);
            JButton btnView = new JButton("💰 View Total Collections by All Operators"); styleDialogButton(btnView);
            
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); topPanel.setBackground(SECONDARY_COLOR);
            topPanel.add(btnView);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

            btnView.addActionListener(e -> {
                String result = currentAdmin.viewTotalCollected(allOperatorsRef);
                displayArea.setText(result);
            });
            return panel;
        }

        private JPanel createConsumptionStatsPanel() {
            JPanel panel = createStyledAdminPanel();
            String[] regionsArray = allCustomersRef.stream().map(OldCustomer::getRegion).filter(r -> r != null && !r.isEmpty()).distinct().sorted().toArray(String[]::new);
            if (regionsArray.length == 0) regionsArray = new String[]{"N/A"};
            JComboBox<String> regionComboBox = new JComboBox<>(regionsArray); regionComboBox.setFont(GLOBAL_FONT_INPUT);
            JTextArea displayArea = new JTextArea(18, 60); displayArea.setFont(GLOBAL_FONT_INPUT); displayArea.setEditable(false);
            displayArea.setLineWrap(true); displayArea.setWrapStyleWord(true);
            JButton btnView = new JButton("📈 View Consumption Statistics for Region"); styleDialogButton(btnView);

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5)); topPanel.setBackground(SECONDARY_COLOR);
            JLabel lblRegion = new JLabel("Select Region:"); lblRegion.setFont(GLOBAL_FONT_LABEL); lblRegion.setForeground(TEXT_COLOR_LIGHT);
            topPanel.add(lblRegion); topPanel.add(regionComboBox); topPanel.add(btnView);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

            btnView.addActionListener(e -> {
                String selectedRegion = (String) regionComboBox.getSelectedItem();
                if(selectedRegion == null || selectedRegion.equals("N/A")) { displayArea.setText("Please select a valid region or add customers with regions."); return; }
                String result = currentAdmin.makeConsumptionStatistics(allCustomersRef, selectedRegion);
                displayArea.setText(result);
            });
            return panel;
        }

        private JPanel createManageCustomersPanel() {
            JPanel panel = createStyledAdminPanel();
            JTextArea displayArea = new JTextArea(15, 60); displayArea.setFont(GLOBAL_FONT_INPUT); displayArea.setEditable(false);
            displayArea.setLineWrap(true); displayArea.setWrapStyleWord(true);
            JButton btnViewAllCust = new JButton("📋 View All Customers"); styleDialogButton(btnViewAllCust);
            JButton btnAddCustomer = new JButton("➕ Add New Customer"); styleDialogButton(btnAddCustomer);
            JButton btnUpdateCustomer = new JButton("✏️ Update Customer"); styleDialogButton(btnUpdateCustomer);
            JButton btnDeleteCustomer = new JButton("🗑️ Delete Customer"); styleDialogButton(btnDeleteCustomer);
            
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5)); topPanel.setBackground(SECONDARY_COLOR);
            topPanel.add(btnViewAllCust);topPanel.add(btnAddCustomer);topPanel.add(btnUpdateCustomer);topPanel.add(btnDeleteCustomer);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

            btnViewAllCust.addActionListener(e -> {
                StringBuilder sb = new StringBuilder("All Customers:\n");
                if (allCustomersRef.isEmpty()) sb.append("No customers found.\n");
                else for(OldCustomer cust : allCustomersRef) {
                    sb.append(String.format("Name: %s, NID: %s, Meter: %s, Region: %s, Balance: %d EGP, Cancelled: %s\n", 
                                          cust.getName(), cust.getNid(), cust.getMeterCode(), cust.getRegion(), cust.getBalanceDue(), cust.isStopAndCancel() ? "Yes" : "No"));
                }
                displayArea.setText(sb.toString());
            });

            btnAddCustomer.addActionListener(e -> showAddCustomerDialog(displayArea));
            btnUpdateCustomer.addActionListener(e -> showUpdateCustomerDialog(displayArea));
            
            btnDeleteCustomer.addActionListener(e -> {
                String meterCode = JOptionPane.showInputDialog(this, "Enter Meter Code of customer to delete:", "Delete Customer", JOptionPane.PLAIN_MESSAGE);
                if(meterCode != null && !meterCode.trim().isEmpty()) {
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
        private void showUpdateCustomerDialog(JTextArea displayArea) {
    // First, get the meter code of customer to update
    String meterCode = JOptionPane.showInputDialog(
        this, 
        "Enter Meter Code of customer to update:", 
        "Update Customer", 
        JOptionPane.PLAIN_MESSAGE
    );
    
    if (meterCode == null || meterCode.trim().isEmpty()) {
        return; // User cancelled or entered empty string
    }
    
    // Find the customer
    OldCustomer customerToUpdate = OldCustomer.findCustomerByMeterCode(allCustomersRef, meterCode.trim());
    if (customerToUpdate == null) {
        JOptionPane.showMessageDialog(
            this, 
            "Customer with meter code " + meterCode + " not found", 
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }
    
    // Create input fields with current values
    JTextField txtName = new JTextField(customerToUpdate.getName(), 20);
    JTextField txtAddress = new JTextField(customerToUpdate.getAddress(), 20);
    JTextField txtEmail = new JTextField(customerToUpdate.getEmail(), 20);
    JTextField txtRegion = new JTextField(customerToUpdate.getRegion(), 20);
    JTextField txtPhone = new JTextField(String.valueOf(customerToUpdate.getNumber()), 20);
    
    // Create panel with form layout
    JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
    panel.add(new JLabel("Full Name:"));
    panel.add(txtName);
    panel.add(new JLabel("Address:"));
    panel.add(txtAddress);
    panel.add(new JLabel("Email:"));
    panel.add(txtEmail);
    panel.add(new JLabel("Region:"));
    panel.add(txtRegion);
    panel.add(new JLabel("Phone Number:"));
    panel.add(txtPhone);
    
    int result = JOptionPane.showConfirmDialog(
        this, 
        panel, 
        "Update Customer: " + customerToUpdate.getMeterCode(), 
        JOptionPane.OK_CANCEL_OPTION, 
        JOptionPane.PLAIN_MESSAGE
    );
    
    if (result == JOptionPane.OK_OPTION) {
        try {
            // Get updated values
            String newName = txtName.getText().trim();
            String newAddress = txtAddress.getText().trim();
            String newEmail = txtEmail.getText().trim();
            String newRegion = txtRegion.getText().trim();
            long newPhone = Long.parseLong(txtPhone.getText().trim());
            
            // Call admin method to update customer
            String updateResult = currentAdmin.updateCustomer(
                customerToUpdate, 
                newName, 
                newAddress, 
                newEmail, 
                newRegion, 
                newPhone
            );
            
            // Show result
            JOptionPane.showMessageDialog(
                this, 
                updateResult, 
                "Update Customer Status", 
                updateResult.startsWith("✅") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
            );
            
            // If successful, save and refresh
            if (updateResult.startsWith("✅")) {
                FileSystem.saveCustomerData(allCustomersRef, CUSTOMERS_FILE);
                btnViewAllCust.doClick(); // Refresh the list
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, 
                "Please enter a valid phone number", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
        
        private void showAddCustomerDialog(JTextArea displayArea) {
    // Create input fields
    JTextField txtName = new JTextField(20);
    JTextField txtNid = new JTextField(20);
    JTextField txtAddress = new JTextField(20);
    JTextField txtEmail = new JTextField(20);
    JTextField txtRegion = new JTextField(20);
    JTextField txtPhone = new JTextField(20);
    
    // Create panel with form layout
    JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
    panel.add(new JLabel("Full Name:"));
    panel.add(txtName);
    panel.add(new JLabel("National ID (14 digits):"));
    panel.add(txtNid);
    panel.add(new JLabel("Address:"));
    panel.add(txtAddress);
    panel.add(new JLabel("Email:"));
    panel.add(txtEmail);
    panel.add(new JLabel("Region:"));
    panel.add(txtRegion);
    panel.add(new JLabel("Phone Number:"));
    panel.add(txtPhone);
    
    int result = JOptionPane.showConfirmDialog(
        this, 
        panel, 
        "Add New Customer", 
        JOptionPane.OK_CANCEL_OPTION, 
        JOptionPane.PLAIN_MESSAGE
    );
    
    if (result == JOptionPane.OK_OPTION) {
        try {
            // Create NewCustomer object from input
            NewCustomer newCustomer = new NewCustomer();
            newCustomer.setName(txtName.getText().trim());
            newCustomer.setNid(txtNid.getText().trim());
            newCustomer.setAddress(txtAddress.getText().trim());
            newCustomer.setEmail(txtEmail.getText().trim());
            newCustomer.setRegion(txtRegion.getText().trim());
            newCustomer.setNumber(Long.parseLong(txtPhone.getText().trim()));
            newCustomer.generateMeterCode(); // Generate meter code
            
            // Call admin method to add customer
            String addResult = currentAdmin.addNewCustomer(allCustomersRef, newCustomer);
            
            // Show result
            JOptionPane.showMessageDialog(
                this, 
                addResult, 
                "Add Customer Status", 
                addResult.startsWith("✅") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
            );
            
            // If successful, save and refresh
            if (addResult.startsWith("✅")) {
                FileSystem.saveCustomerData(allCustomersRef, CUSTOMERS_FILE);
                displayArea.setText(""); // Clear display
                // You might want to trigger a refresh of the customer list here
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, 
                "Please enter a valid phone number", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
        

        private JPanel createManageOperatorsPanel() {
            JPanel panel = createStyledAdminPanel();
            JTextArea displayArea = new JTextArea(12, 60); displayArea.setFont(GLOBAL_FONT_INPUT); displayArea.setEditable(false);
            displayArea.setLineWrap(true); displayArea.setWrapStyleWord(true);
            JButton btnViewAllOps = new JButton("📋 View All Operators"); styleDialogButton(btnViewAllOps);
            JButton btnAddOperator = new JButton("➕ Add New Operator"); styleDialogButton(btnAddOperator);
            JButton btnUpdateOperator = new JButton("✏️ Update Operator"); styleDialogButton(btnUpdateOperator);
            JButton btnDeleteOperator = new JButton("🗑️ Delete Operator by Name"); styleDialogButton(btnDeleteOperator);

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5)); topPanel.setBackground(SECONDARY_COLOR);
            topPanel.add(btnViewAllOps); topPanel.add(btnAddOperator); topPanel.add(btnUpdateOperator); topPanel.add(btnDeleteOperator);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
            

            btnViewAllOps.addActionListener(e -> {
                StringBuilder sb = new StringBuilder("All Operators:\n");
                if (allOperatorsRef.isEmpty()) sb.append("No operators found.\n");
                else for(Operator op : allOperatorsRef) {
                    sb.append(String.format("Name: %s, Total Collected: %.2f EGP\n", op.getOperatorName(), op.getTotalCollected()));
                }
                displayArea.setText(sb.toString());
            });

            btnAddOperator.addActionListener(e -> {
                String opName = JOptionPane.showInputDialog(this, "Enter new operator\'s name:", "Add Operator", JOptionPane.PLAIN_MESSAGE);
                if(opName != null && !opName.trim().isEmpty()) {
                    String result = currentAdmin.addNewOperator(allOperatorsRef, opName.trim());
                    JOptionPane.showMessageDialog(this, result, "Add Operator Status", result.startsWith("✅") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                    if(result.startsWith("✅")) {
                        FileSystem.saveOperatorData(allOperatorsRef, OPERATORS_FILE);
                        btnViewAllOps.doClick(); 
                    } 
                }
            });
            
            btnUpdateOperator.addActionListener(e -> showUpdateOperatorDialog());
            
            
            btnDeleteOperator.addActionListener(e -> {
                String opName = JOptionPane.showInputDialog(this, "Enter name of operator to delete:", "Delete Operator", JOptionPane.PLAIN_MESSAGE);
                if(opName != null && !opName.trim().isEmpty()) {
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
        private void showUpdateOperatorDialog() {
    // Create input panel
    JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
    
    // Operator selection combo box
    JComboBox<Operator> operatorCombo = new JComboBox<>();
    allOperatorsRef.forEach(operatorCombo::addItem);
    operatorCombo.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Operator) {
                setText(((Operator) value).getOperatorName());
            }
            return this;
        }
    });
    
    // Input fields
    JTextField txtNewName = new JTextField(20);
    JTextField txtNewTotal = new JTextField(10);
    
    // Add components to panel
    panel.add(new JLabel("Select Operator:"));
    panel.add(operatorCombo);
    panel.add(new JLabel("New Name:"));
    panel.add(txtNewName);
    panel.add(new JLabel("New Total Collected:"));
    panel.add(txtNewTotal);
    
    // Pre-fill current values when operator is selected
    operatorCombo.addActionListener(e -> {
        Operator selected = (Operator) operatorCombo.getSelectedItem();
        if (selected != null) {
            txtNewName.setText(selected.getOperatorName());
            txtNewTotal.setText(String.valueOf(selected.getTotalCollected()));
        }
    });
    
    // Initialize with first operator if available
    if (operatorCombo.getItemCount() > 0) {
        operatorCombo.setSelectedIndex(0);
    }
    
    // Show dialog
    int result = JOptionPane.showConfirmDialog(
        this, 
        panel, 
        "Update Operator", 
        JOptionPane.OK_CANCEL_OPTION, 
        JOptionPane.PLAIN_MESSAGE
    );
    
    // Process update if OK clicked
    if (result == JOptionPane.OK_OPTION) {
        try {
            Operator selectedOperator = (Operator) operatorCombo.getSelectedItem();
            String newName = txtNewName.getText().trim();
            int newTotal = Integer.parseInt(txtNewTotal.getText().trim());
            
            if (selectedOperator == null) {
                JOptionPane.showMessageDialog(this, "Please select an operator", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Operator name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check for duplicate names (excluding current operator)
            boolean nameExists = allOperatorsRef.stream()
                .anyMatch(op -> !op.equals(selectedOperator) && 
                               op.getOperatorName().equalsIgnoreCase(newName));
            
            if (nameExists) {
                JOptionPane.showMessageDialog(this, "Operator with this name already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Call your update method
            String updateResult = currentAdmin.updateOperator(
                selectedOperator, 
                newName, 
                newTotal
            );
            
            // Show result
            JOptionPane.showMessageDialog(
                this, 
                updateResult, 
                "Update Status", 
                updateResult.startsWith("✅") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
            );
            
            // Save and refresh if successful
            if (updateResult.startsWith("✅")) {
                FileSystem.saveOperatorData(allOperatorsRef, OPERATORS_FILE);
                btnViewAllOps.doClick(); // Refresh the list
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, 
                "Please enter a valid number for total collected", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
    }
    
    // BiConsumer functional interface for cleaner form field creation in NewCustomerDialog
    @FunctionalInterface
    interface BiConsumer<T, U> {
        void accept(T t, U u);
    }

    public static void main(String[] args) {
        // Apply a modern Look and Feel if available
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to the default L&F
        }
        
        SwingUtilities.invokeLater(() -> {
            ElectricityBillingSystemGUI gui = new ElectricityBillingSystemGUI();
            gui.setVisible(true);
        });
    }
}

