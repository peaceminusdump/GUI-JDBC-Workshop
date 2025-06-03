package pages;

import dal.admins.AdminDAO;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    // DAO INSTANCE TO ACCESS ADMIN DATABASE OPERATIONS
    private final AdminDAO adminDao = new AdminDAO();

    // INPUT FIELDS AND BUTTONS
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton logInButton, signUpButton;

    // CONSTRUCTOR TO SET UP THE LOGIN UI
    public LoginPage() {
        setTitle("Admin Login");                  // WINDOW TITLE
        setSize(400, 200);                        // WINDOW SIZE
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // CLOSE APPLICATION ON EXIT
        setLayout(new GridBagLayout());           // USING GRIDBAGLAYOUT FOR FLEXIBLE UI
        GridBagConstraints gbc = new GridBagConstraints();

        // TITLE LABEL SETUP
        JLabel titleLabel = new JLabel("Student Record System Prototype");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;                        // SPAN ACROSS 2 COLUMNS
        gbc.insets = new Insets(10, 10, 20, 10);  // PADDING
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);                     // ADD TITLE TO FRAME

        gbc.gridwidth = 1;                        // RESET GRID WIDTH

        // USERNAME LABEL
        JLabel usernameLabel = new JLabel("Username: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(usernameLabel, gbc);

        // USERNAME TEXT FIELD
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(usernameField, gbc);

        // PASSWORD LABEL
        JLabel passwordLabel = new JLabel("Password: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(passwordLabel, gbc);

        // PASSWORD FIELD
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(passwordField, gbc);

        // BUTTON PANEL WITH LOG IN AND SIGN UP BUTTONS CENTERED
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        logInButton = new JButton("Log In");
        signUpButton = new JButton("Sign Up");
        buttonPanel.add(logInButton);
        buttonPanel.add(signUpButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;  // BUTTONS SPAN ACROSS BOTH COLUMNS
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);
        add(buttonPanel, gbc);

        // ADD ACTION LISTENERS TO BUTTONS
        logInButton.addActionListener(e -> handleLogin());
        signUpButton.addActionListener(e -> handleSignUp());

        setLocationRelativeTo(null);  // CENTER WINDOW ON SCREEN
        setVisible(true);             // SHOW THE WINDOW
    }

    // HANDLES THE LOGIN PROCESS
    private void handleLogin() {
        String username = usernameField.getText().trim();           // GET USERNAME INPUT
        String password = new String(passwordField.getPassword());  // GET PASSWORD INPUT

        // CHECK IF FIELDS ARE EMPTY
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            return;
        }

        // CHECK IF ADMIN EXISTS WITH THE PROVIDED CREDENTIALS
        boolean valid = adminDao.checkIfAdminExists(username, password);
        if (valid) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            new StudentPage();  // OPEN STUDENT PAGE AFTER SUCCESSFUL LOGIN
            dispose();          // CLOSE LOGIN WINDOW
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }

    // HANDLES THE SIGN-UP PROCESS FOR NEW ADMINS
    private void handleSignUp() {
        String username = usernameField.getText().trim();           // GET USERNAME INPUT
        String password = new String(passwordField.getPassword());  // GET PASSWORD INPUT

        // CHECK IF FIELDS ARE EMPTY
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            return;
        }

        // CHECK IF USERNAME ALREADY EXISTS
        boolean isValid = adminDao.checkIfAdminExists(username, password);
        if (isValid) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.");
            return;
        }

        // PASSWORD CONFIRMATION LOOP WITH HIDDEN INPUT
        while (true) {
            JPasswordField confirmField = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(
                this,
                confirmField,
                "Confirm Password",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            // IF USER CANCELS SIGN-UP
            if (option != JOptionPane.OK_OPTION) {
                JOptionPane.showMessageDialog(this, "Sign-up cancelled.");
                return;
            }

            String confirmPassword = new String(confirmField.getPassword());

            // CHECK IF CONFIRM PASSWORD MATCHES
            if (confirmPassword.equals(password)) {
                break;
            } else {
                JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.");
            }
        }

        try {
            adminDao.addSignupAccount(username, password);  // ADD NEW ADMIN ACCOUNT TO DB
            JOptionPane.showMessageDialog(this, "Account created successfully!");
            new StudentPage();                              // OPEN STUDENT PAGE AFTER SIGN-UP
            dispose();                                      // CLOSE LOGIN WINDOW
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to create account. Please try again.");
        }
    }

}
