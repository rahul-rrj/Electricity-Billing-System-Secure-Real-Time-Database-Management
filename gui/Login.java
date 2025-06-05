package gui;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {
    private JTextField caNumberField;
    private JPasswordField passwordField;

    public Login() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center screen

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("User Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel caLabel = new JLabel("CA Number:");
        caLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(caLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        caNumberField = new JTextField(15);
        formPanel.add(caNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setBackground(new Color(45, 137, 239));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.setPreferredSize(new Dimension(100, 35));

        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registerBtn.setBackground(new Color(100, 100, 100));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerBtn.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        mainPanel.add(buttonPanel);

        add(mainPanel);

        // Action listeners

        loginBtn.addActionListener(e -> {
            String ca = caNumberField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (ca.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both CA Number and Password.",
                        "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM users WHERE ca_number=? AND password=?"
                );
                ps.setString(1, ca);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("role");
                    dispose();
                    if ("admin".equals(role)) {
                        new AdminDashboard();
                    } else {
                        new UserDashboard(ca);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials!",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerBtn.addActionListener(e -> {
            dispose();
            new Register();
        });

        setVisible(true);
    }
}
