package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person implements Comparable<Student> {

    private int studentId;
    private List<Integer> enrolledCourses;

    public Student(String firstName, String lastName,  List<Integer> enrolledCourses, int studentId){
        super(firstName, lastName);
        this.studentId = studentId;
        this.enrolledCourses = enrolledCourses;
    }

    public void addCourse(int courseId){
        enrolledCourses.add(courseId);
    }

    public void deleteCourse(int courseId){
        enrolledCourses.remove(courseId);
    }

    public int getEnrolledCoursesSize(){
        return enrolledCourses.size();
    }


    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public List<Integer> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Integer> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    @Override
    public int compareTo(Student student){
        if(studentId == student.getStudentId())
            return 1;
        return 0;

    }


    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName +
                ", lastName='" + lastName +
                ", studentId=" + studentId +
                ", enrolledCourses=" + enrolledCourses +
                '}';
    }

public int getCoursesNumber(){
        return enrolledCourses.size();
}



}
