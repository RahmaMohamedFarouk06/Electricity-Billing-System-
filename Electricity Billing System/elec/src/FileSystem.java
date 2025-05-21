import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.util.Map;

public class FileSystem {

    // Saves customer data. Returns true on success, false on failure.
    public static boolean saveCustomerData(List<OldCustomer> customers, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (OldCustomer customer : customers) {
                writer.write("Name: " + customer.getName() + "\n");
                writer.write("NID: " + customer.getNid() + "\n");
                writer.write("Address: " + customer.getAddress() + "\n");
                writer.write("Email: " + customer.getEmail() + "\n");
                writer.write("Meter Code: " + customer.getMeterCode() + "\n");
                writer.write("Region: " + customer.getRegion() + "\n");
                writer.write("Phone Number: " + customer.getNumber() + "\n");
                writer.write("Current Reading: " + customer.getCurrentReading() + "\n");
                writer.write("Last Reading: " + customer.getLastReading() + "\n");
                writer.write("Balance Due: " + customer.getBalanceDue() + "\n");
                writer.write("Unpaid Months: " + customer.getUnpaidMonths() + "\n");
                writer.write("Complaint: " + customer.isComplain() + "\n");
                writer.write("Stop and Cancel: " + customer.isStopAndCancel() + "\n");
                writer.write("--------------------------------------------------\n");
            }
            // System.out.println("✅ Customer data saved to " + filename); // GUI will handle notifications
            return true;
        } catch (IOException e) {
            // System.out.println("❌ Error saving customer data to " + filename); // GUI will handle error messages
            e.printStackTrace(); // Log error for debugging
            return false;
        }
    }

    // Loads customer data. Returns true on success, false on failure.
    public static boolean loadCustomersFromFile(List<OldCustomer> customers, String filename) {
        customers.clear(); // Clear existing list before loading
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            Map<String, String> fields = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--------------------------------------------------")) {
                    if (!fields.isEmpty()) {
                        try {
                            OldCustomer c = new OldCustomer(
                                    fields.get("Name"),
                                    fields.get("NID"),
                                    fields.get("Address"),
                                    fields.get("Email"),
                                    fields.get("Meter Code"),
                                    fields.get("Region"),
                                    Long.parseLong(fields.getOrDefault("Phone Number", "0")),
                                    Integer.parseInt(fields.getOrDefault("Current Reading", "0")),
                                    Integer.parseInt(fields.getOrDefault("Last Reading", "0")),
                                    Integer.parseInt(fields.getOrDefault("Balance Due", "0"))
                            );
                            c.setUnpaidMonths(Integer.parseInt(fields.getOrDefault("Unpaid Months", "0")));
                            c.setComplain(Boolean.parseBoolean(fields.getOrDefault("Complaint", "false")));
                            c.setStopAndCancel(Boolean.parseBoolean(fields.getOrDefault("Stop and Cancel", "false")));
                            customers.add(c);
                        } catch (NumberFormatException nfe) {
                            // Handle error for a specific record, maybe log it and continue
                            System.err.println("Skipping customer record due to number format error: " + fields.get("Name"));
                            nfe.printStackTrace();
                        } catch (NullPointerException npe){
                            System.err.println("Skipping customer record due to missing field: " + fields.toString());
                            npe.printStackTrace();
                        }
                        fields.clear();
                    }
                } else {
                    String[] parts = line.split(": ", 2);
                    if (parts.length == 2) {
                        fields.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
            // System.out.println("✅ Customer data loaded successfully."); // GUI will handle notifications
            return true;
        } catch (FileNotFoundException e) {
            // System.out.println("Customer data file not found. Starting with an empty list."); // GUI can inform this
            return true; // File not found is not necessarily a critical error for loading, could be first run
        } catch (IOException e) {
            // System.out.println("❌ Error loading customer data from file."); // GUI will handle error messages
            e.printStackTrace();
            return false;
        }
    }

    // Saves operator data. Returns true on success, false on failure.
    public static boolean saveOperatorData(List<Operator> operators, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Operator operator : operators) {
                writer.write("Operator Name: " + operator.getOperatorName() + "\n");
                writer.write("Total Collected: " + operator.getTotalCollected() + "\n");
                writer.write("--------------------------------------------------\n");
            }
            // System.out.println("✅ Operator data saved to " + filename); // GUI will handle notifications
            return true;
        } catch (IOException e) {
            // System.out.println("❌ Error saving operator data to " + filename); // GUI will handle error messages
            e.printStackTrace();
            return false;
        }
    }

    // Loads operator data. Returns true on success, false on failure.
    public static boolean loadOperatorsFromFile(List<Operator> operators, String filename) {
        operators.clear(); // Clear existing list before loading
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String operatorName = null;
            int totalCollected = 0;
            boolean readingOperator = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("--------------------------------------------------")) {
                    if (readingOperator && operatorName != null) {
                        operators.add(new Operator(operatorName, totalCollected));
                        operatorName = null;
                        totalCollected = 0;
                        readingOperator = false;
                    }
                    continue;
                }

                if (line.startsWith("Operator Name: ")) {
                    operatorName = line.substring("Operator Name: ".length()).trim();
                    readingOperator = true;
                } else if (line.startsWith("Total Collected: ") && readingOperator) {
                    try {
                        totalCollected = Integer.parseInt(line.substring("Total Collected: ".length()).trim());
                    } catch (NumberFormatException e) {
                        // System.out.println("⚠️ Invalid number format in Total Collected for operator: " + operatorName); // GUI
                        e.printStackTrace();
                        // Decide: skip operator, set totalCollected to 0, or stop loading?
                        // For now, if format is bad, it might use the last valid totalCollected or 0 if it's the first field.
                    }
                }
            }
            // Add the last operator if file ends without a separator and data was being read
            if (readingOperator && operatorName != null) {
                operators.add(new Operator(operatorName, totalCollected));
            }
            // System.out.println("✅ Operator data loaded successfully."); // GUI
            return true;
        } catch (FileNotFoundException e) {
            // System.out.println("Operator data file not found. Starting with an empty list."); // GUI
            return true; // File not found is not necessarily a critical error for loading
        } catch (IOException e) {
            // System.out.println("❌ Error loading operator data: " + e.getMessage()); // GUI
            e.printStackTrace();
            return false;
        }
    }
}

