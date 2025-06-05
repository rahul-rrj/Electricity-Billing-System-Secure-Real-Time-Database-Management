package gui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserDashboard extends JFrame {
    String ca;

    // Table components
    private JTable billsTable;
    private DefaultTableModel tableModel;

    public UserDashboard(String caNumber) {
        this.ca = caNumber;
        setTitle("User Dashboard - " + ca);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Top panel for buttons
        JPanel topPanel = new JPanel();
        JButton viewBill = new JButton("View Bills");
        JButton genBill = new JButton("Generate New Bill");
        JButton payBill = new JButton("Pay Selected Bill");

        topPanel.add(viewBill);
        topPanel.add(genBill);
        topPanel.add(payBill);

        add(topPanel, BorderLayout.NORTH);

        // Table to display bills
        tableModel = new DefaultTableModel(new String[]{"Bill ID", "Month", "Units", "Amount", "Status"}, 0);
        billsTable = new JTable(tableModel);
        add(new JScrollPane(billsTable), BorderLayout.CENTER);

        // Load bills initially
        loadBills();

        // View Bills button loads bills into table
        viewBill.addActionListener(e -> loadBills());

        // Generate Bill with units input & automatic amount calculation
        genBill.addActionListener(e -> {
            String month = JOptionPane.showInputDialog(this, "Enter Month (e.g., May 2025):");
            if (month == null || month.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Month cannot be empty.");
                return;
            }

            String unitsStr = JOptionPane.showInputDialog(this, "Enter Units Consumed:");
            int units;
            try {
                units = Integer.parseInt(unitsStr);
                if (units < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive integer for units.");
                return;
            }

            // For demo, assume housing type is residential; you can pass it from login/user info
            String housingType = "residential";

            double amount = calculateAmount(units, housingType);

            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO bills (ca_number, month, units_consumed, amount, status) VALUES (?, ?, ?, ?, 'unpaid')"
                );
                ps.setString(1, ca);
                ps.setString(2, month);
                ps.setInt(3, units);
                ps.setDouble(4, amount);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Bill Generated!\nAmount: ₹" + String.format("%.2f", amount));
                loadBills(); // refresh table
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error generating bill: " + ex.getMessage());
            }
        });

        // Pay selected bill
        payBill.addActionListener(e -> {
            int selectedRow = billsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a bill to pay.");
                return;
            }

            String status = (String) tableModel.getValueAt(selectedRow, 4);
            if ("paid".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(this, "Selected bill is already paid.");
                return;
            }

            int billId = (int) tableModel.getValueAt(selectedRow, 0);

            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE bills SET status='paid' WHERE bill_id=?"
                );
                ps.setInt(1, billId);
                int updated = ps.executeUpdate();

                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Bill paid successfully.");
                    loadBills();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to pay the bill.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error paying bill: " + ex.getMessage());
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadBills() {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT bill_id, month, units_consumed, amount, status FROM bills WHERE ca_number=? ORDER BY bill_id DESC"
            );
            ps.setString(1, ca);
            ResultSet rs = ps.executeQuery();

            tableModel.setRowCount(0); // clear existing rows

            while (rs.next()) {
                int id = rs.getInt("bill_id");
                String month = rs.getString("month");
                int units = rs.getInt("units_consumed");
                double amount = rs.getDouble("amount");
                String status = rs.getString("status");
                tableModel.addRow(new Object[]{id, month, units, amount, status});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bills: " + ex.getMessage());
        }
    }

    private double calculateAmount(int units, String housingType) {
        double amount = 0;
        int remainingUnits = units;

        // Example slabs (in INR)
        if (remainingUnits > 100) {
            amount += 100 * 3.5;
            remainingUnits -= 100;
        } else {
            amount += remainingUnits * 3.5;
            remainingUnits = 0;
        }

        if (remainingUnits > 100) {
            amount += 100 * 4.5;
            remainingUnits -= 100;
        } else {
            amount += remainingUnits * 4.5;
            remainingUnits = 0;
        }

        if (remainingUnits > 0) {
            amount += remainingUnits * 6.0;
        }

        amount += 50; // fixed meter charge

        if ("commercial".equalsIgnoreCase(housingType)) {
            amount *= 1.10; // 10% extra
        }

        amount *= 1.05; // 5% tax

        return amount;
    }
}
