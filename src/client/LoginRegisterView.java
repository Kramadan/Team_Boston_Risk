package client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import communicationObjects.Login;
import communicationObjects.Register;

/**
 * public class LoginRegisterView extends JPanel implements Observer.
 * This JPanel class is the ClientGui's login/register view which can be displayed on the ClientGui's main frame.
 * It is the view displayed when the user initially opens the client and succesfully connects and authenticates with the server.
 * This is quite a basic view, containing only two buttons and two text fields.
 * Its update method updates the view based upon the Client's Data object.
 * @author Oliver Kamperis
 */
public class LoginRegisterView extends JPanel implements Observer {
	private static final long serialVersionUID = 2550715969773396621L;
	private final Client client;
	private final Data data;
	
	private final JTextPane displayPane;
	private final JTextField userNameField;
	private final JPasswordField passwordField;
	private final JButton loginButton;
	private final JButton registerButton;
	
	/**
	 * Constructs a new LoginRegisterView to interact with a given Client object.
	 * Its initial state has all its JComponents initialized but aren't populated with data.
	 * @param client - The Client to make the Gui view for
	 */
	public LoginRegisterView(Client client) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.client = client;
		this.data = client.getData();
		
		this.displayPane = new JTextPane();
		this.userNameField = new JTextField();
		this.passwordField = new JPasswordField();
		this.loginButton = new JButton("Login");
		this.registerButton = new JButton("Register");
		
		this.addDisplayPane();
		this.populateAndAddMainPanel();
	}
	
	/**
	 * Updates the display panel at the top of the view based on the data's text field.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		displayPane.setText(data.getText());
	}
	
	/**
	 * Populates the display panel of the view with one display JTextPane and then adds it to the main panel.
	 */
	public void addDisplayPane() {
		displayPane.setEditable(false);
		displayPane.setPreferredSize(new Dimension(550, 40));
		
		JPanel displayPanel = new JPanel();
		displayPanel.add(displayPane);
		displayPanel.setPreferredSize(new Dimension(600, 50));
		
		this.add(displayPanel);
	}
	
	/**
	 * Populates the main panel of the view with two JTextFields and two JButtons.
	 */
	public void populateAndAddMainPanel() {
		userNameField.setEditable(true);
		userNameField.setPreferredSize(new Dimension(550, 50));
		userNameField.addActionListener(new LoginListener());
		
		JPanel userNameDisplay = new JPanel() {
			private static final long serialVersionUID = -3548006397271288681L;

			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 40));
				g.drawString("Username:", 0, 40);
			}
		};
		userNameDisplay.setPreferredSize(new Dimension(550, 50));
		
		JPanel userNamePanel = new JPanel();
		userNamePanel.add(userNameDisplay);
		userNamePanel.add(userNameField);
		
		passwordField.setEditable(true);
		passwordField.setPreferredSize(new Dimension(550, 50));
		passwordField.addActionListener(new LoginListener());
		
		JPanel passwordDisplay = new JPanel() {
			private static final long serialVersionUID = 3368463290771555173L;

			@Override
			public void paintComponent(Graphics g) {
				g.setFont(new Font("", Font.BOLD, 40));
				g.drawString("Password:", 0, 40);
			}
		};
		passwordDisplay.setPreferredSize(new Dimension(550, 50));
		
		JPanel passwordPanel = new JPanel();
		passwordPanel.add(passwordDisplay);
		passwordPanel.add(passwordField);
		
		this.loginButton.addActionListener(new LoginListener());
		this.registerButton.addActionListener(new RegisterListener());
		
		loginButton.addActionListener(new LoginListener());
		loginButton.setPreferredSize(new Dimension(550, 100));
		JPanel loginButtonPanel = new JPanel();
		loginButtonPanel.add(loginButton);
		
		registerButton.addActionListener(new RegisterListener());
		registerButton.setPreferredSize(new Dimension(550, 100));
		JPanel registerButtonPanel = new JPanel();
		registerButtonPanel.add(registerButton);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(4, 1));
		mainPanel.add(userNamePanel);
		mainPanel.add(passwordPanel);
		mainPanel.add(loginButtonPanel);
		mainPanel.add(registerButtonPanel);
		mainPanel.setPreferredSize(new Dimension(600, 580));
		
		this.add(mainPanel);
	}
	
	/**
	 * Clears the username and password fields of the login/register view.
	 */
	public void clearTextFields() {
		userNameField.setText("");
		passwordField.setText("");
	}
	
	/**
	 * public class LoginListener implements ActionListener.
	 * This ActionListener class sends a Login request to the server if the username and password fields are not empty and are both alphanumeric.
	 * This ActionListener is assigned to the "Login" button and if enter is pressed whilst typing in either of the username and password fields.
	 */
	public class LoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (userNameField.getText() == null || userNameField.getText().equals("")) {
				data.setText("Username field is blank!");
			} else if (passwordField.getPassword() == null || new String(passwordField.getPassword()).equals("")) {
				data.setText("Password field is blank!");
			} else if (!ClientGui.isAlphaNumeric(userNameField.getText())) {
				data.setText("Username must be alphanumeric!");
			} else if (!ClientGui.isAlphaNumeric(new String(passwordField.getPassword()))) {
				data.setText("Password must be alphanumeric!");
			} else {
				client.sendRequest(new Login("", userNameField.getText(), new String(passwordField.getPassword())));
			}
		}
	}
	
	/**
	 * public class RegisterListener implements ActionListener.
	 * This ActionListener class sends a Register request to the server if the username and password fields are not empty and are both alphanumeric.
	 * This ActionListener is assigned only to the "Register" button.
	 */
	public class RegisterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (userNameField.getText() == null || userNameField.getText().equals("")) {
				data.setText("Username field is blank!");
			} else if (passwordField.getPassword() == null || new String(passwordField.getPassword()).equals("")) {
				data.setText("Password field is blank!");
			} else if (!ClientGui.isAlphaNumeric(userNameField.getText())) {
				data.setText("Username must be alphanumeric!");
			} else if (!ClientGui.isAlphaNumeric(new String(passwordField.getPassword()))) {
				data.setText("Password must be alphanumeric!");
			} else {
				client.sendRequest(new Register("", userNameField.getText(), new String(passwordField.getPassword())));
			}
		}
	}
}
