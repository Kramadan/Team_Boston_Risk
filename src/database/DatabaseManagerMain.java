package database;

import java.util.ArrayList;

public class DatabaseManagerMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DatabaseManager d1 = new DatabaseManager();
		
		ArrayList<String> game = new ArrayList<String>();
		game.add(0, "riskGame");
		game.add(1, "matthew");
		game.add(2, "true");
		game.add(3, "Middle East, 2, Quebec, 3, Japan, 4, China, 2");
		game.add(4, "sam");
		game.add(5,"false");
		game.add(6, "South Africa, 1, North Africa, 2");

		d1.saveGameDatabase(game);

		ArrayList<String> game2 = new ArrayList<String>();
		game2.add(0, "riskgame2");
		game2.add(1, "Mark");
		game2.add(2, "true");
		game2.add(3, "Siam, 4, Siberia, 7");
		game2.add(4, "John");
		game2.add(5, "false");
		game2.add(6, "Congo, 8, Yatkusk, 5");

		d1.saveGameDatabase(game2);
		
		ArrayList<String> game3 = new ArrayList<String>();
		game3.add(0, "riskGame3");
		game3.add(1, "Alex45");
		game3.add(2, "false");
		game3.add(3, "North Africa, 4, Western Europe, 10, Southern Europe, 5, Northern Europe, 2, China, 7");
		game3.add(4, "David");
		game3.add(5, "true");
		game3.add(6, "New Guinea, 8, Madagascar, 7, Argentina, 2");
		
		d1.saveGameDatabase(game3);

		ArrayList<String> game3Amended = new ArrayList<String>();
		game3Amended.add(0, "riskGame3");
		game3Amended.add(1, "Alex45");
		game3Amended.add(2, "true");
		game3Amended.add(3, "North Africa, 4, Western Europe, 10, Northern Europe, 2, China, 7");
		game3Amended.add(4, "dan");
		game3Amended.add(5, "false");
		game3Amended.add(6, "New Guinea, 8, Madagascar, 7, Argentina, 5, Southern Europe, 4");
		
		d1.saveGameDatabase(game3Amended);
	
		ArrayList<String> game4 = new ArrayList<String>();
		
		game4.add(0, "riskGame4");
		game4.add(1, "Ollie");
		game4.add(2, "true");
		game4.add(3, "Alaska, 1, Alberta, 2, Central America, 1, Eastern United States, 3, Greenland, 1, Northwest Territory, 2,"
				+ "Ontaria, 2, Quebec, 4,  Western United States, 2, Argentina, 4, Brazil, 2, Peru, 1, Venezuela, 5, Great Britain, 2,"
				+ "Iceland, 1, Northern Europe, 4, Scandinavia, 1, New Guinea, 2, Western Australia, 1");
		game4.add(4, "hannah");
		game4.add(5, "false");
		game4.add(6, "Southern Europe, 2, Ukraine, 1, Western Europe, 2, Congo, 1, East Africa, 2, Egypt, 1, Madagascar, 3, "
				+ "North Africa, 2, South Africa, 3, Afghanistan, 2, China, 4, India, 6, Irkutsk, 2, Japan, 1, Kamchatka, 2,"
				+ "Middle East, 1, Mongolia, 2, Siam, 4, Siberia, 5, Ural, 2, Yatkusk, 1, Eastern Australia, 1, Indonesia, 1");
		d1.saveGameDatabase(game4);
		
		String winner = "matthew";
		String loser = "sam";
		
		d1.updateStats(winner, loser);
	}
}

