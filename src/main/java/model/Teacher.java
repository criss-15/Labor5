package model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person implements Comparable<Teacher>{

    private int teacherId;
    private List<Integer> coursesId;


    public Teacher(String firstName, String lastName, List<Integer> coursesId, int teacherId) {
        super(firstName, lastName);
        this.teacherId = teacherId;
        this.coursesId = coursesId;

    }

    public void addCourse(int courseId){
        coursesId.add(courseId);
    }

    public void deleteCourse(int courseId){
        coursesId.remove(courseId);
    }

    public int getNumberOfCourses(){
        return coursesId.size();
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public List<Integer> getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(List<Integer> coursesId) {
        this.coursesId = coursesId;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "firstName='" + firstName +
                ", lastName='" + lastName +
                ", teacherId=" + teacherId +
                ", coursesId=" + coursesId +
                '}';
    }


    @Override
    public int compareTo(Teacher teacher){
        if(teacherId == teacher.getTeacherId())
            return 1;
        return 0;
    }
}
