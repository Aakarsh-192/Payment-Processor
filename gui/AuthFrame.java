package gui;

import dao.UserDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.User;

public class AuthFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel authPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel registerPanel;

    public AuthFrame() {
        setTitle("CMPay - Authentication");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.BLACK);
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 3;
        JButton switchToRegister = new JButton("New User? Register Here");
        switchToRegister.setBorderPainted(false);
        switchToRegister.setContentAreaFilled(false);
        switchToRegister.setForeground(new Color(70, 130, 180));
        loginPanel.add(switchToRegister, gbc);

        registerPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        registerPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField regUsernameField = new JTextField(15);
        registerPanel.add(regUsernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        registerPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField regPasswordField = new JPasswordField(15);
        registerPanel.add(regPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        registerPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        JTextField regPhoneField = new JTextField(15);
        registerPanel.add(regPhoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.BLACK);
        registerPanel.add(registerButton, gbc);

        gbc.gridy = 4;
        JButton switchToLogin = new JButton("Already have an account? Login Here");
        switchToLogin.setBorderPainted(false);
        switchToLogin.setContentAreaFilled(false);
        switchToLogin.setForeground(new Color(70, 130, 180));
        registerPanel.add(switchToLogin, gbc);

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registerPanel, "REGISTER");

        add(mainPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                User user = UserDAO.getUserByUsername(username);
                if (user != null && user.getPassword().equals(password)) {
                    dispose();
                    new MainFrame(user).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(AuthFrame.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = regUsernameField.getText();
                String password = new String(regPasswordField.getPassword());
                String phone = regPhoneField.getText();

                if (username.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(AuthFrame.this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User newUser = new User(username, password, phone, 1000.00); // Starting balance Rs. 1000
                if (UserDAO.addUser(newUser)) {
                    JOptionPane.showMessageDialog(AuthFrame.this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "LOGIN");
                    usernameField.setText(username);
                    passwordField.setText("");
                } else {
                    JOptionPane.showMessageDialog(AuthFrame.this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        switchToRegister.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));
        switchToLogin.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
    }
}