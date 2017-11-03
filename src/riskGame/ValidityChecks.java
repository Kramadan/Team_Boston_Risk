package riskGame;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

//@author Khalid Ramadan

public class ValidityChecks implements Serializable{//some checks that can be used later (or not) 

	private static final long serialVersionUID = -395435709892778126L;
	
	private Player player;
	
	
	public ValidityChecks(Player player) {
		
		this.player = player;
		
	}
	
	public boolean ownsCountry(Country country){
		
		return player.getOwnedTerritories().containsKey(country);
		
	}
	
	public boolean canDraft(){
		
		return player.getGivenTroops() > 0;
		
	}
	
	public boolean validDraft(Integer draftNumber){
		
		return player.getGivenTroops() > draftNumber;
		
	}
	
	public boolean canFortifyFrom(Country fortifyFrom){
		
		if (ownsCountry(fortifyFrom)) {

			if (hasTroopsForAttackOrFortify(fortifyFrom)) {

				Set<String> adjacentCountries = fortifyFrom.getAdjacentCountries();
				
				Iterator<String> it = adjacentCountries.iterator();

				while (it.hasNext()) {
					
					String countryName = (String) it.next();
					
					Country country = Map.findCountryObject(countryName);

					if (ownsCountry(country)) {

						return true;

					}

				}

				return false;

			}

			return false;
		}

		return false;
		
	}
	
	public boolean isAdjacentTarget(Country att, Country def){	//target country is adjacent
		
		return att.getAdjacentCountries().contains(def.getCountryName());	
		
	}
	
	public boolean hasTroopsForAttackOrFortify(Country att) { // troops at attCountry > 1

		return player.getOwnedTerritories().get(att) > 1;

	}

	public boolean canAttackFrom(Country att) {
		
		if (ownsCountry(att)) {

			if (hasTroopsForAttackOrFortify(att)) {

				Set<String> adjacentCountries = att.getAdjacentCountries();
				
				Iterator<String> it = adjacentCountries.iterator();

				while (it.hasNext()) {
					
					String countryName = (String) it.next();
					
					Country country = Map.findCountryObject(countryName);

					if (!ownsCountry(country)) {

						return true;

					}

				}

				return false;

			}

			return false;
		}

		return false;
	}

	public boolean allCountriesOwned(){
		
		Map gameMap = new Map();
		
		return player.getOwnedTerritories().keySet().equals(gameMap.getMapSet());
			
	}
	
}
