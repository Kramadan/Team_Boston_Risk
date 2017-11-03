package riskGame;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class Country implements Comparable<Country>, Serializable{

	private static final long serialVersionUID = -5592578049982030448L;
	
	private final String countryName;
	private final Set<String> adjacentCountries;
	
	public Country(String countryName, String...strings) {
		
		this.countryName = countryName;
		this.adjacentCountries = countryBuilder(strings);
		
	}
	
	public Country(String countryname) {// for ease of use when countries need to be created but adjacent countries aren't required
		
		this.countryName = countryname;
		
		String[] placeholder = {""};
		this.adjacentCountries = countryBuilder(placeholder);
		
	}

	public String getCountryName() {
		return countryName;
	}

	public Set<String> getAdjacentCountries() {
		
		return adjacentCountries;
		
	}
	
	public static HashSet<String> countryBuilder(String[] countries){
		
		HashSet<String> theSet = new HashSet<>();
		
		for (String country : countries) {
			theSet.add(country);
		}
		
		return theSet;
		
	}
	
	@Override
	public boolean equals(Object object) {//ensures that countries created from either constructors are the same if the name is the same

		if (object == null) {
			return false;
		}
		
		if (!Country.class.isAssignableFrom(object.getClass())) {
			return false;
		}

		final Country other = (Country) object;
		
		if ((countryName == null) ? (other.countryName != null) : !countryName.equals(other.countryName)) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public int compareTo(Country country){ //sorts by name if required, and is wholesome
		
		return getCountryName().compareTo(country.getCountryName());
		
	}
	@Override
	public String toString() {
		
		return countryName;
	
	}
	
	public static void main(String[] args){
		
		Map gameMap = new Map();
		
		TreeMap<Country, Integer> test = new TreeMap<>();
		
		Country etste = new Country("ye");
		Country yei = new Country("yei");
		
		test.put(etste, 2);
		
		if (test.containsKey(yei)) {
			System.out.println("ye");
		} else {
			System.out.println("ew");
		}
	}
	
	
	
	
}
