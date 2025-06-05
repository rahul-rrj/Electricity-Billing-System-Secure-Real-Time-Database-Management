package gui;

import db.DBConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main container with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // padding

        // Top button panel (horizontal layout)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 0)); // space below buttons

        JButton userList = new JButton("View All Users");
        JButton totalCollected = new JButton("Total Collected");
        JButton unpaidList = new JButton("Unpaid Bills");
        JButton userBillingDetails = new JButton("User Billing Details");

        // Add some spacing between buttons
        buttonPanel.add(userList);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(totalCollected);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(unpaidList);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(userBillingDetails);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Placeholder center panel for messages or table (initially empty)
        JPanel centerPanel = new JPanel(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Button actions same as before
        userList.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users");
                StringBuilder sb = new StringBuilder("User List:\n");
                while (rs.next()) {
                    sb.append(rs.getString("ca_number")).append(" - ")
                            .append(rs.getString("name")).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        totalCollected.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery(
                        "SELECT SUM(amount) AS total FROM bills WHERE status='paid'"
                );
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Total Collected: ₹" + rs.getDouble("total"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        unpaidList.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                ResultSet rs = con.createStatement().executeQuery(
                        "SELECT * FROM bills WHERE status='unpaid'"
                );
                StringBuilder sb = new StringBuilder("Unpaid Bills:\n");
                while (rs.next()) {
                    sb.append("CA: ").append(rs.getString("ca_number"))
                            .append(", Month: ").append(rs.getString("month"))
                            .append(", Amount: ₹").append(rs.getDouble("amount")).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Show billing details table in a separate frame with enhanced UI
        userBillingDetails.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new UserBillingDetailsFrame());
        });

        setVisible(true);
    }

    // Inner class for detailed billing table with improved UI
    private static class UserBillingDetailsFrame extends JFrame {
        public UserBillingDetailsFrame() {
            setTitle("User Billing Details");
            setSize(900, 400);
            setLocationRelativeTo(null);

            String[] columns = {
                    "CA Number", "Name", "Email",
                    "Paid Bills", "Unpaid Bills",
                    "Total Paid (₹)", "Total Unpaid (₹)"
            };

            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable table = new JTable(model);

            // Improve table header font
            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));

            // Improve table row height and font
            table.setRowHeight(25);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            // Alternate row colors (striped effect)
            table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (!isSelected) {
                        setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                    }
                    return this;
                }
            });

            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            loadUserBillingData(model);

            setVisible(true);
        }

        private void loadUserBillingData(DefaultTableModel model) {
            String query = "SELECT u.ca_number, u.name, u.email, " +
                    "SUM(CASE WHEN b.status='paid' THEN 1 ELSE 0 END) AS paid_bills, " +
                    "SUM(CASE WHEN b.status='unpaid' THEN 1 ELSE 0 END) AS unpaid_bills, " +
                    "COALESCE(SUM(CASE WHEN b.status='paid' THEN b.amount ELSE 0 END), 0) AS total_paid, " +
                    "COALESCE(SUM(CASE WHEN b.status='unpaid' THEN b.amount ELSE 0 END), 0) AS total_unpaid " +
                    "FROM users u LEFT JOIN bills b ON u.ca_number = b.ca_number " +
                    "GROUP BY u.ca_number, u.name, u.email ORDER BY u.name";

            try (Connection con = DBConnection.getConnection();
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("ca_number"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getInt("paid_bills"),
                            rs.getInt("unpaid_bills"),
                            rs.getDouble("total_paid"),
                            rs.getDouble("total_unpaid")
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
            }
        }
    }
}
