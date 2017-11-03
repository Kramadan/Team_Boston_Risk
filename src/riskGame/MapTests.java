package riskGame;

public class MapTests {

	public static void main(String[] args) {

		Game g1 = new Game("me", "other", true);
		Player me = g1.getMe(), other = g1.getOther();
		
		for (Country country : me.getOwnedTerritories().keySet()) {
			
			System.out.println(country.getCountryName() + " ");
		}
		
		System.out.println(me.getOwnedTerritories().get(Map.findCountryObject("Afghanistan")));
		
		System.out.println();
		
		for (Country country : other.getOwnedTerritories().keySet()) {
			
			System.out.print(country.getCountryName() + " ");
		}
		
	}

}
