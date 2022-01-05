package repository;

import model.Student;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements ICrudRepository<Student> {


    private final String db_URL; //= "jdbc:mysql://localhost:3306";
    private final String user; //= "root";
    private final String password;  //= "SimoInfo15";

    public StudentRepository(String db_URL, String user, String password){
        this.db_URL = db_URL;
        this.user = user;
        this.password = password;
    };


    @Override
    public void create(Student obj) throws SQLException{
        Connection connection = DriverManager.getConnection(db_URL, user, password);
        Statement statement = connection.createStatement();

            String queryInsert = String.format("INSERT INTO school.student(Student_id, Name, LastName) Values  ( %2d, \"%s\", \"%s\")", obj.getStudentId(), obj.getFirstName(), obj.getLastName());               //(" + "'" + obj.getStudentId() + "'" + "," + "'" + obj.getFirstName() + "'" + "," + "'" + obj.getLastName() + "'" + ")");// "," + "'" + obj.getTotalCredits() + "'" + ")";
            statement.execute(queryInsert);

            statement.close();;
            connection.close();

    }


    @Override
    public List<Student> getAll() throws SQLException{
        Connection connection = DriverManager.getConnection(db_URL, user, password);
        Statement statement = connection.createStatement();

        List<Student> students = new ArrayList<>();

        String querySelectStudents = "SELECT * FROM school.student";
        ResultSet resultSet = statement.executeQuery(querySelectStudents);
        while(resultSet.next()){
            String FirstName = resultSet.getString("Name");
            String LastName = resultSet.getString("LastName");
            int studentId = resultSet.getInt("Student_id");

            List<Integer> courses = new ArrayList<>();
            String queryCoursesEnrolled = "SELECT course_id FROM school.studentsenrolledtocourse " +
                    "WHERE student_id= " + studentId;
            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(queryCoursesEnrolled);
            while(resultSet1.next()){
                courses.add(resultSet1.getInt("course_id"));
            }
            statement1.close();

            students.add(new Student(FirstName, LastName, courses, studentId));
        }
        statement.close();
        connection.close();
        return students;


    }



    @Override
    public void update(Student obj) throws SQLException{
        Connection connection = DriverManager.getConnection(db_URL, user, password);
        String query = "UPDATE school.student " +
                    "SET Name =" +obj.getFirstName()+ ",LastName= " +obj.getLastName()+
                    "WHERE Student_id = " + obj.getStudentId();
        Statement statement = connection.createStatement();
        statement.executeQuery(query);

        List<Integer> updatedEnrolledCourses = obj.getEnrolledCourses();

        String queryEnrollment = "SELECT course_id FROM studentsenrolledtocourse" +
                "WHERE student_id= " +obj.getStudentId();
        Statement statement1 = connection.createStatement();
        ResultSet resultSet = statement1.executeQuery(queryEnrollment);

        while(resultSet.next()){
            int courseId = resultSet.getInt("course_id");
            if(updatedEnrolledCourses.contains(courseId)){
                updatedEnrolledCourses.remove(courseId);
            }else{
                Statement statement2 = connection.createStatement();
                String query1 = "DELETE FROM school.studentsenrolledtocourse" +
                        "WHERE student_id= " + obj.getStudentId() + "AND course_id= " +courseId;

                statement2.executeQuery(query1);
                statement2.close();
            }
        }

        if(!updatedEnrolledCourses.isEmpty()){
            Statement statement3 = connection.createStatement();
            statement3.execute(String.format("INSERT INTO studentsenrolledtocourse(student_id, course_id) VALUES"+obj.getStudentId(), updatedEnrolledCourses.get(0)));
            statement3.close();
        }
        statement.close();
        connection.close();

    }

    @Override
    public void delete(Student obj) throws SQLException{
        Connection connection = DriverManager.getConnection(db_URL, user, password);
        Statement statement = connection.createStatement();

            String queryDeleteEnrollment = "DELETE FROM school.studentsenrolledtocourse" +
                    "WHERE student_id = " +obj.getStudentId();
            statement.executeQuery(queryDeleteEnrollment);

            String queryDeleteStudent = "DELETE FROM student " +
                    "WHERE Student_id= " + obj.getStudentId();
            Statement statement1 = connection.createStatement();

            statement.close();
            connection.close();

    }
}
