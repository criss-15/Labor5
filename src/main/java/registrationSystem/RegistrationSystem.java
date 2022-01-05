package registrationSystem;


import exceptions.*;
import model.Course;
import model.Student;
import model.Teacher;
import repository.CourseRepository;
import repository.ICrudRepository;
import repository.StudentRepository;
import repository.TeacherRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationSystem {

    private ICrudRepository<Course> courseRepository;
    private ICrudRepository<Teacher> teacherRepository;
    private ICrudRepository<Student> studentRepository;


    public RegistrationSystem(String bd_url, String user, String password) {
        courseRepository = new CourseRepository(bd_url, user, password);
        teacherRepository = new TeacherRepository(bd_url, user, password);
        studentRepository = new StudentRepository(bd_url, user, password);
    }


    /**
     * The method register a student to a course.
     *
     * @param studentId the student id
     * @param courseId  the course id
     * @throws ExceptionLimitReached  the exception limit reached
     * @throws ExceptionMaximCredits  the exception maxim credits
     * @throws SQLException           the sql exception
     * @throws ExceptionNotFound      the exception not found
     * @throws AlreadyExistsException the already exists exception
     */
    public void register(int studentId, int courseId) throws ExceptionLimitReached, ExceptionMaximCredits, SQLException, ExceptionNotFound, AlreadyExistsException {
        int studentIdx = -1;

        for(int i=0; i < studentRepository.getAll().size(); i++) {
            if (studentRepository.getAll().get(i).getStudentId() == studentId) {
                studentIdx = i;
                break;
            }
        }
        int courseIdx = -1;
        for(int i=0; i < courseRepository.getAll().size(); i++){
            if(courseRepository.getAll().get(i).getCourseId() == courseId){
                courseIdx = i;
                break;
            }
        }

        if((courseIdx == -1) || (studentIdx == -1)){
            throw new ExceptionNotFound("The student or course does not exist!");
        }

        Student student = studentRepository.getAll().get(studentIdx);
        Course course = courseRepository.getAll().get(courseIdx);

        if(course.getStudentsEnrolled().contains(studentId)){
            throw new AlreadyExistsException("Student is already registered to the course!");
        }

        if(totalCreditsOfaStudent(student) >= 30){
            throw new ExceptionMaximCredits("The student can not register to the course because the maxim credits number is reached!");
        }

        if(course.getMaxEnrollment() <= course.getStudentsEnrolled().size()){
            throw new ExceptionLimitReached("The maximum number of students for the course is reached!");
        }

        course.addStudent(studentId);
        student.addCourse(courseId);

        courseRepository.update(course);
        studentRepository.update(student);
    }




    public int totalCreditsOfaStudent(Student student) throws SQLException{
        int totalCredits = 0;
        for(int courseId : student.getEnrolledCourses()){
            for(Course course : courseRepository.getAll()){
                if(course.getCourseId() == courseId){
                    totalCredits += course.getCredits();
                }
            }
        }
        return totalCredits;

    }


    public List<Course> retrieveCoursesWithFreePlaces() throws SQLException{
        List<Course> coursesFreePlaces = new ArrayList<>();
        for(Course course : courseRepository.getAll()){
            if(course.getMaxEnrollment() > course.getStudentsEnrolled().size()){
                coursesFreePlaces.add(course);
            }
        }
        return coursesFreePlaces;
    }

    public List<Student> retrieveStudentsEnrolledForACourse(int courseId) throws SQLException, ExceptionNotFound {
        List<Student> studentsEnrolled = new ArrayList<>();
        for(Student student : studentRepository.getAll()){
            if(student.getEnrolledCourses().contains(courseId)){
                studentsEnrolled.add(student);
            }
        }
        return studentsEnrolled;
    }



    public List<Course> getAllCourses() throws SQLException{
        return courseRepository.getAll();
    }


    public void deleteCourseByTeacher(int teacherID, int courseID) throws  SQLException, ExceptionNotFound, ExceptionNotTeaching {
        int teacherIndex = -1;

        for (int i = 0; i < teacherRepository.getAll().size(); i++) {
            if (teacherRepository.getAll().get(i).getTeacherId() == teacherID) {
                teacherIndex = i;
                break;
            }

        }

        int courseIndex = -1;
        for (int i = 0; i < courseRepository.getAll().size(); i++) {
            if (courseRepository.getAll().get(i).getCourseId() == courseID) {
                courseIndex = i;
                break;

            }
        }


            if((courseIndex == -1) || (teacherIndex == -1)){
                throw new ExceptionNotFound("The course or teacher does not exist!");
            }

            Course course = courseRepository.getAll().get(courseIndex);
            Teacher teacher = teacherRepository.getAll().get(teacherIndex);

            if (course.getTeacherId() != teacherID) {
                throw new ExceptionNotTeaching("The teacher does not teach this course!");
            }

            for (int studentID : course.getStudentsEnrolled()) {
                for (Student student : studentRepository.getAll()) {
                    if (student.getStudentId() == studentID) {
                        student.deleteCourse(courseID);
                        studentRepository.update(student);

                    }
                }

            }
            courseRepository.delete(course);
        }





    public void addTeacher(String firstName, String lastName, int teacherId) throws AlreadyExistsException, SQLException{
            for(Teacher teacher : teacherRepository.getAll()){
                if(teacher.getTeacherId() != teacherId){
                    teacherRepository.create(new Teacher(firstName, lastName, new ArrayList<>(), teacherId));

                }else{
                    throw new AlreadyExistsException("The teacher already exists!");
                }
            }
    }


    public void addStudent(String firstName, String lastName, int studentID) throws AlreadyExistsException, SQLException{
            for(Student student : studentRepository.getAll()){
                if(student.getStudentId() != studentID){
                    studentRepository.create(new Student(firstName, lastName, new ArrayList<>(),studentID));
                }else{
                    throw new AlreadyExistsException("The student already exists!");
                }
            }
    }


    public List<Student> retrieveAllStudents() throws SQLException{
        return studentRepository.getAll();
    }


    public List<Teacher> retrieveAllTeachers() throws SQLException {
        return teacherRepository.getAll();
    }



    public void addCourse(int courseId, String name, int maxEnrollment, int credits, int teacherId) throws AlreadyExistsException, ExceptionNotFound, SQLException {
        int teacherIndex = 0;
        for(Course course : courseRepository.getAll()){
            if(course.getCourseId() == courseId){
                throw new AlreadyExistsException("Course already exists!");
            }
        }
        for(int i=0; i < teacherRepository.getAll().size(); i++){
            if(teacherRepository.getAll().get(i).getTeacherId() == teacherId){
                teacherIndex = i;
            }else{
                throw new ExceptionNotFound("The teacher does not exist!");
            }

        }
        courseRepository.create(new Course(courseId, name, maxEnrollment, credits, teacherId, new ArrayList<>()));

    }


    public List<Course> sortCourse() throws SQLException { //sorteaza cursurile dupa nume, iar daca au numele la fel, le sorteaza dupa nr de locuri libere
        List<Course> courseList=courseRepository.getAll();
        Comparator<Course> courseComparator = Comparator.comparing(Course::getName);
        return courseList.stream().sorted(courseComparator).toList();

    }




    public List<Course> filterCourse() throws SQLException { //afiseaza cursurile cu mai putin de 6 credite
        List<Course> courses=courseRepository.getAll();
        courses = courses.stream()
                .filter( (Course course) ->
                        course.getCredits() < 6)
                .collect(Collectors.toList());
        return courses;
    }





    public List<Student> sortStudenten() throws SQLException { //sorteaza studentii dupa nume, iar daca au numele la fel, le sorteaza dupa prenume
        List<Student> studentList = studentRepository.getAll();
        Comparator<Student> studentComparator = Comparator.comparing(Student::getFirstName);
        return studentList.stream().sorted(studentComparator).toList();
    }



    public List<Student> filterStudents() throws SQLException {
        List<Student> studentsList = studentRepository.getAll();
        return studentsList.stream().filter(student -> student.getEnrolledCoursesSize() > 0).toList();
    }


}