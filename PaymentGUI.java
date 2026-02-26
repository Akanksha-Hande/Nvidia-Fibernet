package nvidia.in;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
public class PaymentGUI extends JFrame{
	private JTextField otherAmountField;
	private JTextField dueAmountField;
	private ButtonGroup paymentGroup;
	private JRadioButton payDueButton;
	private JRadioButton payOtherButton;
 
	// bill details //
	private double billAmount;
	private double tax;
	private long accountNo;
	private String transactionID;
	private String paymentType;
	private double dueFine;
	private String date;
	private double planAmt;
	BillLogics logics;
	Image backgroundImage;
 
	private static final Color BUTTON_COLOR = new Color(76,161,175);
	private static final Color BUTTON_HOVER_COLOR = new Color(58,143,157);
	private static final Color BUTTON_PRESS_COLOR = new Color(45,125,138);
 
	public PaymentGUI()
	{
		logics= new BillLogics();
		try {
			logics.getDetails();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		this.billAmount=logics.billAmount;
	   	this.accountNo=logics.accountNo;
	   	this.date=logics.date;
	   	this.tax=logics.tax;
	   	this.transactionID=logics.transactionID;
	   	this.paymentType=logics.paymentType;
	   	this.dueFine=logics.dueFine;
 
	   	setTitle("Nvidia-Fibernet Bill Payment");
	   	setSize(600,400);
	   	setVisible(true);
 
	   	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	   	loadBackgroundImage();
 
	   	BackgroundPanel contentPane = new BackgroundPanel();
	   	setContentPane(contentPane);
 
	   	JPanel mainPanel = new JPanel();
	   	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	   	mainPanel.setOpaque(false);
	   	mainPanel.setBorder(BorderFactory.createEmptyBorder(20,25,20,25));
 
	   	addHeaderPanel(mainPanel);
	   	addPaymentOptionsPanel(mainPanel);
	   	addPlanDetailsPanel(mainPanel);
	   	addButtonPanel(mainPanel);
	   //	generateAndSaveBill();
	   	addSaveBillButton(mainPanel);
	   	contentPane.add(mainPanel);
	}
 
	private void loadBackgroundImage()
	{
		backgroundImage = new ImageIcon(getClass().getResource("/icons/payment.jpg")).getImage();
	}
 
	public class BackgroundPanel extends JPanel
	{
		public BackgroundPanel() {
			setLayout(new BorderLayout());
 
		}
 
		@Override
		 protected void paintComponent(Graphics g)
		 {
			if(backgroundImage!=null)
			{
				Graphics2D g2d = (Graphics2D)g.create();
				g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				g2d.setColor(new Color(255,255,255,200));
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.dispose();
			}
		 }
	}
 
	private void addHeaderPanel(JPanel mainPanel)
	{	
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(2,2,10,5));
		headerPanel.setOpaque(false);
 
		JLabel billLabel = new JLabel("Bill Amt");
		billLabel.setFont(new Font("Segoe UI", Font.PLAIN,16));
 
		JLabel amountLabel = new JLabel("$"+String.format("%.2f", billAmount));
		amountLabel.setFont(new Font("Segoe UI",Font.BOLD,24));
 
		JLabel dueDateLabel = new JLabel("Due Date: ");
		dueDateLabel.setFont(new Font("Segoe UI",Font.PLAIN,16));
		dueDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
 
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM. dd,yyyy");
		JLabel dateLabel = new JLabel(dateFormat.format(new Date()));	
		dateLabel.setFont(new Font("Segoe UI",Font.BOLD,16));
		dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
 
		headerPanel.add(billLabel);
		headerPanel.add(dueDateLabel);
		headerPanel.add(amountLabel);
		headerPanel.add(dateLabel);
 
		mainPanel.add(headerPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0,20)));	
	}
 
	private JTextField createStyledTextField()
	{
		JTextField textField = new JTextField(10);
		textField.setFont(new Font("Segoe UI",Font.PLAIN,14));
 
		textField.setBorder(BorderFactory.createCompoundBorder(
		           new LineBorder(new Color(200, 200, 200)),
		           new EmptyBorder(5, 10, 5, 10)
		       ));
 
		return textField;
	}
 
	private void addPaymentOptionsPanel(JPanel mainPanel)
	{
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		optionsPanel.setOpaque(false);
 
		paymentGroup=new ButtonGroup();
 
		JPanel duePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		duePanel.setOpaque(false);
 
		payDueButton=new JRadioButton("Pay Due Amount");
		payDueButton.setFont(new Font("Segoe UI",Font.PLAIN,14));
		payDueButton.setOpaque(false);
		dueAmountField=createStyledTextField();
		dueAmountField.setText("$"+String.format("%.2f",billAmount));
		dueAmountField.setEnabled(false);
 
		JPanel otherPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		otherPanel.setOpaque(false);
		payOtherButton=new JRadioButton("Pay Other Amount");
		payOtherButton.setFont(new Font("Segoe UI",Font.PLAIN,14));
		payOtherButton.setOpaque(false);
		otherAmountField=createStyledTextField();
		otherAmountField.setEnabled(false);
 
		paymentGroup.add(payDueButton);
		paymentGroup.add(payOtherButton);
 
		payDueButton.addActionListener(new ActionListener() {
 
			@Override
			public void actionPerformed(ActionEvent e) {
				dueAmountField.setEnabled(false);
				otherAmountField.setEnabled(false);
			}
 
 
		});
 
		payOtherButton.addActionListener(new ActionListener() {
 
			@Override
			public void actionPerformed(ActionEvent e) {
				dueAmountField.setEnabled(false);
				otherAmountField.setEnabled(true);
 
			}
		});
 
		duePanel.add(payDueButton);
		duePanel.add(dueAmountField);
		otherPanel.add(payOtherButton);
		otherPanel.add(otherAmountField);
 
		optionsPanel.add(duePanel);
		optionsPanel.add(otherPanel);
		mainPanel.add(optionsPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0,20)));	
 
 
	}
 
	private void addPlanDetailsPanel(JPanel mainPanel)
	{
		JPanel planPanel = new JPanel(new GridLayout(2,1,0,5));
		planPanel.setOpaque(false);
 
		JLabel planLabel = new JLabel(UserDashboard.planType);
		planLabel.setFont(new Font("Segoe UI",Font.BOLD,16));
 
		JLabel billingLabel = new JLabel("Monthly Billing");
		billingLabel.setFont(new Font("Segoe UI",Font.PLAIN,14));
		billingLabel.setForeground(Color.GRAY);
 
		planPanel.add(planLabel);
		planPanel.add(billingLabel);
 
		mainPanel.add(planPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0,20)));
 
	}
 
	private void addButtonPanel(JPanel mainPanel)
	{
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setOpaque(false);
 
		final JButton proccedButton = createStyledButton("Proceed To Pay");
		proccedButton.addActionListener(new ActionListener() {
 
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!payDueButton.isSelected() && !payOtherButton.isSelected())
				{
					JOptionPane.showMessageDialog(PaymentGUI.this, "Please Select A Payment Option","Payment Option Required",JOptionPane.WARNING_MESSAGE);
					return;
				}
 
				double amount=payDueButton.isSelected()? billAmount :parseAmount(otherAmountField.getText());
 
				if(payOtherButton.isSelected() && amount<=0)
				{
					JOptionPane.showMessageDialog(PaymentGUI.this,"Please Enter A Valid Amount","Invalid Amount",JOptionPane.WARNING_MESSAGE);
					return;
				}
 
				try {
					logics.pay();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
 
				if(billAmount==0)
				{
					JOptionPane.showMessageDialog(PaymentGUI.this, "Due Is $ 0.0. Cannot Proceed To Payment "+String.format("%.2f", amount),"Payment Failed",JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(PaymentGUI.this,"Proceeding To Payment Gateway With Amount: $ "+String.format("%.2f",amount),"Payment Processing",JOptionPane.INFORMATION_MESSAGE);
 
				}
 
 
			}
		});
 
		buttonPanel.add(proccedButton);
		mainPanel.add(buttonPanel);
	}
 
	private double parseAmount(String text)
	{
		try
		{
			return Double.parseDouble(text.replace("$","").trim());
		}
		catch(NumberFormatException ne)
		{
			return 0.0;
		}
 
	}
	private JButton createStyledButton(String text) {
		JButton button = new JButton(text) {
           @Override
           protected void paintComponent(Graphics g) {
               Graphics2D g2 = (Graphics2D) g.create();
               g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
               boolean isPressed = getModel().isPressed();
               boolean isMouseOver = getModel().isRollover();
 
               Color bgColor = isPressed ? BUTTON_PRESS_COLOR :
                             isMouseOver ? BUTTON_HOVER_COLOR :
                             BUTTON_COLOR;
 
               GradientPaint gradient = new GradientPaint(
                   0, 0, bgColor,
                   0, getHeight(), bgColor.darker()
               );
               g2.setPaint(gradient);
               g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
 
               g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
               FontMetrics metrics = g2.getFontMetrics();
               int textX = (getWidth() - metrics.stringWidth(getText())) / 2;
               int textY = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
 
               g2.setColor(new Color(0, 0, 0, 50));
               g2.drawString(getText(), textX + 1, textY + 1);
 
               g2.setColor(Color.WHITE);
               g2.drawString(getText(), textX, textY);
 
               g2.dispose();
           }
       };
       button.setPreferredSize(new Dimension(150, 40));
       button.setBorder(new EmptyBorder(5, 15, 5, 15));
       button.setFocusPainted(false);
       button.setContentAreaFilled(false);
       button.setBorderPainted(false);
       button.setCursor(new Cursor(Cursor.HAND_CURSOR));
       return button;
	}
 
	private void generateAndSaveBill()
	{
		JFileChooser fileChooser = new JFileChooser();
	       fileChooser.setDialogTitle("Save Bill");
	       fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
 
	       if(billAmount!=0)
	       {
	       	if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
	               File file = fileChooser.getSelectedFile();
	               if (!file.getName().toLowerCase().endsWith(".txt")) {
	                   file = new File(file.getAbsolutePath() + ".txt");
	               }
 
	               try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	               	System.out.println(billAmount);
	               	System.out.println(dueFine);
	               	System.out.println(date);
	               	System.out.println(accountNo);
	               	System.out.println(billAmount);
	               	System.out.println(tax);
	               	System.out.println(transactionID);
	                   writer.write("=================================\n");
	                   writer.write("        Nvidia-Fibernet Bill      \n");
	                   writer.write("=================================\n\n");
	                   writer.write("Bill Due Date: " + new SimpleDateFormat("MMM. dd, yyyy").format(new Date()) + "\n");
	                   date=new SimpleDateFormat("MMM. dd, yyyy").format(new Date());
	                   writer.write("Plan Bill: "+planAmt+"\n");
	                   writer.write("Due Fine: $ " + dueFine+ "\n");
	                   writer.write("State Tax: $ "+tax+"\n");
	                   writer.write("Payment Type: "+paymentType+"\n");
	                   writer.write("Total Bill: $ "+billAmount+"\n");
	                   writer.write("Billing Type: Monthly Billing\n");
	                   writer.write("\n=================================\n");
 
	                   JOptionPane.showMessageDialog(this,
	                       "Bill saved successfully!",
	                       "Success",
	                       JOptionPane.INFORMATION_MESSAGE);
 
	               } catch (Exception ex) {
	                   JOptionPane.showMessageDialog(this,
	                       "Error saving bill: " + ex.getMessage(),
	                       "Error",
	                       JOptionPane.ERROR_MESSAGE);
	               }
	           }
	       }
	       else
	       {
	       	JOptionPane.showMessageDialog(this,
	                   "Bill Amount Is $0.0 Cannot Save The Bill!",
	                   "Failed",
	                   JOptionPane.INFORMATION_MESSAGE);
	       }
	}
 
	private void addSaveBillButton(JPanel mainPanel)
	{
		 JPanel saveBillPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        saveBillPanel.setOpaque(false);
	        JButton saveBillButton = createStyledButton("Save Bill");
	        saveBillButton.setPreferredSize(new Dimension(120, 40));
	        saveBillButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                generateAndSaveBill();
	            }
	        });
	        saveBillPanel.add(saveBillButton);
	        mainPanel.add(saveBillPanel);
	}
 
	public static void main(String[] args) {
		new PaymentGUI();
	}
 
}