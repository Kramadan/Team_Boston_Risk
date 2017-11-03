package riskGame;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import client.Client;
import client.Data;
import communicationObjects.Attack;
import communicationObjects.Draft;
import communicationObjects.Fortify;
import communicationObjects.Result;

//@author Khalid Ramadan

public class Game implements Serializable{

	private static final long serialVersionUID = -5480089944358149485L;
	
	private Player me;
	private Player other;
	private Map gameMap = new Map(); // map to be used for the game
	private Client client;
	private Data data;
	private boolean gameFinished = false; // whether the game has finished
	private Integer troopNumber = 40; // number of troops at start of game for
										// each player - should be at least
										// (Total# Countries)/(# Players)

	public Game(String hostName, String guestName, boolean isHost, Data data, Client client) { 
		
		this.client = client;
		this.data = client.getData();

		this.me = new Player(hostName, isHost, isHost, client);
		this.other = new Player(guestName, !isHost, !isHost);

		me.calculateTroops();
		other.calculateTroops();

		countryAllocation();

		Player[] players = { me, other };

		for (Player player : players) {

			troopDeployment(player);

		}

		me.setGivenTroops(20);
		other.setGivenTroops(20);
	}

	//Game constructor for testing purposes
	
	public Game(String hostName, String guestName, boolean isHost) { 
		
		this.client = null;
		this.data = null;

		this.me = new Player(hostName, isHost, isHost);
		this.other = new Player(guestName, !isHost, !isHost);

		me.allocateTroops(troopNumber);
		other.allocateTroops(troopNumber);

		countryAllocation();

		
		Player[] players = { me, other };

		for (Player player : players) {

			troopDeployment(player);

		}

	}
	
	public Game(Player me, Player other) { // Game constructor for saved games, player territories, troop placement & turn need to be specified

		this.me = me;
		this.other = other;

		me.calculateTroops();
		other.calculateTroops();
	}

	private void troopDeployment(Player player) {

		int troopTotal = player.getGivenTroops();

		Set<Country> countrySet = player.getOwnedTerritories().keySet();

		Iterator<Country> it = countrySet.iterator();

		while (it.hasNext()) {

			player.updateTroopAndCountry(it.next(), 1);

			troopTotal--;
		}

		while (troopTotal != 0) {

			it = countrySet.iterator();

			while (it.hasNext() && troopTotal != 0) {

				Country country = it.next();
				int currentTroopCount = player.getOwnedTerritories().get(country);

				player.getOwnedTerritories().put(country, ++currentTroopCount);
				troopTotal--;
			}
		}
	}

	private void countryAllocation() {


		if (me.isHost()) {

			int counter = 0;

			for (Country country : gameMap.getMapSet()) {

				if (counter % 2 == 0 ) {

					me.updateTroopAndCountry(country);

					
				} else {

					other.updateTroopAndCountry(country);
					
				}

				counter++;
			}

		} else if (!me.isHost()) {

			int counter = 0;

			for (Country country : gameMap.getMapSet()) {

				if (counter  % 2 == 0) {

					other.updateTroopAndCountry(country);

				} else {

					me.updateTroopAndCountry(country);
				}

				counter++;
			}
		}

	}


	public Player getMe() {
		return me;

	}

	public Player getOther() {
		return other;
	}

	public Map getGameMap() {
		return gameMap;
	}

	public int getTroopNumber() {
		
		return troopNumber;
	}
	
	public void processDraft(Draft draft) {
		
		Iterator<Country> it = draft.getCountryTroopAllocatement().keySet().iterator();

		while (it.hasNext()) {

			Country country2 = it.next();

			other.updateTroopAndCountry(country2, other.getOwnedTerritories().get(country2) + draft.getCountryTroopAllocatement().get(country2));

		}
		
		data.notifyGUIs();
	}

