import java.util.ArrayList;
import java.util.List;
// Scanner will not be used in GUI version for direct input in this class

public class Admin {
    private String adminName;

    public Admin() {
    }

    public Admin(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    // Method to view all bills by region, returns a formatted string for GUI
    public String viewAllBillsByRegion(List<OldCustomer> customers, String region) {
        StringBuilder sb = new StringBuilder("📋 Bills in Region: " + region + "\n");
        boolean found = false;
        for (OldCustomer customer : customers) {
            if ((customer.getRegion()).equalsIgnoreCase(region.trim())) {
                sb.append("--------------------\n");
                sb.append("Customer: ").append(customer.getName()).append(" (Meter: ").append(customer.getMeterCode()).append(")\n");
                sb.append("- Balance Due: ").append(customer.getBalanceDue()).append(" EGP\n");
                found = true;
            }
        }
        if (!found) {
            sb.append("No customers found in this region.\n");
        }
        return sb.toString();
    }

    // Method to view total collected by all operators, returns a formatted string for GUI
    public String viewTotalCollected(List<Operator> operators) {
        int total = 0;
        for (Operator operator : operators) {
            total += operator.getTotalCollected();
        }
        return "💰 Total Collected by all operators: " + total + " EGP.";
    }

    // Method to make consumption statistics by region, returns a formatted string for GUI
    public String makeConsumptionStatistics(List<OldCustomer> customers, String region) {
        int totalConsumption = 0;
        int count = 0;
        for (OldCustomer customer : customers) {
            if ((customer.getRegion()).equalsIgnoreCase(region.trim())) {
                totalConsumption += (customer.getCurrentReading() - customer.getLastReading());
                count++;
            }
        }
        if (count == 0) {
            return "📈 No customers found in region " + region + " for consumption statistics.";
        }
        return "📈 Consumption in " + region + ": " + totalConsumption + " units for " + count + " customers.";
    }

    // Method to add a new customer (NewCustomer object created by GUI)
    // Returns a status message for GUI
    public String addNewCustomer(List<OldCustomer> oldCustomersList, NewCustomer newCustomerToAdd) {
        // Check for duplicates based on NID or Meter Code before adding
        for (OldCustomer existingCustomer : oldCustomersList) {
            if (existingCustomer.getNid().equals(newCustomerToAdd.getNid())) {
                return "Error: Customer with NID " + newCustomerToAdd.getNid() + " already exists.";
            }
            if (existingCustomer.getMeterCode().equals(newCustomerToAdd.getMeterCode())) {
                return "Error: Customer with Meter Code " + newCustomerToAdd.getMeterCode() + " already exists.";
            }
        }
        
        OldCustomer customerToAddAsOld = new OldCustomer(
            newCustomerToAdd.getName(), 
            newCustomerToAdd.getNid(), 
            newCustomerToAdd.getAddress(), 
            newCustomerToAdd.getEmail(), 
            newCustomerToAdd.getMeterCode(), 
            newCustomerToAdd.getRegion(), 
            newCustomerToAdd.getNumber()
        );
        // Contract info from NewCustomer is not directly stored in OldCustomer in the current model
        // If it needs to be, OldCustomer or a new structure would need to accommodate it.

        oldCustomersList.add(customerToAddAsOld);
        // FileSystem.saveCustomerData will be called by the GUI controller
        return "✅ New Customer added: " + newCustomerToAdd.getName() + " (Meter: " + newCustomerToAdd.getMeterCode() + ")";
    }

    // Method to update customer details (GUI will provide new details)
    // Returns a status message for GUI
    public String updateCustomer(OldCustomer customerToUpdate, String newName, String newAddress, String newEmail, String newRegion, long newPhoneNumber) {
        if (customerToUpdate == null) {
            return "Error: Customer to update not found.";
        }
        customerToUpdate.setName(newName);
        customerToUpdate.setAddress(newAddress);
        customerToUpdate.setEmail(newEmail); // Assumes setEmail in Customer is adapted for direct string input
        customerToUpdate.setRegion(newRegion);
        customerToUpdate.setNumber(newPhoneNumber); // Assumes setNumber in Customer is adapted for long input
        // FileSystem.saveCustomerData will be called by the GUI controller
        return "✅ Customer details updated for: " + customerToUpdate.getName() + " (Meter: " + customerToUpdate.getMeterCode() + ")";
    }

    // Method to delete a customer by meter code
    // Returns a status message for GUI
    public String deleteCustomerByMeterCode(List<OldCustomer> customers, String meterCode) {
        OldCustomer customerToRemove = null;
        for (OldCustomer customer : customers) {
            if (customer.getMeterCode().equalsIgnoreCase(meterCode)) {
                customerToRemove = customer;
                break;
            }
        }
        if (customerToRemove != null) {
            customers.remove(customerToRemove);
            // FileSystem.saveCustomerData will be called by the GUI controller
            return "✅ Customer deleted: " + customerToRemove.getName() + " (Meter: " + meterCode + ")";
        } else {
            return "❌ Customer with Meter Code " + meterCode + " not found.";
        }
    }

    // Method to add a new operator (GUI provides name)
    // Returns a status message for GUI
    public String addNewOperator(List<Operator> operators, String operatorName) {
        if (operatorName == null || operatorName.trim().isEmpty()){
            return "Error: Operator name cannot be empty.";
        }
        for (Operator existingOperator : operators) {
            if (existingOperator.getOperatorName().equalsIgnoreCase(operatorName.trim())) {
                return "Error: Operator with name '" + operatorName.trim() + "' already exists.";
            }
        }
        Operator newOperator = new Operator(operatorName.trim(), 0);
        operators.add(newOperator);
        // FileSystem.saveOperatorData will be called by the GUI controller
        return "✅ New Operator added: " + operatorName.trim();
    }

    // Method to update operator details (GUI provides new details)
    // Returns a status message for GUI
    public String updateOperator(Operator operatorToUpdate, String newName, int newTotalCollected) {
        if (operatorToUpdate == null) {
            return "Error: Operator to update not found.";
        }
        if (newName == null || newName.trim().isEmpty()){
            return "Error: New operator name cannot be empty.";
        }
        // Check if new name conflicts with another existing operator (excluding itself)
        // This logic should be in the GUI controller or a higher level managing the list.
        // For now, we assume the name is valid or the check is done before calling.

        operatorToUpdate.setOperatorName(newName.trim());
        operatorToUpdate.setTotalCollected(newTotalCollected);
        // FileSystem.saveOperatorData will be called by the GUI controller
        return "✅ Operator details updated for: " + newName.trim();
    }

    // Method to delete an operator by name
    // Returns a status message for GUI
    public String deleteOperatorByName(List<Operator> operators, String operatorName) {
        Operator operatorToRemove = null;
        for (Operator operator : operators) {
            if (operator.getOperatorName().equalsIgnoreCase(operatorName.trim())) {
                operatorToRemove = operator;
                break;
            }
        }
        if (operatorToRemove != null) {
            operators.remove(operatorToRemove);
            // FileSystem.saveOperatorData will be called by the GUI controller
            return "✅ Operator deleted: " + operatorName.trim();
        } else {
            return "❌ Operator with name " + operatorName.trim() + " not found.";
        }
    }
    
    // Static method to find admin by name - for login purposes
    public static Admin findAdminByName(List<Admin> admins, String adminName) {
        for (Admin admin : admins) {
            if (admin.getAdminName().equalsIgnoreCase(adminName)) {
                return admin;
            }
        }
        return null;
    }
}

