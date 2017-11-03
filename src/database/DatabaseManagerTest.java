package database;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import communicationObjects.User;

/**
 * A class to test the read methods within the DatabaseManager class.
 * @author MatthewByrne
 *
 */
public class DatabaseManagerTest {
	
	DatabaseManager d1 = new DatabaseManager();

	@Test // test registering a new user
	public void test1() {
		
		User user = d1.register("Team", "boston");
	
		String expectedMessage = "User registered successfully";
		String actual = user.getMessage();		
		
		assertEquals(actual, expectedMessage);
	}

	
	@Test //check registered user
	public void test2() {
		
		User user = d1.register("daniel45", "marley");
		
		String expectedMessage = "Username already registered";
		String actualMessage = user.getMessage();
		
		assertEquals(actualMessage, expectedMessage);
		
	}

	@Test // login user who's already registered
	public void test3(){
		
		User user = d1.checkLogin("daniel45", "marley");
		
		String expectedMessage = "Password accepted, login successful";
		String actualMessage = user.getMessage();
		
		assertEquals(actualMessage, expectedMessage);
	}
	
	@Test // test incorrect password for registered user
	public void test4(){
		
		User user = d1.checkLogin("daniel145", "beetroot");
		
		String expectedMessage = "Incorrect password entered, please try again";
		String actualMessage = user.getMessage();
		
		assertEquals(actualMessage, expectedMessage);
	}
	
	@Test // test incorrect username format for new user.
	public void test5(){
		
		User user = d1.register("bob$123", "CS124");
		
		String expectedMessage = "Username or password does not match the required format, please enter a username and password using only alphanumeric characters";
		String actualMessage = user.getMessage();
		
		assertEquals(expectedMessage, actualMessage);
		
	}
	
	@Test //test incorrect password format for new user
	public void test6(){
		
		User user = d1.register("bob123", "45g*");
		
		String expectedMessage = "Username or password does not match the required format, please enter a username and password using only alphanumeric characters";
		String actualMessage = user.getMessage();
		
		assertEquals(expectedMessage, actualMessage);
	}
	
	@Test //test loading a game from the database
	public void test7(){
		
		String hostName = "Mark";
		ArrayList<String> expected = new ArrayList<String>();
		
		expected.add(0, "riskgame2");
		expected.add(1, hostName);
		expected.add(2, "true");
		expected.add(3, "Siam, 4, Siberia, 7");
		expected.add(4, "John");
		expected.add(5,"false");
		expected.add(6, "Congo, 8, Yatkusk, 5");
		
		assertEquals(expected, d1.loadGameFromDatabase(hostName));
		
	}

	@Test //test a load game  where user hasn't saved one already.
	public void test8(){
		
		String hostName2 = "dog";
		ArrayList<String> expected = null;
		
		assertEquals(expected, d1.loadGameFromDatabase(hostName2));
	}
	

	@Test //testing another normal case for a load game
	public void test9(){
		
		String hostName = "Alex45";
		
		ArrayList<String> expected = new ArrayList<>();
		
		expected.add(0, "riskGame3");
		expected.add(1, "Alex45");
		expected.add(2, "true");
		expected.add(3, "North Africa, 4, Western Europe, 10, Northern Europe, 2, China, 7");
		expected.add(4, "dan");
		expected.add(5, "false");
		expected.add(6, "New Guinea, 8, Madagascar, 7, Argentina, 5, Southern Europe, 4");
		
		assertEquals(expected, d1.loadGameFromDatabase(hostName));
		
	}
	
	
	
}
	

