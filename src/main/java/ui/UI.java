package ui;

import exceptions.*;
import model.Course;
import model.Student;
import model.Teacher;
import registrationSystem.RegistrationSystem;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class UI {

    private RegistrationSystem registrationSystem;
    private Scanner inputScanner;


    /**
     * Constructor for the user interface
     */
    public UI() {
        registrationSystem = new RegistrationSystem("jdbc:mysql://localhost:3306/school", "root", "SimoInfo15");
        inputScanner = new Scanner(System.in);
    }


    /**
     * Starts the application (the option menu)
     */
    public void start(){

        int option = -1;
        while (option != 0) {
            this.showMenu();
            System.out.print("Please choose an Option : ");
            option = inputScanner.nextInt();
            if (option > 13 || option < 0){
                System.out.println("This Option does not exist, please try again !");
            }

            if (option == 1) {
                this.addStudent();
            } else if (option == 2) {
                this.addTeacher();
            } else if (option == 3) {
                this.addCourse();
            } else if (option == 4) {
                this.register();
            } else if (option == 5) {
                this.retrieveFree();
            } else if (option == 6) {
                this.retrieveAll();
            } else if (option == 7) {
                this.teacherDeleteCourse();
            } else if (option == 8) {
                this.showAllTeachers();
            } else if (option == 9) {
                this.showAllStudents();
            } else if (option == 10) {
                this.showStudentsSortedByFirstName();
            } else if (option == 11) {
                this.showCoursesSortedByName();
            } else if (option == 12) {
                this.filterStudentsEnrolled();
            } else if (option == 13) {
                this.filterCoursesWithStudents();
            }
        }

        System.out.println("The Application was closed !\s");
    }


    /**
     * gets from user the input for adding a student
     */
    public void addStudent() {
        inputScanner.nextLine();

        System.out.print("Enter first name : ");
        String firstName = inputScanner.nextLine();

        System.out.print("Enter last name : ");
        String lastName = inputScanner.nextLine();

        System.out.print("Enter id : ");
        int id = inputScanner.nextInt();

        try {
            registrationSystem.addStudent(firstName, lastName, id);
            System.out.println("Student added successfully !");
        } catch (AlreadyExistsException e) {
            System.out.println("Student already exists !");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    /**
     * gets from user the input for adding a teacher
     */
    public void addTeacher(){
        inputScanner.nextLine();

        System.out.print("Enter first name : ");
        String firstName = inputScanner.nextLine();

        System.out.print("Enter last name : ");
        String lastName = inputScanner.nextLine();

        System.out.print("Enter id : ");
        int id = inputScanner.nextInt();

        try {
            registrationSystem.addTeacher(firstName, lastName, id);
            System.out.println("Teacher added successfully !");
        } catch (AlreadyExistsException e) {
            System.out.println("Teacher already exists !");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    /**
     * gets from user the input for adding a course
     */
    public void addCourse(){
        inputScanner.nextLine();

        System.out.print("Enter course name : ");
        String name = inputScanner.nextLine();

        System.out.print("Enter a teacher id (that actually exists) : ");
        int teacherId = inputScanner.nextInt();

        System.out.print("Enter max enrollment : ");
        int maxEnrollment = inputScanner.nextInt();

        System.out.print("Enter number of credits : ");
        int credits = inputScanner.nextInt();

        System.out.print("Enter course id : ");
        int courseId = inputScanner.nextInt();


        try {
            registrationSystem.addCourse(courseId, name,  maxEnrollment, credits, teacherId );
            System.out.println("Course added successfully !");
        } catch (AlreadyExistsException e) {
            System.out.println("Course already exists !");
        } catch (ExceptionNotFound e) {
            System.out.println("That teacher does not exist !");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    /**
     * gets from user the input for registering a student to a course
     */
    public void register(){
        inputScanner.nextLine();

        System.out.print("Enter course id : ");
        int courseId = inputScanner.nextInt();

        System.out.print("Enter student id : ");
        int studentId = inputScanner.nextInt();

        try {
            registrationSystem.register(studentId, courseId);
            System.out.println("Successfully registered to the course !");
        } catch (ExceptionNotFound e) {
            System.out.println("The Course or the Student could not be found !");
        } catch (ExceptionMaximCredits e) {
            System.out.println("Can't perform the operation ! The student will have more than 30 credits !");
        } catch (ExceptionLimitReached e) {
            System.out.println("This course has no available places !");
        } catch (AlreadyExistsException e) {
            System.out.println("Student is already registered to this course !");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    /**
     * shows the courses with free places
     */
    public void retrieveFree(){
        try {
            for (Course course : registrationSystem.retrieveCoursesWithFreePlaces()){
                System.out.println(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * shows all courses
     */
    public void retrieveAll(){
        try {
            for (Course course : registrationSystem.getAllCourses()){
                System.out.println(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * gets from user the input for deleting a course
     */
    public void teacherDeleteCourse(){
        inputScanner.nextLine();

        System.out.print("Enter teacher id : ");
        int teacherId = inputScanner.nextInt();

        System.out.print("Enter course id : ");
        int courseId = inputScanner.nextInt();

        try {
            registrationSystem.deleteCourseByTeacher(teacherId, courseId);
            System.out.println("Course successfully deleted !");
        } catch (ExceptionNotFound e) {
            System.out.println("Course or teacher does not exist !");
        } catch (ExceptionNotTeaching e) {
            System.out.println("The specified teacher is not teaching this course !");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    /**
     * shows all teachers
     */
    public void showAllTeachers(){
        try {
            for (Teacher teacher : registrationSystem.retrieveAllTeachers()){
                System.out.println(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * shows all students
     */
    public void showAllStudents(){
        try {
            for (Student student : registrationSystem.retrieveAllStudents()){
                System.out.println(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * shows all students sorted ascending by their id
     */
    public void showStudentsSortedByFirstName(){
        try {
            for (Student student : registrationSystem.sortStudenten()){
                System.out.println(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * shows all courses sorted alphabetically by their name
     */
    public void showCoursesSortedByName(){
        try {
            for (Course course : registrationSystem.sortCourse()){
                System.out.println(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * filters the students enrolled for at least one course
     */
    public void filterStudentsEnrolled(){
        try {
            for (Student student : registrationSystem.filterStudents()){
                System.out.println(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * filters the courses with at least one student enrolled for
     */
    public void filterCoursesWithStudents(){
        try {
            for (Course course : registrationSystem.filterCourse()){
                System.out.println(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * shows the user menu
     */
    public void showMenu(){
        System.out.print("""
                0. Exit\s
                1. Add student\s
                2. Add teacher\s
                3. Add course\s
                4. Register a student to a course\s
                5. Retrieve courses with free places\s
                6. Retrieve all available courses\s
                7. Delete a Teachers course\s
                8. Show all teachers\s       
                9. Show all students\s
                10. Show students sorted by name\s
                11. Show courses sorted by name\s
                12. Filter students \s
                13. Filter courses 
                """);
    }
}