	public void processAttack(Attack attack) {

		int[] attackResults	= attack.getAttackResult();
		
	
		
		if (attackResults[0] == 0 && attackResults[1] == 0) {
			
			me.removeCountry(attack.getTarget());
			other.updateTroopAndCountry(attack.getTarget(),  attackResults[2]);
			other.updateTroopAndCountry(attack.getAttackFrom(), other.getOwnedTerritories().get(attack.getAttackFrom()) - attackResults[2]);
			
		} else {

			me.updateTroopAndCountry(attack.getTarget(), me.getOwnedTerritories().get(attack.getTarget()) - attackResults[0]);
			other.updateTroopAndCountry(attack.getAttackFrom(), other.getOwnedTerritories().get(attack.getAttackFrom()) - attackResults[1]);
			
		}

		data.notifyGUIs();

	}

	public void processSkipAttack() {

		other.turnphase.setTurnPhase("fortify");

		data.notifyGUIs();

	}

	public void processFortify(Fortify fortify) {

		int currentTroopCountFrom = other.getOwnedTerritories().get(fortify.getFortifyFrom());
		int currentTroopCountTo = other.getOwnedTerritories().get(fortify.getFortifyTo());
		
		other.updateTroopAndCountry(fortify.getFortifyFrom(),  currentTroopCountFrom- fortify.getFortifiedWith());
		other.updateTroopAndCountry(fortify.getFortifyTo(),  currentTroopCountTo + fortify.getFortifiedWith());
		
		data.notifyGUIs();
	}

	public void processEndTurn() {

		other.setTurn(false);
		me.setTurn(true);
		
		me.calculateTroops();

		other.turnphase.setTurnPhase("draft");
		me.turnphase.setTurnPhase("draft");
		
		data.notifyGUIs();
		
	}
	
	public void processForfeit(){
		
		Result resultRequest = new Result(me.getUserID() + " has forfeited!", me.getUserID(), true);
		
		client.sendRequest(resultRequest);
	}

	public static void initialTroopAllocation(Player player) { // allocating
																// troops at
																// start of game
																// once owned
																// countries
																// have been
																// decided

		int troopTotal = player.getGivenTroops();

		Set<Country> countrySet = player.getOwnedTerritories().keySet();
		Iterator<Country> it = countrySet.iterator();

		while (it.hasNext()) {
			player.updateTroopAndCountry(it.next(), 1); // i.e. place at least
														// one unit in every
														// owned territory
			troopTotal--; // decrement after every unit allocation (assuming
							// allocated troops > # of territories)
		}

		while (troopTotal != 0) { // i.e. some troops are left over

			it = countrySet.iterator(); // reinitialise iterator

			while (it.hasNext() && troopTotal != 0) {

				Country country = it.next();
				int currentTroopCount = player.getOwnedTerritories().get(country); // get
																					// current
																					// troop
																					// count
																					// at
																					// current
																					// territory

				player.updateTroopAndCountry(country, currentTroopCount++);
				troopTotal--;
			}

		} // terminates only when all troops have been allocated

	}

	public static void randomisedTerritoryAllocation(Player[] players, Map gameMap) { // randomises
																						// territories
																						// across
																						// all
																						// players
																						// for
																						// given
																						// gameMap

		ArrayList<Integer> playerAllocation = playerOrderAllocation(players.length);

		int totalCountries = gameMap.getMapSet().size();

		Iterator<Country> it = gameMap.getMapSet().iterator();

		for (int i = 0; i < totalCountries; i++) {

			players[playerAllocation.get(i % (players.length))].updateTroopAndCountry(it.next());

			if ((i + 1) % 4 == 0)
				playerAllocation = playerOrderAllocation(players.length);

		}
	}

	public static ArrayList<Integer> playerOrderAllocation(int numberOfPlayers) { // helper
																					// method
																					// for
																					// randomisedTerritoryAllocation()

		SecureRandom random = new SecureRandom();

		ArrayList<Integer> playerAllocation = new ArrayList<>(numberOfPlayers);

		for (int i = 0; i < numberOfPlayers; i++) {

			int labeller = random.nextInt(numberOfPlayers);

			while (playerAllocation.contains(labeller)) {
				labeller = random.nextInt(numberOfPlayers);
			}

			playerAllocation.add(labeller);
		}

		return playerAllocation;
	}

