package sepractice;
public class Administator extends Patron {
   /*
    admin class: admin attributes
   inherited from patron:
    A)First Name
    B)last Name
     F) phone number
    G) email address

    -id number
    D) Department
    E) if instructor as well, instructor profile
    F) phone number
    G) email address
    H)

    admin abilities:
    create course
    delete course
    create new student

            */

    private String firstName;
    private String lastName;
    private Long idNumber;
    private String emailAddress;
    private Long phoneNumber;

    public Administator(String firstName, String lastName, String emailAddress,String password, String phoneNumber) {
        super(firstName, lastName, emailAddress,password, phoneNumber);
    }


    private long setIDNumber() {
        return 0000;
    }

    public Course createCourse(){
        return null;
    }


}



