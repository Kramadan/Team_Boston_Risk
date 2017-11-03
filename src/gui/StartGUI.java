package gui;

import java.awt.Color;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.OverlayLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

import client.AuthentificationFailException;
import client.Client;
import client.Data;
import communicationObjects.Attack;
import communicationObjects.Draft;
import communicationObjects.EndTurn;
import communicationObjects.Forfeit;
import communicationObjects.Fortify;
import communicationObjects.LeaveLobby;
import communicationObjects.Result;
import riskGame.Country;
import riskGame.Player;

/**
 * This is the main GUI file that contains the code that is responsible for
 * handling the clients UI experience. This has been created to be a RISK game
 * representation.
 * 
 * This class implements observer so that it can observe the back-end and the
 * server
 * 
 * @author Abdikhaliq Timer
 */
public class StartGUI extends JFrame implements Observer, IStartGUI {

	
	// ---------------------------------------------------------------------------------------------------------------------------------------
	// Field Variables
	// ---------------------------------------------------------------------------------------------------------------------------------------
	private static final long serialVersionUID = 3604478573348126988L;
	// the connected client
	Client client;
	// data associated with client
	Data data;
	// Colors
	Color colorMine = Color.BLUE;
	Color colorOther = Color.RED;
	int xMouse, yMouse;
	audioPlayMusic song = new audioPlayMusic();
	Font riskFont = new Font("URW Chancery L", 1, 18);

	public Player me;
	public Player other;

	// creates tutorial slides
	public TutorialSlides tutorialSlides;

	// Just text that says "number of troops"
	public JLabel confirmPhaseLabel;
	public JLabel stateShowLabel;
	public JLabel whoseGoIsIt;
	
	public JLabel name1;
	public JLabel name2;

	public JToggleButton nightModeButton;
	public JLabel numberOfTroopsUpdateLabel;
	public JLabel backgroundImgLabel;
	public JLabel closeJLabel1;
	public JLabel moveScreenJLabel;
	public JLabel backgroundNightImgLabel;
	public JLabel chatJLabel;
	public JToggleButton musicButton;
	public JLabel minusJLabel1;

	public JButton confirmPhaseJButton;
	public JButton forfitJButton;
	public JButton endDraftJButton;
	public JButton endAttackJButton;

	public JButton saveGameJButton;

	public JLayeredPane jLayeredPane1;
	public JLayeredPane jLayeredPane2;
	public JLayeredPane jLayeredPane3;

	public JLabel alaskaTroops;
	public JLabel albertaTroops;
	public JLabel centralAmericaTroops;
	public JLabel easternUnitedStatesTroops;
	public JLabel greenlandTroops;
	public JLabel northWestTerritoryTroops;
	public JLabel ontarioTroops;
	public JLabel quebecTroops;
	public JLabel westernUnitedStatesTroops;
	public JLabel argentinaTroops;
	public JLabel brazilTroops;
	public JLabel peruTroops;
	public JLabel venezuelaTroops;
	public JLabel greatBritainTroops;
	public JLabel icelandTroops;
	public JLabel northernEuropeTroops;
	public JLabel scandinaviaTroops;
	public JLabel southernEuropeTroops;
	public JLabel ukraineTroops;
	public JLabel westernEuropeTroops;
	public JLabel congoTroops;
	public JLabel eastAfricaTroops;
	public JLabel egyptTroops;
	public JLabel madagascarTroops;
	public JLabel northAfricaTroops;
	public JLabel southAfricaTroops;
	public JLabel afghanistanTroops;
	public JLabel chinaTroops;
	public JLabel indiaTroops;
	public JLabel irkutskTroops;
	public JLabel japanTroops;
	public JLabel kamchatkaTroops;
	public JLabel middleEastTroops;
	public JLabel mongoliaTroops;
	public JLabel siamTroops;
	public JLabel siberiaTroops;
	public JLabel uralTroops;
	public JLabel yakutskTroops;
	public JLabel easternAustraliaTroops;
	public JLabel indonesiaTroops;
	public JLabel newGuineaTroops;
	public JLabel westernAustraliaTroops;

	public WideComboBox EastAfricaJComboBox2;
	public WideComboBox EgyptJComboBox2;
	public WideComboBox NorthWestTerritory;
	public WideComboBox afghanistanJComboBox;
	public WideComboBox alaskaJComboBox;
	public WideComboBox albertaJComboBox;
	public WideComboBox argentinaJComboBox;
	public WideComboBox brazilJComboBox1;
	public WideComboBox centralAmericaJComboBox;
	public WideComboBox chinaJComboBox;
	public WideComboBox congoJComboBox3;
	public WideComboBox easternAustraliaJComboBox;
	public WideComboBox easternUnitedStatesJComboBox;
	public WideComboBox greatBritainJComboBox1;
	public WideComboBox greenlandJComboBox4;
	public WideComboBox icelandJComboBox;
	public WideComboBox indiaJComboBox;
	public WideComboBox indonesiaJComboBox;
	public WideComboBox irkutskJComboBox;
	public WideComboBox kamchatkaJComboBox;
	public WideComboBox madagascarJComboBox1;
	public WideComboBox middleEastJComboBox;
	public WideComboBox mongoliaJComboBox;
	public WideComboBox newGuineaJComboBox;
	public WideComboBox northAfricaJComboBox2;
	public WideComboBox northernEuropeJComboBox;
	public WideComboBox ontarioJComboBox;
	public WideComboBox peruJComboBox1;
	public WideComboBox quebecJComboBox;
	public WideComboBox scandinaviaJComboBox;
	public WideComboBox siamJComboBox;
	public WideComboBox siberiaJComboBox;
	public WideComboBox southAfricaJComboBox4;
	public WideComboBox southernEuropeJComboBox;
	public WideComboBox ukraineJComboBox;
	public WideComboBox uralJComboBox;
	public WideComboBox venezuela1;
	public WideComboBox westernAustraliaJComboBox;
	public WideComboBox westernEuropeJComboBox;
	public WideComboBox westernUnitedStatesJComboBox;
	public WideComboBox yakutskJComboBox;
	public WideComboBox japanJComboBox;

	public Country alaska;
	public Country alberta;
	public Country centralAmerica;
	public Country easternUnitedStates;
	public Country greenland;
	public Country northWestTerritory;
	public Country ontario;
	public Country quebec;
	public Country westernUnitedStates;
	public Country argentina;
	public Country brazil;
	public Country peru;
	public Country venezuela;
	public Country greatBritain;
	public Country iceland;
	public Country northernEurope;
	public Country scandinavia;
	public Country southernEurope;
	public Country ukraine;
	public Country westernEurope;
	public Country congo;
	public Country eastAfrica;
	public Country egypt;
	public Country madagascar;
	public Country northAfrica;
	public Country southAfrica;
	public Country afghanistan;
	public Country china;
	public Country india;
	public Country irkutsk;
	public Country japan;
	public Country kamchatka;
	public Country middleEast;
	public Country mongolia;
	public Country siam;
	public Country siberia;
	public Country ural;
	public Country yakutsk;
	public Country easternAustralia;
	public Country indonesia;
	public Country newGuinea;
	public Country westernAustralia;

	public JTextArea historyDisplayJTextArea;

