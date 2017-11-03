package riskGame;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

public class GameAndPlayerTests {
		
	//me is the host, other is the guest
		Game g1 = new Game("me", "other", true);
		Player g1me = g1.getMe(), g1other = g1.getOther();
		
		//me is the guest, other is host		
		Game g2 = new Game("other", "me", false);
		Player g2me = g2.getMe(), g2other = g2.getOther();
		
		//note that g2me should be g1other and vice versa

	@Test
	public void matchingCountryAllocation() {
		
		Set<Country> g1meCountries = g1me.getOwnedTerritories().keySet();
		Set<Country> g1otherCountries = g1other.getOwnedTerritories().keySet();
		
		assertTrue(g1meCountries.equals(g2other.getOwnedTerritories().keySet()));
		assertTrue(g1otherCountries.equals(g2me.getOwnedTerritories().keySet()));
		
	}
	
	@Test
	public void matchingTroopAllocation() {
		
		//Checking whether the host has their troops synchronized across both games
		
		Iterator<Country> g1meCountries = g1me.getOwnedTerritories().keySet().iterator();
		
		ArrayList<Integer> g1meTroopCounts = new ArrayList<>();
		ArrayList<Integer> g2otherTroopCounts = new ArrayList<>();
		
		while (g1meCountries.hasNext()) {
			Country country = g1meCountries.next();
			
			int g1meCountryTroopCount = g1me.getOwnedTerritories().get(country);
			int g2otherCountryTroopCount = g2other.getOwnedTerritories().get(country);
			
			g1meTroopCounts.add(g1meCountryTroopCount);
			g2otherTroopCounts.add(g2otherCountryTroopCount);
			
		}

		assertEquals(g1meTroopCounts, g2otherTroopCounts);

		
		//Checking whether the guest has their troops synchronized across both games
		
		Iterator<Country> g1otherCountries = g1other.getOwnedTerritories().keySet().iterator();
		
		ArrayList<Integer> g1otherTroopCounts = new ArrayList<>();
		ArrayList<Integer> g2meTroopCounts = new ArrayList<>();
		
		while (g1otherCountries.hasNext()) {
			
			Country country = g1otherCountries.next();
			
			int g1otherCountryTroopCount = g1other.getOwnedTerritories().get(country);
			int g2meCountryTroopCount = g2me.getOwnedTerritories().get(country);
			
			g1otherTroopCounts.add(g1otherCountryTroopCount);
			g2meTroopCounts.add(g2meCountryTroopCount);
			
		}

		assertEquals(g1otherTroopCounts, g2meTroopCounts);
	}
	
	@Test
	public void mapPartitionTest(){
		
		Set<Country> meTerritories = g1me.getOwnedTerritories().keySet();
		
		for (Country country : meTerritories) {
			
			g1other.updateTroopAndCountry(country);
		}
		
		assertTrue(g1.getGameMap().getMapSet().equals(g1other.getOwnedTerritories().keySet()));
		
	}
	
	
	@Test
	public void correctTroopNumberTest() {
		
		int g1metroopCount = g1me.getNumberOfTroops();
		int g1othertroopCount = g1other.getNumberOfTroops();
		
		int g2metroopCount = g2me.getNumberOfTroops();
		int g2othertroopCount = g2other.getNumberOfTroops();
		
		assertEquals(g1.getTroopNumber(), g2.getTroopNumber());
		
		assertEquals(g1.getTroopNumber(), g1metroopCount);
		assertEquals(g1.getTroopNumber(), g1othertroopCount);
		
		assertEquals(g2.getTroopNumber(), g2metroopCount);
		assertEquals(g2.getTroopNumber(), g2othertroopCount);
	}

}
