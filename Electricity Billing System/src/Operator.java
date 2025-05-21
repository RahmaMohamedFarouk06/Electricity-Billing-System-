import java.util.List;
// Scanner will not be used in GUI version for direct input in this class

public class Operator {
    private String operatorName;
    private int totalCollected;
    private boolean duplicated; // This flag's logic might need review for GUI context

    public Operator() {}

    public Operator(String operatorName) {
        this.operatorName = operatorName;
        this.totalCollected = 0;
        this.duplicated = false; // Initialize duplicated status
    }

    public Operator(String operatorName, int totalCollected) {
        this.operatorName = operatorName;
        this.totalCollected = totalCollected;
        this.duplicated = false; // Initialize duplicated status
    }

    // Method to collect payment, returns a status string for GUI
    public String collectPayment(OldCustomer customer, int paymentAmount) {
        if (customer == null) {
            return "Error: Customer not found.";
        }
        // The OldCustomer.payBill method now returns a status string
        String paymentStatus = customer.payBill(customer.getMeterCode(), paymentAmount);
        
        // Check if payment was successful based on the message from payBill
        // This is a bit fragile; ideally, payBill would return a boolean or status code
        if (paymentStatus.startsWith("Payment of")) { // Assuming success message starts this way
            totalCollected += paymentAmount; // Update total collected by the operator
            // FileSystem.saveOperatorData and FileSystem.saveCustomerData will be called by GUI controller
            return paymentStatus + "\nTotal collected by " + operatorName + " now: " + totalCollected + " EGP.";
        } else {
            return "Payment was unsuccessful: " + paymentStatus;
        }
    }

    // Method to get bill details as a string for GUI display
    public String getBillDetails(OldCustomer customer) {
        if (customer == null) {
            return "Error: Customer not found.";
        }
        return "📄 Bill for :" + customer.getName() +
               "\n- Meter Code: " + customer.getMeterCode() +
               "\n- Balance Due: " + customer.getBalanceDue() + " EGP.";
    }

    // Method to get bills by region as a formatted string for GUI display
    public String getBillsByRegion(List<OldCustomer> customers, String region) {
        StringBuilder billsText = new StringBuilder("📂 Bills in Region: " + region + "\n");
        boolean found = false;
        for (OldCustomer customer : customers) {
            if ((customer.getRegion()).equalsIgnoreCase(region.trim())) {
                billsText.append("--------------------\n");
                billsText.append(getBillDetails(customer)).append("\n");
                found = true;
            }
        }
        if (!found) {
            billsText.append("No customers found in this region.");
        }
        return billsText.toString();
    }

    // Method to validate reading, returns a status string for GUI
    public String validateReading(OldCustomer customer) {
        if (customer == null) {
            return "Error: Customer not found.";
        }
        int consumption = customer.getCurrentReading() - customer.getLastReading();
        if (consumption >= 0) {
            return "✅ Valid consumption: " + consumption + " units.";
        } else {
            return "❌ Error in readings! Current reading is less than last reading.";
        }
    }

    // Method to define tariff, returns a status string for GUI
    public String defineTariff(OldCustomer customer, int pricePerUnit) {
        if (customer == null) {
            return "Error: Customer not found.";
        }
        if (pricePerUnit <= 0) {
            return "Error: Price per unit must be positive.";
        }
        int consumption = customer.getCurrentReading() - customer.getLastReading();
        if (consumption < 0) {
            return "Error: Cannot define tariff due to invalid readings (current < last).";
        }
        int monthlyBalance = consumption * pricePerUnit;
        int newBalanceDue = customer.getBalanceDue() + monthlyBalance;
        customer.setBalanceDue(newBalanceDue);
        // FileSystem.saveCustomerData will be called by GUI controller
        return "✅ New bill defined: " + monthlyBalance + " EGP for consumption of " + consumption + " units." +
               "\n✅ Updated total balance due: " + newBalanceDue + " EGP.";
    }

    // Method to stop meter and cancel subscription, returns a status string for GUI
    public String stopMeterAndCancelSubscription(OldCustomer customer) {
        if (customer == null) {
            return "Error: Customer not found.";
        }
        // Add logic for unpaidMonths check if required before cancellation, e.g.
        // if (customer.getUnpaidMonths() < 12) { 
        //     return "Customer has less than 12 unpaid months. Cannot cancel yet."; 
        // }
        customer.setStopAndCancel(true);
        // FileSystem.saveCustomerData will be called by GUI controller
        return "🚫 Meter " + customer.getMeterCode() + " for " + customer.getName() + " has been stopped and subscription canceled.";
    }

    public String getOperatorName() { return operatorName; }
    public double getTotalCollected() { return totalCollected; }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public void setTotalCollected(int totalCollected) {
        this.totalCollected = totalCollected;
    }

    public boolean isDuplicated() {
        return duplicated;
    }

    public void setDuplicated(boolean duplicated) {
        this.duplicated = duplicated;
    }

    // Method to create a new operator, returns the operator or null (GUI will handle messaging)
    // The list of operators and saving will be managed by the main application/GUI controller
    public Operator createNewOperatorInstance() {
        if (!duplicated) { 
            this.duplicated = true; 
            return this; 
        } else {
            return null; 
        }
    }

    // Static method to find operator by name - logic remains the same
    public static Operator findOperatorByName(List<Operator> operators, String operatorName) {
        for (Operator operator : operators) {
            if (operator.getOperatorName().equalsIgnoreCase(operatorName)) {
                return operator;
            }
        }
        return null;
    }
}

