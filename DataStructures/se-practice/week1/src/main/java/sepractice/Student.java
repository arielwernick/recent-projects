package sepractice;
import org.w3c.dom.Document;

import java.util.Set;

public class Student  extends Patron {

    /*
    Student class: students attributes
      A)First Name
      B)last Name
      C) id number
      email
      phone number
       //to improve make the above into a parent class called patron and have this class inherit those methods
      D) enrolled classes
      E)transcript
      F)program
      E) honors attribute
      F) semester on campus/ level
      H) credits taken
     */

    int classStanding;
    Set<Course> enrolledClasses;
    String semesterOnCampus;
    private Document transcript;




    public Student(String firstName, String lastName, String emailAddress,String password, String phoneNumber){
        super(firstName,lastName,emailAddress,password, phoneNumber);
        setClassStanding(0);
    }



    private long setIDNumber(){
        return 0000;
    }

    //above is same for all patrons

    protected Course addCourse(Course course){
        enrolledClasses.add(course);
        return course;
    }

    protected String dropCourse(Course course){
        enrolledClasses.remove(course);
        String returnMessage = "you have successfully dropped course "+ course.getTitle();
        return returnMessage;
    }

    public Set<Course> getCourses(){
        return enrolledClasses;
    }

    public void setClassStanding(int classStanding) {

        this.classStanding += classStanding;
    }

    public String setSemesterOnCampusLevel(String semesterOncampus){
        //switch, freshman/ junior/ sophmore/ senior/grad student - throw error otherwise
        return null;
    }

    public String getSemesterOnCampus(){
        return semesterOnCampus;
    }

    public int getClassStanding() {
        return classStanding;
    }
}
