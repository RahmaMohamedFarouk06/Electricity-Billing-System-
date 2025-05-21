import java.util.List;

public class OldCustomer extends Customer {
    private int currentReading;
    private int lastReading;
    private int balanceDue;
    private int unpaidMonths;
    private boolean complain;
    private boolean StopAndCancel;

    public OldCustomer() {}

    // Constructor for converting NewCustomer to OldCustomer
    public OldCustomer(String name,String Nid, String address, String email, String meterCode, String region , long number) {
        super(name,Nid, address, email, meterCode, region , number);  // Call the parent class constructor
        this.currentReading = 0;
        this.lastReading = 0;
        this.balanceDue = 0;
        this.unpaidMonths=0;
        this.complain=false;
        this.StopAndCancel=false;
    }

    // Constructor to initialize the old customer object (e.g., when loading from file)
    public OldCustomer(String name,String Nid, String address, String email, String meterCode,String region ,long number, int currentReading, int lastReading, int balanceDue) {
        super(name,Nid, address, email, meterCode, region , number);  // Call the parent class constructor
        this.currentReading = currentReading;
        this.lastReading = lastReading;
        this.balanceDue = balanceDue;
        // unpaidMonths, complain, StopAndCancel will be set separately after loading if needed
    }

    // Getter and Setter methods remain largely the same, GUI will use them
    public int getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(int currentReading) {
        this.currentReading = currentReading;
    }

    public int getLastReading() {
        return lastReading;
    }

    public void setLastReading(int lastReading) {
        this.lastReading = lastReading;
    }

    public int getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(int balanceDue) {
        this.balanceDue = balanceDue;
    }

    public int getUnpaidMonths() {
        return unpaidMonths;
    }

    public void setUnpaidMonths(int unpaidMonths) {
        this.unpaidMonths = unpaidMonths;
    }

    public boolean isComplain() {
        return complain;
    }

    public void setComplain(boolean complain) {
        this.complain = complain;
    }

    public boolean isStopAndCancel() {
        return StopAndCancel;
    }

    public void setStopAndCancel(boolean StopAndCancel) {
        this.StopAndCancel = StopAndCancel;
    }

    public int getTotalUsage() {
        return currentReading - lastReading;
    }

    // Method for entering monthly reading, returns a status message for GUI
    public String enterMonthlyReading(int monthlyReading) {
        if (monthlyReading > currentReading) {
            setLastReading(currentReading);
            setCurrentReading(monthlyReading);
            unpaidMonths++;
            String message = "Monthly reading updated. Current reading: " + monthlyReading + ", Last reading: " + lastReading + ".";
            if (unpaidMonths >= 3) {
                message += "\nWarning: You haven't paid for " + unpaidMonths + " months. Please check your notifications.";
            }
            // FileSystem.saveCustomerData will be called by the GUI controller after this method
            return message;
        }
        else {
            return "Error: Monthly reading cannot be less than or equal to the current reading (" + currentReading + ").";
        }
    }

    // Method for paying bill, returns a status message for GUI
    public String payBill(String meterCode, int paymentAmount) {
        if (!meterCode.equalsIgnoreCase(getMeterCode())) {
            return "Error: Meter code does not match.";
        }
        if (balanceDue <= 0) {
            return "Error: No balance due.";
        }
        if (paymentAmount < balanceDue) {
            return "Error: Payment amount (" + paymentAmount + ") is less than the balance due (" + balanceDue + "). Please pay the exact amount.";
        }
        if (paymentAmount > balanceDue) {
            return "Error: Payment amount (" + paymentAmount + ") is more than the balance due (" + balanceDue + "). Please pay the exact amount.";
        }

        balanceDue = 0;
        unpaidMonths = 0;
        // FileSystem.saveCustomerData will be called by the GUI controller
        return "Payment of " + paymentAmount + " successful. Remaining balance: 0.";
    }

    // Static method to find customer by meter code - logic remains the same
    public static OldCustomer findCustomerByMeterCode(List<OldCustomer> customers, String meterCode) {
        for (OldCustomer customer : customers) {
            if (customer.getMeterCode().equalsIgnoreCase(meterCode)) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ", OldCustomer [currentReading=" + currentReading + ", lastReading=" + lastReading + ", balanceDue=" + balanceDue + ", unpaidMonths=" + unpaidMonths + ", complain=" + complain + ", StopAndCancel=" + StopAndCancel + "]";
    }
}

