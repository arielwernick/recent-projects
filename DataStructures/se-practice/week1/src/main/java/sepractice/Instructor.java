package sepractice;

import java.util.HashSet;
import java.util.Set;

public class Instructor extends Patron {


    /*
    Instructor class: Instructor attributes
      A)First Name
      B)last Name
      C) id number
      D) department
      E) phone number
      I) email address
      //to improve make the above into a parent class called patron and have this class inherit those methods
      E)  classes currently teaching
      F)classes previously taught
      G) department
      H) phone number
      I) email address
      H)
     */
    private String department;
   private Set<Course> courseList = new HashSet<>();

    public Instructor(String firstName, String lastName, String emailAddress, String password,String phoneNumber, String department){
        super(firstName,lastName,emailAddress,password,phoneNumber);
        this.department = department;

    }

    public Course addCourse(Course course){
        courseList.add(course);
        return course;
    }

    public Set<Course> getCourseList(){
        return courseList;
    }



}