	/**
	 * Lists where we will store our buttons and labels - for easy re-use
	 * 
	 */
	public JComboBox<String> cb;
	// List for comboboxes
	public Map<JComboBox<String>, Country> comboBoxesToCountryMap = new HashMap<>();
	// List for all the labels
	public Map<JLabel, Country> jLabelToCountryMap = new HashMap<>();

	/**
	 * These are treeMaps which we will be sending to the other client
	 */
	public TreeMap<Country, Integer> draftToSend = new TreeMap<>();

	/**
	 * Constructor to start the GUI.
	 * This is what the client will call in order to play a game.
	 * It combines the game engine to create a game of Risk.
	 * 
	 * @param client The client which is connecting
	 */
	public StartGUI(Client client) {
		//make sure that the same nimbus feel is on all computers
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(StartGUI.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(StartGUI.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(StartGUI.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(StartGUI.class.getName()).log(Level.SEVERE, null, ex);
		}

		this.client = client;
		this.data = client.getData();
		this.me = data.getGame().getMe();
		this.other = data.getGame().getOther();
		data.addObserver(this);

		initComponents();
		song.startSong();
	}

	/**
	 * This method initialises all the components, creating our Risk GUI
	 */
	@SuppressWarnings("unchecked")
	private void initComponents() {
		jLayeredPane1 = new JLayeredPane();
		jLayeredPane2 = new JLayeredPane();
		jLayeredPane3 = new JLayeredPane();

		westernUnitedStatesJComboBox = new WideComboBox();
		easternUnitedStatesJComboBox = new WideComboBox();
		NorthWestTerritory = new WideComboBox();
		ontarioJComboBox = new WideComboBox();
		albertaJComboBox = new WideComboBox();
		quebecJComboBox = new WideComboBox();
		alaskaJComboBox = new WideComboBox();
		madagascarJComboBox1 = new WideComboBox();
		EgyptJComboBox2 = new WideComboBox();
		EastAfricaJComboBox2 = new WideComboBox();
		congoJComboBox3 = new WideComboBox();
		southAfricaJComboBox4 = new WideComboBox();
		northAfricaJComboBox2 = new WideComboBox();
		centralAmericaJComboBox = new WideComboBox();
		argentinaJComboBox = new WideComboBox();
		venezuela1 = new WideComboBox();
		brazilJComboBox1 = new WideComboBox();
		peruJComboBox1 = new WideComboBox();
		mongoliaJComboBox = new WideComboBox();
		chinaJComboBox = new WideComboBox();
		kamchatkaJComboBox = new WideComboBox();
		indiaJComboBox = new WideComboBox();
		irkutskJComboBox = new WideComboBox();
		siamJComboBox = new WideComboBox();
		yakutskJComboBox = new WideComboBox();
		easternAustraliaJComboBox = new WideComboBox();
		indonesiaJComboBox = new WideComboBox();
		newGuineaJComboBox = new WideComboBox();
		westernAustraliaJComboBox = new WideComboBox();
		southernEuropeJComboBox = new WideComboBox();
		siberiaJComboBox = new WideComboBox();
		uralJComboBox = new WideComboBox();
		ukraineJComboBox = new WideComboBox();
		middleEastJComboBox = new WideComboBox();
		afghanistanJComboBox = new WideComboBox();
		northernEuropeJComboBox = new WideComboBox();
		icelandJComboBox = new WideComboBox();
		greenlandJComboBox4 = new WideComboBox();
		westernEuropeJComboBox = new WideComboBox();
		scandinaviaJComboBox = new WideComboBox();
		greatBritainJComboBox1 = new WideComboBox();
		japanJComboBox = new WideComboBox();

		chatJLabel = new JLabel();
		closeJLabel1 = new JLabel();
		minusJLabel1 = new JLabel();
		moveScreenJLabel = new JLabel();
		confirmPhaseLabel = new JLabel();
		musicButton = new JToggleButton();
		nightModeButton = new JToggleButton();
		stateShowLabel = new JLabel();
		numberOfTroopsUpdateLabel = new JLabel();
		backgroundImgLabel = new JLabel();
		backgroundNightImgLabel = new JLabel();
		whoseGoIsIt = new JLabel();

		confirmPhaseJButton = new JButton();
		forfitJButton = new JButton();
		endDraftJButton = new JButton();
		endAttackJButton = new JButton();

		saveGameJButton = new JButton();
		name1 = new JLabel();
		name2 = new JLabel();

		alaska = riskGame.Map.findCountryObject("Alaska");
		alberta = riskGame.Map.findCountryObject("Alberta");
		centralAmerica = riskGame.Map.findCountryObject("Central America");
		easternUnitedStates = riskGame.Map.findCountryObject("Eastern United States");
		greenland = riskGame.Map.findCountryObject("Greenland");
		northWestTerritory = riskGame.Map.findCountryObject("Northwest Territory");
		ontario = riskGame.Map.findCountryObject("Ontario");
		quebec = riskGame.Map.findCountryObject("Quebec");
		westernUnitedStates = riskGame.Map.findCountryObject("Western United States");
		argentina = riskGame.Map.findCountryObject("Argentina");
		brazil = riskGame.Map.findCountryObject("Brazil");
		peru = riskGame.Map.findCountryObject("Peru");
		venezuela = riskGame.Map.findCountryObject("Venezuela");
		greatBritain = riskGame.Map.findCountryObject("Great Britain");
		iceland = riskGame.Map.findCountryObject("Iceland");
		northernEurope = riskGame.Map.findCountryObject("Northern Europe");
		scandinavia = riskGame.Map.findCountryObject("Scandinavia");
		southernEurope = riskGame.Map.findCountryObject("Southern Europe");
		ukraine = riskGame.Map.findCountryObject("Ukraine");
		westernEurope = riskGame.Map.findCountryObject("Western Europe");
		congo = riskGame.Map.findCountryObject("Congo");
		eastAfrica = riskGame.Map.findCountryObject("East Africa");
		egypt = riskGame.Map.findCountryObject("Egypt");
		madagascar = riskGame.Map.findCountryObject("Madagascar");
		northAfrica = riskGame.Map.findCountryObject("North Africa");
		southAfrica = riskGame.Map.findCountryObject("South Africa");
		afghanistan = riskGame.Map.findCountryObject("Afghanistan");
		china = riskGame.Map.findCountryObject("China");
		india = riskGame.Map.findCountryObject("India");
		irkutsk = riskGame.Map.findCountryObject("Irkutsk");
		japan = riskGame.Map.findCountryObject("Japan");
		kamchatka = riskGame.Map.findCountryObject("Kamchatka");
		middleEast = riskGame.Map.findCountryObject("Middle East");
		mongolia = riskGame.Map.findCountryObject("Mongolia");
		siam = riskGame.Map.findCountryObject("Siam");
		siberia = riskGame.Map.findCountryObject("Siberia");
		ural = riskGame.Map.findCountryObject("Ural");
		yakutsk = riskGame.Map.findCountryObject("Yakutsk");
		easternAustralia = riskGame.Map.findCountryObject("Eastern Australia");
		indonesia = riskGame.Map.findCountryObject("Indonesia");
		newGuinea = riskGame.Map.findCountryObject("New Guinea");
		westernAustralia = riskGame.Map.findCountryObject("Western Australia");

		alaskaTroops = new JLabel();
		albertaTroops = new JLabel();
		centralAmericaTroops = new JLabel();
		easternUnitedStatesTroops = new JLabel();
		greenlandTroops = new JLabel();
		northWestTerritoryTroops = new JLabel();
		ontarioTroops = new JLabel();
		quebecTroops = new JLabel();
		westernUnitedStatesTroops = new JLabel();
		argentinaTroops = new JLabel();
		brazilTroops = new JLabel();
		peruTroops = new JLabel();
		venezuelaTroops = new JLabel();
		greatBritainTroops = new JLabel();
		icelandTroops = new JLabel();
		northernEuropeTroops = new JLabel();
		scandinaviaTroops = new JLabel();
		southernEuropeTroops = new JLabel();
		ukraineTroops = new JLabel();
		westernEuropeTroops = new JLabel();
		congoTroops = new JLabel();
		eastAfricaTroops = new JLabel();
		egyptTroops = new JLabel();
		madagascarTroops = new JLabel();
		northAfricaTroops = new JLabel();
		southAfricaTroops = new JLabel();
		afghanistanTroops = new JLabel();
		chinaTroops = new JLabel();
		indiaTroops = new JLabel();
		irkutskTroops = new JLabel();
		japanTroops = new JLabel();
		kamchatkaTroops = new JLabel();
		middleEastTroops = new JLabel();
		mongoliaTroops = new JLabel();
		siamTroops = new JLabel();
		siberiaTroops = new JLabel();
		uralTroops = new JLabel();
		yakutskTroops = new JLabel();
		easternAustraliaTroops = new JLabel();
		indonesiaTroops = new JLabel();
		newGuineaTroops = new JLabel();
		westernAustraliaTroops = new JLabel();

		historyDisplayJTextArea = new JTextArea();
		JScrollPane historyScrollJScrollPane = new JScrollPane(historyDisplayJTextArea);

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// Below sets the design of the GUI
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setUndecorated(true);
		getContentPane().setLayout(new OverlayLayout(getContentPane()));

		jLayeredPane1.setLayout(new OverlayLayout(jLayeredPane1));
		jLayeredPane2.setLayout(new OverlayLayout(jLayeredPane2));
		jLayeredPane1.add(jLayeredPane2);
		jLayeredPane3.setLayout(new AbsoluteLayout());

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - western US -HAS UPDATES COMMENTED OUT
		// ---------------------------------------------------------------------------------------------------------------------------------------

		// setup the visibility, color, row count + information within the rows
		// for this button
		// it also sets up how the button will look.
		setupComboBox(westernUnitedStatesJComboBox, westernUnitedStates, "Western United States", 270, 240, 40, -1);

		westernUnitedStatesJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				westernUnitedStatesJComboBoxActionPerformed(evt);
			}
		});

