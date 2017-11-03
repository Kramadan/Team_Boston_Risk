package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class ServerDataGUI implements Observer {

	//Field Variables
	private JFrame frame;
	private JTextField textField_0;
	private JTextField txtTotalClientThreads;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_15;
	private JTextField textField_16;
	private JTextField txtClientsCurrentlyLoggin;
	private JTextField textField_18;
	private JPanel panel_4;
	private JTextField txtClienclientsWaitingTo;
	private JTextField textField_19;
	private JPanel panel_5;
	private JTextField txtClientsInWaiting;
	private JTextField textField_21;
	private JPanel panel_6;
	private JTextField txtClientsPlayingA;
	private JTextField textField_23;
	private ServerMain serverMain;

	/**
	 * Create the application.
	 * @param serverMain 
	 */
	public ServerDataGUI(ServerMain serverMain) {
		this.serverMain = serverMain;
		initialize();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Server Management Information");
		frame.setResizable(false);
		
		frame.setSize(800, 670);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 5));
		panel_1.setBounds(10, 11, 312, 612);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(6, 16, 300, 589);
		panel_1.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{300, 0};
		gbl_panel.rowHeights = new int[]{33, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 61, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		textField_16 = new JTextField();
		textField_16.setEditable(false);
		textField_16.setColumns(10);
		textField_16.setBackground(new Color(255,30,0));
		GridBagConstraints gbc_textField_16 = new GridBagConstraints();
		gbc_textField_16.insets = new Insets(0, 0, 5, 0);
		gbc_textField_16.fill = GridBagConstraints.BOTH;
		gbc_textField_16.gridx = 0;
		gbc_textField_16.gridy = 0;
		panel.add(textField_16, gbc_textField_16);
		textField_16.setVisible(false);
		
		textField_15 = new JTextField();
		textField_15.setEditable(false);
		textField_15.setColumns(10);
		textField_15.setBackground(new Color(255,60,0));
		GridBagConstraints gbc_textField_15 = new GridBagConstraints();
		gbc_textField_15.insets = new Insets(0, 0, 5, 0);
		gbc_textField_15.fill = GridBagConstraints.BOTH;
		gbc_textField_15.gridx = 0;
		gbc_textField_15.gridy = 1;
		panel.add(textField_15, gbc_textField_15);
		textField_15.setVisible(false);
		
		textField_14 = new JTextField();
		textField_14.setEditable(false);
		textField_14.setColumns(10);
		textField_14.setBackground(new Color(255,90,0));
		GridBagConstraints gbc_textField_14 = new GridBagConstraints();
		gbc_textField_14.insets = new Insets(0, 0, 5, 0);
		gbc_textField_14.fill = GridBagConstraints.BOTH;
		gbc_textField_14.gridx = 0;
		gbc_textField_14.gridy = 2;
		panel.add(textField_14, gbc_textField_14);
		textField_14.setVisible(false);
		
		textField_13 = new JTextField();
		textField_13.setEditable(false);
		textField_13.setColumns(10);
		textField_13.setBackground(new Color(255, 120,0));
		GridBagConstraints gbc_textField_13 = new GridBagConstraints();
		gbc_textField_13.insets = new Insets(0, 0, 5, 0);
		gbc_textField_13.fill = GridBagConstraints.BOTH;
		gbc_textField_13.gridx = 0;
		gbc_textField_13.gridy = 3;
		panel.add(textField_13, gbc_textField_13);
		textField_13.setVisible(false);
		
		textField_12 = new JTextField();
		textField_12.setEditable(false);
		textField_12.setColumns(10);
		textField_12.setBackground(new Color(255, 150,0));
		GridBagConstraints gbc_textField_12 = new GridBagConstraints();
		gbc_textField_12.insets = new Insets(0, 0, 5, 0);
		gbc_textField_12.fill = GridBagConstraints.BOTH;
		gbc_textField_12.gridx = 0;
		gbc_textField_12.gridy = 4;
		panel.add(textField_12, gbc_textField_12);
		textField_12.setVisible(false);
		
		textField_11 = new JTextField();
		textField_11.setEditable(false);
		textField_11.setColumns(10);
		textField_11.setBackground(new Color(255, 180,0));
		GridBagConstraints gbc_textField_11 = new GridBagConstraints();
		gbc_textField_11.insets = new Insets(0, 0, 5, 0);
		gbc_textField_11.fill = GridBagConstraints.BOTH;
		gbc_textField_11.gridx = 0;
		gbc_textField_11.gridy = 5;
		panel.add(textField_11, gbc_textField_11);
		textField_11.setVisible(false);
		
		textField_10 = new JTextField();
		textField_10.setEditable(false);
		textField_10.setColumns(10);
		textField_10.setBackground(new Color(255,210,0));
		GridBagConstraints gbc_textField_10 = new GridBagConstraints();
		gbc_textField_10.insets = new Insets(0, 0, 5, 0);
		gbc_textField_10.fill = GridBagConstraints.BOTH;
		gbc_textField_10.gridx = 0;
		gbc_textField_10.gridy = 6;
		panel.add(textField_10, gbc_textField_10);
		textField_10.setVisible(false);
		
		textField_9 = new JTextField();
		textField_9.setEditable(false);
		textField_9.setColumns(10);
		textField_9.setBackground(new Color(255,230,0));
		GridBagConstraints gbc_textField_9 = new GridBagConstraints();
		gbc_textField_9.insets = new Insets(0, 0, 5, 0);
		gbc_textField_9.fill = GridBagConstraints.BOTH;
		gbc_textField_9.gridx = 0;
		gbc_textField_9.gridy = 7;
		panel.add(textField_9, gbc_textField_9);
		textField_9.setVisible(false);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBackground(new Color(255,255,0));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 8;
		panel.add(textField, gbc_textField);
		textField.setVisible(false);
		
		textField_8 = new JTextField();
		textField_8.setBackground(new Color(230,255,0));
		textField_8.setEditable(false);
		textField_8.setColumns(10);
		GridBagConstraints gbc_textField_8 = new GridBagConstraints();
		gbc_textField_8.insets = new Insets(0, 0, 5, 0);
		gbc_textField_8.fill = GridBagConstraints.BOTH;
		gbc_textField_8.gridx = 0;
		gbc_textField_8.gridy = 9;
		panel.add(textField_8, gbc_textField_8);
		textField_8.setVisible(false);
		
		textField_7 = new JTextField();
		textField_7.setHorizontalAlignment(SwingConstants.CENTER);
		textField_7.setBackground(new Color(210,255,0));
		textField_7.setEditable(false);
		textField_7.setColumns(10);
		GridBagConstraints gbc_textField_7 = new GridBagConstraints();
		gbc_textField_7.insets = new Insets(0, 0, 5, 0);
		gbc_textField_7.fill = GridBagConstraints.BOTH;
		gbc_textField_7.gridx = 0;
		gbc_textField_7.gridy = 10;
		panel.add(textField_7, gbc_textField_7);
		textField_7.setVisible(false);
		
		textField_6 = new JTextField();
		textField_6.setHorizontalAlignment(SwingConstants.CENTER);
		textField_6.setBackground(new Color(180,255,0));
		textField_6.setEditable(false);
		textField_6.setColumns(10);
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.insets = new Insets(0, 0, 5, 0);
		gbc_textField_6.fill = GridBagConstraints.BOTH;
		gbc_textField_6.gridx = 0;
		gbc_textField_6.gridy = 11;
		panel.add(textField_6, gbc_textField_6);
		textField_6.setVisible(false);
		
		textField_5 = new JTextField();
		textField_5.setHorizontalAlignment(SwingConstants.CENTER);
		textField_5.setBackground(new Color(150,255,0));
		textField_5.setEditable(false);
		textField_5.setColumns(10);
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 0);
		gbc_textField_5.fill = GridBagConstraints.BOTH;
		gbc_textField_5.gridx = 0;
		gbc_textField_5.gridy = 12;
		panel.add(textField_5, gbc_textField_5);
		textField_5.setVisible(false);
		
		textField_4 = new JTextField();
		textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		textField_4.setBackground(new Color(120,255,0));
		textField_4.setEditable(false);
		textField_4.setColumns(10);
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 0);
		gbc_textField_4.fill = GridBagConstraints.BOTH;
		gbc_textField_4.gridx = 0;
		gbc_textField_4.gridy = 13;
		panel.add(textField_4, gbc_textField_4);
		textField_4.setVisible(false);
		
		textField_3 = new JTextField();
		textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		textField_3.setBackground(new Color(90,255,0));
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.fill = GridBagConstraints.BOTH;
		gbc_textField_3.gridx = 0;
		gbc_textField_3.gridy = 14;
		panel.add(textField_3, gbc_textField_3);
		textField_3.setVisible(false);
		
		textField_2 = new JTextField();
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setBackground(new Color(60,255,0));
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.BOTH;
		gbc_textField_2.gridx = 0;
		gbc_textField_2.gridy = 15;
		panel.add(textField_2, gbc_textField_2);
		textField_2.setVisible(false);
		
		textField_1 = new JTextField();
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setBackground(new Color(30,255,0));
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.BOTH;
		gbc_textField_1.gridx = 0;
		gbc_textField_1.gridy = 16;
		panel.add(textField_1, gbc_textField_1);
		textField_1.setVisible(false);
		
		textField_0 = new JTextField();
		textField_0.setHorizontalAlignment(SwingConstants.CENTER);
		textField_0.setBackground(new Color(0,255,0));
		textField_0.setEditable(false);
		GridBagConstraints gbc_textField_0 = new GridBagConstraints();
		gbc_textField_0.insets = new Insets(0, 0, 5, 0);
		gbc_textField_0.fill = GridBagConstraints.BOTH;
		gbc_textField_0.gridx = 0;
		gbc_textField_0.gridy = 17;
		panel.add(textField_0, gbc_textField_0);
		textField_0.setColumns(10);
		textField_0.setVisible(false);
		
		txtTotalClientThreads = new JTextField();
		txtTotalClientThreads.setForeground(Color.WHITE);
		txtTotalClientThreads.setBackground(Color.BLACK);
		txtTotalClientThreads.setFont(new Font("Tahoma", Font.BOLD, 15));
		txtTotalClientThreads.setHorizontalAlignment(SwingConstants.CENTER);
		txtTotalClientThreads.setEditable(false);
		txtTotalClientThreads.setText("TOTAL CLIENT THREADS RUNNING");
		GridBagConstraints gbc_txtTotalClientThreads = new GridBagConstraints();
		gbc_txtTotalClientThreads.fill = GridBagConstraints.BOTH;
		gbc_txtTotalClientThreads.gridx = 0;
		gbc_txtTotalClientThreads.gridy = 18;
		panel.add(txtTotalClientThreads, gbc_txtTotalClientThreads);
		txtTotalClientThreads.setColumns(15);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 5));
		panel_2.setBounds(332, 11, 452, 612);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_3.setBounds(30, 64, 393, 101);
		panel_2.add(panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{30, 30, 30, 30, 30, 0};
		gbl_panel_3.rowHeights = new int[]{85, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		txtClientsCurrentlyLoggin = new JTextField();
		txtClientsCurrentlyLoggin.setText("CLIENTS CURRENTLY LOGGING IN");
		txtClientsCurrentlyLoggin.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtClientsCurrentlyLoggin.setEditable(false);
		txtClientsCurrentlyLoggin.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_txtClientsCurrentlyLoggin = new GridBagConstraints();
		gbc_txtClientsCurrentlyLoggin.insets = new Insets(0, 0, 0, 5);
		gbc_txtClientsCurrentlyLoggin.fill = GridBagConstraints.BOTH;
		gbc_txtClientsCurrentlyLoggin.gridx = 0;
		gbc_txtClientsCurrentlyLoggin.gridy = 0;
		panel_3.add(txtClientsCurrentlyLoggin, gbc_txtClientsCurrentlyLoggin);
		txtClientsCurrentlyLoggin.setColumns(10);
		
		textField_18 = new JTextField();
		textField_18.setFont(new Font("Tahoma", Font.BOLD, 18));
		textField_18.setHorizontalAlignment(SwingConstants.CENTER);
		textField_18.setText("0");
		textField_18.setEditable(false);
		GridBagConstraints gbc_textField_18 = new GridBagConstraints();
		gbc_textField_18.gridwidth = 4;
		gbc_textField_18.insets = new Insets(0, 0, 0, 5);
		gbc_textField_18.fill = GridBagConstraints.BOTH;
		gbc_textField_18.gridx = 1;
		gbc_textField_18.gridy = 0;
		panel_3.add(textField_18, gbc_textField_18);
		
		panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_4.setBounds(30, 195, 393, 101);
		panel_2.add(panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{30, 30, 30, 30, 30, 0};
		gbl_panel_4.rowHeights = new int[]{85, 0};
		gbl_panel_4.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		txtClienclientsWaitingTo = new JTextField();
		txtClienclientsWaitingTo.setText("CLIENTS WAITING TO JOIN LOBBY");
		txtClienclientsWaitingTo.setHorizontalAlignment(SwingConstants.CENTER);
		txtClienclientsWaitingTo.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtClienclientsWaitingTo.setEditable(false);
		txtClienclientsWaitingTo.setColumns(10);
		GridBagConstraints gbc_txtClienclientsWaitingTo = new GridBagConstraints();
		gbc_txtClienclientsWaitingTo.fill = GridBagConstraints.BOTH;
		gbc_txtClienclientsWaitingTo.insets = new Insets(0, 0, 0, 5);
		gbc_txtClienclientsWaitingTo.gridx = 0;
		gbc_txtClienclientsWaitingTo.gridy = 0;
		panel_4.add(txtClienclientsWaitingTo, gbc_txtClienclientsWaitingTo);
		
		textField_19 = new JTextField();
		textField_19.setFont(new Font("Tahoma", Font.BOLD, 18));
		textField_19.setHorizontalAlignment(SwingConstants.CENTER);
		textField_19.setText("0");
		textField_19.setEditable(false);
		GridBagConstraints gbc_textField_19 = new GridBagConstraints();
		gbc_textField_19.insets = new Insets(0, 0, 0, 5);
		gbc_textField_19.fill = GridBagConstraints.BOTH;
		gbc_textField_19.gridwidth = 4;
		gbc_textField_19.gridx = 1;
		gbc_textField_19.gridy = 0;
		panel_4.add(textField_19, gbc_textField_19);
		
		panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_5.setBounds(30, 324, 393, 101);
		panel_2.add(panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{30, 30, 30, 30, 30, 0};
		gbl_panel_5.rowHeights = new int[]{85, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		txtClientsInWaiting = new JTextField();
		txtClientsInWaiting.setText("CLIENTS IN WAITING LOBBY");
		txtClientsInWaiting.setHorizontalAlignment(SwingConstants.CENTER);
		txtClientsInWaiting.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtClientsInWaiting.setEditable(false);
		txtClientsInWaiting.setColumns(10);
		GridBagConstraints gbc_txtClientsInWaiting = new GridBagConstraints();
		gbc_txtClientsInWaiting.fill = GridBagConstraints.BOTH;
		gbc_txtClientsInWaiting.insets = new Insets(0, 0, 0, 5);
		gbc_txtClientsInWaiting.gridx = 0;
		gbc_txtClientsInWaiting.gridy = 0;
		panel_5.add(txtClientsInWaiting, gbc_txtClientsInWaiting);
		
		textField_21 = new JTextField();
		textField_21.setHorizontalAlignment(SwingConstants.CENTER);
		textField_21.setText("0");
		textField_21.setFont(new Font("Tahoma", Font.BOLD, 18));
		textField_21.setEditable(false);
		GridBagConstraints gbc_textField_21 = new GridBagConstraints();
		gbc_textField_21.insets = new Insets(0, 0, 0, 5);
		gbc_textField_21.fill = GridBagConstraints.BOTH;
		gbc_textField_21.gridwidth = 4;
		gbc_textField_21.gridx = 1;
		gbc_textField_21.gridy = 0;
		panel_5.add(textField_21, gbc_textField_21);
		
		panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_6.setBounds(30, 453, 393, 101);
		panel_2.add(panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[]{30, 30, 30, 30, 30, 0};
		gbl_panel_6.rowHeights = new int[]{85, 0};
		gbl_panel_6.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		txtClientsPlayingA = new JTextField();
		txtClientsPlayingA.setText("CLIENTS PLAYING A GAME");
		txtClientsPlayingA.setHorizontalAlignment(SwingConstants.CENTER);
		txtClientsPlayingA.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtClientsPlayingA.setEditable(false);
		txtClientsPlayingA.setColumns(10);
		GridBagConstraints gbc_txtClientsPlayingA = new GridBagConstraints();
		gbc_txtClientsPlayingA.fill = GridBagConstraints.BOTH;
		gbc_txtClientsPlayingA.insets = new Insets(0, 0, 0, 5);
		gbc_txtClientsPlayingA.gridx = 0;
		gbc_txtClientsPlayingA.gridy = 0;
		panel_6.add(txtClientsPlayingA, gbc_txtClientsPlayingA);
		
		textField_23 = new JTextField();
		textField_23.setText("0");
		textField_23.setFont(new Font("Tahoma", Font.BOLD, 18));
		textField_23.setHorizontalAlignment(SwingConstants.CENTER);
		textField_23.setEditable(false);
		GridBagConstraints gbc_textField_23 = new GridBagConstraints();
		gbc_textField_23.insets = new Insets(0, 0, 0, 5);
		gbc_textField_23.fill = GridBagConstraints.BOTH;
		gbc_textField_23.gridwidth = 4;
		gbc_textField_23.gridx = 1;
		gbc_textField_23.gridy = 0;
		panel_6.add(textField_23, gbc_textField_23);
	}
	
	/**
	 * This method runs everytime the serverDataCollection object changes, and everytime the ServerMain.threads changes in size!
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		ArrayList<JTextField> stack = new ArrayList<JTextField>();
		stack.add(textField_0);
		stack.add(textField_1);
		stack.add(textField_2);
		stack.add(textField_3);
		stack.add(textField_4);
		stack.add(textField_5);
		stack.add(textField_6);
		stack.add(textField_7);
		stack.add(textField_8);
		stack.add(textField);
		stack.add(textField_9);
		stack.add(textField_10);
		stack.add(textField_11);
		stack.add(textField_12);
		stack.add(textField_13);
		stack.add(textField_14);
		stack.add(textField_15);
		stack.add(textField_16);
		
		/*
		 * Set the number of bars showing clients logged on.
		 */
		for(int i=0 ; i < stack.size() ; i++) {
			stack.get(i).setVisible(false);
		}
		
		for(int i=0 ; i < ServerMain.threads.size() ; i++) {
			if(i >= stack.size()) {
			} else {
				stack.get(i).setVisible(true);
			}
		}
		
		/*
		 * Set the number of clients logging on.
		 */
		int loggingOn = (ServerMain.threads.size() - this.serverMain.serverDataCollection.getPlayersList().size());
		textField_18.setText("" + loggingOn);
		
		/*
		 * Set the number of clients in a game.
		 */
		int inGame = this.serverMain.serverDataCollection.getGameList().size() * 2;
		textField_23.setText("" + inGame);
		
		/*
		 * Set number of players in lobby.
		 */
		int inLobby = 0;
		for(int i=0 ; i < this.serverMain.serverDataCollection.getLobbyList().size() ; i++) {
			inLobby++;
			if(this.serverMain.serverDataCollection.getLobbyList().get(i).getGuest() != null) {
				inLobby++;
			}
		}
		textField_21.setText("" + inLobby);
		
		
		/*
		 * Set the number of clients waiting to join a lobby.
		 */
		int waitingToJoinLobby = ServerMain.threads.size() - loggingOn - inLobby - inGame;
		textField_19.setText("" + waitingToJoinLobby);
		
		frame.repaint();
		frame.setVisible(true);
		
	}
}