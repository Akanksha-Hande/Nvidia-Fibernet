package nvidia.in;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ServiceRequest extends JFrame {
    private JTextField requestIdTextField, userTextField;
    private JComboBox<String> requestTypeCombo, dateCombo;
    private JButton submitRequestButton, clearFormButton, goBackButton;
    private Color buttonColor = new Color(30, 144, 255);

    public ServiceRequest() {
        setUpFrame();
        initializeComponents();
        addComponents();
        addActionListeners();
    }

    private void setUpFrame() {
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(createBackgroundImage());
        setVisible(true);
    }

    private JPanel createBackgroundImage() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon(getClass().getResource("/icons/back.jpg"));
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 140));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private void initializeComponents() {
        requestIdTextField = createStyledTextField(20);
        requestIdTextField.setText(generateUnique5DigitId());;
        
        userTextField = createStyledTextField(20);

        String[] requestTypes = {"Connection issue", "Technical Support", "Billing Inquiry", "General Inquiry"};
        requestTypeCombo = new JComboBox<>(requestTypes);
        createStyledComboBox(requestTypeCombo);

        String[] dates = {"2025-03-01", "2025-03-02", "2025-03-03", "2025-03-04"};
        dateCombo = new JComboBox<>(dates);
        createStyledComboBox(dateCombo);

        submitRequestButton = createStyledButtons("Submit Request", buttonColor);
        clearFormButton = createStyledButtons("Clear Form", buttonColor);
        goBackButton = createStyledButtons("Go Back", buttonColor);
    }

    private void addComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(Box.createVerticalStrut(20));

        JLabel title = new JLabel("Service Request Form");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(title);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(CENTER_ALIGNMENT);

        addRowForm("Request ID", requestIdTextField, formPanel);
        formPanel.add(Box.createVerticalStrut(50));
        addRowForm("User", userTextField, formPanel);
        formPanel.add(Box.createVerticalStrut(50));
        addRowForm("Request Type", requestTypeCombo, formPanel);
        formPanel.add(Box.createVerticalStrut(50));
        addRowForm("Date", dateCombo, formPanel);

        contentPanel.add(formPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.add(submitRequestButton);
        buttonPanel.add(clearFormButton);
        buttonPanel.add(goBackButton);
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());

        add(contentPanel);
    }

    private void addActionListeners() {
        submitRequestButton.addActionListener(this::submitServiceRequest);
        clearFormButton.addActionListener(e -> {
            userTextField.setText("");
            requestTypeCombo.setSelectedIndex(0);
            dateCombo.setSelectedIndex(0);
        });
        goBackButton.addActionListener(e -> dispose());
    }

    private void submitServiceRequest(ActionEvent e) {
        String userName = userTextField.getText().trim();
        if (userName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a user name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String requestType = (String) requestTypeCombo.getSelectedItem();
        String requestDate = (String) dateCombo.getSelectedItem();
        String registrationId = getOrGenerateRegistrationId(userName);

        requestIdTextField.setText(registrationId);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/linkdein_db", "root", "Root@123")) {
            String query = "INSERT INTO user_service_request (registration_id, user_name, request_type, request_date) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, registrationId);
            pstmt.setString(2, userName);
            pstmt.setString(3, requestType);
            pstmt.setString(4, requestDate);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Registration Submitted Successfully!\n" +
                        "Registration ID: " + registrationId + "\nUser: " + userName, 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit request. Please try again!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error! Request not saved.", 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getOrGenerateRegistrationId(String userName) {
        return (userName.equalsIgnoreCase("Sanika")) ? "60267" : generateUnique5DigitId();
    }

    private String generateUnique5DigitId() {
        return String.valueOf(10000 + new Random().nextInt(90000));
    }

    private JTextField createStyledTextField(int cols) {
        JTextField textField = new JTextField(cols);
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        return textField;
    }

    private JButton createStyledButtons(String text, Color background) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        return button;
    }

    private void createStyledComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 19));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(Color.BLACK);
    }

    private void addRowForm(String labelText, JComponent component, JPanel formPanel) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setPreferredSize(new Dimension(120, 25));
        rowPanel.add(label);
        rowPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        rowPanel.add(component);

        formPanel.add(rowPanel);
    }

    public static void main(String[] args) {
        new ServiceRequest();
    }
}
