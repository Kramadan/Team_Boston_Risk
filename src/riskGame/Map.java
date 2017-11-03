package riskGame;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

// @author Khalid Ramadan
public class Map implements Serializable{

	private static final long serialVersionUID = 751799647629916470L;
	
	private final Set<Country> mapSet = new TreeSet<>(); //Set containing all countries in specified map
	private final TreeMap<String, Set<Country>> Continents = new TreeMap<>(); 	//Map where keys are continent names, sets are set of countries in 
																				//said continent
	
	// Default Map is the Standard World Map as specified at https://en.wikipedia.org/wiki/Risk_(game)#Territories

	public Map() {

		//Strings of all country names
		String alaska = "Alaska", 
				alberta = "Alberta", 
				centralAmerica = "Central America",
				easternUnitedStates = "Eastern United States", 
				greenland = "Greenland",
				northWestTerritory = "Northwest Territory", 
				ontario = "Ontario", 
				quebec = "Quebec",
				westernUnitedStates = "Western United States", 
				argentina = "Argentina", 
				brazil = "Brazil",
				peru = "Peru", 
				venezuela = "Venezuela", 
				greatBritain = "Great Britain", 
				iceland = "Iceland",
				northernEurope = "Northern Europe", 
				scandinavia = "Scandinavia", 
				southernEurope = "Southern Europe",
				ukraine = "Ukraine", 
				westernEurope = "Western Europe", 
				congo = "Congo",
				eastAfrica = "East Africa", 
				egypt = "Egypt", 
				madagascar = "Madagascar", 
				northAfrica = "North Africa",
				southAfrica = "South Africa",
				afghanistan = "Afghanistan", 
				china = "China",
				india = "India", 
				irkutsk = "Irkutsk", 
				japan = "Japan", 
				kamchatka = "Kamchatka",
				middleEast = "Middle East", 
				mongolia = "Mongolia", 
				siam = "Siam", 
				siberia = "Siberia", 
				ural = "Ural",
				yakutsk = "Yakutsk", 
				easternAustralia = "Eastern Australia",
				indonesia = "Indonesia", 
				newGuinea = "New Guinea", 
				westernAustralia = "Western Australia";
		
		

		
		
		
		//All subsequent String[] are given arrays of adjacent countries (e.g. alaskaAdj contains all Territories adjacent to alaska)
		//All subsequent Country[] are arrays of countries in a specified continent
		
		
		Country alaskaC = new Country(alaska, alberta, northWestTerritory, kamchatka);
		Country albertaC = new Country(alberta, alaska, northWestTerritory, ontario, westernUnitedStates);
		Country centralAmericaC = new Country(centralAmerica, easternUnitedStates, westernUnitedStates, venezuela);
		Country easternUnitedStatesC = new Country(easternUnitedStates, centralAmerica, ontario, quebec, westernUnitedStates);
		Country greenlandC = new Country(greenland, northWestTerritory, ontario, quebec, iceland);
		Country northWestTerritoryC = new Country(northWestTerritory, alaska, alberta, greenland, ontario);
		Country ontarioC = new Country(ontario, alberta, easternUnitedStates, greenland, northWestTerritory, quebec);
		Country quebecC = new Country(quebec, easternUnitedStates, greenland, ontario);
		Country westernUnitedStatesC = new Country(westernUnitedStates, alberta, centralAmerica, easternUnitedStates, ontario);

		Country[] nAmerica = { albertaC, alaskaC, centralAmericaC, greenlandC, easternUnitedStatesC,
				northWestTerritoryC, ontarioC, quebecC, westernUnitedStatesC };

		Country venezuelaC = new Country(venezuela, centralAmerica, peru, brazil);
		Country brazilC = new Country(brazil, argentina, peru, venezuela, northAfrica);
		Country peruC = new Country(peru, argentina, venezuela, brazil);
		Country argentinaC = new Country(argentina, brazil, peru);

		Country[] sAmerica = { venezuelaC, brazilC, peruC, argentinaC };

		Country greatBritainC = new Country(greatBritain, iceland, northernEurope, scandinavia, westernEurope);
		Country icelandC = new Country(iceland, greenland, greatBritain, scandinavia);
		Country northernEuropeC = new Country(northernEurope, greatBritain, scandinavia, southernEurope, ukraine, westernEurope);
		Country scandinaviaC = new Country(scandinavia, iceland, greatBritain, northernEurope, ukraine);
		Country southernEuropeC = new Country(southernEurope, northernEurope, westernEurope, ukraine, middleEast, egypt, northAfrica);
		Country ukraineC = new Country(ukraine, northernEurope, scandinavia, southernEurope, afghanistan, ural, middleEast);
		Country westernEuropeC = new Country(westernEurope, greatBritain, northernEurope, southernEurope, northAfrica);

		Country[] europe = { greatBritainC, icelandC, northernEuropeC, scandinaviaC, southernEuropeC, ukraineC,
				westernEuropeC };

		Country congoC 		= new Country(congo, eastAfrica, northAfrica, southAfrica);
		Country eastAfricaC = new Country(eastAfrica, congo, madagascar, southAfrica, northAfrica, egypt, middleEast);
		Country egyptC 		=new Country(egypt, northAfrica, eastAfrica, middleEast, southernEurope);
		Country madagascarC = new Country(madagascar, eastAfrica, southAfrica);
		Country northAfricaC = new Country(northAfrica, congo, eastAfrica, egypt, brazil, westernEurope, southernEurope);
		Country southAfricaC = new Country(southAfrica, congo, eastAfrica, madagascar);
		
		Country[] africa = {congoC, eastAfricaC, egyptC, madagascarC, northAfricaC, southAfricaC};
				
		Country afghanistanC = new Country(afghanistan, china, india, middleEast, ural, ukraine);
		Country chinaC = new Country(china, afghanistan, india, mongolia, siam, siberia, ural);
		Country indiaC = new Country(india, afghanistan, china, siam, middleEast);
		Country irkutskC = new Country(irkutsk, kamchatka, mongolia, siberia, yakutsk);
		Country japanC = new Country(japan, kamchatka, mongolia);
		Country kamchatkaC = new Country(kamchatka, irkutsk, mongolia, yakutsk, alaska);
		Country middleEastC = new Country(middleEast, afghanistan, india, ukraine, southernEurope, egypt, eastAfrica);
		Country mongoliaC = new Country(mongolia, china, irkutsk, japan, kamchatka, siberia);
		Country siamC = new Country(siam, china, india, indonesia);
		Country siberiaC = new Country(siberia, china, irkutsk, mongolia, ural, yakutsk);
		Country uralC =new Country(ural, afghanistan, china, siberia, ukraine);
		Country yakutskC = new Country(yakutsk, irkutsk, kamchatka, siberia);
		
		Country[] asia = {afghanistanC, chinaC, indiaC, irkutskC, japanC, kamchatkaC, middleEastC,
							mongoliaC, siamC, siberiaC, uralC, yakutskC};
		
		Country easternAustraliaC = new Country(easternAustralia, newGuinea, westernAustralia);
		Country indonesiaC = new Country(indonesia, newGuinea, westernAustralia, siam);
		Country newGuineaC = new Country(newGuinea, indonesia, westernAustralia, easternAustralia);
		Country westernAustraliaC = new Country(westernAustralia, easternAustralia, indonesia, newGuinea);
		
		Country[] australia = {easternAustraliaC, indonesiaC, newGuineaC, westernAustraliaC};
		
		
		HashSet<Country> nAmericaContinent = countrySetBuilder(nAmerica);
		HashSet<Country> sAmericaContinent = countrySetBuilder(sAmerica);
		HashSet<Country> europeContinent = countrySetBuilder(europe);  
		HashSet<Country> africaContinent = countrySetBuilder(africa);  
		HashSet<Country> asiaContinent = countrySetBuilder(asia);    
		HashSet<Country> australiaContinent= countrySetBuilder(australia);
		
		this.mapSet.addAll(countrySetBuilder(nAmerica));
		this.mapSet.addAll(countrySetBuilder(sAmerica));
		this.mapSet.addAll(countrySetBuilder(europe));
		this.mapSet.addAll(countrySetBuilder(africa));
		this.mapSet.addAll(countrySetBuilder(asia));
		this.mapSet.addAll(countrySetBuilder(australia));
		
		this.Continents.put("North America", nAmericaContinent);
		this.Continents.put("South America", sAmericaContinent);
		this.Continents.put("Europe", europeContinent);
		this.Continents.put("Africa", africaContinent);
		this.Continents.put("Asia", asiaContinent);
		this.Continents.put("Australia", australiaContinent);
	}

	
	
	
	public Set<Country> getMapSet() {

		return mapSet;

	}

	public TreeMap<String, Set<Country>> getContinents() {

		return Continents;

	}

	public static HashSet<Country> countrySetBuilder(Country[] countries) {

		HashSet<Country> theSet = new HashSet<>();

		for (Country country : countries) {
			theSet.add(country);
		}
		return theSet;
	}

	@Override
	public String toString() {

		String mapString = "All the countries in this map: ";

		while (mapSet.iterator().hasNext()) {

			mapString += mapSet.iterator().next() + "\n";

		}

		return mapString + Continents.toString();
	}

	public static Country findCountryObject(String countryName){
		
		Map mapSet = new Map();
				
		for (Country country : mapSet.getMapSet()) {
			
			if (country.getCountryName().equals(countryName)) {
				
				return country;
				
			}
		}
		
		return null;
		
	}
	public static void main(String[] args) {

		Map test = new Map();
	}
}