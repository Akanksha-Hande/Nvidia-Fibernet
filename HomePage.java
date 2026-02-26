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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class HomePage  extends JFrame{
	
	
	private JButton existingCustomerButton, newConnectionButton, adminSignInButton;
	Color existingCustomerColor = new Color(30,144,255);
	Color newConnectionColor = new Color(220,20,60);
	Color adminSignInColor =new Color(50,205,50);

	
	
	public HomePage() {
		
		setUpFrame();
		initializeButtonComponents();
		addComponents();
		setUpListeners();
		uiManager();
	}
	
	
	public void setUpFrame() {
		
		setSize(1366, 768);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		setContentPane(createBackgroundImage());
	}
	
	public JPanel createBackgroundImage() {
		
		return new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				
				ImageIcon icon = new ImageIcon(getClass().getResource("/icons/logo.jpg"));
				Image image = icon.getImage();
				
				double panelWidth = getWidth();
				double panelHeight = getHeight();
				
				double imageWidth = image.getWidth(this);
				double imageHeight = image.getHeight(this);
				
				double scaled = Math.max(panelWidth/imageWidth, panelHeight/imageHeight);
				
				int scaledHeight = (int)(scaled * imageHeight);
				int scaledWidth = (int)(scaled * imageWidth);
				
				int p = ((int)(panelWidth - scaledWidth) / 2);
				int q = ((int)(panelHeight - scaledHeight) / 2);
				
				g.drawImage(image, p, q, scaledWidth, scaledHeight, this);
				
				g.setColor(new Color(0, 0, 0, 100));
				
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
	}
	
	public void addComponents() {
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));	
		mainPanel.setOpaque(false);
		
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
		
		JLabel title = new JLabel("Welcome To Nvidia Fibernet", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 24));
		title.setForeground(Color.white);
		
		titlePanel.add(title);
		
		JPanel buttonPanel = new JPanel()
		{
			@Override
			protected void paintComponent(Graphics g) {									
				
				g.setColor(new Color(0, 0, 0, 100));									
				g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);											
			}	
		};
		
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(existingCustomerButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(newConnectionButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(adminSignInButton);

        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createVerticalStrut(50));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(50));

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(Box.createVerticalGlue());
        contentPane.add(mainPanel);
        contentPane.add(Box.createVerticalGlue());
	}
	
	public JButton createStyledButtons(String text, Color backColor, Color borderColor) {
		
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 18));
		button.setBackground(backColor);
		button.setForeground(Color.black);
		button.setFocusPainted(false);
		
        button.setPreferredSize(new Dimension(300, 50));
        button.setMaximumSize(new Dimension(300, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        button.setBorder(BorderFactory.createCompoundBorder(
        		
        		BorderFactory.createLineBorder(borderColor, 2), BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));


        button.addMouseListener(new MouseAdapter() {					
			
			@Override
			public void mouseEntered(MouseEvent e) {					
				
				button.setBackground(backColor.brighter());					
			}
			
			@Override
			public void mouseExited(MouseEvent e) {						
				
				button.setBackground(backColor);
			}
		});

		return button;
	}
	
	
	
	public void setUpListeners() {
		
		existingCustomerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				handleExistingCustomerButton();
			
			}
		});
		
		newConnectionButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				handleNewConnectionButton();
			
			}
		});

		adminSignInButton.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
		
				handleAdminSignInButton();
	
			}
		});
	}
	
	
	
	public void handleNewConnectionButton() {
		
		int option = JOptionPane.showConfirmDialog(this, "New Connection", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if(option == JOptionPane.YES_OPTION) {
			
			try {
				
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				 new UserRegistrationPage();
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(null, "Create Account..!", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	
	
	public void handleAdminSignInButton() {
		
		int option = JOptionPane.showConfirmDialog(this, "You are admin", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if(option == JOptionPane.YES_OPTION) {
			
			try {
				
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			
			//JOptionPane.showMessageDialog(null, "Successfully logged in", "Success", JOptionPane.INFORMATION_MESSAGE);
			new AdminSignInPage();
			dispose();
		}
	}




	public void handleExistingCustomerButton() {
	
		int option = JOptionPane.showConfirmDialog(this, "Existing Customer", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	
		if(option == JOptionPane.YES_OPTION) {
		
			new SignInPage();
		dispose();
		}
		
			//JOptionPane.showMessageDialog(null, "Successfully logged in", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
	
	
	
	
	
	public void initializeButtonComponents() {
		
		this.existingCustomerButton = createStyledButtons("Existing Customer", existingCustomerColor, Color.blue);
		this.newConnectionButton = createStyledButtons("New User", newConnectionColor, Color.red);
		this.adminSignInButton = createStyledButtons("Admin sign-in", adminSignInColor, Color.green);
	}
	
	public void uiManager() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Apply system look and feel
        } catch (Exception e) {
            e.printStackTrace(); // Print exception stack trace if an error occurs
        }
    }
		
	
	public static void main(String [] args) {
		
		new HomePage();
	}
}