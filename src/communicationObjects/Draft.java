package communicationObjects;

import java.util.TreeMap;

import riskGame.Country;

public class Draft extends Action {

	private static final long serialVersionUID = -7496677474505399101L;
	
	private final TreeMap<Country, Integer> countryTroopAllocatement;	
	

	public Draft(String message, TreeMap<Country, Integer> countryTroopAllocatement) {
		super(message);
		this.countryTroopAllocatement = countryTroopAllocatement;
	}
	
	public TreeMap<Country, Integer> getCountryTroopAllocatement() {
		return countryTroopAllocatement;
	}
}
