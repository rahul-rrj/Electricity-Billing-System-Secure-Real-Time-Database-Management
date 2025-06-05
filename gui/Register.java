package gui;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Register extends JFrame {
    private JTextField nameField, caNumberField, emailField;
    private JPasswordField passwordField;

    public Register() {
        setTitle("User Registration");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        // CA Number
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel caLabel = new JLabel("CA Number:");
        caLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(caLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        caNumberField = new JTextField(15);
        formPanel.add(caNumberField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel);

        // Register button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registerBtn.setBackground(new Color(45, 137, 239));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerBtn.setPreferredSize(new Dimension(120, 35));

        buttonPanel.add(registerBtn);
        mainPanel.add(buttonPanel);

        add(mainPanel);

        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String ca = caNumberField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || ca.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                        "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO users (ca_number, name, password, email, role) VALUES (?, ?, ?, ?, 'user')"
                );
                ps.setString(1, ca);
                ps.setString(2, name);
                ps.setString(3, password);
                ps.setString(4, email);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registered successfully!");
                dispose();
                new Login();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
