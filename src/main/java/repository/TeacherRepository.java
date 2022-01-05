package repository;

import model.Teacher;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository implements ICrudRepository<Teacher>{

    private String db_URL; // = "jdbc:mysql://localhost:3306";
    private String user; // = "root";
    private String password; // = "SimoInfo15";

    public TeacherRepository(String db_URL, String user, String password){
        this.db_URL = db_URL;
        this.user = user;
        this.password = password;
    }

    @Override
    public void create(Teacher obj) throws  SQLException{
        Connection connection = DriverManager.getConnection(db_URL,user,password);
            String query = "INSERT INTO school.teacher(Teacher_id, FirstName, LastName) VALUES("+"'"+obj.getTeacherId()+"'" + "," + "'"+obj.getFirstName()+"'"+ ","+ "'"+obj.getLastName()+"'"+")";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        statement.close();
        connection.close();

    }



    @Override
    public List<Teacher> getAll() throws SQLException{
        Connection connection = DriverManager.getConnection(db_URL, user, password);
        Statement statement = connection.createStatement();

        List<Teacher> teachers = new ArrayList<>();

        String queryTeachers = "SELECT * FROM teacher";
        ResultSet resultSet = statement.executeQuery(queryTeachers);
        while(resultSet.next()){
            int teacherId = resultSet.getInt("Teacher_id");
            String firstName = resultSet.getString("FirstName");
            String lastName = resultSet.getString("LastName");

            List<Integer> courses = new ArrayList<>();
            String queryTeacherCourse = "SELECT course.Course_id FROM school.course " +
                    "INNER JOIN school.teacher ON course.teacher_id=teacher.Teacher_id WHERE course.teacher_id=" +teacherId;
            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(queryTeacherCourse);
            while(resultSet1.next()){
                courses.add(resultSet1.getInt("course_id"));

            }
            statement1.close();
            teachers.add(new Teacher(firstName, lastName, courses, teacherId));
        }
        statement.close();
        connection.close();
        return teachers;


    }




    @Override
    public void update(Teacher obj) throws SQLException{
        Connection connection = DriverManager.getConnection(db_URL, user, password);
            String query = "UPDATE school.teacher " +
                    "SET Name =" +obj.getFirstName() +
                    ",LastName =" +obj.getLastName() +
                    "WHERE Teacher_id =" + obj.getTeacherId();
            Statement statement = connection.createStatement();
            statement.executeQuery(query);
        statement.close();
        connection.close();

    }

    @Override
    public void delete(Teacher obj) throws SQLException {
        Connection connection = DriverManager.getConnection(db_URL, user, password);
        Statement statement = connection.createStatement();

        String query = "DELETE FROM school.studentsenrolledtocourse enrolled" +
                "INNER JOIN course ON course.Course_id = enrolled.course_id" +
                "WHERE course.teacher= " + obj.getTeacherId();
        statement.executeQuery(query);
        statement.close();

        String queryDeleteCourses = "DELETE FROM course " +
                "WHERE teacher= " + obj.getTeacherId();
        Statement statement1 = connection.createStatement();
        statement1.executeQuery(queryDeleteCourses);
        statement1.close();

        String queryDeleteTeacher = "DELETE FROM teacher WHERE Teacher_id= " + obj.getTeacherId();
        Statement statement2 = connection.createStatement();
        statement2.executeQuery(queryDeleteTeacher);
        statement2.close();

        connection.close();
    }
}