	public static void main(String[] args) {

		Game g1 = new Game("me", "other", true);
		
		/*System.out.println();
    	
    	System.out.println(g1.me.getUserID() + " is turn? " + g1.me.IsTurn() + " and is Host? " + g1.me.isHost());
    	
    	System.out.println();
    	Iterator<Country> it = g1.me.getOwnedTerritories().keySet().iterator();
    	
    	while (it.hasNext()) {
    		
			Country country = it.next();
	    		
			System.out.println(country.getCountryName() + " " + g1.me.getOwnedTerritories().get(country));
			
		}
		System.out.println();*/
		
		/*Game g2 = new Game("other", "me", false);
	*/
    	
    	/*System.out.println(g2.me.getUserID() + " is turn? " + g2.me.IsTurn() + " and is Host? " + g2.me.isHost());
    	
    	System.out.println();
    	Iterator<Country> it2 = g2.me.getOwnedTerritories().keySet().iterator();
    	
    	while (it2.hasNext()) {
    		
			Country country = it2.next();
	    		
			System.out.println(country.getCountryName() + " " + g2.me.getOwnedTerritories().get(country));
			
		}
    	
		System.out.println();*/
		
		//Note g1.me should be g2.other
		/*System.out.println(g1.me.getOwnedTerritories().keySet().containsAll(g2.other.getOwnedTerritories().keySet()));
		System.out.println(g2.other.getOwnedTerritories().keySet().containsAll(g1.me.getOwnedTerritories().keySet()));
		
		
		
		System.out.println("testing getNumberOfTRoops");
		
		Map test = new Map();
		
		Country alaska = test.findCountryObject("Alaska");
		
		System.out.println(Arrays.toString(g2.me.getGivenTroopsAsArray(alaska)));
		System.out.println(Arrays.toString(g1.me.getGivenTroopsAsArray(alaska)));
		
		System.out.println(Arrays.toString(g1.other.getGivenTroopsAsArray(alaska)));
		System.out.println(Arrays.toString(g2.other.getGivenTroopsAsArray(alaska)));*/
		/*Map test = new Map();
		
		Country alaska = test.findCountryObject("Alaska");
		System.out.println(g1.other.getOwnedTerritories().get(alaska));
		
		Dice testDice = new Dice();
		
		int currentAttTroops = 6, 
				
				currentDefTroops = 2,
				
				attNumber = Math.min(currentAttTroops - 1, 3),
			
				defNumber = currentDefTroops > 1 ? 2 : 1;

				for (int i = 0; i < 10; i++) {
					
				System.out.println();
			System.out.println("currentAttTroops: " + currentAttTroops + ", currentDefTroops: " + currentDefTroops);				

				
			
			int[] diceComparisons = testDice.diceComparison(attNumber, defNumber);
			
			System.out.println("diceComparisons: " + diceComparisons[0] + ", " + diceComparisons[1]);
			
			int attWins = diceComparisons[0], defWins = diceComparisons[1];

			int[] takeoverArray = {0, 0}; //in the case a takeover occurs

			System.out.println("Result: ");
			if (currentDefTroops == defNumber) { // i.e. def troops are all that remain

				if (defWins == 0) {// complete takeover

					if (attNumber == 1 && defNumber == 2) {// the one case where defWins isn't a takeover

						System.out.println(" currentDefTroops: " + (currentDefTroops - attWins));
						System.out.println("diceComparisons: " + diceComparisons[0] + ", " + diceComparisons[1]);
						
						
					} else {
						
						System.out.println("{0,0}");
						
					}
					
				} else { //simple skirmish, troops lost on either side/one side

					System.out.println("currentAttTroops: " + (currentAttTroops - defWins));
					System.out.println(" currentDefTroops: " + (currentDefTroops - attWins));
					
					System.out.println("diceComparisons: " + diceComparisons[0] + ", " + diceComparisons[1]);
				}

			} else { //def has more than defending troops on defTerr => no possibility of takeover => simple skirmish, troops lost on either side/one side

				System.out.println("currentAttTroops: " + (currentAttTroops - defWins));
				System.out.println(" currentDefTroops: " + (currentDefTroops - attWins));
				
				System.out.println("diceComparisons: " + diceComparisons[0] + ", " + diceComparisons[1]);
			}
		
			}*/
	}

}
