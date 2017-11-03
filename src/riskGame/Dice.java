package riskGame;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@author Khalid Ramadan

public class Dice implements Serializable{

	private static final long serialVersionUID = 7261174115474688571L;
	
	private SecureRandom randomNumber = new SecureRandom();
	private Integer outcome = 1 + randomNumber.nextInt(6);

	public Integer getOutcome() {
		return outcome;
	}

	public void roll() {

		outcome = 1 + randomNumber.nextInt(6);

	}
	
	public ArrayList<Integer> diceRolls(int rolls){
		
		ArrayList<Integer> Outcomes = new ArrayList<>();

		for (int i = 0; i < rolls; i++) {
			roll();
			Outcomes.add(getOutcome());
		}
		
		Collections.sort(Outcomes);
		Collections.reverse(Outcomes);
		
		return Outcomes;
		
	}
	
	public int[] diceComparison(int attNumber, int defNumber){
		
		ArrayList<Integer> attRolls = diceRolls(attNumber), defRolls = diceRolls(defNumber);

		int faceOff = Math.min(attNumber, defNumber), attWins = 0, defWins = 0;
		
		for (int i = 0; i < faceOff; i++) {
			
			if (attRolls.get(i)>defRolls.get(i)) {
				
				attWins++;
				
			} else {

				defWins++;
			}
		}
		
		int[] comparisonResults = {attWins, defWins};
		
		return comparisonResults;
		
	}
	
	public static void main(String[] args){
		
		Dice test = new Dice();
		
		for (int i = 1; i < 10; i++) {
			
			
			int[] testarray = test.diceComparison(1, 2);
			System.out.println();
			System.out.println(testarray[0]);
			System.out.println(testarray[1]);
			if (testarray[1] == 0) {
				System.out.println("Loss");
			}
			
		}
	}

}
