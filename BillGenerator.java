package nvidia.in;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BillGenerator extends JFrame {
    private JTextField accountIdField, planBillField, dueFineField, stateTaxField, totalAmountField;
    private JButton calculateButton, generateButton;

    public BillGenerator() {
        setTitle("Bill Generator");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, getHeight())); // Fixed width
        sidebar.setBackground(new Color(50, 50, 50));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); // Vertical layout

        add(sidebar, BorderLayout.WEST);

        // Sidebar Buttons
        String[] menuItems = {"Bill Generator", "Account Details", "Service Requests", "Profile"};
        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Align center
            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setMaximumSize(new Dimension(180, 40)); // Fixed button size
            button.setPreferredSize(new Dimension(180, 40)); // Preferred button size
            sidebar.add(Box.createVerticalStrut(10)); // Add spacing
            sidebar.add(button);

            // Example Action for Navigation
            button.addActionListener(e -> JOptionPane.showMessageDialog(null, item + " clicked!"));
        }

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(new JLabel("Account ID:"), gbc);
        gbc.gridx = 1;
        accountIdField = new JTextField(15);
        formPanel.add(accountIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Plan Bill ($):"), gbc);
        gbc.gridx = 1;
        planBillField = new JTextField("0.00", 15);
        formPanel.add(planBillField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Due Fine ($):"), gbc);
        gbc.gridx = 1;
        dueFineField = new JTextField("0.00", 15);
        formPanel.add(dueFineField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("State Tax (%):"), gbc);
        gbc.gridx = 1;
        stateTaxField = new JTextField("18.00", 15);
        stateTaxField.setEditable(false);
        formPanel.add(stateTaxField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Total Amount ($):"), gbc);
        gbc.gridx = 1;
        totalAmountField = new JTextField("0.00", 15);
        totalAmountField.setEditable(false);
        formPanel.add(totalAmountField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        calculateButton = new JButton("Calculate Total");
        generateButton = new JButton("Generate Bill");

        buttonPanel.add(calculateButton);
        buttonPanel.add(generateButton);
        formPanel.add(buttonPanel, gbc);

        // Button Actions
        calculateButton.addActionListener(new CalculateAction());
        generateButton.addActionListener(new GenerateAction());

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        accountIdField.requestFocus();
    }

    // Calculate Total Amount
    private class CalculateAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                double planBill = Double.parseDouble(planBillField.getText().trim());
                double dueFine = Double.parseDouble(dueFineField.getText().trim());
                double stateTax = Double.parseDouble(stateTaxField.getText().trim());

                if (planBill < 0 || dueFine < 0) {
                    throw new NumberFormatException();
                }

                double total = planBill + dueFine + (planBill * stateTax / 100);
                totalAmountField.setText(String.format("%.2f", total));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter positive numbers.");
            }
        }
    }

    // Generate Bill Action
    private class GenerateAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (accountIdField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter Account ID.");
                return;
            }
            JOptionPane.showMessageDialog(null, "Bill Generated Successfully for Account ID: " + accountIdField.getText());
        }
    }

    public static void main(String[] args) {
        new BillGenerator();
    }
}
