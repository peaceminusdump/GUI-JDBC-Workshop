/* JOSE, GABRIEL ANTONIO B.
 * BSIT 2-2
 * GUI - JDBC WORKSHOP
 */

import pages.LoginPage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}