package sepractice;
import java.util.HashSet;
import java.util.Set;


public class Course {
    /*
    course: courseAttributes
      A)title
      B)Instructor - is this supposed to be a link to the professors public profile (like a social network) or is it just a string?) / handle error for multiple instructors
      C)Course Max size
      D)number of students enrolled
      E)course timeSlot
      F)list of students enrolled
      E) number of credits in course
      F) semester on campus/ level
      G) course Attributes
     */
    private String courseTitle;
    private long crn;
    private Instructor courseInstructor;
    private int courseSize;
    private Set<Student> studentsInClass = new HashSet<>();
    private int creditsAmount;
     private int timesOfMeeting;
    private Set<Course> preRequisites;
    private Set<Course> coRequisites;
    private String attribute;

    public Course(String title, Instructor instructor, int courseSize, int creditsAmount, int timesOfMeeting, Set<Course> preReq, Set<Course> coRequisites, String attribute){
        this.courseTitle = title;
        this.courseInstructor = instructor;
        this.crn = createCRN();
        this.courseSize = courseSize;
        this.creditsAmount = creditsAmount;
        //times of meeting is similar to the way yeshiva college uses 231, or 432 to say a class happens on MW from 3-4:15 and such
        this.timesOfMeeting = timesOfMeeting;
        this.preRequisites = preReq;
        this.coRequisites = coRequisites;
        this.attribute = attribute;
    }

    private long createCRN(){
        long crn = (long) Math.random()*100_000;
        return crn;
    }

    public String getTitle(){
        return this.courseTitle;
    }

    public long getCRN(){
        return this.crn;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setCourseSize(int courseSize){
        this.courseSize = courseSize;
    }

    public int getCourseSize() {
        return courseSize;
    }

    public String  getPrequisites(){
        String listRec = "these are the prerequisits for this course: ";
        for(Course course: this.preRequisites){
            listRec += course.getTitle() + "," + course.getCRN() + " ;";
        }
        return listRec;
    }

    public String getcoRequisites(){
        String listRec = "these are the prerequisits for this course: ";
        for(Course course: this.coRequisites){
            listRec += course.getTitle() + "," + course.getCRN() + " ;";
        }
        return listRec;

    }



    public boolean addStudent(Student student){
        studentsInClass.add(student);
        return true;
    }

    public Student removeStudent(Student student){
        return student;
    }

    public Set<Student> getStudentsInClass() {
        return studentsInClass;
    }

    public Instructor getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(Instructor courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public int getCreditsAmount() {
        return creditsAmount;
    }

    public void setCreditsAmount(int creditsAmount) {
        this.creditsAmount = creditsAmount;
    }

    public int getTimesOfMeeting() {
        return timesOfMeeting;
    }

    public void setTimesOfMeeting(int timesOfMeeting) {
        this.timesOfMeeting = timesOfMeeting;
    }
}
