package riskGame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import client.Client;
import client.Data;
import communicationObjects.Action;

//@author Khalid Ramadan

public class Player implements Serializable {

	private static final long serialVersionUID = -2432906364769000434L;
	
	private String userID;
	private final boolean isHost;
	private TreeMap<Country, Integer> ownedTerritories = new TreeMap<>();
	private boolean isTurn = false;
	public TurnPhases turnphase = new TurnPhases("draft");
	private static boolean lost = false;
	private static boolean won = false;
	public final ValidityChecks checker = new ValidityChecks(this);
	private final Dice playerDice = new Dice();
	private Integer givenTroops; // used at start of game and throughout the
									// game at the start of every turn
	private Data data;
	private Client client;
	private int allTroopsOwnedCurrently; // number of troops owned by player at
											// any given moment

	//Constructor to be used when creating a player for start of new game
	public Player(String name, boolean isHost, boolean isTurn, Client client) {

		this.userID = name;
		this.isHost = isHost;
		this.isTurn = isTurn;

		this.client = client;
		this.data = client.getData();

	}

	// Constructor to be used when recreating a saved game
	public Player(String name, boolean isHost, boolean isTurn, Client client, TreeMap<Country, Integer> ownedTerritories) {

		this.userID = name;
		this.isHost = isHost;
		this.isTurn = isTurn;
		this.ownedTerritories = ownedTerritories;

		this.client = client;
		this.data = client.getData();

	}

	// Player constructor for testing purposes
	public Player(String name, boolean isHost, boolean isTurn) {

		this.userID = name;
		this.isHost = isHost;
		this.isTurn = isTurn;

		this.client = null;
		this.data = null;

	}

	public String getUserID() {

		return userID;

	}

	public TreeMap<Country, Integer> getOwnedTerritories() {

		return ownedTerritories;

	}

	/**
	 * Method is for returning the countries owned by a player
	 * 
	 * @return String[] of countries owned by player
	 */
	public String[] getOwnedCountriesOnly() {

		String[] countriesOwnedMe = new String[ownedTerritories.keySet().size()];

		Iterator<Country> it = ownedTerritories.keySet().iterator();

		for (int i = 0; i < countriesOwnedMe.length; i++) {
			countriesOwnedMe[i] = it.next().getCountryName();
		}

		return countriesOwnedMe;
	}

	/**
	 * Will return the number of troops owned by the player
	 * 
	 * @return number of troops owned by player
	 */
	public int getNumberOfTroops() {
		int numberOfTroops = 0;

		for (Entry<Country, Integer> entry : ownedTerritories.entrySet()) {
			numberOfTroops += entry.getValue();
		}

		return numberOfTroops;
	}

	/**
	 * This method gets the number of troops on a country as a string array
	 * 
	 * @param country
	 *            country you want to find out number of troops you have on it
	 * @return string[] of range of troops, so if a country has 3 it will be
	 *         [1,2,3]
	 */
	public String[] getGivenTroopsAsArray(Country country) {

		String[] sizeOfTroops = new String[getGivenTroops()];
		for (int i = 0; i < sizeOfTroops.length; i++) {

			sizeOfTroops[i] = "" + (i + 1);

		}

		return sizeOfTroops;
	}

	public boolean IsTurn() {

		return isTurn;

	}

	public void setTurn(boolean isTurn) {

		this.isTurn = isTurn;

	}

	public boolean isHost() {
		return isHost;
	}

	public void updateTroopAndCountry(Country country, Integer number) {

		getOwnedTerritories().put(country, number);

	}

	public void updateTroopAndCountry(Country country) {// simply to allocate a
														// country to a player

		getOwnedTerritories().put(country, 0);

	}

	public void removeCountry(Country country) {

		getOwnedTerritories().remove(country);

	}

	public static boolean isLost() {

		return lost;

	}

	public void setLost(boolean lost) {

		Player.lost = lost;

	}

	public boolean getWin() {

		return won;

	}

	public void setWin(boolean win) {

		Player.won = win;

	}

	public Integer getPlayerRoll() {

		this.playerDice.roll();

		return playerDice.getOutcome();

	}

	public String getTurnPhase() {

		return turnphase.getTurnPhase();

	}

	public void allocateTroops(Integer allocatedTroops) {

		givenTroops = allocatedTroops;

	}

	public Integer getGivenTroops() {

		return givenTroops;

	}

	public void setGivenTroops(Integer givenTroops) {

		this.givenTroops = givenTroops;

	}

	// used for calculating number of additional troops for draft stage
	// for loop adds an additional 5 troop units for every continent owned
	// at least 3 troops are allocated regardless of territories owned

	public void calculateTroops() {

		Map map = new Map();

		int continentalTotal = 0;
		
		Set<Country> playersCountries = getOwnedTerritories().keySet();

		for (String continent : map.getContinents().keySet()) {

			if (playersCountries.containsAll(map.getContinents().get(continent)))
				continentalTotal += 5;

		}

		int allocatedTroopNumber = ((playersCountries.size() / 3) + continentalTotal);

		givenTroops = Math.max(allocatedTroopNumber, 3);

	}

	public void draft(Country country, Integer troopNumber) {

		updateTroopAndCountry(country, ownedTerritories.get(country) + troopNumber);

		setGivenTroops(givenTroops - troopNumber);

		data.notifyGUIs();
	}

