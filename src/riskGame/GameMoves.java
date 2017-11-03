package riskGame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@author Khalid Ramadan

public class GameMoves extends java.util.Observable implements IGameMoves {

	/*
	 * public static Integer calculateTroops(Player player, Map map) {
	 * 
	 * int continentalTotal = 0; Set<Country> playersCountries =
	 * player.getOwnedTerritories().keySet();
	 * 
	 * for (String continent : map.getContinents().keySet()) { //Add 5 troop
	 * units for every continent owned by the player if
	 * (playersCountries.containsAll(map.getContinents().get(continent)))
	 * continentalTotal += 5; }
	 * 
	 * return (player.getOwnedTerritories().size() * 3) + continentalTotal; //
	 * 
	 * }
	 */

	@Override
	public void attack(Player att, Country attTerr, Integer attNumber, Player def, Country defTerr, Integer defNumber, Map gameMap) {

		List<Integer> attOutcomes = new ArrayList<>();
		List<Integer> defOutcomes = new ArrayList<>();

		for (int i = 0; i < attNumber; i++) {

			attOutcomes.add(att.getPlayerRoll());
		}

		for (int i = 0; i < defNumber; i++) {

			defOutcomes.add(def.getPlayerRoll());

		}

		Collections.sort(defOutcomes);
		Collections.sort(attOutcomes);

		//sort Dice rolls 
		// Will need to display results

		int faceOff = Math.min(attNumber, defNumber);

		comparison_Checks: {
			for (int index = 0; index < faceOff; index++) { // for every two opposing dice that are matched up

				if (attOutcomes.get(index) > defOutcomes.get(index)) { // if  attDice is  successful

					if (def.getOwnedTerritories().get(defTerr) == 1) { // if current  Dice  roll  would cause takeover

						def.removeCountry(defTerr);
						att.updateTroopAndCountry(defTerr);

						if (att.getOwnedTerritories().keySet().equals(gameMap.getMapSet())) { 	// if takeover
																								//gives player all territories in map

							att.setWin(true); // need to notify observers
							break comparison_Checks;

						} else if (def.getOwnedTerritories().keySet().isEmpty()) { // if
																					// removal
																					// of
																					// defTerr
																					// was
																					// last
																					// territory
																					// owned
																					// by
																					// def

							def.setLost(true); // need to notify

						}

						// need to prompt player to select amount of troops to
						// move (say, troopsMoved)
						// 1 <= attNumber <=
						// min(att.getOwnedTerritories.get(attTerr), 3);
						// Hence, 1 <= troopsMoved <
						// att.getOwnedTerritories.get(attTerr)-1

						// att.updateTroopAndCountry(defTerr, //troopsMoved );

						break comparison_Checks;

					} else {// def still has troops
						def.updateTroopAndCountry(defTerr, def.getOwnedTerritories().get(defTerr) - 1);
						att.updateTroopAndCountry(attTerr, att.getOwnedTerritories().get(attTerr) - 1);
						//notify observers
					}

				} else { // def wins dice matchup

					Integer attCurrentTroopCount = att.getOwnedTerritories().get(attTerr);
					att.updateTroopAndCountry(attTerr, attCurrentTroopCount - 1);
					//notify observers
				}

			}

		}

	}

	@Override
	public void endTurn(Player player) { //proceed to next player's turn, either from attack state or fortify state
		
		
		player.setTurn(false);
		// notify observers
		
	}

	@Override
	public void skipAttack() {	//allow player to move from attack to fortify stage	

		
	}

	@Override
	public void fortify(Player player, Country fortifyFrom, Integer supportTroops, Country fortifyTo) {//move troops from one to another
		
		//assumes supportTroops< player.getOwnedTerritory().get(fortifyFrom)-1
		
		player.updateTroopAndCountry(fortifyFrom, player.getOwnedTerritories().get(fortifyFrom) - supportTroops);
		player.updateTroopAndCountry(fortifyTo, player.getOwnedTerritories().get(fortifyFrom) + supportTroops);
		
		//notify observers
		
		endTurn(player);
		
	}

	@Override
	public void draft(Player player, Country country, Integer troopNumber) {
		// for given player, allocate troops to given country
		// 

		int allocatedTroops = player.getGivenTroops();
		
		player.updateTroopAndCountry(country, player.getOwnedTerritories().get(country) + troopNumber);
		
		//notify
		
		player.setGivenTroops(allocatedTroops - troopNumber);
		
		if (player.getGivenTroops() == 0) {
			
			//notify observer
		}
		
		
		//notify
		
	}
	
	public static void main(String[] args){
		
		ArrayList<Integer> test = new ArrayList<>();
		test.add(1); test.add(3) ; test.add(7);
		Collections.sort(test);
		System.out.println(test);
	}

}
