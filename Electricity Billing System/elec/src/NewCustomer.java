import java.util.List;
import java.util.Scanner;
import java.io.*;

public class NewCustomer extends Customer {
    private String Contract;
    private  boolean duplicated;

    public NewCustomer() {

    }

    public NewCustomer(String name,String Nid, String address, String email, String meterCode, String region, long number, String Contract) {
        super(name,Nid, address, email, meterCode, region, number);  // Call parent constructor
        this.Contract = Contract;
    }

    public String getContract() {
        return Contract;
    }

    public void setContract(String Contract) {
        this.Contract = Contract;
    }
    public void notifyMeterReady() {
        // This will be handled by GUI, System.out.println will be replaced
        // System.out.println("📧 Email sent to " + getName() + ": Your meter is ready!");
        // System.out.println("Your Meter Code is: " + getMeterCode());
    }
    public void attachContract() {
        // This will be handled by GUI, Scanner input will be replaced
        // Scanner input = new Scanner(System.in); 
        // System.out.println("Enter contract copy path:");
        // this.Contract = input.nextLine(); 
        // System.out.println("✅ Contract attached successfully at: " + this.Contract);
    }
    public void fillinformation(){
        // This will be handled by GUI, Scanner input will be replaced
        // Scanner input = new Scanner(System.in); 
        // System.out.println("Enter your name:");
        // String name = input.nextLine();
        // setName(name);
        // setNid();
        // setEmail();
        // setNumber();
        // System.out.println("Enter your address:");
        // String address = input.nextLine();
        // setAddress(address);
        // System.out.println("Enter your Region:");
        // String Region = input.nextLine();
        // setRegion(Region);
        // setMeterCode();
        // attachContract();
        // System.out.println("Your meter code is " + getMeterCode());
        // notifyMeterReady();
    }

    public  OldCustomer createNewCustomer( List<OldCustomer> OldCustomers) {
        if (!duplicated){
        OldCustomer newCustomer = new OldCustomer(getName(),getNid(), getAddress(), getEmail(), getMeterCode(), getRegion() , getNumber());
        OldCustomers.add(newCustomer); // Add new customer to the list
        // Save new customer data to the text file
        FileSystem.saveCustomerData(OldCustomers, "customers.txt"); // This might need GUI feedback
        duplicated=true;
        return newCustomer;}
        else{
            // System.out.println("Can't add Customer already Exists in customers.txt "); // GUI feedback
            return null;}
    }

    // GUI will call setters directly after getting data from input fields
    // The original setNid, setEmail, setNumber with Scanner need to be adapted or new setters provided if GUI needs them.
    // For now, assuming GUI will get validated input and call super.setName(), super.setAddress(), super.setRegion(), etc.
    // and this.setContract().
    // The setNid(), setEmail(), setNumber() in Customer class also use Scanner, they will need to be adapted for GUI.
    // For example, by creating versions that take arguments, or by having the GUI perform validation.

}

