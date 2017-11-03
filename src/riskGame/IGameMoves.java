package riskGame;

// @author Khalid Ramadan

public interface IGameMoves {


	
	//DRAFT STATE
	//Place specified number of troop units on country (from over 
	public void draft(Player player, Country country, Integer troopNumber);
		
	public void endTurn(Player player); 
	
	public void attack(Player att, Country attTerr, Integer attNumber, Player def, Country defTerr, Integer defNumber, Map gameMap);
	
	public void skipAttack();
	
	public void fortify(Player player, Country fortifyFrom, Integer supportTroops, Country fortifyTo);


}
