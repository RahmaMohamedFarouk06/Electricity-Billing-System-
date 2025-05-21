import java.util.Scanner;

public class Customer {
    private String name;
    private String Nid;
    private String address;
    private String email;
    private String meterCode;
    private String region;
    private long number;


    public Customer(){};
    // Constructor to initialize the customer object
    public Customer(String name, String Nid,String address, String email, String meterCode, String region ,long number) {
        this.name = name;
        this.Nid=Nid;
        this.address = address;
        this.email = email;
        this.meterCode = meterCode;
        this.region=region;
        this.number = number;
    }

    // Getter methods remain the same
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getNid() {
        return Nid;
    }

    // Setter for NID - GUI will provide validated NID
    public void setNid(String nidStr) {
        // Validation logic can be moved to GUI or kept here if GUI calls this setter
        // For now, assume GUI provides a valid 14-digit NID string
        this.Nid = nidStr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    // Setter for Email - GUI will provide validated email
    public void setEmail(String email) {
        // Validation logic (contains "@" and ends with ".com") can be moved to GUI or kept here
        this.email = email;
    }

    public String getMeterCode() {
        return meterCode;
    }

    // Setter for MeterCode - usually generated, but can be set if needed
    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    public long getNumber() {
        return number;
    }

    // Setter for Number - GUI will provide validated number
    public void setNumber(long number) {
        // Validation (11 digits) can be moved to GUI or kept here
        this.number = number;
    }

    // Overloaded setNumber for String input from GUI, if GUI handles validation to String
    public void setNumber(String phoneNumberStr) {
        // Assuming GUI validates it's 11 digits and converts to long or this method does
        if (phoneNumberStr != null && phoneNumberStr.matches("\\d{11}")) {
            this.number = Long.parseLong(phoneNumberStr);
        } else {
            // Handle error or rely on GUI validation
        }
    }


    // Method to generate meter code - logic remains the same
    public void generateMeterCode() {
        if (this.Nid != null && this.Nid.length() >= 4) {
            this.meterCode = "MTR-" + this.Nid.substring(Nid.length() - 4);
        } else {
            // System.out.println("Invalid NID. Cannot generate meter code."); // GUI feedback needed
        }
    }

    // Method to display customer information
    @Override
    public String toString() {
        return "Customer [name=" + name + ", NationalID=" + Nid + ", address=" + address + ", email=" + email + ", meterCode=" + meterCode +", Region=" + region + ", number=" + number + "]";
    }
}

