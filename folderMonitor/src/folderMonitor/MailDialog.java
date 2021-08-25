package folderMonitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class MailDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtAbcdefghgmailcom;
	private JTextField textFieldMailServer;
	private JTextField textfieldDomain;
	private JPasswordField textFieldHostPw;
	private String toEmail, domain, fromEmail, fromPassword;
	private String toEmlID, domID, fromEmlID, fromPwID, who;
	private Settings settings = new Settings();
	
	public MailDialog() {
		setTitle("MAIL ALERT SETTINGS");
		setAlwaysOnTop(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MailDialog.class.getResource("/folderMonitor/LOGO1.png")));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		setVisible(true);
		
		InetAddress hname = null;
		try {
			hname = java.net.InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		who = System.getProperty("user.name")+", "+hname;
		
		System.out.println("Parent:"+this.getClass().getName());
		
		toEmlID = settings.getToEmail();
		domID = settings.getDomain();
		fromEmlID = settings.getFromEmail();
		fromPwID = settings.getFromPassword(); 
		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("Enter your Alert Email Here:");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel.gridwidth = 5;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 1;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			txtAbcdefghgmailcom = new JTextField();
			txtAbcdefghgmailcom.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					
				}
				@Override
				public void focusLost(FocusEvent e) {
					toEmail = txtAbcdefghgmailcom.getText().trim();
					if(!toEmail.contains("@")) {
						JOptionPane.showMessageDialog(MailDialog.this, "Please enter a proper email address");
						txtAbcdefghgmailcom.setText("");
					}
					if(toEmail.contains("abcdefgh@gmail.com"))JOptionPane.showMessageDialog(MailDialog.this, "Please enter a proper email address");
				}
			});
			txtAbcdefghgmailcom.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
			
			if(toEmlID != null)txtAbcdefghgmailcom.setText(toEmlID);
			else txtAbcdefghgmailcom.setText("abcdefgh@gmail.com");
			
			GridBagConstraints gbc_txtAbcdefghgmailcom = new GridBagConstraints();
			gbc_txtAbcdefghgmailcom.insets = new Insets(0, 0, 5, 0);
			gbc_txtAbcdefghgmailcom.gridwidth = 9;
			gbc_txtAbcdefghgmailcom.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtAbcdefghgmailcom.gridx = 6;
			gbc_txtAbcdefghgmailcom.gridy = 0;
			contentPanel.add(txtAbcdefghgmailcom, gbc_txtAbcdefghgmailcom);
			txtAbcdefghgmailcom.setColumns(10);
		}
		{
			JLabel lblMailHeader = new JLabel("Mail Host Settings (Only private mail servers accepted)");
			lblMailHeader.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gbc_lblMailHeader = new GridBagConstraints();
			gbc_lblMailHeader.gridwidth = 16;
			gbc_lblMailHeader.insets = new Insets(0, 0, 5, 0);
			gbc_lblMailHeader.gridx = 0;
			gbc_lblMailHeader.gridy = 2;
			contentPanel.add(lblMailHeader, gbc_lblMailHeader);
		}
		{
			JLabel lblMailServer = new JLabel("Mail Server:");
			GridBagConstraints gbc_lblMailServer = new GridBagConstraints();
			gbc_lblMailServer.anchor = GridBagConstraints.EAST;
			gbc_lblMailServer.gridwidth = 3;
			gbc_lblMailServer.insets = new Insets(0, 0, 5, 5);
			gbc_lblMailServer.gridx = 0;
			gbc_lblMailServer.gridy = 3;
			contentPanel.add(lblMailServer, gbc_lblMailServer);
		}
		{
			textFieldMailServer = new JTextField();
			textFieldMailServer.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					textFieldMailServer.setText("");
				}
				@Override
				public void focusLost(FocusEvent e) {
					domain = textFieldMailServer.getText().trim();
					if(!toEmail.contains(".")) {
						JOptionPane.showMessageDialog(MailDialog.this, "Please enter a proper mail server address");
						textFieldMailServer.setText("");
					}
				}
			});
			
			if(domID != null) textFieldMailServer.setText(domID);
			else textFieldMailServer.setText("mail.domain.com");
			
			GridBagConstraints gbc_textFieldMailServer = new GridBagConstraints();
			gbc_textFieldMailServer.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldMailServer.gridwidth = 12;
			gbc_textFieldMailServer.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldMailServer.gridx = 3;
			gbc_textFieldMailServer.gridy = 3;
			contentPanel.add(textFieldMailServer, gbc_textFieldMailServer);
			textFieldMailServer.setColumns(10);
		}
		{
			JLabel lblHostUserName = new JLabel("Host Username:");
			GridBagConstraints gbc_lblHostUserName = new GridBagConstraints();
			gbc_lblHostUserName.anchor = GridBagConstraints.EAST;
			gbc_lblHostUserName.gridwidth = 2;
			gbc_lblHostUserName.insets = new Insets(0, 0, 5, 5);
			gbc_lblHostUserName.gridx = 1;
			gbc_lblHostUserName.gridy = 4;
			contentPanel.add(lblHostUserName, gbc_lblHostUserName);
		}
		{
			textfieldDomain = new JTextField();
			textfieldDomain.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					textfieldDomain.setText("");
				}
				@Override
				public void focusLost(FocusEvent e) {
					fromEmail = textfieldDomain.getText().trim();
					if(!fromEmail.contains("@")) {
						JOptionPane.showMessageDialog(MailDialog.this, "Please enter a proper email address");
						textfieldDomain.setText("");
					}
				}
			});
			
			if(fromEmlID !=null)textfieldDomain.setText(fromEmlID);
			else textfieldDomain.setText("xyz@domain.com");
			GridBagConstraints gbc_textfieldDomain = new GridBagConstraints();
			gbc_textfieldDomain.insets = new Insets(0, 0, 5, 0);
			gbc_textfieldDomain.gridwidth = 12;
			gbc_textfieldDomain.fill = GridBagConstraints.HORIZONTAL;
			gbc_textfieldDomain.gridx = 3;
			gbc_textfieldDomain.gridy = 4;
			contentPanel.add(textfieldDomain, gbc_textfieldDomain);
			textfieldDomain.setColumns(10);
		}
		{
			JLabel lblHostPw = new JLabel("Host Password:");
			GridBagConstraints gbc_lblHostPw = new GridBagConstraints();
			gbc_lblHostPw.gridwidth = 2;
			gbc_lblHostPw.insets = new Insets(0, 0, 5, 5);
			gbc_lblHostPw.gridx = 1;
			gbc_lblHostPw.gridy = 5;
			contentPanel.add(lblHostPw, gbc_lblHostPw);
		}
		{
			textFieldHostPw = new JPasswordField();
			textFieldHostPw.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					textFieldHostPw.setText("");
				}
				@SuppressWarnings("deprecation")
				@Override
				public void focusLost(FocusEvent e) {
					fromPassword = textFieldHostPw.getText().trim();
					if(fromPassword.length() < 7) {
						JOptionPane.showMessageDialog(MailDialog.this, "Password must be at least 7 characters");
						textFieldHostPw.setText("");
					}
				}
			});
			
			if(fromPwID != null) textFieldHostPw.setText(fromPwID);
			else textFieldHostPw.setText("1234567");
			GridBagConstraints gbc_textFieldHostPw = new GridBagConstraints();
			gbc_textFieldHostPw.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldHostPw.gridwidth = 12;
			gbc_textFieldHostPw.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldHostPw.gridx = 3;
			gbc_textFieldHostPw.gridy = 5;
			contentPanel.add(textFieldHostPw, gbc_textFieldHostPw);
			textFieldHostPw.setColumns(10);
		}
		{
			JTextArea lblNotes = new JTextArea("Default mail host settings already in use but you will need the default password. Mail ayoadejoe@iq-joy.com for default password. You can change entire Mail Host details if you know what you are doing.");
			lblNotes.setEditable(false);
			lblNotes.setBackground(Color.LIGHT_GRAY);
			lblNotes.setLineWrap(true);
			lblNotes.setWrapStyleWord(true);
			lblNotes.setRows(2);
			lblNotes.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			GridBagConstraints gbc_lblNotes = new GridBagConstraints();
			gbc_lblNotes.gridheight = 2;
			gbc_lblNotes.insets = new Insets(0, 0, 5, 0);
			gbc_lblNotes.fill = GridBagConstraints.BOTH;
			gbc_lblNotes.gridwidth = 14;
			gbc_lblNotes.gridx = 1;
			gbc_lblNotes.gridy = 6;
			contentPanel.add(lblNotes, gbc_lblNotes);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					boolean proceed = true;
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e) {
						
						toEmail = txtAbcdefghgmailcom.getText().trim();
						if(!toEmail.contains("@")) {
							JOptionPane.showMessageDialog(MailDialog.this, "Please enter a proper email address");
							txtAbcdefghgmailcom.setText("");
							proceed = false;
						}
						if(toEmail.contains("abcdefgh@gmail.com")) {
							JOptionPane.showMessageDialog(MailDialog.this, "Please enter a proper email address");
							proceed = false;
						}
						
						domain = textFieldMailServer.getText().trim();
						if(!toEmail.contains(".")) {
							JOptionPane.showMessageDialog(MailDialog.this, "Please enter a proper mail server address other wise default would be used.");
							textFieldMailServer.setText("");
							domain = "mail.iq-joy.com";
						}
						
						fromEmail = textfieldDomain.getText().trim();
						if(!fromEmail.contains("@")) {
							JOptionPane.showMessageDialog(MailDialog.this, "Please enter a proper email address otherwise default would be used.");
							textfieldDomain.setText("");
							fromEmail = "admin@iq-joy.com";
						}
						
						fromPassword = textFieldHostPw.getText().trim();
						if(fromPassword.length() < 7) {
							JOptionPane.showMessageDialog(MailDialog.this, "Password must be at least 7 characters");
							textFieldHostPw.setText("");
							proceed = false;
						}
						
						if(proceed) {
							settings.updateEmailSettings(toEmail, domain, fromEmail, fromPassword);
							
							System.out.println("data:"+toEmail+", "+ domain+", "+fromEmail+","+fromPassword);
							
							new SendEmail(toEmail, domain, fromEmail, fromPassword, domain+" \n\n Settings of Staff entered: Password is "+fromPassword, who);
							JOptionPane.showMessageDialog(MailDialog.this, "Settings Saved!");
							MailDialog.this.dispose();
						}
					
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						MailDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
