package model;

import java.util.List;

public class Course implements Comparable<Course> {

    private int courseId;
    private String name;
    private int teacherId;
    private int maxEnrollment;
    private List<Integer> studentsEnrolled;
    private int credits;

    public Course( int courseId, String name,  int maxEnrollment, int credits, int teacherId, List<Integer> studentsEnrolled) {
        this.courseId = courseId;
        this.name = name;
        this.maxEnrollment = maxEnrollment;
        this.credits = credits;
        this.teacherId = teacherId;
        this.studentsEnrolled = studentsEnrolled;



    }

    public void addStudent(int studetId){
        studentsEnrolled.add(studetId);
    }

    public void removeStudent(int studentId){
        studentsEnrolled.remove(studentId);
    }

    public int getNumberOfStudents(){
        return studentsEnrolled.size();
    }


    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public List<Integer> getStudentsEnrolled() {
        return studentsEnrolled;
    }

    public void setStudentsEnrolled(List<Integer> studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", name='" + name + '\'' +
                ", teacherId=" + teacherId +
                ", maxEnrollment=" + maxEnrollment +
                ", studentsEnrolled=" + studentsEnrolled +
                ", credits=" + credits +
                '}';
    }


    @Override
    public int compareTo(Course course){
        if(courseId == course.getCourseId())
            return 1;
        return 0;
    }
}
