package pages;

import dal.students.StudentDAO;
import models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StudentPage extends JFrame {
    // DAO FOR STUDENT DATABASE OPERATIONS
    private final StudentDAO studentDao = new StudentDAO();

    // SWING COMPONENTS FOR UI
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField studentNumberField;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField programField;
    private final JSpinner levelSpinner;

    // BUTTONS FOR CRUD OPERATIONS AND LOGOUT
    private final JButton addButton, updateButton, clearButton, searchButton, deleteButton, logOutButton;

    public StudentPage() {
        // SET WINDOW TITLE AND SIZE
        setTitle("Student CRUD");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));  // MAIN LAYOUT MANAGER

        // CREATE FORM PANEL USING GRIDBAGLAYOUT FOR FORM FIELDS
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);       // PADDING AROUND COMPONENTS
        gbc.fill = GridBagConstraints.HORIZONTAL;  // FIELDS WILL EXPAND HORIZONTALLY

        // INITIALIZE FORM FIELDS
        studentNumberField = new JTextField(15);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        programField = new JTextField(15);
        levelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1)); // LEVEL 1-5

        // ADD FORM ROWS USING HELPER METHOD
        int row = 0;
        addFormRow(formPanel, gbc, row++, "Student Number:", studentNumberField);
        addFormRow(formPanel, gbc, row++, "First Name:", firstNameField);
        addFormRow(formPanel, gbc, row++, "Last Name:", lastNameField);
        addFormRow(formPanel, gbc, row++, "Program:", programField);
        addFormRow(formPanel, gbc, row++, "Level:", levelSpinner);

        // CREATE BUTTON PANEL AND ADD CRUD + LOGOUT BUTTONS
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        clearButton = new JButton("Clear");
        searchButton = new JButton("Search");
        deleteButton = new JButton("Delete");
        logOutButton = new JButton("Log Out");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(logOutButton);

        // ADD FORM PANEL AND BUTTON PANEL TO TOP OF WINDOW
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // INITIALIZE TABLE MODEL AND TABLE FOR DISPLAYING STUDENTS
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Student Number", "First Name", "Last Name", "Program", "Level"}, 0
        );
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);  // ADD TABLE TO CENTER OF WINDOW

        // LOAD STUDENTS FROM DATABASE INTO TABLE
        loadStudents();

        // SETUP BUTTON ACTION LISTENERS
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        clearButton.addActionListener(e -> clearStudent());
        searchButton.addActionListener(e -> searchStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        logOutButton.addActionListener(e -> logOut());

        // WHEN A TABLE ROW IS CLICKED, POPULATE FORM FIELDS WITH SELECTED STUDENT DATA
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                studentNumberField.setText(tableModel.getValueAt(row, 1).toString());
                firstNameField.setText(tableModel.getValueAt(row, 2).toString());
                lastNameField.setText(tableModel.getValueAt(row, 3).toString());
                programField.setText(tableModel.getValueAt(row, 4).toString());
                levelSpinner.setValue((int) tableModel.getValueAt(row, 5));
                updateClearButtonState();   // UPDATE CLEAR BUTTON STATE WHEN LOADING FROM TABLE
            }
        });

        // ADD LISTENERS TO TRACK CHANGES IN FIELDS
        addFieldListeners();
        updateClearButtonState();  // INITIALIZE CLEAR BUTTON STATE

        // CENTER WINDOW ON SCREEN AND SHOW IT
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // HELPER METHOD TO ADD A LABEL + FIELD TO FORM PANEL AT SPECIFIED ROW
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    // CHECK IF ALL FIELDS ARE EMPTY (OR DEFAULT)
    private boolean areFieldsEmpty() {
        return studentNumberField.getText().trim().isEmpty() &&
                firstNameField.getText().trim().isEmpty() &&
                lastNameField.getText().trim().isEmpty() &&
                programField.getText().trim().isEmpty() &&
                (int) levelSpinner.getValue() == 1;
    }

    // ENABLE OR DISABLE CLEAR BUTTON BASED ON FIELDS
    private void updateClearButtonState() {
        clearButton.setEnabled(!areFieldsEmpty());
    }

    // ADD LISTENERS TO ALL INPUT FIELDS TO TRACK CHANGES
    private void addFieldListeners() {
        DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateClearButtonState();
            }

            public void removeUpdate(DocumentEvent e) {
                updateClearButtonState();
            }

            public void changedUpdate(DocumentEvent e) {
                updateClearButtonState();
            }
        };

        studentNumberField.getDocument().addDocumentListener(docListener);
        firstNameField.getDocument().addDocumentListener(docListener);
        lastNameField.getDocument().addDocumentListener(docListener);
        programField.getDocument().addDocumentListener(docListener);

        levelSpinner.addChangeListener(e -> updateClearButtonState());
    }

    // LOAD ALL STUDENTS FROM DATABASE AND POPULATE TABLE
    private void loadStudents() {
        tableModel.setRowCount(0);  // CLEAR EXISTING ROWS
        List<Student> students = studentDao.getAllStudents();
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getStudentNumber(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getProgram(),
                    student.getLevel()
            });
        }
    }

    // ADD NEW STUDENT BASED ON FORM INPUTS
    private void addStudent() {
        String studentNumber = studentNumberField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String program = programField.getText();
        int level = (int) levelSpinner.getValue();

        // VALIDATE ALL FIELDS ARE FILLED BEFORE ADDING
        if (
                !studentNumber.isEmpty() &&
                !firstName.isEmpty() &&
                !lastName.isEmpty() &&
                !program.isEmpty()
        ) {
            studentDao.addStudent(new Student(0, studentNumber, firstName, lastName, program, level));
            loadStudents();  // REFRESH TABLE
            clearFields();   // CLEAR FORM FIELDS
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        }
    }

    // UPDATE SELECTED STUDENT WITH CURRENT FORM VALUES
    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row != -1) {  // ENSURE A ROW IS SELECTED
            int id = (int) tableModel.getValueAt(row, 0);
            String studentNumber = studentNumberField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String program = programField.getText();
            int level = (int) levelSpinner.getValue();

            // VALIDATE ALL FIELDS ARE FILLED BEFORE UPDATING
            if (
                    !studentNumber.isEmpty() &&
                    !firstName.isEmpty() &&
                    !lastName.isEmpty() &&
                    !program.isEmpty()
            ) {
                studentDao.updateStudent(new Student(id, studentNumber, firstName, lastName, program, level));
                loadStudents();  // REFRESH TABLE
                clearFields();   // CLEAR FORM FIELDS
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }
        }
    }

    // CLEAR ALL FORM FIELDS
    private void clearStudent() {
        studentNumberField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        programField.setText("");
        levelSpinner.setValue(1);
        updateClearButtonState();  // UPDATE CLEAR BUTTON STATE AFTER CLEARING
    }

    private void clearFields() {
        studentNumberField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        programField.setText("");
        levelSpinner.setValue(1);
    }

    private void filterStudents(String keyword) {
        List<Student> allStudents = studentDao.getAllStudents();
        tableModel.setRowCount(0); // CLEAR EXISTING ROWS BEFORE ADDING FILTERED RESULTS

        for (Student student : allStudents) {  // SHOWS THE NEW TABLE WITH FILTERED RESULTS
            if (student.getStudentNumber().toLowerCase().contains(keyword) ||
                student.getFirstName().toLowerCase().contains(keyword) ||
                student.getLastName().toLowerCase().contains(keyword) ||
                student.getProgram().toLowerCase().contains(keyword)) {
                tableModel.addRow(new Object[]{
                        student.getId(),
                        student.getStudentNumber(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getProgram(),
                        student.getLevel()
                });
            }
        }
    }

    private void searchStudent() {
        String keyword = JOptionPane.showInputDialog(
                this,
                "Enter keyword to search (Student Number, First Name, Last Name, Program):",
                "Search Students",
                JOptionPane.PLAIN_MESSAGE
        );

        if (keyword == null || keyword.trim().isEmpty()) {
            loadStudents();  // RELOAD ALL STUDENTS TO RESET THE TABLE
            return;
        }

        filterStudents(keyword.trim().toLowerCase());  // OTHERWISE FILTER BY KEYWORD
    }

    // DELETE SELECTED STUDENT FROM DATABASE
    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            studentDao.deleteStudent(id);
            loadStudents();  // REFRESH TABLE AFTER DELETE
            clearFields();   // CLEAR FORM FIELDS
        }
    }

    // LOGOUT USER AND RETURN TO LOGIN PAGE WITH CONFIRMATION DIALOG
    private void logOut() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to log out?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();                         // CLOSE CURRENT WINDOW
            new LoginPage().setVisible(true);  // SHOW LOGIN PAGE
        }
    }
}