		// absolute constraints: An object that encapsulates position and
		// (optionally) size for Absolute positioning of components.
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - eastern US
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(easternUnitedStatesJComboBox, easternUnitedStates, "Eastern United States", 340, 260, 40, -1);

		easternUnitedStatesJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				easternUnitedStatesJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - Northwest Territory
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(NorthWestTerritory, northWestTerritory, "Northwest Territory", 310, 160, 40, -1);
		NorthWestTerritory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				NorthWestTerritoryActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - ontario
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(ontarioJComboBox, ontario, "Ontario", 360, 200, 40, -1);
		ontarioJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ontarioJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - alberta
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(albertaJComboBox, alberta, "Alberta", 280, 200, 40, -1);
		albertaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				albertaJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - quebec
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(quebecJComboBox, quebec, "Quebec", 430, 200, 40, -1);
		quebecJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				quebecJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - alaska
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(alaskaJComboBox, alaska, "Alaska", 230, 160, 40, -1);
		alaskaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				alaskaJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - madagascar
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(madagascarJComboBox1, madagascar, "Madagascar", 790, 480, 40, -1);
		madagascarJComboBox1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				madagascarJComboBox1ActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - egypt
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(EgyptJComboBox2, egypt, "Egypt", 700, 300, 40, -1);
		EgyptJComboBox2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				EgyptJComboBox2ActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - eastAfrica
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(EastAfricaJComboBox2, eastAfrica, "East Africa", 730, 360, 40, -1);
		EastAfricaJComboBox2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				EastAfricaJComboBox2ActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - congo
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(congoJComboBox3, congo, "Congo", 700, 400, 40, -1);
		congoJComboBox3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				congoJComboBox3ActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - southAfrica
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(southAfricaJComboBox4, southAfrica, "South Africa", 710, 490, 40, -1);
		southAfricaJComboBox4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				southAfricaJComboBox4ActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - North Africa
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(northAfricaJComboBox2, northAfrica, "North Africa", 630, 340, 40, -1);
		northAfricaJComboBox2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				northAfricaJComboBox2ActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - "Central America"
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(centralAmericaJComboBox, centralAmerica, "Central America", 280, 330, 40, -1);
		centralAmericaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				centralAmericaJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - argentina
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(argentinaJComboBox, argentina, "Argentina", 410, 530, 40, -1);
		argentinaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				argentinaJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - Venezuela
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(venezuela1, venezuela, "Venezuela", 380, 380, 40, -1);
		venezuela1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				venezuela1ActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - Brazil
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(brazilJComboBox1, brazil, "Brazil", 440, 440, 40, -1);
		brazilJComboBox1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				brazilJComboBox1ActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - Peru
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(peruJComboBox1, peru, "Peru", 380, 460, 40, -1);
		peruJComboBox1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				peruJComboBox1ActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - mongolia
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(mongoliaJComboBox, mongolia, "Mongolia", 970, 240, 40, -1);
		mongoliaJComboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				mongoliaJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - China
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(chinaJComboBox, china, "China", 990, 290, 40, -1);
		chinaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				chinaJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - kamchatka
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(kamchatkaJComboBox, kamchatka, "Kamchatka", 1080, 160, 40, -1);
		kamchatkaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				kamchatkaJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - india
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(indiaJComboBox, india, "India", 900, 310, 40, -1);
		indiaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				indiaJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - irkutsk
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(irkutskJComboBox, irkutsk, "Irkutsk", 950, 200, 40, -1);
		irkutskJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				irkutskJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - siam
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(siamJComboBox, siam, "Siam", 970, 330, 40, -1);
		siamJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				siamJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - yakutsk
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(yakutskJComboBox, yakutsk, "Yakutsk", 970, 160, 40, -1);
		yakutskJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				yakutskJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - easternAustralia
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(easternAustraliaJComboBox, easternAustralia, "Eastern Australia", 1110, 490, 40, -1);
		easternAustraliaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				easternAustraliaJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - indonesia
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(indonesiaJComboBox, indonesia, "Indonesia", 1020, 400, 40, -1);
		indonesiaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				indonesiaJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - newGuinea
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(newGuineaJComboBox, newGuinea, "New Guinea", 1120, 420, 40, -1);
		newGuineaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				newGuineaJComboBoxActionPerformed(evt);
			}
		});
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - westernAustralia
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(westernAustraliaJComboBox, westernAustralia, "Western Australia", 1040, 510, 40, -1);
		westernAustraliaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				westernAustraliaJComboBoxActionPerformed(evt);
			}
		});
		
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - southernEurope
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(southernEuropeJComboBox, southernEurope, "Southern Europe", 690, 240, 40, -1);
		southernEuropeJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				southernEuropeJComboBoxActionPerformed(evt);
			}
		});
		
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - siberia
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(siberiaJComboBox, siberia, "Siberia", 900, 170, 40, -1);
		siberiaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				siberiaJComboBoxActionPerformed(evt);
			}
		});
		
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - ural
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(uralJComboBox, ural, "Ural", 830, 180, 40, -1);
		uralJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				uralJComboBoxActionPerformed(evt);
			}
		});
		

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - ukraine
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(ukraineJComboBox, ukraine, "Ukraine", 750, 190, 40, -1);
		ukraineJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ukraineJComboBoxActionPerformed(evt);
			}
		});
		
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - middleEast
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(middleEastJComboBox, middleEast, "Middle East", 770, 280, 40, -1);
		middleEastJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				middleEastJComboBoxActionPerformed(evt);
			}
		});
		

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - afghanistan
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(afghanistanJComboBox, afghanistan, "Afghanistan", 840, 230, 40, -1);
		afghanistanJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				afghanistanJComboBoxActionPerformed(evt);
			}
		});
		

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - Northern Europe
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(northernEuropeJComboBox, northernEurope, "Northern Europe", 670, 210, 40, -1);
		northernEuropeJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				northernEuropeJComboBoxActionPerformed(evt);
			}
		});
	
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - Iceland
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(icelandJComboBox, iceland, "Iceland", 590, 160, 40, -1);
		icelandJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				icelandJComboBoxActionPerformed(evt);
			}
		});
		

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - greenland
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(greenlandJComboBox4, greenland, "Greenland", 530, 130, 40, -1);
		greenlandJComboBox4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				greenlandJComboBox4ActionPerformed(evt);
			}
		});
		

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - westernEurope
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(westernEuropeJComboBox, westernEurope, "Western Europe", 620, 250, 40, -1);

		westernEuropeJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				westernEuropeJComboBoxActionPerformed(evt);
			}
		});
		

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - scandinavia
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(scandinaviaJComboBox, scandinavia, "Scandinavia", 680, 170, 40, -1);
		scandinaviaJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				scandinaviaJComboBoxActionPerformed(evt);
			}
		});
		

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - japan
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(japanJComboBox, japan, "Japan", 1080, 270, 40, -1);
		japanJComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				japanJComboBoxActionPerformed(evt);
			}

		});
		

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ComboBox for each country - greatBritain
		// ---------------------------------------------------------------------------------------------------------------------------------------
		setupComboBox(greatBritainJComboBox1, greatBritain, "Great Britain", 610, 200, 40, -1);
		greatBritainJComboBox1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				greatBritainJComboBox1ActionPerformed(evt);
			}
		});
		
		// ********************************************************************************************************************************************
		// JLabel showing number of troops next to combobox
		// ********************************************************************************************************************************************

		setupTerritoriesTroopLabel(alaskaTroops, alaska, 175, 165);
		setupTerritoriesTroopLabel(albertaTroops, alberta, 280, 200);
		setupTerritoriesTroopLabel(centralAmericaTroops, centralAmerica, 280, 330);
		setupTerritoriesTroopLabel(easternUnitedStatesTroops, easternUnitedStates, 340, 260);
		setupTerritoriesTroopLabel(greenlandTroops, greenland, 530, 130);
		setupTerritoriesTroopLabel(northWestTerritoryTroops, northWestTerritory, 310, 160);
		setupTerritoriesTroopLabel(ontarioTroops, ontario, 360, 200);
		setupTerritoriesTroopLabel(quebecTroops, quebec, 430, 200);
		setupTerritoriesTroopLabel(westernUnitedStatesTroops, westernUnitedStates, 270, 240);
		setupTerritoriesTroopLabel(argentinaTroops, argentina, 410, 530);
		setupTerritoriesTroopLabel(brazilTroops, brazil, 440, 440);
		setupTerritoriesTroopLabel(peruTroops, peru, 380, 460);
		setupTerritoriesTroopLabel(venezuelaTroops, venezuela, 380, 380);
		setupTerritoriesTroopLabel(greatBritainTroops, greatBritain, 610, 200);
		setupTerritoriesTroopLabel(icelandTroops, iceland, 590, 160);
		setupTerritoriesTroopLabel(northernEuropeTroops, northernEurope, 670, 210);
		setupTerritoriesTroopLabel(scandinaviaTroops, scandinavia, 680, 170);
		setupTerritoriesTroopLabel(southernEuropeTroops, southernEurope, 690, 240);
		setupTerritoriesTroopLabel(ukraineTroops, ukraine, 750, 190);
		setupTerritoriesTroopLabel(westernEuropeTroops, westernEurope, 620, 250);
		setupTerritoriesTroopLabel(congoTroops, congo, 700, 400);
		setupTerritoriesTroopLabel(eastAfricaTroops, eastAfrica, 730, 360);
		setupTerritoriesTroopLabel(egyptTroops, egypt, 700, 300);
		setupTerritoriesTroopLabel(madagascarTroops, madagascar, 790, 480);
		setupTerritoriesTroopLabel(northAfricaTroops, northAfrica, 630, 340);
		setupTerritoriesTroopLabel(southAfricaTroops, southAfrica, 710, 490);
		setupTerritoriesTroopLabel(afghanistanTroops, afghanistan, 840, 230);
		setupTerritoriesTroopLabel(chinaTroops, china, 990, 290);
		setupTerritoriesTroopLabel(indiaTroops, india, 900, 310);
		setupTerritoriesTroopLabel(irkutskTroops, irkutsk, 950, 200);
		setupTerritoriesTroopLabel(japanTroops, japan, 1080, 270);
		setupTerritoriesTroopLabel(kamchatkaTroops, kamchatka, 1080, 160);
		setupTerritoriesTroopLabel(middleEastTroops, middleEast, 770, 280);
		setupTerritoriesTroopLabel(mongoliaTroops, mongolia, 970, 240);
		setupTerritoriesTroopLabel(siamTroops, siam, 970, 330);
		setupTerritoriesTroopLabel(siberiaTroops, siberia, 900, 170);
		setupTerritoriesTroopLabel(uralTroops, ural, 830, 180);
		setupTerritoriesTroopLabel(yakutskTroops, yakutsk, 970, 160);
		setupTerritoriesTroopLabel(easternAustraliaTroops, easternAustralia, 1110, 490);
		setupTerritoriesTroopLabel(indonesiaTroops, indonesia, 1020, 400);
		setupTerritoriesTroopLabel(newGuineaTroops, newGuinea, 1120, 420);
		setupTerritoriesTroopLabel(westernAustraliaTroops, westernAustralia, 1040, 510);
		
		
		
		
		
		

		// ***************************************************************************************************************************************
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// Extra buttons
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ***************************************************************************************************************************************
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// ***************************************************************************************************************************************
		
		//displaying the names
		name1.setHorizontalAlignment(SwingConstants.RIGHT);
        name1.setText(me.getUserID());
        jLayeredPane3.add(name1,new AbsoluteConstraints(510, 65, 190, 20));

        name2.setText(other.getUserID());
        jLayeredPane3.add(name2, new AbsoluteConstraints(726, 67, 210, -1));

		
		
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// Buttons for ending turn + ending phases
		// ---------------------------------------------------------------------------------------------------------------------------------------

		// End Turn
		confirmPhaseJButton.setText("End my turn!");
		confirmPhaseJButton.setForeground(new Color(255, 255, 255));
		confirmPhaseJButton.setFont(new Font("URW Chancery L", 2, 18));
		confirmPhaseJButton.setOpaque(false);
		confirmPhaseJButton.setContentAreaFilled(false);
		confirmPhaseJButton.setBorderPainted(false);
		confirmPhaseJButton.setVisible(false);
		confirmPhaseJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				confirmPhaseActionPerformed(evt);
			}
		});
		jLayeredPane3.add(confirmPhaseJButton, new AbsoluteConstraints(1260, 320, 120, 30));

		// End Draft phase

		endDraftJButton.setText("enter attack phase");
		endDraftJButton.setForeground(new Color(255, 255, 255));
		endDraftJButton.setFont(new Font("URW Chancery L", 2, 16));
		endDraftJButton.setOpaque(false);
		endDraftJButton.setContentAreaFilled(false);
		endDraftJButton.setBorderPainted(false);
		endDraftJButton.setVisible(false);
		endDraftJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				endDraftActionPerformed(evt);
			}
		});
		jLayeredPane3.add(endDraftJButton, new AbsoluteConstraints(1250, 350, 140, 30));

		// End Attack

		endAttackJButton.setText("enter fortify stage");
		endAttackJButton.setForeground(new Color(255, 255, 255));
		endAttackJButton.setFont(new Font("URW Chancery L", 2, 18));
		endAttackJButton.setOpaque(false);
		endAttackJButton.setContentAreaFilled(false);
		endAttackJButton.setBorderPainted(false);
		endAttackJButton.setVisible(false);
		endAttackJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				endAttackActionPerformed(evt);
			}
		});
		jLayeredPane3.add(endAttackJButton, new AbsoluteConstraints(1250, 380, -1, 30));

		// Forfit
		forfitJButton.setFont(new Font("URW Chancery L", 2, 18));
		forfitJButton.setForeground(new Color(255, 255, 255));
		forfitJButton.setText("I give up");
		forfitJButton.setOpaque(false);
		forfitJButton.setContentAreaFilled(false);
		forfitJButton.setBorderPainted(false);
		forfitJButton.setVisible(false);
		forfitJButton.setVisible(true);
		forfitJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				forfeitActionPerformed(evt);
			}
		});
		jLayeredPane3.add(forfitJButton, new AbsoluteConstraints(1260, 410, 110, 40));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// label showing whose turn it is
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// TODO check code below
		if (me.IsTurn()) {
			whoseGoIsIt.setText("yes");
		} else {
			whoseGoIsIt.setText("no");
		}
		whoseGoIsIt.setFont(riskFont);
		whoseGoIsIt.setForeground(new Color(255, 255, 255));
		jLayeredPane3.add(whoseGoIsIt, new AbsoluteConstraints(52, 307, -1, 60));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// label that shows the current turn phase
		// ---------------------------------------------------------------------------------------------------------------------------------------
		stateShowLabel.setFont(riskFont);
		stateShowLabel.setForeground(Color.WHITE);
		stateShowLabel.setText(me.turnphase.getTurnPhase().toUpperCase());
		jLayeredPane3.add(stateShowLabel, new AbsoluteConstraints(40, 253, -1, -1));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// number of troops shown on screen
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// if its my turn show me the number of troops that I have
		if (me.IsTurn()) {
			numberOfTroopsUpdateLabel.setText("    " + me.getGivenTroops());
		}
		numberOfTroopsUpdateLabel.setFont(riskFont);
		numberOfTroopsUpdateLabel.setForeground(new Color(255, 255, 255));
		jLayeredPane3.add(numberOfTroopsUpdateLabel, new AbsoluteConstraints(30, 448, -1, 30));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// button to open chatroom
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// TODO: Link chatroom to this
		chatJLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		chatJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				chatJLabelMouseClicked(evt);
			}
		});
		jLayeredPane3.add(chatJLabel, new AbsoluteConstraints(1260, 280, 120, 30));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// close button
		// ---------------------------------------------------------------------------------------------------------------------------------------
		closeJLabel1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		closeJLabel1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				closeJLabel1MouseClicked(evt);
			}
		});
		jLayeredPane3.add(closeJLabel1, new AbsoluteConstraints(1338, 0, 50, 30));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// minimise button
		// ---------------------------------------------------------------------------------------------------------------------------------------
		minusJLabel1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		minusJLabel1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				minusJLabel1MouseClicked(evt);
			}
		});
		jLayeredPane3.add(minusJLabel1, new AbsoluteConstraints(1297, 0, 40, 30));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// label that allows you to drag the screen around
		// ---------------------------------------------------------------------------------------------------------------------------------------
		moveScreenJLabel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent evt) {
				moveScreenJLabelMouseDragged(evt);
			}
		});
		moveScreenJLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				moveScreenJLabelMousePressed(evt);
			}
		});
		jLayeredPane3.add(moveScreenJLabel, new AbsoluteConstraints(0, 0, 1400, 90));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// History text area TODO EDIT IN
		// ---------------------------------------------------------------------------------------------------------------------------------------
		historyDisplayJTextArea.setEditable(false);
		historyDisplayJTextArea.setOpaque(false);
		
		
		
		historyScrollJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jLayeredPane3.add(historyScrollJScrollPane, new AbsoluteConstraints(550, 600, 640, 100));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// Save game button - TODO add this
		// ---------------------------------------------------------------------------------------------------------------------------------------
		saveGameJButton.setText("Save Game");
		saveGameJButton.setFont(new Font("URW Chancery L", 2, 18));
		saveGameJButton.setForeground(new Color(255, 255, 255));
		saveGameJButton.setOpaque(false);
		saveGameJButton.setContentAreaFilled(false);
		saveGameJButton.setBorderPainted(false);
		saveGameJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				saveButtonActionPerformed(evt);
			}

		});
		jLayeredPane3.add(saveGameJButton, new AbsoluteConstraints(1280, 450, -1, -1));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// music button
		// ---------------------------------------------------------------------------------------------------------------------------------------
		musicButton.setContentAreaFilled(false);
		musicButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		musicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				musicButtonActionPerformed(evt);
			}
		});
		jLayeredPane3.add(musicButton, new AbsoluteConstraints(1260, 210, 120, 40));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// night mode button
		// ---------------------------------------------------------------------------------------------------------------------------------------
		nightModeButton.setContentAreaFilled(false);
		nightModeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		nightModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				nightModeButtonActionPerformed(evt);
			}
		});
		jLayeredPane3.add(nightModeButton, new AbsoluteConstraints(1260, 250, 130, 30));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// background Image
		// ---------------------------------------------------------------------------------------------------------------------------------------
		backgroundImgLabel.setIcon(new ImageIcon(getClass().getResource("/image/background.png")));
		jLayeredPane3.add(backgroundImgLabel, new AbsoluteConstraints(0, 0, -1, -1));

		// ---------------------------------------------------------------------------------------------------------------------------------------
		// background Night Image
		// ---------------------------------------------------------------------------------------------------------------------------------------
		backgroundNightImgLabel.setIcon(new ImageIcon(getClass().getResource("/image/backgroundNIGHT.png")));
		jLayeredPane3.add(backgroundNightImgLabel, new AbsoluteConstraints(0, 0, -1, -1));

		
		// ---------------------------------------------------------------------------------------------------------------------------------------
		// add it all to our main panel
		// ---------------------------------------------------------------------------------------------------------------------------------------
		jLayeredPane1.add(jLayeredPane3);
		getContentPane().add(jLayeredPane1);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);

		tutorialSlides = new TutorialSlides();
		tutorialSlides.setVisible(true);

	}

	// *******************************************************************************************************************************************
	// InitComponents() ends

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// Action performed methods for each combobox - They are ActionListeners
	// ---------------------------------------------------------------------------------------------------------------------------------------
	private void westernUnitedStatesJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, westernUnitedStates);

	}

	private void madagascarJComboBox1ActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, madagascar);
	}

	private void EgyptJComboBox2ActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, egypt);
	}

	private void EastAfricaJComboBox2ActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, eastAfrica);
	}

	private void congoJComboBox3ActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, congo);
	}

	private void southAfricaJComboBox4ActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, southAfrica);
	}

	private void northAfricaJComboBox2ActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, northAfrica);
	}

	private void NorthWestTerritoryActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, northWestTerritory);
	}

	private void ontarioJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, ontario);
	}

	private void albertaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, alberta);
	}

	private void quebecJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, quebec);
	}

	private void alaskaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, alaska);
	}

	private void easternUnitedStatesJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, easternUnitedStates);
	}

	private void centralAmericaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, centralAmerica);
	}

	private void argentinaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, argentina);
	}

	private void venezuela1ActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, venezuela);
	}

	private void brazilJComboBox1ActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, brazil);
	}

	private void peruJComboBox1ActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, peru);
	}

	private void mongoliaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, mongolia);
	}

	private void chinaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, china);
	}

	private void kamchatkaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, kamchatka);
	}

	private void indiaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, india);
	}

	private void irkutskJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, irkutsk);
	}

	private void siamJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, siam);
	}

	private void yakutskJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, yakutsk);
	}

	private void easternAustraliaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, easternAustralia);
	}

	private void indonesiaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, indonesia);
	}

	private void newGuineaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, newGuinea);
	}

	private void westernAustraliaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, westernAustralia);
	}

	private void southernEuropeJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, southernEurope);
	}

	private void siberiaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, siberia);
	}

	private void uralJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, ural);
	}

	private void ukraineJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, ukraine);
	}

	private void middleEastJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, middleEast);
	}

	private void afghanistanJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, afghanistan);
	}

	private void northernEuropeJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, northernEurope);
	}

	private void icelandJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, iceland);
	}

	private void greenlandJComboBox4ActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, greenland);
	}

	private void westernEuropeJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, westernEurope);
	}

	private void scandinaviaJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, scandinavia);
	}

	private void japanJComboBoxActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, japan);
	}

	private void greatBritainJComboBox1ActionPerformed(ActionEvent evt) {
		comboBoxClicked(evt, greatBritain);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// actionPerformed methods for the different buttons
	// ---------------------------------------------------------------------------------------------------------------------------------------

	private void closeJLabel1MouseClicked(MouseEvent evt) {
		String message = "Are you sure you want to leave the game? Just take another risk...";
		String title = "Really Quit?";
		int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			client.sendRequest(new LeaveLobby(""));
		}

	}

	private void chatJLabelMouseClicked(MouseEvent evt) {
		Chat s = new Chat(this.client);
		s.setVisible(true);
	}

	private void moveScreenJLabelMouseDragged(MouseEvent evt) {
		int xLocation = evt.getXOnScreen();
		int yLocation = evt.getYOnScreen();

		this.setLocation(xLocation - xMouse, yLocation - yMouse);

	}

	private void moveScreenJLabelMousePressed(MouseEvent evt) {
		xMouse = evt.getX();
		yMouse = evt.getY();
	}

	private void minusJLabel1MouseClicked(MouseEvent evt) {
		this.setState(StartGUI.ICONIFIED);
	}

	private void musicButtonActionPerformed(ActionEvent evt) {

		// audioPlayMusic song = new audioPlayMusic();
		if (musicButton.isSelected()) {
			song.stopSong();

		} else {
			song.startSong();

		}
	}

	private void nightModeButtonActionPerformed(ActionEvent evt) {
		if (nightModeButton.isSelected()) {
			backgroundImgLabel.setVisible(false);
		} else {
			backgroundImgLabel.setVisible(true);
		}
	}

	/**
	 * This method changes whose turn it is
	 * 
	 * @param evt
	 */
	private void confirmPhaseActionPerformed(ActionEvent evt) {

		me.endTurn();

		EndTurn endTurnMessage = new EndTurn(me.getUserID() + " has ended their turn!");
		
		client.sendAction(endTurnMessage);

		data.notifyGUIs();

	}

	private void forfeitActionPerformed(ActionEvent evt) {
		String message = "You look really strong out there, sure you want to forfeit?";
		String title = "Really Quit?";
		int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			if (me.isHost()) {

				Result resultRequest = new Result(me.getUserID() + " has forfeited!", me.getUserID(), false);

				client.sendRequest(resultRequest);

			} else {

				Forfeit forfeitMessage = new Forfeit(me.getUserID() + "has forfeited!");
				client.sendAction(forfeitMessage);
			}
		}

	}

	private void endDraftActionPerformed(ActionEvent evt) {
		if (me.getTurnPhase().equals("draft")) {
			// end draft phase
			me.endDraft();

		}
	}

	private void endAttackActionPerformed(ActionEvent evt) {
		if (me.getTurnPhase().equals("attack")) {
			me.skipAttack();

		}
	}

	private void saveButtonActionPerformed(ActionEvent evt) {
		this.client.saveGame();
		data.notifyGUIs();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------
	// TODO: Extra methods
	// ---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * returns number of troops on a specific country by a player
	 * 
	 * @param client
	 *            clients information
	 * @param country
	 *            country whom we want to find out about
	 * @return returns number of troops of that country
	 */
	private int getMaximumRowCount(Country country) {
		// if the country is owned by me
		if (me.checker.ownsCountry(country)) {
			// set the number of rows
			return me.getGivenTroops();
		} else {
			// if its not owned by me, its owned by other player
			return other.getGivenTroops();
		}
	}

	/**
	 * This method sets the names of the rows within the combobox
	 * 
	 * @param client
	 *            the current client
	 */
	private String[] setRowCountPopulation(Country country) {
		// if country is owned by me
		if (me.checker.ownsCountry(country)) {
			return me.getGivenTroopsAsArray(country);
		} else {
			return other.getGivenTroopsAsArray(country);
		}

	}

	/**
	 * method that returns the color that a combobox should be set to
	 * 
	 * @param client
	 * @param country
	 */
	// TODO: This method is for setting background colors NEEDS ownCountry
	// METHOD TO WORK
	private Color setComboBoxBackgroundColor(Country country) {

		// check if the current country belongs to me
		if (me.getOwnedTerritories().containsKey(country)) {
			// if true set color of my combobox to myColor
			return colorMine;
		} else
			// else country belongs to the other player
			return colorOther;
	}

	/**
	 * Checks whether the combobox should be visible to the player. Depending on
	 * if its their turn, and what phase of the game they are in.
	 * 
	 * @param country
	 *            The name of the country of that the combobox is representing
	 * @return True if its my turn, and if the country is owned by me.
	 */
	public boolean myTurnButtonVisible(Country country) {
		boolean returnedBool = false;
		// if its my turn
		if (me.IsTurn()) {
			if (me.checker.ownsCountry(country)) {
				returnedBool = true;
			} else {
				returnedBool = false;
			}
			// if its not my turn
		} else if (other.IsTurn()) {
			returnedBool = false;
		}
		return returnedBool;
	}

	/**
	 * this method sets up each of the combo boxes in the correct location. It
	 * sets the combo box to have the same properties on the entirety of the map
	 * 
	 * @param comboBoxName
	 *            this is the name of the combo box that needs to bet modified
	 * @param country
	 *            The country object that the combo box belongs to
	 * @param nameOfCountry
	 *            this is the string name of the country
	 * @param x
	 *            This is the x position of the combo box
	 * @param y
	 *            This is the y position of the combo box
	 * @param width
	 *            this is the width of the combo box
	 * @param height
	 *            this is the height of the combo box
	 * 
	 */
	public void setupComboBox(JComboBox<String> comboBoxName, Country country, String nameOfCountry, int x, int y,
			int width, int height) {
		
		//shows the country name when you hover over the combobox
		comboBoxName.setToolTipText(nameOfCountry);
		// Should this be accessible? - use turn phase
		// if its not my country, this will be blank
		comboBoxName.setVisible(myTurnButtonVisible(country));

		// Set colour of this box
		comboBoxName.setBackground(setComboBoxBackgroundColor(country));

		// what is written in each row of the drop down box
		if (me.turnphase.getTurnPhase().equals("draft")) {
			// number of rows in the drop down box
			comboBoxName.setMaximumRowCount(15);

			// set combo boxes to number of troops in that country
			comboBoxName.setModel(new DefaultComboBoxModel<>(setRowCountPopulation(country)));
		} else if (me.turnphase.getTurnPhase().equals("attack")) {
			// number of rows in the drop down box TODO
			comboBoxName.setMaximumRowCount(getMaximumRowCount(country));

			// set combo boxes to the number of troops in adjacent countries
			// that belong to me
			comboBoxName.setModel(new DefaultComboBoxModel<>(setRowCountPopulation(country)));
		} else if (me.turnphase.getTurnPhase().equals("fortify")) {
			// number of rows in the drop down box
			comboBoxName.setMaximumRowCount(getMaximumRowCount(country));

			// set combo boxes to the same as the drafting stage
			comboBoxName.setModel(new DefaultComboBoxModel<>(setRowCountPopulation(country)));
		}

		// see the name of the country when you hover over a combo boxes
		comboBoxName.setToolTipText(nameOfCountry);

		// set border for the combo boxes
		comboBoxName.setBorder(null);

		// Add it to our panel - at the correct position
		jLayeredPane3.add(comboBoxName, new AbsoluteConstraints(x, y, width, height));

		// Add the combobox to a list of combo boxes that I can use to update
		comboBoxesToCountryMap.put(comboBoxName, country);
		
	}

	// Lastly we add the combobox that has been created to the Treeset of
	// comboboxes
	// comboBoxesTreeSet.add(comboBoxName);

	/**
	 * This creates the labels showing the number of troops on each territory.
	 * 
	 * @param labelName
	 *            The name of the label; the country name
	 * @param countryname
	 *            The name of the country belonging to this label
	 * @param x
	 *            the x coordinate of this label
	 * @param y
	 *            the y coordinate of this label
	 */
	public void setupTerritoriesTroopLabel(JLabel labelName, Country countryname, int x, int y) {
		// sets the font to be the same which I have used throughout this GUI.
		labelName.setFont(riskFont);

		// sets the colour of the text - if its my country or if its not my
		// country
		// If I own the country
		if (me.checker.ownsCountry(countryname)) {
			// The colour of this label should be my color
			labelName.setForeground(colorMine);

		} else {
			// should be colour of other player
			labelName.setForeground(colorOther);
		}

		// set the troop number of that country
		// if its my country
		if (me.getOwnedTerritories().containsKey(countryname)) {
			// return number of troops that the country has
			labelName.setText(Integer.toString(me.getOwnedTerritories().get(countryname)));
		} else {
			labelName.setText(Integer.toString(other.getOwnedTerritories().get(countryname)));
		}

		// small border around the number
		Border border = LineBorder.createGrayLineBorder();
		labelName.setBorder(border);

		// sets the location of the text but + 39 due to the locations being the
		// same as the combobox
		jLayeredPane3.add(labelName, new AbsoluteConstraints((x + 39), y, -1, -1));

		jLabelToCountryMap.put(labelName, countryname);

	}

	/**
	 * If it is your turn, this this will be visible to you:
	 * 
	 * A combobox can only be selected in the following stages of the game:
	 * <ul>
	 * <li>draft stage</li>
	 * <li>attack stage</li>
	 * <li>fortify stage</li>
	 * 
	 * @param evt
	 *            This will be the evt that is triggered from clicking the
	 *            button
	 * @param nameOfCountry
	 *            country which is clicked
	 */
	public void comboBoxClicked(ActionEvent e, Country nameOfCountry) {

		JComboBox<String> cb = (JComboBox<String>) e.getSource();
		Object selected = cb.getSelectedItem();

		if (me.getTurnPhase().equals("draft")) {

			int troopNumberToDraft = Integer.parseInt((selected.toString()));

			if (draftToSend.containsKey(nameOfCountry)) {

				int currentTroopCount = draftToSend.get(nameOfCountry);

				draftToSend.put(nameOfCountry, currentTroopCount + troopNumberToDraft);

			} else {

				draftToSend.put(nameOfCountry, troopNumberToDraft);

			}
			// draft by sending the country which we have placed troops on + no
			// of troops
			me.draft(nameOfCountry, troopNumberToDraft);

			// draft to send is treeMap which I will be sending to the other
			// client to update him with what has happened
			
		} else if (me.getTurnPhase().equals("attack")) {

			// country selected as a string
			String countryToAttackString = selected.toString();
			// change it into a country
			Country countryToAttack = riskGame.Map.findCountryObject(countryToAttackString);
			
			int[] attackResult = me.attack(nameOfCountry, other, countryToAttack);

			// need to send it to the other client
			// send:
			// name of country
			// country to attack
			// attack result

			Attack attackSending;

			String messageSending = me.getUserID() + " attacked " + countryToAttackString + " from "
					+ nameOfCountry.getCountryName() + ": ";

			if (attackResult[0] == 0 && attackResult[1] == 0) {

				if (me.checker.allCountriesOwned()) {

					if (me.isHost()) {

						Result resultRequest = new Result(me.getUserID() + " has won!", me.getUserID(), true);

						client.sendRequest(resultRequest);

					} else {

						Result resultRequest = new Result(me.getUserID() + " has won!", other.getUserID(), false);

						client.sendRequest(resultRequest);

					}

				}
				
				String takeover = countryToAttackString + " has been takenover!";

				attackSending = new Attack(messageSending + takeover, nameOfCountry, countryToAttack, attackResult);

				client.sendAction(attackSending);

			} else {

				String normalResult = me.getUserID() + " has lost " + attackResult[1] + " & " + other.getUserID()
						+ " has lost " + attackResult[0];

				attackSending = new Attack(messageSending + normalResult, nameOfCountry, countryToAttack, attackResult);

				client.sendAction(attackSending);
			}

		} else if (me.getTurnPhase().equals("fortify")) {
			// country selected as a string
			String countryToFortifyString = selected.toString();
			// change it into a country
			Country countryToFortify = riskGame.Map.findCountryObject(countryToFortifyString);

			// TODO discuss this with DJ
			int numberForFortifiedWith = me.fortify(nameOfCountry, countryToFortify);

			// hide all buttons
			setAllComboBoxesInvisible();

			// update all labels

			// show end turn button

			// fortifyToSend

			String fortifyMessage = me.getUserID() + " has fortified " + countryToFortify.getCountryName() + " from "
					+ nameOfCountry.getCountryName();

			Fortify fortifyObject = new Fortify(fortifyMessage, nameOfCountry, countryToFortify,
					numberForFortifiedWith);

			client.sendAction(fortifyObject);

		}

		/* data.notifyGUIs(); */
	}

	/**
	 * This method checks the turnphase. Only true if its my turn and there are
	 * no more troops left postconditions:
	 * <ul>
	 * <li>if its my turn, and its that turnphase, return True</li>
	 * <li>if its my turn, but not that turnphase, return false</li>
	 * <li>if its not my turn, return false</li>
	 * 
	 * @param player
	 *            the player you are checking this for
	 * @param turnphase
	 *            The turnphase that you are checking if it is
	 * @return true if its
	 */
	public boolean hasPhaseTurnEnded(Player player) {
		// if its my turn
		if (player.IsTurn()) {
			if (me.getGivenTroops() == 0) {
				return true;
			} else
				return false;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		// StartGUI start = new StartGUI();
	}

	/**
	 * This is a loop that sets all combo boxes on my screen to invisible
	 */
	public void setAllComboBoxesInvisible() {
		for (Map.Entry<JComboBox<String>, Country> entry : comboBoxesToCountryMap.entrySet()) {
			entry.getKey().setVisible(false);
		}
	}

	/**
	 * This method updates all the labels on the map
	 * 
	 * @param jLabelToCountryMap
	 *            The jlabel list maped to countries that they represent
	 */
	public void updateAllLabels(Map<JLabel, Country> jLabelToCountryMap) {
		for (Map.Entry<JLabel, Country> entry : jLabelToCountryMap.entrySet()) {
			// we are getting the troops on the countries that we own

			if (me.checker.ownsCountry(entry.getValue())) {
				// update all territory labels
				entry.getKey().setText(Integer.toString(me.getOwnedTerritories().get(entry.getValue())));
				entry.getKey().setForeground(colorMine);

			} else {

				entry.getKey().setText(Integer.toString(other.getOwnedTerritories().get(entry.getValue())));
				entry.getKey().setForeground(colorOther);

			}

		}
	}

	/**
	 * This gets called every time we enter the draft stage, it does the
	 * following: it updates both the combo boxes and the labels on the map
	 * 
	 */
	public void draftStageUpdate() {
		// Loop over the combo boxes and update each one
		for (Map.Entry<JComboBox<String>, Country> entry : comboBoxesToCountryMap.entrySet()) {
			entry.getKey().setModel(new DefaultComboBoxModel<>(setRowCountPopulation(entry.getValue())));
			if (me.checker.ownsCountry(entry.getValue())) {
				entry.getKey().setVisible(true);
				entry.getKey().setBackground(colorMine);

			}
		}

		// update all the jlabels
		updateAllLabels(jLabelToCountryMap);
		// update the number of troops I have left to deploy
		numberOfTroopsUpdateLabel.setVisible(true);
		numberOfTroopsUpdateLabel.setText(me.getGivenTroops() + " pirates");
		// shows the current turnphase which we are in
		stateShowLabel.setText(me.turnphase.getTurnPhase().toUpperCase());
	}

	public void attackStageUpdate() {

		// numberOfTroopsUpdateLabel.setVisible(false);

		for (Map.Entry<JComboBox<String>, Country> entry : comboBoxesToCountryMap.entrySet()) {

			if (me.checker.canAttackFrom(entry.getValue())) {

				entry.getKey().setModel(new DefaultComboBoxModel<>(me.attackableCountries(entry.getValue())));
				entry.getKey().setBackground(colorMine);
				entry.getKey().setVisible(true);

			} else {

				entry.getKey().setVisible(false);

			}

		}

		updateAllLabels(jLabelToCountryMap);

	}

	public void fortifyStageUpdate() {

		// numberOfTroopsUpdateLabel.setVisible(false);

		for (Map.Entry<JComboBox<String>, Country> entry : comboBoxesToCountryMap.entrySet()) {

			if (me.checker.canFortifyFrom(entry.getValue())) {

				entry.getKey().setModel(new DefaultComboBoxModel<>(me.fortifiableCountries(entry.getValue())));
				entry.getKey().setBackground(colorMine);
				entry.getKey().setVisible(true);

			} else {

				entry.getKey().setVisible(false);

			}

		}

		updateAllLabels(jLabelToCountryMap);
		// hide all buttons
		// setAllComboBoxesInvisible();

	}

	@Override
	public void update(Observable o, Object arg) {

		// If its my turn
		if (me.IsTurn()) {
			whoseGoIsIt.setText("yes");
			// if its the draft stage
			if (me.getTurnPhase().equals("draft")) {
				// if me can still draft stage

				if (me.checker.canDraft()) {

					saveGameJButton.setVisible(true);

					draftStageUpdate();

				} else {
					// Can not deploy troops any more

					draftStageUpdate();
					setAllComboBoxesInvisible();
					numberOfTroopsUpdateLabel.setVisible(false);
					saveGameJButton.setVisible(false);
					endDraftJButton.setVisible(true);

					Draft draftMessage = new Draft(me.getUserID() + " has drafted!", draftToSend);
					
					client.sendAction(draftMessage);

				}

			}
			// if its the attack stage
			else if (me.getTurnPhase().equals("attack")) {
				// hide end draft button, message for draft,

				draftToSend.clear();

				endDraftJButton.setVisible(false);
				numberOfTroopsUpdateLabel.setVisible(false);

				// show the buttons for ending turn and ending skip phase
				endAttackJButton.setVisible(true);
				confirmPhaseJButton.setVisible(true);
				// update the state of the game
				stateShowLabel.setText(me.turnphase.getTurnPhase().toUpperCase());
				// udpate attack stage
				attackStageUpdate();

			}
			// if its the fortify stage
			else if (me.getTurnPhase().equals("fortify")) {
				// hide skip attack button
				endAttackJButton.setVisible(false);
				// update state of the game
				stateShowLabel.setText(me.turnphase.getTurnPhase().toUpperCase());
				// update fortify state
				fortifyStageUpdate();
			}

		}

		else {
			whoseGoIsIt.setText("no");
			setAllComboBoxesInvisible();
			endAttackJButton.setVisible(false);
			confirmPhaseJButton.setVisible(false);

			updateAllLabels(jLabelToCountryMap);

			// if its the other players draft stage
			if (other.getTurnPhase().equals("draft")) {
				stateShowLabel.setText(me.turnphase.getTurnPhase().toUpperCase());

			} else if (other.getTurnPhase().equals("attack")) {
				stateShowLabel.setText(me.turnphase.getTurnPhase().toUpperCase());
			} else if (other.getTurnPhase().equals("fortify")) {
				stateShowLabel.setText(me.turnphase.getTurnPhase().toUpperCase());
			}

			// hide everything
			// update everything
		}
		
		historyDisplayJTextArea.setText(data.getText() + "\n");
		repaint();

	}
}
