package registrationSystem;

import exceptions.*;
import model.Course;
import model.Student;
import  org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The class tests all methods from Registrationsystem
 */
class RegistrationSystemTest {
    private RegistrationSystem registrationSystem;

    /**
     * The method tests the connection to the dataBase and deletes everything from the tables
     */
    @BeforeEach
    void setUp(){
        registrationSystem = new RegistrationSystem("jdbc:mysql://localhost:3306/school", "root", "SimoInfo15");

        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "SimoInfo15");
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM studentsenrolledtocourse");
            Statement statement1 = connection.createStatement();
            statement1.execute("DELETE FROM student");
            Statement statement2 = connection.createStatement();
            statement2.execute("DELETE FROM course");
            Statement statement3 = connection.createStatement();
            statement3.execute("DELETE FROM teacher");
        } catch(SQLException e){
            Assertions.fail();
        }

        //adding students

        try {
            registrationSystem.addStudent("Andreea", "Pop", 1);
        } catch(AlreadyExistsException | SQLException e){
            Assertions.fail();
        }

        try {
            registrationSystem.addStudent("Darius", "Morar",2);
        }catch(AlreadyExistsException | SQLException e){
            Assertions.fail();
        }

        try {
            registrationSystem.addStudent("Bianca", "Popescu", 3);
        }catch(AlreadyExistsException |SQLException e){
            Assertions.fail();
        }

        try {
            registrationSystem.addStudent("Maria", "Ionescu", 4);
        }catch(AlreadyExistsException | SQLException e){
            Assertions.fail();
        }

        try{
            registrationSystem.addStudent("Elena", "Cretu", 5);
        }catch(AlreadyExistsException | SQLException e){
            Assertions.fail();
        }



        //adding teachers
        try{
            registrationSystem.addTeacher("Marina", "Rus", 1);
        }catch(AlreadyExistsException | SQLException e){
            Assertions.fail();
        }

        try{
            registrationSystem.addTeacher("Dragos", "Petrescu", 2);
        }catch(AlreadyExistsException | SQLException e){
            Assertions.fail();
        }

        try{
            registrationSystem.addTeacher("Christian", "Diaconescu", 3);
        }catch(AlreadyExistsException | SQLException e){
            Assertions.fail();
        }


        //adding courses
        try{
            registrationSystem.addCourse(1, "Algebra", 10, 6, 1);
        }catch(AlreadyExistsException | ExceptionNotFound |SQLException e){
            Assertions.fail();
        }

        try{
            registrationSystem.addCourse(2, "Geometrie", 4, 5, 2);
        }catch(AlreadyExistsException | ExceptionNotFound | SQLException e){
            Assertions.fail();
        }

        try{
            registrationSystem.addCourse(3, "Sisteme de operare", 10, 35, 3);
        }catch(AlreadyExistsException | ExceptionNotFound | SQLException e){
            Assertions.fail();
        }

        try{
            registrationSystem.addCourse(4, "Statistica", 15, 4, 2);
        }catch(AlreadyExistsException | ExceptionNotFound | SQLException e){
            Assertions.fail();
        }


    }


    /**
     * The method tests the register method from RegistrationSystem
     */
    @Test
    void register() {
        //3 rd course has to many credits
        try {
            for (Student student : registrationSystem.retrieveAllStudents()) {
                try {
                    registrationSystem.register(student.getStudentId(), 3);
                } catch (AlreadyExistsException | ExceptionLimitReached | ExceptionNotFound e) {
                    Assertions.fail();
                } catch (ExceptionMaximCredits e) {
                    Assertions.assertTrue(true);
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }

        //the course has the maximum number of students and the last student could not join the course
        try {
            for (Student student : registrationSystem.retrieveAllStudents()) {
                try {
                    registrationSystem.register(student.getStudentId(), 2);
                } catch (ExceptionNotFound | AlreadyExistsException | ExceptionMaximCredits e) {
                    Assertions.fail();
                } catch (ExceptionLimitReached e) {
                    if (student.getStudentId() == 5) {
                        Assertions.assertTrue(true);
                    }
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }

        //All students join the course
        try {
            for (Student student : registrationSystem.retrieveAllStudents()) {
                try {
                    registrationSystem.register(student.getStudentId(), 1);
                } catch (ExceptionNotFound | AlreadyExistsException | ExceptionMaximCredits | ExceptionLimitReached e) {
                    Assertions.fail();
                }
            }
        } catch (SQLException e) {
            Assertions.fail();
        }

    }


    /**
     * The method tests the retrieveCoursesWithFreePlaces method from RegistrationSystem
     */
    @Test
    void retrieveCoursesWithFreePlaces() {

            try{
                for(Student student :registrationSystem.retrieveAllStudents()){
                    try{
                        registrationSystem.register(student.getStudentId(), 2);
                    }catch(ExceptionMaximCredits | ExceptionNotFound | AlreadyExistsException e){
                        Assertions.fail();
                    }catch(ExceptionLimitReached e){
                        if(student.getStudentId() != 5){
                            Assertions.fail();
                        }
                    }
                }
            }catch(SQLException e){
                Assertions.fail();
            }

            //there are 3 courses with free places

        try {
            Assertions.assertEquals(3, registrationSystem.retrieveCoursesWithFreePlaces().size());
            }catch(SQLException e){
                Assertions.fail();
            }
        }


    /**
     * Retrieve students enrolled for a course.
     */
    @Test
    void retrieveStudentsEnrolledForACourse() {
            //all students join the course
        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                try {
                    registrationSystem.register(student.getStudentId(), 1);
                }catch(ExceptionNotFound | AlreadyExistsException | ExceptionMaximCredits | ExceptionLimitReached e){
                    Assertions.fail();
                }
            }
        }catch(SQLException e){
            Assertions.fail();
        }

        //check if the return is true
            List<Student> studentsEnrolled = new ArrayList<>();
            try {
                studentsEnrolled = registrationSystem.retrieveStudentsEnrolledForACourse(1);
            }catch(SQLException | ExceptionNotFound e){
                Assertions.fail();
            }


            try {
                for (Student student : registrationSystem.retrieveAllStudents()){
                    boolean contained = false;
                    for (Student studentEnrolled : studentsEnrolled){
                        if(student.getStudentId() == studentEnrolled.getStudentId()){
                            contained = true;
                            break;
                        }
                    }
                    if(!contained){
                        Assertions.fail();
                    }
                }
            }catch(SQLException e){
                Assertions.fail();
            }

            try{
                Assertions.assertTrue(registrationSystem.retrieveStudentsEnrolledForACourse(3).isEmpty());
            }catch(SQLException | ExceptionNotFound e){
                Assertions.fail();
            }

        }


    /**
     * The method tests the deleteCourseByTeacher method from RegistrationSystem
     */
    @Test
    void deleteCourseByTeacher() {
        try {
            for(Student student : registrationSystem.retrieveAllStudents()){
                try{
                    registrationSystem.register(student.getStudentId(), 1);
                }catch(ExceptionNotFound | ExceptionMaximCredits | ExceptionLimitReached | AlreadyExistsException e){
                    Assertions.fail();
                }
            }
        }catch(SQLException e){
            Assertions.fail();
        }



        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                Assertions.assertNotEquals(0, student.getCoursesNumber());
            }
        }catch(SQLException e){
            Assertions.fail();
        }


        //teacher deletes the course
        try {
            registrationSystem.deleteCourseByTeacher(2, 4);
        }catch (ExceptionNotFound | ExceptionNotTeaching | SQLException e) {
            Assertions.fail();
        }

        try{
            for(Student student : registrationSystem.retrieveAllStudents()){
                Assertions.assertEquals(0, student.getCoursesNumber());
            }
        }catch(SQLException e){
            Assertions.fail();
        }


        try {
            registrationSystem.deleteCourseByTeacher(1, 6);
            Assertions.fail();
        }catch (ExceptionNotFound | ExceptionNotTeaching e){
            Assertions.assertTrue(true);
        }catch (SQLException e){
            Assertions.fail();
        }

        //Both exists, but the teacher is not teaching the course
        try {
            registrationSystem.deleteCourseByTeacher(2, 3);
            Assertions.fail();
        }catch (ExceptionNotFound | SQLException e){
            Assertions.fail();
        }catch(ExceptionNotTeaching e){
            Assertions.assertTrue(true);
        }

    }

    /**
     * The method tests the addTeacher method from RegistrationSystem
     */
    @Test
    void addTeacher() {
        //already exists
        try {
            registrationSystem.addTeacher("Darius", "Morar", 2);
            Assertions.fail();
        }catch (AlreadyExistsException e){
            Assertions.assertTrue(true);
        } catch (SQLException e){
            Assertions.fail();
        }

        //added
        try {
            registrationSystem.addTeacher("Madalina", "Oprea", 4);
        }catch(AlreadyExistsException | SQLException e){
            Assertions.fail();
        }
    }


    /**
     * The method tests the addStudent method from RegistrationSystem
     */
    @Test
    void addStudent() {
        //already exists
        try {
            registrationSystem.addStudent("Maria", "Ionescu", 4);
            Assertions.fail();
        }catch(AlreadyExistsException | SQLException e){
            Assertions.assertTrue(true);
        }

        //added
        try {
            registrationSystem.addStudent("Tudor", "Gavriliu", 6);
        }catch(AlreadyExistsException |SQLException e){
            Assertions.fail();
        }
    }


    /**
     * The method tests the addCourse method from the RegistrationSystem
     */
    @Test
    void addCourse() {
        //added
        try {
            registrationSystem.addCourse(5, "Baze de date", 20, 6, 2);
        }catch(AlreadyExistsException | ExceptionNotFound | SQLException e){
            Assertions.fail();
        }

        //teacher exists, but course already exits
        try {
            registrationSystem.addCourse(5, "Baze de date", 20, 6, 2 );
            Assertions.fail();
        }catch(AlreadyExistsException e){
            Assertions.assertTrue(true);
        }catch(ExceptionNotFound | SQLException e){
            Assertions.fail();
        }

        try {
            registrationSystem.addCourse(10, "Baze de date", 20, 6 , 10);
            Assertions.fail();
        }catch (AlreadyExistsException | SQLException e){
            Assertions.fail();
        }catch(ExceptionNotFound e){
            Assertions.assertTrue(true);
        }
    }

    /**
     * The method tests the sortCourse method from RegistrationSystem
     */
    @Test
    void sortCourse() {
        List<Course> coursesList = new ArrayList<>();
        try {
            coursesList = registrationSystem.sortCourse();
        }catch (SQLException e){
            Assertions.fail();
        }

        Assertions.assertEquals(1, coursesList.get(0).getCourseId());
        Assertions.assertEquals(2, coursesList.get(1).getCourseId());
        Assertions.assertEquals(3, coursesList.get(2).getCourseId());
        Assertions.assertEquals(4, coursesList.get(3).getCourseId());


    }

    /**
     * The method tests the filterCourse from Registrationsystem
     */
    @Test
    void filterCourse() {
        List<Course> courseList = new ArrayList<>();
        try {
            courseList = registrationSystem.filterCourse();
        }catch (SQLException e){
            Assertions.fail();
        }

        Assertions.assertEquals(2, courseList.size());
        Assertions.assertEquals(2, courseList.get(0).getCourseId());
        Assertions.assertEquals(4, courseList.get(1).getCourseId());


    }

    /**
     * The method tests the sortStudenten method from RegistrationSystem
     */
    @Test
    void sortStudenten() {
        List<Student> studentList = new ArrayList<>();
        try {
            studentList = registrationSystem.sortStudenten();
        }catch (SQLException e){
            Assertions.fail();
        }

        Assertions.assertEquals(1, studentList.get(0).getStudentId());
        Assertions.assertEquals(3, studentList.get(1).getStudentId());
        Assertions.assertEquals(2, studentList.get(2).getStudentId());
        Assertions.assertEquals(5, studentList.get(3).getStudentId());
        Assertions.assertEquals(4, studentList.get(4).getStudentId());



    }

    /**
     * The method tests the filterStudents method from RegistrationSystem
     */
    @Test
    void filterStudents() {
        List<Course> studentsList = new ArrayList<>();
        try {
            studentsList = registrationSystem.filterCourse();
        }catch (SQLException e){
            Assertions.fail();
        }

        try{
            registrationSystem.register(1,3);
        }catch (SQLException | ExceptionMaximCredits | ExceptionNotFound | AlreadyExistsException e){
            Assertions.fail();
        }
        Assertions.assertEquals(4, studentsList.size());

    }


}