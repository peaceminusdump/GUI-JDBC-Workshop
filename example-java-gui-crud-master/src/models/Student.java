package models;

public class Student {
    // STUDENT ID (PRIMARY KEY)
    private int id;
    // STUDENT NUMBER (UNIQUE IDENTIFIER)
    private String studentNumber;
    // STUDENT FIRST NAME
    private String firstName;
    // STUDENT LAST NAME
    private String lastName;
    // STUDENT PROGRAM (E.G., COURSE OR MAJOR)
    private String program;
    // STUDENT LEVEL (E.G., YEAR OR GRADE LEVEL)
    private int level;

    // NO-ARGUMENT CONSTRUCTOR FOR DEFAULT INSTANTIATION
    public Student() {}

    // FULL CONSTRUCTOR TO INITIALIZE ALL FIELDS
    public Student(
            int id,
            String studentNumber,
            String firstName,
            String lastName,
            String program,
            int level
    ) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.program = program;
        this.level = level;
    }

    // GETTER AND SETTER METHODS FOR ALL FIELDS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
