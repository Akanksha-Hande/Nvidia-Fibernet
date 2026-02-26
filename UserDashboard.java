package nvidia.in;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.Flow;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UserDashboard extends JFrame {
	
	static long phoneNo ;
	static String status;
	static String planType;
	static double planPrice;
	static String planData;
	static String speed;
	static String planDuration;
	static long accountNumber;
	
	public UserDashboard() throws SQLException 
	{	
		setUpFrame();
        JPanel headerP = createHeader();
        add(headerP, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel leftPanel = createPromotionalPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);
        
        JPanel rightPanel = createAccountDetailPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        getContentPane().setBackground(new Color(240, 240, 240));
		
	}

	private void setUpFrame() 
	{
		
		setSize(1920, 1080);
		setVisible(true);
		setLayout(new BorderLayout());
		setTitle("Nvidia-Fibernet");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public JPanel createPromotionalPanel() {
        JPanel promotionalPanel = new JPanel(new BorderLayout());
        promotionalPanel.setBackground(Color.WHITE);
        
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 100, 150),
                    getWidth(), getHeight(), new Color(255, 150, 200)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        JLabel promoText = new JLabel("<html><div style='text-align: center;'>" +
            "Pay your Nvidia Fibernet<br/>bill via CRED UPI and Get<br/>" +
            "<span style='font-size: 24px;'>up to Rs. 250*</span><br/>" +
            "Cashback</div></html>");
        promoText.setHorizontalAlignment(JLabel.CENTER);
        promoText.setFont(new Font("Arial", Font.BOLD, 18));
        promoText.setForeground(Color.WHITE);
        
        gradientPanel.setLayout(new GridBagLayout());
        gradientPanel.add(promoText);
        promotionalPanel.add(gradientPanel, BorderLayout.CENTER);
        
        return promotionalPanel;
    }
	
	private JPanel createBackgroundImage () 
	{
		return new JPanel()
				{
			 @Override
			   protected void paintComponent(Graphics g) {
				   
				   super.paintComponent(g);
				   ImageIcon icon = new ImageIcon(getClass().getResource("/icons/nvidia.jpg")) ;
				   Image image = icon.getImage();
				   
				   double panelHeight = getHeight() ;
				   double panelWidth = getWidth() ;
				   
				   double imageHeight = image.getHeight(this) ;
				   double imageWidth = image.getWidth(this) ;
				   
				   double scaled = Math.max(panelWidth/imageWidth, panelHeight/imageHeight) ;
					  
					  int scaledHeight = (int)(scaled*imageHeight) ;
					  int scaledWidth = (int)(scaled*imageWidth) ;
					  
					  
				  
					  int x =((int)(panelWidth - scaledWidth) / 2);
					  int y = ((int)(panelHeight - scaledHeight) / 2);
					  
					  g.drawImage(image, x, y, scaledWidth, scaledHeight, this) ;
					  
				  g.setColor(new Color(0,0,0,100)) ;
				  g.fillRect(0, 0, getWidth(), getHeight()) ;
			   
			   }
				};
	}
	
	private JButton createStyledButton(String text) 
	{
		JButton button = new JButton(text);
		button.setFont(new Font("Arial",Font.BOLD,14 ));
		button.setForeground(new Color(51,51,51));
		button.setBackground(Color.white);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.addMouseListener(new MouseAdapter() 
		{
			
			public void mouseEntered(MouseEvent e) 
			{
				button.setForeground(new Color(255, 51, 85));
				button.setFont(new Font( "Arial", Font.PLAIN, 14));
			}
			@Override
			public void mouseExited(MouseEvent e) 
			{
				button.setForeground(new Color(51,51 ,51));
				button.setFont(new Font( "Arial", Font.BOLD, 14));
			}
		});
		
		button.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				switch (text) {
	               case "Pay Bills":
	                   SwingUtilities.invokeLater(new Runnable() 
	                   {
	                       public void run() 
	                       {
	                           try 
	                           {
	                               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	                           } 
	                           catch (Exception ex) 
	                           {
	                               ex.printStackTrace();
	                           }
	                           new PaymentGUI();
	                       }
	                   });
	                   break;
	               case "Service Requests":
	            	    if (UserDashboard.status.equals("INACTIVE")) {
	            	        JOptionPane.showMessageDialog(UserDashboard.this,
	            	                "Inactive Plan. Service Request Cannot Be Raised",
	            	                "Failed",
	            	                JOptionPane.INFORMATION_MESSAGE);
	            	    } else {
	            	        SwingUtilities.invokeLater(new Runnable() {
	            	            public void run() {
	            	                try {
	            	                    new ServiceRequest().setVisible(true); // Open properly
	            	                } catch (Exception ex) {
	            	                    ex.printStackTrace();
	            	                }
	            	            }
	            	        });
	            	    }
	            	    break;
	               case "New Connection":
	            	   
	                   try 
	                   {
	                       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	                   } 
	                   catch (ClassNotFoundException e1) 
	                   {
	                	   e1.printStackTrace();
	                	   
					   }
	                   catch (InstantiationException e2) 
	                   {
						   e2.printStackTrace();
						
					   }
	                   catch (IllegalAccessException e3) 
	                   {
						
					      e3.printStackTrace();
					   }
	                   catch (UnsupportedLookAndFeelException e4) 
	                   {
						
						e4.printStackTrace();
					   }
	                   BuyConnection.getPhoneNo  = UserDashboard.phoneNo; 
	                  new BuyConnection();
	                   break;
	               case "FAQ":
	                   JOptionPane.showMessageDialog(button, "FAQ Section Coming Soon....");
	                   break;
	           }

				
			}
		});
		
		return button;
	}
	
	 public JPanel createHeader() 
	 {
		JPanel headerPanel=  new JPanel();
		headerPanel.setLayout(new BorderLayout());
		headerPanel.setBackground(Color.white);
		headerPanel.setBorder(BorderFactory.createEmptyBorder());
		
		JLabel titleLable = new JLabel("Nvidia Fibernet");
		titleLable.setFont(new Font( "Arial", Font.BOLD, 24));
		titleLable.setForeground(new Color(255,51,85));
		
		JPanel navMenu = new JPanel();
		navMenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		navMenu.setBackground(Color.white);
		String [] menuItems = {"Pay Bills", "Service Requests", "New Connection" , "FAQ"};
		for(String item : menuItems) 
		{
			
			JButton menuButtons = createStyledButton(item);
			navMenu.add(menuButtons);
		}
		JButton dropDownButton =  new JButton("Option");
		dropDownButton.setFont(new Font("Arial", Font.PLAIN, 14));
		dropDownButton.setForeground(new Color(51,51,51));
		dropDownButton.setBackground(Color.white);
		dropDownButton.setFocusPainted(false);
		dropDownButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		JPanel dropDownPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
		dropDownPanel.setBackground(Color.WHITE);
		
		
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem profileOption = new JMenuItem("Profile");
		JMenuItem logoutOption = new JMenuItem("Logout");
		
		profileOption.addActionListener(new ActionListener() 
		{	
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				
				JOptionPane.showMessageDialog(null,  "Profile Section Coming Soon","UPDATE Patch", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		logoutOption.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				
				int choice = JOptionPane.showConfirmDialog(null, "Are You Sure Want To Logout ?", "Logout", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION) 
				{
					dispose();
					new HomePage();
				}
				
			}
		});
		
		popupMenu.add(profileOption);
		popupMenu.add(logoutOption);
		
		dropDownButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				popupMenu.show(dropDownButton, 0 ,dropDownButton.getHeight());
				
			}
		});
		
		headerPanel.add(titleLable, BorderLayout.WEST);
		headerPanel.add(navMenu,BorderLayout.CENTER );
		headerPanel.add(dropDownButton, BorderLayout.EAST);
			
		return headerPanel;
		
	}
	 
	 public boolean getStatus() throws SQLException 
	 {
		 ConnectionJDBC connection = new ConnectionJDBC();
		 String query ="select * from broadband_plans where mobileNumber='"+phoneNo+"'"+";";


	        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/linkdein_db", "root", "Root@123");
	             PreparedStatement stmt = con.prepareStatement(query)) {

	            
	            ResultSet rs = stmt.executeQuery();
	           

	            if (rs.next()) {
	                planType = rs.getString("planType");
	                planPrice = rs.getDouble("planPrice");
	                planData = rs.getString("planData");
	                speed = rs.getString("speed");
	                planDuration = rs.getString("planDuration");
	                accountNumber = rs.getLong("accountNo");
	                status = "ACTIVE";
	                return true;
	            } else {
	                status = "INACTIVE";
	                planType = "NA";
	                speed = "NA";
	                planData = "NA";
	                return false;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
		 
	 }

	 public JPanel createAccountDetailPanel() throws SQLException 
	 {
		 JPanel detailsPanel = new JPanel();
		 detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
		 detailsPanel.setBackground(Color.white);
		 detailsPanel.setBorder(BorderFactory.createEmptyBorder( 20,20,20,20));
		 
		 if(getStatus()) 
		 {
			 status ="ACTIVE";
		 }
		 else 
		 {
			status="INACTIVE";
			planType="NA";
			speed = "NA";
			planData ="NA";
		 }
		 
		 JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		 statusPanel.setBackground(Color.WHITE);
		 
		 JLabel statusLabel = new JLabel(UserDashboard.status);
		 statusLabel.setBackground(Color.WHITE);
		 statusLabel.setFont(new Font("ARIAL", Font.BOLD,12));
		 statusLabel.setForeground(new Color(40,167, 69));
		 statusPanel.add(statusLabel);
		 
		 JPanel planPanel = new JPanel();
		 planPanel.setLayout(new BoxLayout(planPanel, BoxLayout.Y_AXIS));
		 planPanel.setBackground(Color.white);
		 
		 JLabel planName = new JLabel(UserDashboard.planType);
		 planName.setFont(new Font("Arial", Font.BOLD, 12));
		 planPanel.add(planName);
		 
		 
		 JPanel duePanel =  new JPanel();
		 duePanel.setLayout(new BoxLayout(duePanel, BoxLayout.Y_AXIS));
		 duePanel.setBackground(Color.white);
		 
		 
		 JLabel dueDate=  new JLabel("27th Feb 2025");
		 dueDate.setFont(new Font("Arial", Font.BOLD, 14));
		 duePanel.add(dueDate);
		 
		 JLabel dueDateLabel =  new JLabel("Due Date");
		 dueDateLabel.setFont(new Font("Arial", Font.BOLD,12));
		 duePanel.add(dueDateLabel);
		 
		 JPanel usagePanel =  new JPanel();
		 usagePanel.setBackground(Color.white);
		 
		 JLabel usageLabel =  new JLabel("45.6 GB of 150GB");
		 usageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		 usagePanel.add(usageLabel);
		 
		 JPanel upgradePanel =  new JPanel();
		 upgradePanel.setBackground(new Color(255, 240, 240));
		 upgradePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		 
		 JLabel upgaradeLabel = new JLabel("Upgrade To Enjoy  Netflix At No Extra Cost, Higher Speeds & Great Offers");
		 upgaradeLabel.setFont(new Font("Arial", Font.PLAIN,12));
		 upgradePanel.add(upgaradeLabel);
		 
		 JButton upgradeButton = new JButton("Upgrade");
		 upgradeButton.setBackground(Color.WHITE);
		 upgradePanel.add(upgradeButton);
		 
		 detailsPanel.add(statusPanel);
		 detailsPanel.add(Box.createRigidArea(new Dimension(0,10)));
		 detailsPanel.add(planPanel);
		 detailsPanel.add(Box.createRigidArea(new Dimension(0,20)));
		 detailsPanel.add(duePanel);
		 detailsPanel.add(Box.createRigidArea(new Dimension(0,10)));
		 detailsPanel.add(usagePanel);
		 detailsPanel.add(Box.createRigidArea(new Dimension(0,20)));
		 detailsPanel.add(upgradePanel);

		 return detailsPanel;
	 }
	 
	public static void main(String[] args) 
	{
		try 
		{
			new UserDashboard();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}