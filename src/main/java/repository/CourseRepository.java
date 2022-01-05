package repository;

import model.Course;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository implements ICrudRepository<Course> {

    private String db_URL;
    private String user;
    private String password;

    public CourseRepository(String db_URL, String user, String password){
        this.db_URL = db_URL;
        this.user =  user;
        this.password = password;
    }



    @Override
    public void create(Course obj) throws SQLException {
        Connection connection = DriverManager.getConnection(db_URL, user, password);
        Statement statement = connection.createStatement();

            String queryInsert = String.format("INSERT INTO school.course(Course_id, Name, MaxEnrollment, Credits) Values ("+"'"+obj.getCourseId()+"'" + "," + "'"+obj.getName()+"'" +","+"'"+obj.getMaxEnrollment()+"'"+","+"'"+obj.getCredits()+"'"+")");
            statement.executeQuery(queryInsert);
            statement.close();
            connection.close();



    }

    @Override
    public List<Course> getAll() throws SQLException {
        Connection connection = DriverManager.getConnection(db_URL, user,password);
        Statement statement = connection.createStatement();

        List<Course> courses = new ArrayList<>();

        String queryGetAllCourses = "SELECT * FROM school.course";
        ResultSet resultSet = statement.executeQuery(queryGetAllCourses);
        while(resultSet.next()){
            int courseId = resultSet.getInt("Course_id");
            String name = resultSet.getString("Name");
            int maxEnrollment = resultSet.getInt("MaxEnrollment");
            int totalCredits = resultSet.getInt("Credits");
            int teacherId = resultSet.getInt("teacher_id");

            List<Integer> studentList = new ArrayList<>();
            String queryStudentsEnrolled = String.format("SELECT student_id FROM school.studentsenrolledtocourse WHERE course_id=%2d",courseId);
            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(queryStudentsEnrolled);
            while(resultSet1.next()){
                studentList.add(resultSet1.getInt("student_id"));
            }
            statement1.close();
            courses.add(new Course(courseId, name, maxEnrollment, totalCredits, teacherId, studentList));

        }
        statement.close();
        connection.close();
        return courses;
    }



    @Override
    public void update(Course obj) throws SQLException{
        Connection connection = DriverManager.getConnection(db_URL, user, password);
        Statement statement = connection.createStatement();

        String queryUpdate = String.format("UPDATE school.course SET Name =\"%s\", MaxEnrollment =%2d, Credits=%2d, teacher_id=%2d WHERE Course_id =%2d ",
        obj.getName(), obj.getMaxEnrollment(), obj.getCredits(), obj.getTeacherId(), obj.getCourseId());


        statement.execute(queryUpdate);

        List<Integer> updatedStudents = obj.getStudentsEnrolled();

        String queryEnrollment = String.format("SELECT student_id FROM school.studentsenrolledtocourse WHERE course_id= " +obj.getCourseId());
        Statement statement1 = connection.createStatement();
        ResultSet enrolledStudents = statement1.executeQuery(queryEnrollment);

        while(enrolledStudents.next()){
            int studentId = enrolledStudents.getInt("student_id");
            if(!updatedStudents.contains(studentId)){
                Statement statement2 = connection.createStatement();
                statement2.execute(String.format("DELETE FROM school.studentsenrolledtocourse WHERE student_id= " +studentId + "AND course_id= " + obj.getCourseId()));
                statement2.close();


            }else{
                updatedStudents.remove(studentId);
                }
        }
        if(!updatedStudents.isEmpty()){
            Statement statement3 = connection.createStatement();
            statement3.execute(String.format("INSERT INTO school.studentsenrolledtocourse(student_id, course_id) VALUES" + updatedStudents.get(0), obj.getCourseId()));
            statement3.close();
        }

        statement.close();
        connection.close();

    }

    @Override
    public void delete(Course obj) throws SQLException {
        Connection connection = DriverManager.getConnection(db_URL, user, password);
        Statement statement = connection.createStatement();

        //delete all students from the course
        String queryDeleteStudents = String.format("DELETE FROM school.studentsenrolledtocourse " +
                "WHERE course_id = " + obj.getCourseId());
        statement.execute(queryDeleteStudents);

        //delete the course
        Statement statement1 = connection.createStatement();
        String queryDeleteCourse = String.format("DELETE FROM school.course " +
                "WHERE Course_id= " + obj.getCourseId());
        statement1.execute(queryDeleteCourse);

        statement.close();
        connection.close();

    }


}