	public void endDraft() {
		
		turnphase.setTurnPhase("attack");

		data.notifyGUIs();
	}
	
	public String[] attackableCountries(Country country){
		
		ArrayList<String> attackableCountriesList = new ArrayList<>();
		
		Iterator<String> it = country.getAdjacentCountries().iterator();
		
		while (it.hasNext()) {
			
			String countryName = it.next();
			Country adjacentCountry = Map.findCountryObject(countryName);	
			
			if (!checker.ownsCountry(adjacentCountry)) {
				
				attackableCountriesList.add(countryName);
				
			}
		}
		
		String[] attackableCountries = attackableCountriesList.toArray(new String[attackableCountriesList.size()]);
		
		return attackableCountries;
		
	}

	// attacking troop number will be max of either 3 or 1 short off the
	// troopcount on attTerr
	// defending troop number will be either 2 or 1 depending on troops
	// available
	// highest dice rolls are compared to determine winner of each skirmish

	public int[] attack(Country attTerr, Player def, Country defTerr) {

		int currentAttTroops = getOwnedTerritories().get(attTerr),

			currentDefTroops = def.getOwnedTerritories().get(defTerr),

			attNumber = Math.min(currentAttTroops - 1, 3),

			defNumber = currentDefTroops > 1 ? 2 : 1;

		int[] diceComparisons = playerDice.diceComparison(attNumber, defNumber);

		int attWins = diceComparisons[0], defWins = diceComparisons[1];

		int[] takeoverArray = {0,0, attNumber}; // in the case a takeover occurs

		if (currentDefTroops == defNumber) { // i.e. def troops are all that
												// remain

			if (defWins == 0) {// complete takeover

				if (attNumber == 1 && defNumber == 2) {// the one case where defWins isn't a takeover

					def.updateTroopAndCountry(defTerr, currentDefTroops - attWins);
					
					data.notifyGUIs();

					return diceComparisons;

				} else {

					def.removeCountry(defTerr);
					
					updateTroopAndCountry(attTerr, currentAttTroops - attNumber);
					
					updateTroopAndCountry(defTerr, attNumber);
					
					data.notifyGUIs();

					
					return takeoverArray;

				}

			} else { // simple skirmish, troops lost on either side/one side

				updateTroopAndCountry(attTerr, currentAttTroops - defWins);
				def.updateTroopAndCountry(defTerr, currentDefTroops - attWins);

				data.notifyGUIs();

				return diceComparisons;
			}

		} else { // def has more than defending troops on defTerr => no
					// possibility of takeover => simple skirmish, troops lost
					// on either side/one side

			updateTroopAndCountry(attTerr, currentAttTroops - defWins);
			def.updateTroopAndCountry(defTerr, currentDefTroops - attWins);

			data.notifyGUIs();

			return diceComparisons;
		}

	}

	public void skipAttack() {

		turnphase.setTurnPhase("fortify");

		data.notifyGUIs();
	}
	
	public String[] fortifiableCountries(Country country){
		
		ArrayList<String> fortifiableCountriesList = new ArrayList<>();
		
		Iterator<String> it = country.getAdjacentCountries().iterator();
		
		while (it.hasNext()) {
			
			String countryName = it.next();
			
			Country adjacentCountry = Map.findCountryObject(countryName);	
			
			if (checker.ownsCountry(adjacentCountry)) {
				
				fortifiableCountriesList.add(countryName);
				
			}
		}
		
		String[] attackableCountries = fortifiableCountriesList.toArray(new String[fortifiableCountriesList.size()]);
		
		return attackableCountries;
		
	}

	// move troops from one owned territory to another that's adjacent
	// minimum of either 3 troops or 1 less than the current total can be moved
	
	public int fortify(Country fortifyFrom, Country fortifyTo) {

		int currentTroopCount = getOwnedTerritories().get(fortifyFrom);
		
		int supportTroops = Math.min(currentTroopCount -1, 3);
		
		updateTroopAndCountry(fortifyFrom, getOwnedTerritories().get(fortifyFrom) - supportTroops);
		updateTroopAndCountry(fortifyTo, getOwnedTerritories().get(fortifyTo) + supportTroops);

		data.notifyGUIs();
		
		return supportTroops;

	}

	// proceed to next player's turn, either from attack state or fortify state
	
	public void endTurn() {

		setTurn(false);

	}

	public static void main(String[] args) {

		int defNumber = 2; // i.e. either defend with 1 or 2 troops depending on
							// available troops

		List<Integer> attOutcomes = new ArrayList<>(), defOutcomes = new ArrayList<>();

		Dice test = new Dice();

		for (int i = 0; i < 3; i++) {

			test.roll();
			defOutcomes.add(test.getOutcome());

		}

		/*
		 * for (int i = 0; i < defNumber; i++) { "testing getNumberOfTRoops
		 * test.roll(); attOutcomes.add(test.getOutcome());
		 * 
		 * }
		 */

		Collections.sort(defOutcomes);
		Collections.reverse(defOutcomes); // sort dice rolls in descending order

		Collections.sort(attOutcomes);
		Collections.reverse(attOutcomes); // sort dice rolls in descending order

		int faceOff = Math.min(3, defNumber); // i.e. if att rolls 3x and def
												// once, only highest dice
												// across both are compared
	}

}
