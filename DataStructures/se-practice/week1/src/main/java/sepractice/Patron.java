package sepractice;
public class Patron {


    String firstName;
    String lastName;
    Long idNumber;
    String emailAddress;
    String phoneNumber;
    String password;

    public static void main(String[] args) {
        double randomnnumbers = Math.random();
        System.out.println(randomnnumbers);
    }
    public Patron(String firstName, String lastName, String emailAddress, String password, String phoneNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        setIDNumber();
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        setPassword(password);
    }



    private long setIDNumber(){
        int begining = 800;
        int randomnnumbers = (int) Math.random();
        System.out.println(randomnnumbers);
        return 0000;
    }

    public long getIdNumber(){
        return this.idNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getPassword() { return this.password;}

    public void setPassword(String password){
        this.password = password;
    }


    public String getPhoneNumber() {
        return this.phoneNumber;
    }


}
